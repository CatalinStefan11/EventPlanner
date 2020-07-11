package ro.ase.eventplanner.Model;


import com.google.firebase.firestore.IgnoreExtraProperties;
import java.util.List;

@IgnoreExtraProperties
public class ServiceProvided{

    private String name;
    private String description;
    private String location;
    private String creator;
    private List<String> images_links;
    private int numRatings;
    private double avgRating;

    public ServiceProvided()
    {

    }

    public ServiceProvided(String name, String description, String location, String creator) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.creator = creator;
        this.images_links = null;
        this.numRatings = 0;
        this.avgRating = 0;
    }


    public ServiceProvided(String name, String description, String location, String creator, List<String> images_links) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.creator = creator;
        this.images_links = images_links;
        this.numRatings = 0;
        this.avgRating = 0;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getImages_links() {
        return images_links;
    }

    public void setImages_links(List<String> images_links) {
        this.images_links = images_links;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }



    @Override
    public String toString() {
        return "ServiceProvided{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", creator='" + creator + '\'' +
                ", images_links=" + images_links +
                '}';
    }
}
