package ro.ase.eventplanner.Adapter;



import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;

import ro.ase.eventplanner.Model.Ballroom2;
import ro.ase.eventplanner.Model.BallroomFirebase;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.BallroomResult;

public class BallroomAdapter extends RecyclerView.Adapter<BallroomAdapter.BallroomViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private BallroomViewHolder mBallroomViewHolder;
    private List<BallroomFirebase> mBallrooms;
    private StorageReference mStorageReference;
    private final RequestManager glide;


    public BallroomAdapter(Context context, List<BallroomFirebase> ballrooms, RequestManager manager) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mBallrooms = ballrooms;
        this.glide = manager;

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
                .getReference(mBallrooms.get(position).getImages_links().get(0));
        mStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Uri downloadUri = task.getResult();
                glide.load(downloadUri).apply(options).into(holder.ballroomImage);
            }
        });



//
//        holder.ballroomRatings.setText(mBallrooms.get(position).getRatings());
        holder.ballroomName.setText(mBallrooms.get(position).getName());
//        holder.ballroomLikes.setText(mBallrooms.get(position).getLikes() + "\nLikes");


    }


    @Override
    public int getItemCount() {
        return mBallrooms.size();
    }

    class BallroomViewHolder extends RecyclerView.ViewHolder {

        ImageView ballroomImage;
        TextView ballroomRatings, ballroomName, ballroomLikes;

        public BallroomViewHolder(@NonNull View itemView) {
            super(itemView);
            ballroomImage = itemView.findViewById(R.id.ballroomImage);
//            ballroomRatings = itemView.findViewById(R.id.ratings);
            ballroomName = itemView.findViewById(R.id.ballroomNamet);
//            ballroomLikes = itemView.findViewById(R.id.ballroomLikes);



        }
    }


}
