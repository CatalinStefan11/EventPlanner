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

import java.util.List;

import ro.ase.eventplanner.Adapter.BallroomAdapter;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Callbacks;
import ro.ase.eventplanner.Util.FirebaseMethods;
import ro.ase.eventplanner.Util.FirebaseTag;

public class HomeFragment extends Fragment {

    private BallroomAdapter mBallroomAdapter;
    private RecyclerView mBallroomRecyclerView;
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

    private void initUI() {

        FirebaseMethods fb = FirebaseMethods.getInstance(getContext());
        fb.readServices(new Callbacks() {
            @Override
            public void onGetServices(List<ServiceProvided> serviceProvideds) {
                Log.d("MAIN", serviceProvideds.toString());

                mBallroomAdapter = new BallroomAdapter(getContext(), serviceProvideds, Glide.with(thisFragment));
                mBallroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mBallroomRecyclerView.setAdapter(mBallroomAdapter);

            }
        }, FirebaseTag.TAG_BALLROOM);
    }


}