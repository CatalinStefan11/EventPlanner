package ro.ase.eventplanner.Model;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ro.ase.eventplanner.Util.ReminderParams;
import ro.ase.eventplanner.Util.ReminderType;


public class ReminderItem implements Parcelable {


  private int id;
  private ReminderType type;
  private String title;
  private String content;
  private long timeInMillis;
  private int frequency;

  private static final SimpleDateFormat TIME_FORMAT =
      new SimpleDateFormat("HH:mm, MMM d ''yy", Locale.CANADA);


  public ReminderItem() {

  }

  public ReminderItem(Cursor cursor) {
    id = cursor.getInt(cursor.getColumnIndex(ReminderParams.ID));
    type = ReminderType.fromString(cursor.getString(cursor.getColumnIndex(ReminderParams.TYPE)));
    content = cursor.getString(cursor.getColumnIndex(ReminderParams.CONTENT));
    title = cursor.getString(cursor.getColumnIndex(ReminderParams.TITLE));
    timeInMillis = cursor.getLong(cursor.getColumnIndex(ReminderParams.TIME));
    frequency = cursor.getInt(cursor.getColumnIndex(ReminderParams.FREQUENCY));

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ReminderType getType() {
    return type;
  }

  public void setType(ReminderType mtype) {
    type = mtype;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public long getTimeInMillis() {
    return timeInMillis;
  }

  public void setTimeInMillis(long timeInMillis) {
    this.timeInMillis = timeInMillis;
  }

  public String getFormattedTime() {
    return TIME_FORMAT.format(getTimeInMillis());
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.type.getName());
    dest.writeString(this.title);
    dest.writeString(this.content);
    dest.writeLong(this.timeInMillis);
    dest.writeInt(this.frequency);
  }

  protected ReminderItem(Parcel in) {
    this.id = in.readInt();
    this.type =  ReminderType.fromString(in.readString());
    this.title = in.readString();
    this.content = in.readString();
    this.timeInMillis = in.readLong();
    this.frequency = in.readInt();
  }

  public static final Creator<ReminderItem> CREATOR = new Creator<ReminderItem>() {
    @Override
    public ReminderItem createFromParcel(Parcel source) {
      return new ReminderItem(source);
    }

    @Override
    public ReminderItem[] newArray(int size) {
      return new ReminderItem[size];
    }
  };
}
