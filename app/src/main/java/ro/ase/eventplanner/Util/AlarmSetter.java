package ro.ase.eventplanner.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;



public class AlarmSetter extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Cursor cursor = context.getContentResolver().query(ReminderContract.Alerts.CONTENT_URI,
            null, null, null, null);

    if (cursor == null || !cursor.moveToFirst()) {
      return;
    }

    try {
      while (cursor.moveToNext()) {
        String type = cursor.getString(cursor.getColumnIndex(ReminderParams.TYPE));
        long time = cursor.getLong(cursor.getColumnIndex(ReminderParams.TIME));

        if (ReminderType.fromString(type) == ReminderType.ALERT
            && time > Calendar.getInstance().getTimeInMillis()) {
          Intent service = new Intent(context, AlarmService.class);
          service.setAction(AlarmService.CREATE);
          service.putExtra(ReminderParams.ID, cursor.getInt(cursor.getColumnIndex(
              ReminderParams.ID)));
          context.startService(service);
        }
      }
    } finally {
      cursor.close();
    }

  }

}

