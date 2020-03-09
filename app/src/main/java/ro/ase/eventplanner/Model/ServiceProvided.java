package ro.ase.eventplanner.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class ServiceProvided implements Parcelable {

    private String name;
    private String description;
    private String location;
    private String creator;
    private List<String> images_links;

    public ServiceProvided()
    {
    }


    public ServiceProvided(String name, String description, String location, String creator, List<String> images_links) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.creator = creator;
        this.images_links = images_links;
    }

    protected ServiceProvided(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        creator = in.readString();
        images_links = in.createStringArrayList();
    }

    public static final Creator<ServiceProvided> CREATOR = new Creator<ServiceProvided>() {
        @Override
        public ServiceProvided createFromParcel(Parcel in) {
            return new ServiceProvided(in);
        }

        @Override
        public ServiceProvided[] newArray(int size) {
            return new ServiceProvided[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(creator);
        dest.writeStringList(images_links);
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
