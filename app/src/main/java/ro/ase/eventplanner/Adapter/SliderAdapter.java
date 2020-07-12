package ro.ase.eventplanner.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderViewAdapter;
import java.util.List;
import ro.ase.eventplanner.R;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> mServiceProvided;
    private StorageReference mStorageReference;

    public SliderAdapter(Context context, List<String> serviceProvided) {
        this.context = context;
        mServiceProvided = serviceProvided;
    }



    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(final SliderAdapterVH viewHolder, final int position) {



       String image_link = mServiceProvided.get(position);

            mStorageReference = FirebaseStorage
                    .getInstance()
                    .getReference(image_link);

            mStorageReference.getDownloadUrl().addOnCompleteListener(task -> {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Uri downloadUri = task.getResult();

                Glide.with(viewHolder.itemView)
                        .load(downloadUri)
                        .into(viewHolder.imageViewBackground);
            });



        viewHolder.itemView.setOnClickListener(v -> Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getCount() {
        return mServiceProvided.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}