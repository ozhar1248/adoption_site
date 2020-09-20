package animalsBeans;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "animal")
@SessionScoped
public class Animal implements Serializable {
    private String name;
    private String category;
    private String area;
    private Integer age;
    private Integer weight;
    private String city;
    private String shortDescription;
    private String longDescription;
    private String[] communications;
    private String[] imagesNames;
    private String id;
    private java.sql.Date date;
    @Inject
    private User user;
    @Inject
    private DBLoader dataBase;
    @Inject
    private AnimalIDGenerator idGenerator;
    
    
    // pay attention that communications can be set to null during application
    public Animal()
    {
        reset();
    }
    
    public void reset()
    {
        name = null;
        category = null;
        area = null;
        age = null;
        weight = null;
        city = null;
        shortDescription = null;
        longDescription = null;
        id = null;
        date = null;
        communications = new String[UtilsFunc.NUMBER_OF_CONTACTS];
        imagesNames = new String[UtilsFunc.NUMBER_IMAGES_FOR_ANIMAL];
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAge() {
        if (age == null || age == -1)
        {
            return null;
        }
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Integer getWeight() {
        if (age == null || weight == -1)
        {
            return null;
        }
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public String[] getCommunications() {
        return communications;
    }
    
    public String getCommunicationByIndex(int index)
    {
        return communications[index];
    }

    public void setCommunications(String[] communications) {
        this.communications = communications;
    }
     
    public String getImageName(int index) {
        return imagesNames[index];
    }

    public void setImageName(int index, String name)
    {
        imagesNames[index] = name;
    }
    
    public void resetNames()
    {
        int len = imagesNames.length;
        for (int i = 0 ; i<len ; ++i)
        {
            imagesNames[i] = null;
        }
    }
    
    public String register() throws SQLException
    {
        
        String commandAnimal = "INSERT INTO ANIMAL "
                + "(ANIMAL_ID, ANIMAL_NAME, CATEGORY, AGE, WEIGHT, CITY) "
                + "VALUES(?,?,?,?,?,?)";
        DBLoader.Statement statement = dataBase.getStatement(commandAnimal);

        id = idGenerator.generateID();
        statement.addString(id);
        statement.addString(name);
        statement.addString(category);
        statement.addInt((age != null)? age : -1);
        statement.addInt((weight != null)? weight : -1);
        statement.addString(city);
        dataBase.executeUpdate(statement);
        
        String commandOffer = "INSERT INTO OFFER "
                + "(EMAIL, ANIMAL_ID,DATE_PUBLICATION,SHORT_DESCRIPTION,LONG_DESCRIPTION,IS_RELEVANT,CONTACT1,CONTACT2,CONTACT3) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        statement = dataBase.getStatement(commandOffer);
        
        Date utilDate = Calendar.getInstance().getTime();

        statement.addString(user.getEmail());
        statement.addString(id);
        statement.addDate(new java.sql.Date(utilDate.getTime()));
        statement.addString(shortDescription);
        statement.addString(longDescription);
        statement.addBoolean(true);
        
        int numOfCom = communications.length;
        statement.addString((numOfCom > 0)? communications[0] : null);
        statement.addString((numOfCom > 1)? communications[1] : null);
        statement.addString((numOfCom > 2)? communications[2] : null);

        dataBase.executeUpdate(statement);
        
        return "images_upload.xhtml";
    }
    
}
