package ro.ase.eventplanner.Model;


public class UserProfile {

    private String email;
    private String username;


    public UserProfile() {
        //firebase constructor
    }


    public UserProfile(String email, String username) {
        this.email = email;
        this.username = username;

    }

    public String getUsername() {
        return username;
    }


}
