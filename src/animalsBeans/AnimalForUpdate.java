package animalsBeans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "animalForUpdate")
@ViewScoped
public class AnimalForUpdate implements Serializable {
    private Map<String,String> animalsID; //map between options to choose and id of animals
    @Inject
    private User user;
    @Inject
    private DBLoader db;
    List<String> names; //options to choose
    private List<String> relevantOptions;
    private String relevantChoice;
    private String shortDesc;
    private String[] contacts;
    @Inject
    private Animal animal;
    private String id;
    private String userChoise;

    public AnimalForUpdate() {
        animalsID = new HashMap<>();
        relevantOptions = new ArrayList<>();
        relevantOptions.add("RELEVANT");
        relevantOptions.add("NOT RELEVANT");
        names = null;
        contacts = new String[UtilsFunc.NUMBER_OF_CONTACTS];
    }

    public String getName() {
        return animal.getName();
    }

    public void setName(String name) {
        animal.setName(name);
    }

    public Integer getAge() {
        return animal.getAge();
    }

    public void setAge(Integer age) {
        animal.setAge(age);
    }

    public Integer getWeight() { 
        return animal.getWeight();
    }

    public void setWeight(Integer weight) {
        animal.setWeight(weight);
    }

    public String getCity() {
        return animal.getCity();
    }

    public void setCity(String city) {
        animal.setCity(city);
    }

    public List<String> getRelevantOptions() {
        return relevantOptions;
    }

    public String getRelevantChoice() {
        return relevantChoice;
    }

    public void setRelevantChoice(String relevantChoice) {
        this.relevantChoice = relevantChoice;
    }

    public String getShortDesc() {
        return animal.getShortDescription();
    }

    public void setShortDesc(String shortDesc) {
        animal.setShortDescription(shortDesc);
    }

    public String getLongDesc() {
        return animal.getLongDescription();
    }

    public void setLongDesc(String longDesc) {
        animal.setLongDescription(shortDesc);
    }

    public String getContact1() {
        return contacts[0];
    }

    public void setContact1(String contact1) {
        contacts[0] = contact1;
    }

    public String getContact2() {
        return contacts[1];
    }

    public void setContact2(String contact2) {
        contacts[1] = contact2;
    }

    public String getContact3() {
        return contacts[2];
    }

    public void setContact3(String contact3) {
        contacts[2] = contact3;
    }
    
    public String getUserChoise() {
        return userChoise;
    }
    
    public void setUserChoise(String userChoise) throws SQLException
    {
        if (userChoise == null || userChoise.length() == 0)
        {
            animal.reset();
            contacts[0] = null;
            contacts[1] = null;
            contacts[2] = null;
            return;
        }
        
        if (userChoise.equals(getUserChoise()))
        {
            return;
        }
        this.userChoise = userChoise;
        
        id = animalsID.get(userChoise);
        animal.setId(id);
        String command = "select ANIMAL.ANIMAL_ID , ANIMAL.ANIMAL_NAME , ANIMAL.AGE , ANIMAL.WEIGHT , ANIMAL.CITY , "
                + "OFFER.IS_RELEVANT , OFFER.SHORT_DESCRIPTION , OFFER.LONG_DESCRIPTION , "
                + "OFFER.CONTACT1 , OFFER.CONTACT2 , OFFER.CONTACT3 "
                + " FROM ANIMAL natural join OFFER"
                + " where ANIMAL.ANIMAL_ID = ?";
        
        DBLoader.Statement statement = db.getStatement(command);

        statement.addString(id);
        DBLoader.ResultQuary result = db.executeQuary(statement);
        if (result.next())
        {
            animal.setName(result.getStringForAttribute("ANIMAL_NAME"));
            animal.setAge(result.getIntForAttribute("AGE"));
            animal.setWeight(result.getIntForAttribute("WEIGHT"));
            animal.setCity(result.getStringForAttribute("CITY"));
            if (result.getBooleanForAttribute("IS_RELEVANT"))
            {
                relevantChoice = relevantOptions.get(0);  
            }
            else
            {
                relevantChoice = relevantOptions.get(1);
            }
            animal.setShortDescription(result.getStringForAttribute("SHORT_DESCRIPTION"));
            animal.setLongDescription(result.getStringForAttribute("LONG_DESCRIPTION"));
            contacts[0] = result.getStringForAttribute("CONTACT1");
            contacts[1] = result.getStringForAttribute("CONTACT2");
            contacts[2] = result.getStringForAttribute("CONTACT3");
            animal.setCommunications(contacts);
        }
    }

    public List<String> listAnimals() throws SQLException
    {
        if (names != null)
        {
            return names;
        }
        names = new  ArrayList<>();
        names.add("");
        String command = "select * from ANIMAL natural join OFFER where "
                + "OFFER.EMAIL = ? ";
        DBLoader.Statement statement = db.getStatement(command);

        statement.addString(user.getEmail());
        DBLoader.ResultQuary result = db.executeQuary(statement);
        int count = 1;
        while (result.next())
        {
            String label = count+". "+result.getStringForAttribute("ANIMAL_NAME");
            animalsID.put(label, result.getStringForAttribute("ANIMAL_ID"));
            names.add(label);
            ++count;
        }
        return names;
    }
    
    public String update() throws SQLException
    {
        String command = "update ANIMAL "
                + "set ANIMAL.ANIMAL_NAME = ? , ANIMAL.AGE = ? , ANIMAL.WEIGHT = ? , ANIMAL.CITY = ? "
                + "where ANIMAL.ANIMAL_ID = ?";
        
        String commandOffer = "update OFFER "
                + "set OFFER.IS_RELEVANT = ? , OFFER.SHORT_DESCRIPTION = ? , OFFER.LONG_DESCRIPTION = ? , "
                + "OFFER.CONTACT1 = ? , OFFER.CONTACT2 = ? , OFFER.CONTACT3 = ?"
                + "where OFFER.ANIMAL_ID = ? ";
        
        DBLoader.Statement statement = db.getStatement(command);
        DBLoader.Statement statementOffer = db.getStatement(commandOffer);

        statement.addString(animal.getName());
        statement.addInt((animal.getAge() != null)? animal.getAge(): -1);
        statement.addInt((animal.getWeight() != null)? animal.getWeight(): -1);
        statement.addString(animal.getCity());
        statement.addString(id);
        db.executeUpdate(statement);

        statementOffer.addBoolean(relevantChoice.equals(relevantOptions.get(0)));
        statementOffer.addString(animal.getShortDescription());
        statementOffer.addString(animal.getLongDescription());
        statementOffer.addString(animal.getCommunicationByIndex(0));
        statementOffer.addString(animal.getCommunicationByIndex(1));
        statementOffer.addString(animal.getCommunicationByIndex(2));
        statementOffer.addString(id);
        db.executeUpdate(statementOffer);   
        return "menu.xhtml";
    }
    
    public String updateAndChangeImages() throws SQLException
    {
        update();
        animal.resetNames();
        
        String command = "SELECT DISTINCT * FROM IMAGE WHERE IMAGE.ANIMAL_ID = ? ";
        DBLoader.Statement statement = db.getStatement(command);

        statement.addString(id);
        DBLoader.ResultQuary result = db.executeQuary(statement);
        while (result.next())
        {
            int index = result.getIntForAttribute("SERIAL");
            animal.setImageName(index, result.getStringForAttribute("IMAGE_NAME"));
        }
        return "images_upload.xhtml";
    }
}
