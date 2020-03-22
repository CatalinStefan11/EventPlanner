package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ro.ase.eventplanner.Adapter.RecyclerServiceAdapter;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.CallbackServiceList;
import ro.ase.eventplanner.Util.FirebaseMethods;
import ro.ase.eventplanner.Util.FirebaseTag;

public class BallroomsFragment extends Fragment {

    private RecyclerServiceAdapter mRecyclerServiceAdapter;
    private RecyclerView mBallroomRecyclerView;
    private BallroomsFragment thisFragment = this;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ballrooms, container, false);
        mBallroomRecyclerView = root.findViewById(R.id.ballroomsRecyclerView);

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
        fb.readServices(new CallbackServiceList() {
            @Override
            public void onGetServices(List<ServiceProvided> serviceProvideds) {
                Log.d("MAIN", serviceProvideds.toString());

                mRecyclerServiceAdapter = new RecyclerServiceAdapter(getContext(), serviceProvideds, Glide.with(thisFragment), FirebaseTag.TAG_BALLROOM);
                mBallroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mBallroomRecyclerView.setAdapter(mRecyclerServiceAdapter);

            }
        }, FirebaseTag.TAG_BALLROOM);
    }


}