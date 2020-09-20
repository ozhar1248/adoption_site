package animalsBeans;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "city")
@ViewScoped
public class City implements Serializable{
    private List<String> cities;
    private String selectedArea;
    @Inject
    private DBLoader db;
    
    
    public City() {
        cities = new ArrayList<>();
    }

    public String getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(String selectedArea) {
        this.selectedArea = selectedArea;
    }

    public void changeArea() throws SQLException
    {
        cities.clear();

        String command = "SELECT (CITY_NAME) FROM CITY "
                + "WHERE AREA_NAME = ? ";

        DBLoader.Statement statement = db.getStatement(command);
        statement.addString(selectedArea);
        DBLoader.ResultQuary result = db.executeQuary(statement);
        while (result.next())
        {
            cities.add(result.getStringForAttribute("CITY_NAME"));
        }
    }
    
    public List<String> citiesForArea() throws SQLException
    {
        changeArea();
        return cities;
    }
    
    public List<String> allCities() throws SQLException
    {
        List<String> all_cities = new ArrayList<>();
        
        String command = "SELECT CITY_NAME FROM CITY ";
        
        DBLoader.Statement statement = db.getStatement(command);
        
        DBLoader.ResultQuary result = db.executeQuary(statement);
        while (result.next())
        {
            all_cities.add(result.getStringForAttribute("CITY_NAME"));
        }
        
        return all_cities;
    }
}
