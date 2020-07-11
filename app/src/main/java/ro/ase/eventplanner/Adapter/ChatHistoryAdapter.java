package ro.ase.eventplanner.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import ro.ase.eventplanner.Model.UserProfile;
import ro.ase.eventplanner.R;


public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mUsersIds;


    public ChatHistoryAdapter(Context mContext, List<String> mUsers) {
        this.mUsersIds = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_user_item, parent, false);
        return new ChatHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String userId = mUsersIds.get(position);

        FirebaseFirestore.getInstance().collection("users").document(userId).
                addSnapshotListener((snapshot, exception) -> {
                    UserProfile user = snapshot.toObject(UserProfile.class);
                    holder.username.setText(user.getUsername());
                    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
                });


        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("user_id", userId);
            Navigation.findNavController(v).
                    navigate(R.id.action_global_chatFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mUsersIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;


        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image2);
        }
    }


}