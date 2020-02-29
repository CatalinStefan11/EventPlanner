package ro.ase.eventplanner.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class BallroomFirebase implements Parcelable {

    private String name;
    private String description;
    private String location;
    private String creator;
    private List<String> images_links;

    public BallroomFirebase()
    {
    }


    public BallroomFirebase(String name, String description, String location, String creator, List<String> images_links) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.creator = creator;
        this.images_links = images_links;
    }

    protected BallroomFirebase(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        creator = in.readString();
        images_links = in.createStringArrayList();
    }

    public static final Creator<BallroomFirebase> CREATOR = new Creator<BallroomFirebase>() {
        @Override
        public BallroomFirebase createFromParcel(Parcel in) {
            return new BallroomFirebase(in);
        }

        @Override
        public BallroomFirebase[] newArray(int size) {
            return new BallroomFirebase[size];
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
        return "BallroomFirebase{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", creator='" + creator + '\'' +
                ", images_links=" + images_links +
                '}';
    }
}
