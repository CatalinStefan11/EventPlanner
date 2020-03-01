package ro.ase.eventplanner.Util;

import java.util.List;

import ro.ase.eventplanner.Model.BallroomFirebase;

public interface Callbacks {
    void OnGetAllBallrooms(List<BallroomFirebase> ballrooms);
}
