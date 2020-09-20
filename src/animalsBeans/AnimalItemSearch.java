package animalsBeans;

import java.sql.Date;

public class AnimalItemSearch 
{
    private String id;
    private String name;
    private Date date;
    private int age;
    private int weight;
    private String area;
    private String city;
    private String category;
    private String mainImageName;
    private String shortDescription;

    public AnimalItemSearch(String _id, String _name, Date _date, int _age, int _weight, String _area, 
            String _city, String _image, String _category, String _shortDesc)
    {
        id = _id;
        name = _name;
        date = _date;
        age = _age;
        weight = _weight;
        area = _area;
        city = _city;
        category = _category;
        mainImageName = _image;
        shortDescription = _shortDesc;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAge() {
        if (age == -1)
        {
            return null;
        }
        else 
        {
            return new Integer(age).toString();
        }
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getWeight() {
        if (weight == -1)
        {
            return null;
        }
        else 
        {
            return new Integer(weight).toString();
        }
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMainImageName() {
        return mainImageName;
    }
    
    public String getMainImageDisplay()
    {
        return UtilsFunc.RELATIVE_IMAGE_PATH+mainImageName;
    }

    public void setMainImageName(String mainImageName) {
        this.mainImageName = mainImageName;
    } 

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}

