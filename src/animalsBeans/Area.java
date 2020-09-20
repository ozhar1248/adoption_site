package animalsBeans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "area")
@ViewScoped
public class Area implements Serializable
{
    private boolean loaded;
    private List<String> areas;
    @Inject
    private DBLoader dataBase;
    
    public Area() 
    {
        loaded = false;
        areas = new ArrayList<>();
        areas.add("");
    }
    
    private void loadAreas() throws SQLException
    {
        String command = "SELECT NAME FROM AREA";
        DBLoader.Statement statement = dataBase.getStatement(command);
        DBLoader.ResultQuary result = dataBase.executeQuary(statement);
        while (result.next())
        {
            areas.add(result.getStringForAttribute("NAME"));
        }
        loaded = true;
    }
    
    public List<String> getAreas() throws SQLException
    {
        if (!loaded)
        {
            loadAreas();
        }
        return areas;
    }
    
}