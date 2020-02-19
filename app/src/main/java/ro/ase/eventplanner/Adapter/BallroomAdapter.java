package ro.ase.eventplanner.Adapter;



import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;


import java.util.List;

import ro.ase.eventplanner.Model.Ballroom;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.BallroomResult;

public class BallroomAdapter extends RecyclerView.Adapter<BallroomAdapter.BallroomViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private BallroomViewHolder mBallroomViewHolder;
    private List<Ballroom> mBallrooms;
    private BallroomResult mBallroomResult = new BallroomResult();

    public BallroomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setBallrooms(List<Ballroom> lists) {
        this.mBallrooms = lists;
        mBallroomResult.setBallroomList(mBallrooms);


        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public BallroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.ballroom_card, parent, false);
        mBallroomViewHolder = new BallroomViewHolder(view);

        return mBallroomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BallroomViewHolder holder, final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(mBallrooms.get(position).getImageUrl()))
                .into(holder.ballroomImage);

        holder.ballroomRatings.setText(mBallrooms.get(position).getRatings());
        holder.ballroomName.setText(mBallrooms.get(position).getName());
        holder.ballroomLikes.setText(mBallrooms.get(position).getLikes() + "\nLikes");


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
            ballroomRatings = itemView.findViewById(R.id.ratings);
            ballroomName = itemView.findViewById(R.id.ballroomNamet);
            ballroomLikes = itemView.findViewById(R.id.ballroomLikes);


        }
    }


}
