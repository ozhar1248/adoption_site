package animalsBeans;

import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@Named("user")
@SessionScoped
public class User implements Serializable{
    
    private String firstName;
    private String lastName;
    private String email;
    private String telephone1;
    private String telephone2;
    private String aboutMe;
    private Part image;
    private String imageName;
    private String password;
    private String passwordConfirm;
    @Inject
    private DBLoader dataBase;
    
    public User()
    {
        reset();
    }
    
    public void reset()
    {
        firstName = null;
        lastName = null;
        email = null;
        telephone1 = null;
        telephone2 = null;
        aboutMe = null;
        image = null;
        imageName = null;
        password = null;
        passwordConfirm = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setImage(Part image) throws IOException
    {
        this.image = image;
        imageName = email+UtilsFunc.getExtension(image);
        saveImage();
    }
    
    public Part getImage() {
        return image;
    }

    public String getImageName() {
        if (imageName == null)
        {
            return null;
        }
        return UtilsFunc.RELATIVE_IMAGE_PATH+imageName;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    public void addEntry() throws SQLException, IOException
    {
        String sql_command = 
                "INSERT INTO USERS"
                + "(EMAIL,PASSWORD,FIRST_NAME,LAST_NAME,PHONE1,PHONE2,ABOUT_ME,IMAGE_NAME)"
                + " VALUES(?,?,?,?,?,?,?,?)";
        
        DBLoader.Statement statement = dataBase.getStatement(sql_command);
        statement.addString(email);
        statement.addString(password);
        statement.addString(firstName);  
        statement.addString(telephone1);
        statement.addString(telephone2);
        statement.addString(aboutMe);
        statement.addString(imageName);
        dataBase.executeUpdate(statement);
    }
    
    public String submitImage() throws SQLException
    {
        String command = "UPDATE USERS SET IMAGE_NAME = ? WHERE EMAIL = ? ";
        DBLoader.Statement statement = dataBase.getStatement(command);
        statement.addString(imageName);
        statement.addString(email);
        dataBase.executeUpdate(statement);
        return "menu.xhtml";
    }
    
    public void saveImage() throws IOException
    {   
        UtilsFunc.saveImage(image, imageName);
    }
    
    public String submit() throws SQLException,  IOException 
    {
        addEntry();
        return "uploadImageUser.xhtml";
    }
    
    public String submitBack()
    {
        reset();
        return "index.xhtml";
    }
    
    private void fillUser() throws SQLException
    {
        if (email == null)
        {
            return;
        }
        String command = "SELECT * FROM USERS WHERE EMAIL = ?";
        
        DBLoader.Statement statement = dataBase.getStatement(command);

        statement.addString(email);
        DBLoader.ResultQuary result = dataBase.executeQuary(statement);
        if (!result.next())
        {
            return;
        }
        firstName = result.getStringForAttribute("FIRST_NAME");
        lastName = result.getStringForAttribute("LAST_NAME");
        telephone1 = result.getStringForAttribute("PHONE1");
        telephone2 = result.getStringForAttribute("PHONE2");
        aboutMe = result.getStringForAttribute("ABOUT_ME");
        imageName = result.getStringForAttribute("IMAGE_NAME");
    }

    public String login() throws SQLException
    {
        fillUser();
        
        return "menu.xhtml";
    }
    
    public List<String> communications()
    {
        if (email == null)
        {
            return null;
        }
        
        List<String> newList = new ArrayList<>();
        if (telephone1 != null && !telephone1.equals(""))
        {
            newList.add(telephone1);
        }
        if (telephone2 != null && !telephone2.equals(""))
        {
            newList.add(telephone2);
        }
        newList.add(email);
        return newList;
    }
    
}
