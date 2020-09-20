package animalsBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "navigationParameters")
@SessionScoped
public class NavigationParameters implements Serializable {
    private String chosenAnimalID;

    public NavigationParameters() {
    }

    public String getChosenAnimalID() {
        return chosenAnimalID;
    }

    public void setChosenAnimalID(String chosenAnimalID) {
        this.chosenAnimalID = chosenAnimalID;
    }
    
    
}
