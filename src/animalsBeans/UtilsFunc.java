package animalsBeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.servlet.http.Part;

@Named(value = "utilsFunc")
@Dependent
public class UtilsFunc {
    public static final String PATH_TO_IMAGES = "C:\\Users\\ozhar\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\path\\to\\images\\";
    public static final String RELATIVE_IMAGE_PATH = "\\images\\";
    public static final int NUMBER_IMAGES_FOR_ANIMAL = 5;
    public static final int NUMBER_OF_CONTACTS = 3;

    //extension including the dot!
    public static String getExtension(Part file)
    {
        String name;
        
        if (file == null) 
        {
            return null;
        }
        name = file.getSubmittedFileName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) 
        {
            return ""; 
        }
        return name.substring(lastIndexOf);
    }
    
    
    public static void saveImage(Part imagePart, String imageName) throws IOException
    {   
        Path file = null;
        try
        {
            file = Files.createFile(Paths.get(UtilsFunc.PATH_TO_IMAGES+imageName));
        }
        catch (FileAlreadyExistsException e)
        {
            removeImage(imageName);
            file = Files.createFile(Paths.get(UtilsFunc.PATH_TO_IMAGES+imageName));
        }

        InputStream input = imagePart.getInputStream();
        Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING); 
        input.close();
    }
    
    public static void duplicateImage(String existImage, String newImage) throws IOException
    {
        File oldImgFile = new File(UtilsFunc.PATH_TO_IMAGES+existImage);
        if (oldImgFile.exists())
        {
            InputStream streamOld = new FileInputStream(oldImgFile);
            Path folder = Paths.get(UtilsFunc.PATH_TO_IMAGES+newImage);
            Path file = Files.createFile(folder);
            Files.copy(streamOld, file, StandardCopyOption.REPLACE_EXISTING); 
            streamOld.close();
        }
    }
    
    public static void removeImage(String imageName) throws IOException
    {
        File file = new File(PATH_TO_IMAGES+imageName);
        if (file.exists())
        {
            file.delete();
        }
    }
    
}
