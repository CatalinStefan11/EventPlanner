package ro.ase.eventplanner.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import ro.ase.eventplanner.Adapter.MyRecyclerAdapter;
import ro.ase.eventplanner.Adapter.RecyclerAdapter;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.FirebaseTag;


public class MyPhotographersFragment extends Fragment implements MyRecyclerAdapter.onDeleteButton, MyRecyclerAdapter.onEditButton {


    private RecyclerView mPhotographersRecycler;
    private FirebaseFirestore mFirestore;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ViewGroup mEmptyView;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_photographers, container, false);


        mPhotographersRecycler = view.findViewById(R.id.my_photographers_recycler);
        mEmptyView = view.findViewById(R.id.view_empty_photographers);


        mFirestore = FirebaseFirestore.getInstance();
        Query query = mFirestore.collection(FirebaseTag.TAG_PHOTOGRAPHERS).whereEqualTo("creator", FirebaseAuth.getInstance().getUid());

        mRecyclerAdapter = new MyRecyclerAdapter(query, this, this, Glide.with(this)) {
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

        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.startListening();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.stopListening();
        }
    }


    @Override
    public void onEditButton(DocumentSnapshot restaurant) {
        mRecyclerAdapter.stopListening();
        Bundle bundle = new Bundle();
        bundle.putString("document_id", restaurant.getId());
        bundle.putString("path_tag", FirebaseTag.TAG_PHOTOGRAPHERS);
        Navigation.findNavController(getView()).navigate(R.id.action_global_fragment_edit_service, bundle);
    }

    @Override
    public void onDeleteButton(DocumentSnapshot service) {
        deleteDialog(service).show();
    }



    private AlertDialog deleteDialog(final DocumentSnapshot service) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.confirm)
                .setMessage(R.string.delete_prompt)
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                    String id = service.getId();

                    ServiceProvided serviceProvided = service.toObject(ServiceProvided.class);
                    FirebaseFirestore.getInstance().collection(FirebaseTag.TAG_PHOTOGRAPHERS).document(id).delete().addOnSuccessListener(v -> {

                        for(int j = 0; j < serviceProvided.getImages_links().size(); j++){
                            String path = serviceProvided.getImages_links().get(j);
                            FirebaseStorage.getInstance().getReference(path).delete();
                        }


                    });
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.no, (dialog, i) -> dialog.dismiss())
                .create();

    }
}
















