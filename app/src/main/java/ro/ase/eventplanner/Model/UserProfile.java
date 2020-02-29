package ro.ase.eventplanner.Model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfile {

    private String email;
    private String username;
    private List<String> ballrooms;


    public UserProfile() {
        //firebase constructor
    }


    public UserProfile(String email, String username) {
        this.email = email;
        this.username = username;
        this.ballrooms = null;

    }

//    public Map<String, Object> UserToMap(){
//        Map<String,Object> userMap = new HashMap<>();
//        userMap.put("username", this.username);
//        userMap.put("email", this.email);
//        return userMap;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getBallrooms() {
        return ballrooms;
    }

    public void setBallrooms(List<String> ballrooms) {
        this.ballrooms = ballrooms;
    }
}
