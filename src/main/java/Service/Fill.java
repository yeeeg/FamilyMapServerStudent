package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Result.*;
import Service.Request.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.*;

/**
 * Description: Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server.
 * If there is any data in the database already associated with the given user name, it is deleted.
 * The optional "generations" parameter lets the caller specify the number of generations of ancestors
 * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new persons each with
 * associated events).
 *
 * HTTP Method: POST
 * Auth Token Required: No
 * Request Body: None
 * Errors: Invalid username or generations parameter, Internal server error
 */
public class Fill
{
    public static final int DEFAULT_USER_BIRTH_YEAR = 1997;
    public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final int MAX_BIRTH_AGE = 50;
    public static final int MIN_BIRTH_AGE = 13;
    public static final int MAX_AGE = 120;
    FillRequest request;
    public FillResult result;
    public FillResult fill;
    Database db;
    int people_count;
    int event_count;
    public Gson gson;
    ArrayList<Person> people = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();
    int bound;

    /**
     * Constructor for Fill object
     * @param request Fill request to initialize public FillRequest member
     */
    public Fill(FillRequest request)
    {
        this.request = request;
        this.db = new Database();
        people_count = 1;
        event_count = 0;

        //create gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    /**
     * Fills the database with generated data
     * @throws DataAccessException Issue with database access
     */
    public void doFill() throws DataAccessException
    {
        //first check for any data under provided username and clear it
        try
        {
            //open db connection
            Connection connection = db.openConnection();
            db.userDAO = new UserDAO(connection);
            db.personDAO = new PersonDAO(connection);
            db.authTokenDAO = new AuthTokenDAO(connection);
            db.eventDAO = new EventDAO(connection);
            //check if user exists inside of db
            if (!db.userDAO.contains(request.getUserName()))
            {
                throw new DataAccessException("User is not registered.");
            }

            //delete data that matches username
            db.personDAO.delete(request.getUserName());
            db.authTokenDAO.delete(request.getUserName());
            db.eventDAO.delete(request.getUserName());

            //get the user
            User user = db.userDAO.getUser(request.getUserName());
            //create new person for user and use user info
            Person userPerson = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(),
                    user.getLastName(), user.getGender(), null, null, null);

            //create birth event for user person, 1997 is used as the birth year
            Event userEvent = randomLocation();
            userEvent.updateEvent(UUID.randomUUID().toString(), request.getUserName(),
                    userPerson.getPersonID(), "Birth", DEFAULT_USER_BIRTH_YEAR);

            //add userPerson and userEvent to prospective arrays
            people.add(userPerson);
            events.add(userEvent);


            //create family members and their events
            createGenerations(userPerson, request.getGenerations(), DEFAULT_USER_BIRTH_YEAR);

            //add family members to database
            for (Person person : people)
            {
                db.personDAO.insert(person);
            }

            //add events to database
            for (Event event : events)
            {
                db.eventDAO.insert(event);
            }

            db.closeConnection(true);

            result = new FillResult("Successfully added " + people.size() + " persons and " + events.size() +
                    " events to the database.", true);

        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
            result = new FillResult("\"Error: " + e.toString() + "\"", false);
            throw new DataAccessException(e.getMessage());
        }
        catch (IOException e)
        {
            throw new DataAccessException("Problem with IOException stuff");
        }
    }

    /**
     * Recursive function to create generations and events based off of number provided.
     * @param person the first person provided is the "root" person or the user.
     */
    private void createGenerations(Person person, int generations, int startYear) throws IOException
    {
        if (generations != 0)
        {
            generations--;

            //get random names
            String firstname = randomName("json/mnames.json");
            String lastname = randomName("json/snames.json");
            Person father = new Person(firstname, lastname, "m");

            firstname = randomName("json/fnames.json");
            lastname = randomName("json/snames.json");
            Person mother = new Person(firstname, lastname, "f");

            father.setPersonID(UUID.randomUUID().toString());
            father.setAssociatedUsername(request.getUserName()); //associated username
            mother.setPersonID(UUID.randomUUID().toString());
            mother.setAssociatedUsername(request.getUserName());

            //add parents to array and set ID's
            people.add(father);
            person.setFatherID(father.getPersonID());
            people.add(mother);
            person.setMotherID(mother.getPersonID());

            setSpousePair(father, mother);

            //generate random parent birth dates
            int fatherBirth = genParentBirthYear(startYear, false);
            int motherBirth = genParentBirthYear(startYear, true);

            //create birth of both parents based off of dates
            createBirth(father, fatherBirth);
            createBirth(mother, motherBirth);

            //generate random death dates based on latest event and birth dates and create events
            int fatherDeath = createDeathDate(fatherBirth, startYear);
            int motherDeath = createDeathDate(motherBirth, startYear);
            createDeath(father, fatherDeath);
            createDeath(mother, motherDeath);

            createMarriage(father, mother, fatherBirth, motherBirth, fatherDeath, motherDeath);

            //recursively create another generation for each parent
            createGenerations(father, generations, fatherBirth);
            createGenerations(mother,generations, motherBirth);
        }
    }

