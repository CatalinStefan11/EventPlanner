package ro.ase.eventplanner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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


public class PhotographersFragment extends Fragment {

    private RecyclerServiceAdapter mRecyclerServiceAdapter;
    private RecyclerView mPhotographersRecyclerView;
    private PhotographersFragment thisFragment = this;
    

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
     
        View root = inflater.inflate(R.layout.fragment_photographers, container, false);
        mPhotographersRecyclerView = root.findViewById(R.id.photographersRecyclerView);


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

                mRecyclerServiceAdapter = new RecyclerServiceAdapter(getContext(), serviceProvideds, Glide.with(thisFragment), FirebaseTag.TAG_PHOTOGRAPHERS);
                mPhotographersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mPhotographersRecyclerView.setAdapter(mRecyclerServiceAdapter);

            }
        }, FirebaseTag.TAG_PHOTOGRAPHERS);
    }
}