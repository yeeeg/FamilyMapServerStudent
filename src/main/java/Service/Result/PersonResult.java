package Service.Result;

import Model.Person;
import Model.User;

public class PersonResult
{
    String associatedUsername;
    String firstName;
    String lastName;
    String gender;
    String personID;
    String fatherID;
    String motherID;
    String spouseID;
    String message;
    boolean success;

    public PersonResult(Person person, boolean success)
    {
        this.associatedUsername = person.getAssociatedUsername();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.personID = person.getPersonID();
        this.fatherID = person.getFatherID();
        this.motherID = person.getMotherID();
        this.spouseID = person.getSpouseID();
        message = null;
        this.success = success;
    }
    public PersonResult(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
