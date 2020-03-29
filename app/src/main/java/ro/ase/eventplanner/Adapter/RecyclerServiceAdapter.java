package ro.ase.eventplanner.Adapter;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;


import ro.ase.eventplanner.Fragment.BallroomsFragment;
import ro.ase.eventplanner.Fragment.BallroomsFragmentDirections;
import ro.ase.eventplanner.Fragment.PhotographersFragment;
import ro.ase.eventplanner.Fragment.ViewServiceFragment;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Constants;
import ro.ase.eventplanner.Util.FirebaseTag;

public class RecyclerServiceAdapter extends RecyclerView.Adapter<RecyclerServiceAdapter.BallroomViewHolder> {


    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private BallroomViewHolder mBallroomViewHolder;
    private List<ServiceProvided> mServiceProvideds;
    private StorageReference mStorageReference;
    private final RequestManager glide;
    public static String collection_path;
    public static int restore_id = 0;


    public RecyclerServiceAdapter(Context context, List<ServiceProvided> serviceProvideds, RequestManager manager, String path_tag) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mServiceProvideds = serviceProvideds;
        this.glide = manager;
        collection_path = path_tag;
    }



    @NonNull
    @Override
    public BallroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.ballroom_card, parent, false);
        mBallroomViewHolder = new BallroomViewHolder(view);

        return mBallroomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BallroomViewHolder holder, final int position) {



        mStorageReference = FirebaseStorage
                .getInstance()
                .getReference(mServiceProvideds.get(position).getImages_links().get(0));
        mStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Uri downloadUri = task.getResult();
                glide.load(downloadUri).apply(options).into(holder.ballroomImage);
            }
        });


        holder.mServiceName = mServiceProvideds.get(position).getName();
        holder.mServiceCreator = mServiceProvideds.get(position).getCreator();

//
//        holder.ballroomRatings.setText(mServiceProvideds.get(position).getRatings());
        holder.ballroomName.setText(mServiceProvideds.get(position).getName());
//        holder.ballroomLikes.setText(mServiceProvideds.get(position).getLikes() + "\nLikes");


    }


    @Override
    public int getItemCount() {
        return mServiceProvideds.size();
    }

    class BallroomViewHolder extends RecyclerView.ViewHolder {


        ImageView ballroomImage;
        TextView ballroomRatings, ballroomName, ballroomLikes;
        String mServiceName;
        String mServiceCreator;

        public BallroomViewHolder(@NonNull View itemView) {
            super(itemView);
            ballroomImage = itemView.findViewById(R.id.ballroomImage);
//            ballroomRatings = itemView.findViewById(R.id.ratings);
            ballroomName = itemView.findViewById(R.id.ballroomNamet);
//            ballroomLikes = itemView.findViewById(R.id.ballroomLikes);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {






                  Bundle bundle = new Bundle();
                  bundle.putString(Constants.SERVICE_NAME, mServiceName);
                  bundle.putString(Constants.SERVICE_CREATOR, mServiceCreator);
                  bundle.putString(Constants.PATH_TAG, collection_path);


                  Navigation.findNavController(view).
                          navigate(R.id.action_global_viewServiceFragment, bundle);
//                    if(collection_path.equals(FirebaseTag.TAG_DECORATIONS)){
//                        restore_id = R.id.nav_decorations;
//                        Navigation.findNavController(view)
//                                .navigate(R.id.action_nav_decorations_to_serviceActivity, bundle);
//                    }else if(collection_path.equals(FirebaseTag.TAG_PHOTOGRAPHERS)){
//                        restore_id = R.id.nav_photographers;
//                        Navigation.findNavController(view)
//                                .navigate(R.id.action_nav_photographers_to_serviceActivity, bundle);
//
//                    }else if(collection_path.equals(FirebaseTag.TAG_BALLROOM)){
//                        restore_id = R.id.nav_ballrooms;
//                        Navigation.findNavController(view)
//                                .navigate(R.id.action_nav_ballrooms_to_serviceActivity, bundle);
//                    }


                }
            });


        }
    }


}
