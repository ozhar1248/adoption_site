package animalsBeans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "offerShow")
@ViewScoped
public class OfferShow implements Serializable{
    Animal animal;
    private String id;
    private String firstNameUser;
    private String profileImageUser;
    private String aboutMe;
    private String[] contacts;
    private List<String> images;
    private boolean loaded;
    @Inject
    private DBLoader db;
    @Inject
    private NavigationParameters navPar;
    @Inject
    private Validators utils;

    
    public OfferShow() {
        animal = new Animal();
        contacts = new String[UtilsFunc.NUMBER_OF_CONTACTS];
        images = new ArrayList<>();
        loaded = false;
    }
    
    public void loadAnimalFromDB() throws SQLException
    {
        if (loaded)
        {
            return;
        }
	id = navPar.getChosenAnimalID();
        
        String command = "select ANIMAL.ANIMAL_ID , ANIMAL.ANIMAL_NAME , ANIMAL.CATEGORY , "
                + "ANIMAL.AGE , ANIMAL.WEIGHT , ANIMAL.CITY , "
                + "OFFER.DATE_PUBLICATION , OFFER.SHORT_DESCRIPTION , OFFER.LONG_DESCRIPTION , "
                + "OFFER.CONTACT1 , OFFER.CONTACT2 , OFFER.CONTACT3 , USERS.FIRST_NAME , "
                + "USERS.IMAGE_NAME, USERS.ABOUT_ME "
                + " from (ANIMAL natural join OFFER) natural join USERS "
                + "where ANIMAL.ANIMAL_ID = ?";
        DBLoader.Statement statement = db.getStatement(command);

        statement.addString(id);
        DBLoader.ResultQuary result = db.executeQuary(statement);
        if (result.next())
        {
            String city = result.getStringForAttribute("CITY");
            String area = utils.findArea(city);

            animal.setId(id);
            animal.setName(result.getStringForAttribute("ANIMAL_NAME"));
            animal.setCategory(result.getStringForAttribute("CATEGORY"));
            animal.setAge(result.getIntForAttribute("AGE"));
            animal.setWeight(result.getIntForAttribute("WEIGHT"));
            animal.setArea(area);
            animal.setCity(city);
            animal.setDate(result.getDateForAttribute("DATE_PUBLICATION"));
            animal.setShortDescription(result.getStringForAttribute("SHORT_DESCRIPTION"));
            animal.setLongDescription(result.getStringForAttribute("LONG_DESCRIPTION"));
            firstNameUser = result.getStringForAttribute("FIRST_NAME");
            profileImageUser = result.getStringForAttribute("IMAGE_NAME");
            aboutMe = result.getStringForAttribute("ABOUT_ME");
            contacts[0] = result.getStringForAttribute("CONTACT1");
            contacts[1] = result.getStringForAttribute("CONTACT2");
            contacts[2] = result.getStringForAttribute("CONTACT3");
        }
        
        command = "SELECT DISTINCT IMAGE.IMAGE_NAME FROM IMAGE WHERE IMAGE.ANIMAL_ID = ? ";
        statement = db.getStatement(command);

        statement.addString(id);
        result = db.executeQuary(statement);
        while (result.next())
        {
            images.add(UtilsFunc.RELATIVE_IMAGE_PATH+result.getStringForAttribute("IMAGE_NAME"));
        }
        loaded = true;
    }
    
    public String getName() 
    {
        return animal.getName();
    }
    
    public String getShortDescription() {
        return animal.getShortDescription();
    }
    
    public Integer getAge()
    {
        if (animal.getAge() == null)
        {
            return null;
        }
        return animal.getAge();
    }
    
    public Integer getWeight()
    {
        if (animal.getWeight() == null)
        {
            return null;
        }
        return animal.getWeight();
    }
    
    public String getCity()
    {
        return animal.getCity();
    }
    
    public String getCategory()
    {
        return animal.getCategory();
    }
    
    public String getDate()
    {
        return animal.getDate().toString();
    }
    
    public String getLongDescription()
    {
        return animal.getLongDescription();
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getProfileImageUser() {
        if (profileImageUser == null)
        {
            return null;
        }
        return UtilsFunc.RELATIVE_IMAGE_PATH+profileImageUser;
    }
    
    public String[] getContacts()
    {
        return contacts;
    }

    public List<String> getImages() throws SQLException
    {
        if (!loaded)
        {
            loadAnimalFromDB();
        }
        return images;
    }
    
    
    
}
