package ro.ase.eventplanner.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ro.ase.eventplanner.Model.Ballroom2;

public class BallroomResult implements Serializable {

    @SerializedName("ballrooms")
    @Expose
    private List<Ballroom2> mBallroom2List = null;

    public List<Ballroom2> getBallroom2List(){
        return mBallroom2List;
    }

    public void setBallroom2List(List<Ballroom2> ballroom2List) {
        mBallroom2List = ballroom2List;
    }
}
