package ro.ase.eventplanner.Util;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bignerdranch.android.multiselector.SwappingHolder;

import ro.ase.eventplanner.R;


public class ReminderViewHolder extends SwappingHolder implements
                                                       View.OnClickListener,
                                                       View.OnLongClickListener {
  private TextView mTitle;
  private TextView mContent;
  private TextView mTime;
  private ImageView mIcon;
  private CardView mCardView;

  public interface OnClickListener {
    void onClick(ReminderViewHolder holder);
  }

  public interface OnLongClickListener {
    boolean onLongClick(ReminderViewHolder holder);
  }

  private OnClickListener mOnClickListener;
  private OnLongClickListener mOnLongClickListener;

  public ReminderViewHolder(View view) {
    super(view);
    view.setOnClickListener(this);
    view.setOnLongClickListener(this);

    mTitle = view.findViewById(R.id.title);
    mContent = view.findViewById(R.id.reminder);
    mTime = view.findViewById(R.id.timeLabel);
    mIcon = view.findViewById(R.id.icon);
    mCardView = view.findViewById(R.id.card_view);

  }

  public void setTitle(String title) {
    mTitle.setText(title);
  }

  public void setContent(String content) {
    mContent.setText(content);
  }

  public void setTimeLabel(String timeLabel) {
    if (timeLabel != null) {
      mTime.setText(timeLabel);
      mTime.setVisibility(View.VISIBLE);
    } else {
      mTime.setVisibility(View.GONE);
    }
  }

  public void setIcon(int resId) {
    if (resId != 0) {
      mIcon.setImageResource(resId);
      mIcon.setVisibility(View.VISIBLE);
    } else {
      mIcon.setVisibility(View.GONE);
    }
  }

  public void setOnClickListener(OnClickListener l) {
    mOnClickListener = l;
  }

  public void setOnLongClickListener(OnLongClickListener l) {
    mOnLongClickListener = l;
  }
  @Override
  public boolean onLongClick(View view) {
    if (mOnLongClickListener != null) {
      return mOnLongClickListener.onLongClick(this);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
    if (mOnClickListener != null) {
      mOnClickListener.onClick(this);
    }
  }


}