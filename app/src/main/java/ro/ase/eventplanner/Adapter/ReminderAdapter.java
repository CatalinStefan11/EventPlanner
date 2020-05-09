package ro.ase.eventplanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import ro.ase.eventplanner.Model.ReminderItem;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.ReminderType;



public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {


  private ReminderViewHolder.OnClickListener mOnClickListener;
  private ReminderViewHolder.OnLongClickListener mOnLongClickListener;
  private List<ReminderItem> mReminderItems;

  public ReminderAdapter(List<ReminderItem> items,
                         ReminderViewHolder.OnClickListener clickListener,
                         ReminderViewHolder.OnLongClickListener longClickListener) {
    mReminderItems = items;
    mOnClickListener = clickListener;
    mOnLongClickListener = longClickListener;
  }

  @Override
  public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View reminderView = inflater.inflate(R.layout.card_reminder_item, parent, false);
    return new ReminderViewHolder(reminderView);
  }

  @Override
  public void onBindViewHolder(ReminderViewHolder viewHolder, int position) {
    ReminderItem item = mReminderItems.get(position);
    if (item == null) {
      return;
    }
    if (item.getType().equals(ReminderType.ALERT)) {
      viewHolder.setTimeLabel(item.getFormattedTime());
      viewHolder.setIcon(R.drawable.ic_bell_ring_grey_18dp);
    } else {
      viewHolder.setTimeLabel(null);
      viewHolder.setIcon(0);
    }
    viewHolder.setTitle(item.getTitle());
    viewHolder.setContent(item.getContent());
    viewHolder.setOnClickListener(mOnClickListener);
    viewHolder.setOnLongClickListener(mOnLongClickListener);
  }


  public int getItemCount() {
    return mReminderItems.size();
  }

  @Override
  public long getItemId(int position){
    return (long) mReminderItems.get(position).getId();
  }

  public ReminderItem getItemAtPosition(int position) {
    return mReminderItems.get(position);
  }

}


