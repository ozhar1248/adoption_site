package animalsBeans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;

@Named(value = "imagesUploadHandle")
@ViewScoped
public class ImagesUploadHandle implements Serializable{
    @Inject
    private Animal animal;
    @Inject
    private DBLoader dataBase;
    private String[] imagesNamesPreventCache;
    private static int uniqNum;
    private Part[] images;

    private void insertImageDB(String name, int serial, String id) throws SQLException
    {
        String command = "INSERT INTO IMAGE (IMAGE_NAME, ANIMAL_ID, SERIAL) VALUES(?, ?, ?) ";
        DBLoader.Statement statement = dataBase.getStatement(command);
        
        statement.addString(name);
        statement.addString(id);
        statement.addInt(serial);
        dataBase.executeUpdate(statement);
    }
    
    private void removeImageDB(String name) throws SQLException
    {
        String command = "DELETE FROM IMAGE WHERE IMAGE_NAME = ? ";
        DBLoader.Statement statement = dataBase.getStatement(command);
        statement.addString(name);
        dataBase.executeUpdate(statement);
    }
    
    public ImagesUploadHandle() {
        imagesNamesPreventCache = new String[UtilsFunc.NUMBER_IMAGES_FOR_ANIMAL];
        images = new Part[UtilsFunc.NUMBER_IMAGES_FOR_ANIMAL];
        uniqNum = 1;
    }
    
    private void setImage(Part image, int index) throws SQLException, IOException
    {
        images[index] = image;
        String indexString = new Integer(index).toString();
        animal.setImageName(index, animal.getId()+"_"+indexString+UtilsFunc.getExtension(image));

        UtilsFunc.saveImage(images[index], animal.getImageName(index));
        insertImageDB(animal.getImageName(index), index, animal.getId());
    }
    
    private String getImageName(int index) throws IOException
    {
        if (animal.getImageName(index) == null)
        {
            return null;
        }
        
        if (imagesNamesPreventCache[index] != null)
        {
            UtilsFunc.removeImage(imagesNamesPreventCache[index]);
        }
        
        imagesNamesPreventCache[index] = (uniqNum++)+"_"+animal.getImageName(index);
        UtilsFunc.duplicateImage(animal.getImageName(index), imagesNamesPreventCache[index]);
        return UtilsFunc.RELATIVE_IMAGE_PATH+imagesNamesPreventCache[index];
    }

    private void removeImage(int index) throws IOException, SQLException
    {
        if (imagesNamesPreventCache[index] != null)
        {
            UtilsFunc.removeImage(imagesNamesPreventCache[index]);
            imagesNamesPreventCache[index] = null;
        }
        if (animal.getImageName(index) != null)
        {
            removeImageDB(animal.getImageName(index));
            UtilsFunc.removeImage(animal.getImageName(index));
            animal.setImageName(index, null);
            images[index] = null;
        }
    }
    
    public void remove(String index) throws IOException, SQLException
    {
        Integer indexInt = Integer.parseInt(index);
        removeImage(indexInt);
    }
    
    public Part getImage0() {
        return images[0];
    }
    
    public void setImage0(Part image) throws SQLException, IOException
    {
        if (image == null)
        {
            return;
        }
        setImage(image,0);
    }
    
    public String getImageName0() throws IOException
    {
        return getImageName(0);
    }
    
    public Part getImage1() {
        return images[1];
    }
    
    public void setImage1(Part image) throws SQLException, IOException
    {
        if (image == null)
        {
            return;
        }
        setImage(image,1);
    }
    
    public String getImageName1() throws IOException
    {
        return getImageName(1);
    }
    
    public Part getImage2() {
        return images[2];
    }
    
    public void setImage2(Part image) throws SQLException, IOException
    {
        if (image == null)
        {
            return;
        }
        setImage(image,2);
    }
    
    public String getImageName2() throws IOException
    {
        return getImageName(2);
    }
    
    public Part getImage3() {
        return images[3];
    }
    
    public void setImage3(Part image) throws SQLException, IOException
    {
        if (image == null)
        {
            return;
        }
        setImage(image,3);
    }
    
    public String getImageName3() throws IOException
    {
        return getImageName(3);
    }
    
    public Part getImage4() {
        return images[4];
    }
    
    public void setImage4(Part image) throws SQLException, IOException
    {
        if (image == null)
        {
            return;
        }
        setImage(image,4);
    }
    
    public String getImageName4() throws IOException
    {
        return getImageName(4);
    }
    
    @Override
    public void finalize()
    {
        try
        {
            for (int i=0; i< UtilsFunc.NUMBER_IMAGES_FOR_ANIMAL; ++i)
            {
                if (imagesNamesPreventCache[i] != null)
                {
                    UtilsFunc.removeImage(imagesNamesPreventCache[i]);
                }
            }
            super.finalize();
        }
        catch (Throwable e)
        {
            
        }
    }
}