package ro.ase.eventplanner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ballroom implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ratings")
    @Expose
    private String ratings;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
