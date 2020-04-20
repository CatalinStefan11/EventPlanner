package ro.ase.eventplanner.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.lang.ref.WeakReference;

import ro.ase.eventplanner.Adapter.RecyclerAdapter;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Constants;
import ro.ase.eventplanner.Util.FirebaseTag;

public class BallroomsFragment extends Fragment implements RecyclerAdapter.OnServiceSelectedListener  {

    private RecyclerView mBallroomRecyclerView;
    private FirebaseFirestore mFirestore;
    private RecyclerAdapter mRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ballrooms, container, false);
        mBallroomRecyclerView = root.findViewById(R.id.ballroomsRecyclerView);

        mFirestore = FirebaseFirestore.getInstance();
        Query query = mFirestore.collection(FirebaseTag.TAG_BALLROOM)
                .orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(10);
        mRecyclerAdapter = new RecyclerAdapter(query,this, Glide.with(this)){
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mBallroomRecyclerView.setVisibility(View.GONE);

                } else {
                    mBallroomRecyclerView.setVisibility(View.VISIBLE);

                }
            }
        };
        mBallroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBallroomRecyclerView.setAdapter(mRecyclerAdapter);

        return root;

    }






    @Override
    public void onStart() {
        super.onStart();

        if(mRecyclerAdapter != null){
            mRecyclerAdapter.startListening();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mRecyclerAdapter != null){
            mRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onServiceSelected(DocumentSnapshot service) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.PATH_TAG, FirebaseTag.TAG_BALLROOM);
        bundle.putString("service_id", service.getId());
        Navigation.findNavController(getView()).
                navigate(R.id.action_global_viewService, bundle);

    }


}

