package ro.ase.eventplanner.Activity.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ro.ase.eventplanner.Adapter.BallroomAdapter;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.BallroomResult;
import ro.ase.eventplanner.Util.FirebaseMethods;

public class HomeFragment extends Fragment {

    private BallroomAdapter mBallroomAdapter;
    private RecyclerView mBallroomRecyclerView;
    private BallroomResult mBallroomResult;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mBallroomRecyclerView = root.findViewById(R.id.ballroomRecyclerView);
        BallroomAdapter ballroomAdapter = new BallroomAdapter(getContext());
        Gson gson = new Gson();

        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(getContext().getAssets()
                    .open("ballroom.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBallroomResult = gson.fromJson(br, BallroomResult.class);


        ballroomAdapter.setBallroom2s(mBallroomResult.getBallroom2List());
        mBallroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBallroomRecyclerView.setAdapter(ballroomAdapter);



        return root;



    }



}