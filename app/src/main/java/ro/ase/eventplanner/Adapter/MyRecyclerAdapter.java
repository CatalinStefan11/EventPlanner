package ro.ase.eventplanner.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.DecimalFormat;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;

public class MyRecyclerAdapter extends FirestoreAdapter<MyRecyclerAdapter.ViewHolder> {

    public interface onEditButton {

        void onEditButton(DocumentSnapshot restaurant);

    }

    public interface onDeleteButton {

        void onDeleteButton(DocumentSnapshot restaurant);

    }

    private StorageReference mStorageReference;
    private onEditButton mEditListener;
    private onDeleteButton mDeleteListener;
    private final RequestManager glide;


    public MyRecyclerAdapter(Query query, onEditButton listenerEdit, onDeleteButton listenerDelete, RequestManager manager){
        super(query);
        mEditListener = listenerEdit;
        mDeleteListener = listenerDelete;
        this.glide = manager;
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyRecyclerAdapter.ViewHolder(inflater.inflate(R.layout.card_service_edit_delete, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mEditListener, mDeleteListener);

    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ImageView ballroomImage;
        TextView ballroomRatings, ballroomName, ratingNum;
        Button btnEdit, btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ballroomImage = itemView.findViewById(R.id.ballroomImage);
            ballroomRatings = itemView.findViewById(R.id.ratings);
            ballroomName = itemView.findViewById(R.id.serviceName);
            ratingNum = itemView.findViewById(R.id.serviceRatings);
            btnEdit = itemView.findViewById(R.id.edit_service_button);
            btnDelete = itemView.findViewById(R.id.delete_service_button);


        }

        public void bind(final DocumentSnapshot snapshot, final onEditButton listenerEdit, final onDeleteButton listenerDelete ){

            ServiceProvided service = snapshot.toObject(ServiceProvided.class);

            ballroomName.setText(service.getName());
            ratingNum.setText(String.valueOf(service.getNumRatings()) + "\nRatings");
            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            ballroomRatings.setText( decimalFormat.format(service.getAvgRating()));

            mStorageReference = FirebaseStorage
                    .getInstance()
                    .getReference(service.getImages_links().get(0));

            mStorageReference.getDownloadUrl().addOnCompleteListener(task -> {
                RequestOptions options = new RequestOptions();
                Uri downloadUri = task.getResult();
                glide.load(downloadUri).apply(options).into(ballroomImage);
            });

            btnEdit.setOnClickListener(v -> {
                if(listenerEdit != null){
                    listenerEdit.onEditButton(snapshot);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if(listenerDelete != null){
                    listenerDelete.onDeleteButton(snapshot);
                }
            });

        }
    }

}