    /**
     * I have no idea why I made this a function, but here we are.
     * @param father Father person object
     * @param mother Mother person object
     */
    private void setSpousePair(Person father, Person mother)
    {
        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());
    }

    /**
     * Creates birth for provided person. Marriages are created separately.
     * @param person Provided person
     * @param birthYear Provided birth year
     * @throws IOException Issues with I/O
     */
    private void createBirth(Person person, int birthYear) throws IOException
    {
        Event birth = randomLocation();

        //create birth event
        birth.updateEvent(UUID.randomUUID().toString(), request.getUserName(),
                person.getPersonID(), "Birth", birthYear);

        events.add(birth);
    }

    /**
     * Gets a random name using provided json files
     * @param path Path to json file
     * @return Random name
     * @throws IOException Issues with I/O
     */
    private String randomName(String path) throws IOException
    {
        Reader reader = Files.newBufferedReader(Paths.get(path));
        JsonObject object1 = gson.fromJson(reader, JsonObject.class);
        ArrayList<?> names = gson.fromJson(object1.get("data"), ArrayList.class);
        Random rand = new Random();
        return (String)names.get(rand.nextInt(names.size()));
    }

    /**
     * Gets a random location from the provided json file
     * @return A random event with location data filled in
     * @throws IOException Issues with I/O
     */
    private Event randomLocation() throws IOException
    {
        Gson g = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("json/locations.json"));

        //convert json array to list
        JsonObject object1 = gson.fromJson(reader, JsonObject.class);
        List<Event> locations = new Gson().fromJson(object1.get("data"), new TypeToken<List<Event>>() {}.getType());
        Random rand = new Random();
        return locations.get(rand.nextInt(locations.size()));
    }

    /**
     * Creates a parent birth year compatible with child birth requirements
     * @param childBirthYear Child birth year
     * @param female Whether or not parent is female
     * @return Birth year
     */
    private int genParentBirthYear(int childBirthYear, boolean female)
    {
        Random rand = new Random();
        if (female)
        {
            return childBirthYear - (rand.nextInt((MAX_BIRTH_AGE - MIN_BIRTH_AGE) + 1) + MIN_BIRTH_AGE);
        }
        else
        {
            return childBirthYear - (rand.nextInt((MAX_AGE - MIN_BIRTH_AGE) + 1) + MIN_BIRTH_AGE);
        }
    }

    /**
     * Creates two marriage events with same location and dates for a couple
     * @param father Father object
     * @param mother Mother object
     * @param fatherBirth Father birth date
     * @param motherBirth Mother birth date
     * @param fatherDeath Father death date
     * @param motherDeath Mother death date
     * @throws IOException Issues with I/O
     */
    private void createMarriage(Person father, Person mother, int fatherBirth, int motherBirth,
                               int fatherDeath, int motherDeath) throws IOException
    {
        Event fatherMarriage = randomLocation();
        Event motherMarriage = new Event(fatherMarriage);

        //get youngest parent
        int latestBirth = Math.max(fatherBirth, motherBirth);
        int earliestDeath = Math.min(fatherDeath, motherDeath);

        //generate random marriage date
        int date = genMarriageDate(latestBirth, earliestDeath);

        //add to marriage event and add to events array
        fatherMarriage.updateEvent(UUID.randomUUID().toString(), father.getAssociatedUsername(),
                father.getPersonID(), "Marriage", date);
        motherMarriage.updateEvent(UUID.randomUUID().toString(), mother.getAssociatedUsername(),
                mother.getPersonID(), "Marriage", date);
        events.add(fatherMarriage);
        events.add(motherMarriage);
    }

    /**
     * Create a marriage date based on birth date and death date
     * @param latestBirth Most recent birth
     * @param earliestDeath First occuring death
     * @return Generated random marriage date
     */
    private int genMarriageDate(int latestBirth, int earliestDeath)
    {
        Random rand = new Random();
        int valid_age = latestBirth + MIN_BIRTH_AGE;
        return rand.nextInt((earliestDeath - valid_age) + 1) + valid_age;
    }

    /**
     * Create a death event for provided person
     * @param person Person
     * @param deathYear The generated year of death
     * @throws IOException Issues with I/O
     */
    private void createDeath(Person person, int deathYear) throws IOException
    {
        Event death = randomLocation();
        death.updateEvent(UUID.randomUUID().toString(), request.getUserName(), person.getPersonID(),
                "Death", deathYear);
        events.add(death);
    }

    /**
     * Generate random death date based on birth year and any childbirth restrictions
     * @param birthYear Year of birth
     * @param childBirth Year of childbirth
     * @return Generated death date
     */
    private int createDeathDate(int birthYear, int childBirth)
    {
        //death must be after childbirth and not more than 120 years after birth year
        Random rand = new Random();
        int maxDeath = Math.min(birthYear + MAX_AGE, CURRENT_YEAR);
        return rand.nextInt((maxDeath - childBirth) + 1) + childBirth;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }
    public ArrayList<Event> getEventsArray() {
        return events;
    }
}
