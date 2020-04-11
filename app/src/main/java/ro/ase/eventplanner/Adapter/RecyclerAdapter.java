package ro.ase.eventplanner.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;

public class RecyclerAdapter extends FirestoreAdapter<RecyclerAdapter.ViewHolder> {

    public interface OnServiceSelectedListener {

        void onServiceSelected(DocumentSnapshot restaurant);

    }

    private StorageReference mStorageReference;
    private OnServiceSelectedListener mListener;
    private final RequestManager glide;


    public RecyclerAdapter(Query query, OnServiceSelectedListener listener, RequestManager manager){
        super(query);
        mListener = listener;
        this.glide = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.ballroom_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ImageView ballroomImage;
        TextView ballroomRatings, ballroomName, ratingNum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ballroomImage = itemView.findViewById(R.id.ballroomImage);
            ballroomRatings = itemView.findViewById(R.id.ratings);
            ballroomName = itemView.findViewById(R.id.serviceName);
            ratingNum = itemView.findViewById(R.id.serviceRatings);

        }

        public void bind(final DocumentSnapshot snapshot, final OnServiceSelectedListener listener){

            ServiceProvided service = snapshot.toObject(ServiceProvided.class);


            ballroomName.setText(service.getName());
            ratingNum.setText(String.valueOf(service.getNumRatings()) + "\nRatings");
            ballroomRatings.setText(String.valueOf(service.getAvgRating()));
            mStorageReference = FirebaseStorage
                    .getInstance()
                    .getReference(service.getImages_links().get(0));
            mStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    RequestOptions options = new RequestOptions();
                    Uri downloadUri = task.getResult();
                    glide.load(downloadUri).apply(options).into(ballroomImage);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onServiceSelected(snapshot);
                    }
                }
            });



        }
    }

}
