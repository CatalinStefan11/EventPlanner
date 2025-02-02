package ro.ase.eventplanner.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.legacy.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import ro.ase.eventplanner.Activity.MainActivity;
import ro.ase.eventplanner.R;

/**
 * Created by kyle on 07/09/16.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

  private static final int HOURLY = 1, DAILY = 2, WEEKLY = 3, MONTHLY = 4, YEARLY = 5;

  @Override
  public void onReceive(Context context, Intent intent) {
    int id = intent.getIntExtra(ReminderParams.ID, -1);
    String title = intent.getStringExtra(ReminderParams.TITLE);
    String msg = intent.getStringExtra(ReminderParams.CONTENT);

    if (context == null) {
      return;
    }

    ContentResolver contentResolver = context.getContentResolver();
    if (contentResolver == null) {
      return;
    }

    Uri uri = ContentUris.withAppendedId(ReminderContract.Alerts.CONTENT_URI, id);
    Cursor cursor = contentResolver.query(uri,
            null, null, null, null);

    if (cursor == null || !cursor.moveToFirst()) {
      return;
    }

    int frequency = cursor.getInt(cursor.getColumnIndex(ReminderParams.FREQUENCY));
    Calendar time = Calendar.getInstance();
    time.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(ReminderParams.TIME)));
    cursor.close();

    if (frequency > 0) {
      if (frequency == HOURLY) {
        time.add(Calendar.HOUR, 1);

      } else if (frequency == DAILY) {
        time.add(Calendar.DATE, 1);

      } else if (frequency == WEEKLY) {
        time.add(Calendar.DATE, 7);
      } else if (frequency == MONTHLY) {
        time.add(Calendar.MONTH, 1);

      } else if (frequency == YEARLY) {
        time.add(Calendar.YEAR, 1);

      }

      ContentValues values = new ContentValues();
      values.put(ReminderContract.Alerts.TIME, time.getTimeInMillis());
      uri = ContentUris.withAppendedId(ReminderContract.Alerts.CONTENT_URI, id);
      context.getContentResolver().update(uri, values, null, null);

      Intent setAlarm = new Intent(context, AlarmService.class);
      setAlarm.putExtra(ReminderParams.ID, id);
      setAlarm.setAction(AlarmService.CREATE);
      context.startService(setAlarm);
    }



    Intent result = new Intent(context, MainActivity.class);
    result.putExtra(ReminderParams.ID, id);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(MainActivity.class);
    stackBuilder.addNextIntent(result);
    PendingIntent clicked = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    String channelId = "DEFAULT";

    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
    bigStyle.setBigContentTitle(title);
    bigStyle.bigText(msg);
    NotificationCompat.Builder n = new NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_calendar_check_black_48dp)
            .setContentTitle(title)
            .setContentText(msg)
            .setSound(defaultSoundUri)
            .setPriority(Notification.PRIORITY_MAX)
            .setWhen(0)
            .setStyle(bigStyle)
            .setContentIntent(clicked)
            .setAutoCancel(true);



//    n.defaults |= Notification.DEFAULT_VIBRATE;
//    n.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//    n.defaults |= Notification.DEFAULT_SOUND;

    NotificationManager notificationManager = (NotificationManager)
            context.getSystemService(Context.NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId,
              "Cube Reminders",
              NotificationManager.IMPORTANCE_HIGH);
      notificationManager.createNotificationChannel(channel);
    }



    notificationManager.notify(id, n.build());


  }

}
