package ro.ase.eventplanner.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ro.ase.eventplanner.Model.Ballroom;

public class BallroomResult implements Serializable {

    @SerializedName("ballrooms")
    @Expose
    private List<Ballroom> mBallroomList = null;

    public List<Ballroom> getBallroomList(){
        return mBallroomList;
    }

    public void setBallroomList(List<Ballroom> ballroomList) {
        mBallroomList = ballroomList;
    }
}
