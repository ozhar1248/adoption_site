package animalsBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "imagesList")
@ViewScoped
public class ImagesList implements Serializable{
    private List<AnimalItemSearch> animals;
    private boolean arranged;
    private String chosenCategory;
    private String chosenArea;
    @Inject
    private DBLoader db;
    @Inject
    private NavigationParameters navPar;
    @Inject
    private Validators utils;
    
    public ImagesList() {
        animals = new ArrayList<>();
        arranged = false;
        chosenCategory = null;
        chosenArea = null;
    }

    public String getChosenCategory() {
        return chosenCategory;
    }

    public void setChosenCategory(String chosenCategory) {
        this.chosenCategory = chosenCategory;
        arranged = false;
    }

    public String getChosenArea() {
        return chosenArea;
    }

    public void setChosenArea(String chosenArea) {
        this.chosenArea = chosenArea;
        arranged = false;
    }
    
    private void loadAnimals() throws SQLException
    {
        animals.clear();
        String command = "select ANIMAL.ANIMAL_ID , ANIMAL.ANIMAL_NAME, OFFER.DATE_PUBLICATION , ANIMAL.AGE, "
                + "ANIMAL.WEIGHT, ANIMAL.CITY, ANIMAL.CATEGORY, "
                + "OFFER.IS_RELEVANT , OFFER.SHORT_DESCRIPTION "
                + " FROM ANIMAL natural join OFFER ";
        
        DBLoader.Statement statement = db.getStatement(command);

        DBLoader.ResultQuary result1 = db.executeQuary(statement);
        while (result1.next())
        {
            if (result1.getBooleanForAttribute("IS_RELEVANT"))
            {
                String city = result1.getStringForAttribute("CITY");
                String area = utils.findArea(city);
                
                String command2 = "SELECT IMAGE.IMAGE_NAME FROM ANIMAL natural join IMAGE "
                        + "WHERE IMAGE.SERIAL = ? "
                        + "AND ANIMAL.ANIMAL_ID = ? ";
                DBLoader.Statement statement2 = db.getStatement(command2);
                statement2.addInt(0);
                statement2.addString(result1.getStringForAttribute("ANIMAL_ID"));
                DBLoader.ResultQuary result2 = db.executeQuary(statement2);
                
                String imageName;
                if (result2.next())
                {
                    imageName = result2.getStringForAttribute("IMAGE_NAME");
                }
                else
                {
                    imageName = null;
                }
                
                animals.add(new AnimalItemSearch(result1.getStringForAttribute("ANIMAL_ID"),
                    result1.getStringForAttribute("ANIMAL_NAME"),
                    result1.getDateForAttribute("DATE_PUBLICATION"),
                    result1.getIntForAttribute("AGE"),
                    result1.getIntForAttribute("WEIGHT"),
                    area,
                    city,
                    imageName,
                    result1.getStringForAttribute("CATEGORY"),
                    result1.getStringForAttribute("SHORT_DESCRIPTION")
                ));
            }
        }
        
    }
    
    private void filterCategory()
    {
        int len = animals.size();
        int i = 0;
        while (i < len)
        {
            String current = animals.get(i).getCategory();
            if (current == null || !current.equals(chosenCategory))
            {
                animals.remove(i);
                --len;
                continue;
            }
            ++i;
        }
    }
    
    private void filterArea()
    {
        if (chosenArea.equals(""))
        {
            return;
        }
        int len = animals.size();
        int i = 0;
        while (i < len)
        {
            String area = animals.get(i).getArea();
            if (area == null || !area.equals(chosenArea))
            {
                animals.remove(i);
                --len;
                continue;
            }
            ++i;
        }
    }
    
    private class sortByDate implements Comparator<AnimalItemSearch>
    {
        @Override
        public int compare(AnimalItemSearch a, AnimalItemSearch b)
        {
            int c = a.getDate().compareTo(b.getDate());
            return c;
        }
    }
    
    public List<AnimalItemSearch> getAnimals() throws SQLException
    {
        if (!arranged)
        {
            loadAnimals();
            Collections.sort(animals,new sortByDate());
            arranged = true;
        }
        if (chosenCategory != null && chosenCategory.length() != 0)
        {
            filterCategory();
        }
        if (chosenArea != null && chosenArea.length() != 0)
        {
            filterArea();
        }
        return animals;
    }

    public String chooseAnimal()
    {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	String animalID = params.get("action");
        navPar.setChosenAnimalID(animalID);
        return "offerAdvertise.xhtml";
    }
}
