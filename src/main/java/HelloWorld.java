import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import Service.Clear;
import Service.Request.RegisterRequest;

public class HelloWorld {
    public static void main(String args[]) throws DataAccessException
    {
        System.out.println("Testing...");
        /*RegisterRequest rq = new RegisterRequest("{\n" +
                "\t\"username\":\"yeg\",\n" +
                "\t\"password\":\"1234\",\n" +
                "\t\"email\":\"lost@sea.com\",\n" +
                "\t\"firstName\":\"jeremiah\",\n" +
                "\t\"lastName\":\"brown\",\n" +
                "\t\"gender\":\"m\"\n" +
                "}");

        rq.createUser();*/

        //test person insert
        /*Database db = new Database();
        db.openConnection();
        PersonDAO dao = new PersonDAO(db.getConn());
        Person person = new Person("jerrybro", "jeremiah", "brown", "m");
        dao.insert(person);
        db.closeConnection(true);*/
    }
}
