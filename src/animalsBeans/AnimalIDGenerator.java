package animalsBeans;

import java.sql.SQLException;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Named(value = "animalIDGenerator")
@ApplicationScoped
public class AnimalIDGenerator {
    @Inject
    private DBLoader dataBase;

    public String generateID() throws SQLException
    {
        long current = 0;
        long newnum = 0;
        String command = "SELECT * FROM MAX_ANIMAL_ID ";
        
        DBLoader.Statement statement = dataBase.getStatement(command);
        DBLoader.ResultQuary result = dataBase.executeQuary(statement);
        if (result.next())
        {
            current = result.getLongForAttribute("NUMBER");
        }
        
        newnum = current+1;
        command = "UPDATE MAX_ANIMAL_ID SET NUMBER = ? WHERE NUMBER = ?";
        statement = dataBase.getStatement(command);
        statement.addLong(newnum);
        statement.addLong(current);
        dataBase.executeUpdate(statement);
        
        return new Long(newnum).toString();
    }
    
}
