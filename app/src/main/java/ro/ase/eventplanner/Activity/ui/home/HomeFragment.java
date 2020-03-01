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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import ro.ase.eventplanner.Adapter.BallroomAdapter;
import ro.ase.eventplanner.Model.BallroomFirebase;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.BallroomResult;
import ro.ase.eventplanner.Util.Callbacks;
import ro.ase.eventplanner.Util.FirebaseMethods;

public class HomeFragment extends Fragment {

    private BallroomAdapter mBallroomAdapter;
    private RecyclerView mBallroomRecyclerView;
    private BallroomResult mBallroomResult;
    private HomeFragment thisFragment = this;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mBallroomRecyclerView = root.findViewById(R.id.ballroomRecyclerView);




        initUI();


        return root;




    }

    @Override
    public void onResume() {
       initUI();
        super.onResume();
    }

    private void initUI(){

        FirebaseMethods fb = FirebaseMethods.getInstance(getContext());
        fb.readBallrooms(new Callbacks() {
            @Override
            public void OnGetAllBallrooms(List<BallroomFirebase> ballrooms) {
                Log.d("MAIN", ballrooms.toString());

                mBallroomAdapter = new BallroomAdapter(getContext(), ballrooms, Glide.with(thisFragment));
                mBallroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mBallroomRecyclerView.setAdapter(mBallroomAdapter);

            }
        });
    }






}