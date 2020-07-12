package ro.ase.eventplanner.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ro.ase.eventplanner.Adapter.RecyclerAdapter;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.FirebaseTag;


public class MyPhotographersFragment extends Fragment implements RecyclerAdapter.OnServiceSelectedListener{


    private RecyclerView mPhotographersRecycler;
    private FirebaseFirestore mFirestore;
    private RecyclerAdapter mRecyclerAdapter;
    private ViewGroup mEmptyView;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_photographers, container, false);


        mPhotographersRecycler = view.findViewById(R.id.my_photographers_recycler);
        mEmptyView = view.findViewById(R.id.view_empty_photographers);


        mFirestore = FirebaseFirestore.getInstance();
        Query query = mFirestore.collection(FirebaseTag.TAG_PHOTOGRAPHERS).whereEqualTo("creator", FirebaseAuth.getInstance().getUid())
                .orderBy("avgRating", Query.Direction.DESCENDING);

        mRecyclerAdapter = new RecyclerAdapter(query,this, Glide.with(this)){
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mPhotographersRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                } else {
                    mPhotographersRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);

                }
            }
        };
        mPhotographersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mPhotographersRecycler.setAdapter(mRecyclerAdapter);



        return view;
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
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.PATH_TAG, FirebaseTag.TAG_BALLROOM);
//        bundle.putString("service_id", service.getId());
//        Navigation.findNavController(getView()).
//                navigate(R.id.action_global_viewService, bundle);
    }


}
















