package animalsBeans;

import java.sql.SQLException;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

@Named(value = "validators")
@ApplicationScoped
public class Validators 
{
    @Inject
    private DBLoader dataBase;
    
    private boolean isEmailCorrect(String email)
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    
    private boolean isEmailExist(String email) throws SQLException
    {
        String sql_command = "SELECT EMAIL FROM USERS WHERE EMAIL = ?";
        DBLoader.Statement statement = dataBase.getStatement(sql_command);
        statement.addString(email);
        return dataBase.executeQuary(statement).next();
    }
    
    public void validateEmail(FacesContext context, UIComponent comp, Object value) throws SQLException
    {
        String email = (String) value;
        String errorMsg = null;
        boolean emailExist = isEmailExist(email);
        boolean emailCorrect = isEmailCorrect(email);
        if (emailExist || !emailCorrect)
        {
            if (!emailCorrect)
            {
                errorMsg = "Email pattern is not correct";
            }
            if (emailExist)
            {
                errorMsg = "Email already exist";
            }
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage(errorMsg);
            context.addMessage(comp.getClientId(context), message);
        }
    }
    
    public void validateConfirmPassword(FacesContext context, UIComponent comp, Object value)
    {
        String passwordNew = (String) value;
        UIInput passwordInput = (UIInput) comp.findComponent("Password");
        String passwordOld = (String) passwordInput.getLocalValue();
        
        if (passwordOld == null || !passwordNew.equals(passwordOld))
        {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage("Password doesn't match");
            context.addMessage(comp.getClientId(context), message);
        }
    }
    
    public void validatePhone(FacesContext context, UIComponent comp, Object value)
    {
        String phone = (String) value;
        if (phone == null)
        {
            return;
        }
        
        boolean valid = true;
        int count = 0;
        int len = phone.length();
        boolean separator = false;
        
        while (count < len)
        {
            if (phone.charAt(count) == '-' && !separator) 
            {
                if (count != 3 && count != 2)
                {
                    valid = false;
                    break;
                }
                separator = true;
                ++count;
                continue;
            }
            if (!Character.isDigit(phone.charAt(count)))
            {
                valid = false;
                break;
            }
            ++count;
        }

        if (!valid)
        {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage("Phone is not valid");
            context.addMessage(comp.getClientId(context), message);
        }
    }
    
    public void validateImage(FacesContext context, UIComponent comp, Object value)
    {
        Part image = (Part)value;
        if (image == null)
        {
            return;
        }
        
        String ext = UtilsFunc.getExtension(image);
        boolean valid = ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png") || ext.equals(".gif");
        
        if (!valid)
        {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage("Please choose an image file (jpg, gif, png)");
            context.addMessage(comp.getClientId(context), message);
        }
    }
    
    private boolean isLoginValid(String email, String password) throws SQLException
    {
        if (email == null || password == null )
        {
            return false;
        }
        String sql_command = "SELECT PASSWORD FROM USERS WHERE EMAIL = ? ";
        
        DBLoader.Statement statement = dataBase.getStatement(sql_command);
        statement.addString(email);
        DBLoader.ResultQuary result = dataBase.executeQuary(statement);
        if (!result.next())
        {
            return false;
        }   
        String passwordRight = result.getStringForAttribute("PASSWORD");
        return passwordRight.equals(password);
    }
    
    public void validateLogin(FacesContext context, UIComponent comp, Object value) throws SQLException
    {
        String password = (String) value;
        UIInput textInput = (UIInput) comp.findComponent("email_login");
        String email = (String) textInput.getLocalValue();
        
        if (!isLoginValid(email,password))
        {
            ((UIInput) comp).setValid(false);
            FacesMessage message = new FacesMessage("Email and password are not correct");
            context.addMessage(comp.getClientId(context), message);
        }
    }
    
    public String findArea(String city) throws SQLException
    {
        if (city == null)
        {
            return null;
        }
        
        String area = null;
        String command = "SELECT AREA.NAME FROM AREA natural join CITY WHERE CITY.CITY_NAME = ? ";
        DBLoader.Statement statement = dataBase.getStatement(command);

        statement.addString(city);
        DBLoader.ResultQuary result = dataBase.executeQuary(statement);
        if (result.next())
        {
            area = result.getStringForAttribute("NAME");
        }
        return area;
    }
}
