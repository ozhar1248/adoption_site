package animalsBeans;

import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "category")
@ViewScoped
public class Category implements Serializable {
    private List<String> categories;
    private boolean loaded;
    @Inject
    private DBLoader dataBase;
    
    public Category() {
        loaded = false;
        categories = new ArrayList<>();
        categories.add("");
    }

    private void loadCategories() throws SQLException
    {
        String command = "SELECT NAME FROM CATEGORY";
        
        DBLoader.Statement statement = dataBase.getStatement(command);
        DBLoader.ResultQuary result = dataBase.executeQuary(statement);
        while (result.next())
        {
            categories.add(result.getStringForAttribute("NAME"));
        }
        loaded = true;
    }
    
    public List<String> getCategories() throws SQLException
    {
        if (!loaded)
        {
            loadCategories();
        }
        return categories;
    }
}
