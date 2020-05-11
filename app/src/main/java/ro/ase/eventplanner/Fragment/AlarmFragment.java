package ro.ase.eventplanner.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ro.ase.eventplanner.Model.ReminderItem;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.AlarmService;
import ro.ase.eventplanner.Util.ReminderContract;
import ro.ase.eventplanner.Util.ReminderParams;
import ro.ase.eventplanner.Util.ReminderType;


public class AlarmFragment extends Fragment {

    private SimpleAdapter mAdapter;
    private EditText mContent, mTitle;
    private String mTime, mDate;
    private int mRepeatMode;
    private Map<String, String> mAlarmTime, mAlarmDate, mAlarmRepeat;
    private Calendar mAlertTime;
    private ContentResolver mContentResolver;
    private ReminderItem mData;

    private static final String NONE = "None";
    private static final String HOURLY = "Hourly";
    private static final String DAILY = "Daily";
    private static final String WEEKLY = "Weekly";
    private static final String MONTHLY = "Monthly";
    private static final String YEARLY = "Yearly";

    private static final String[] REPEAT_MODES =
            new String[]{NONE, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY};

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aa", Locale.CANADA);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy", Locale.CANADA);

    private static final String ITEM_TITLE = "header";
    private static final String ITEM_CONTENT = "content";

    private static final String TIME_SETTING = "Time";
    private static final String DATE_SETTING = "Date";
    private static final String REPEAT_SETTING = "Repeat";

    private static final int TIME_POSITION = 0;
    private static final int DATE_POSITION = 1;
    private static final int REPEAT_POSITION = 2;

    private boolean isNewAlarm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.alert_fragment, container, false);


        mContentResolver = getActivity().getContentResolver();

        mContent = root.findViewById(R.id.alert_content);
        mTitle = root.findViewById(R.id.alert_title);


        Bundle bundle = this.getArguments();

        if (bundle != null) {
            mData = bundle.getParcelable("data");
        } else {
            mData = null;
        }

        if (mData != null) {
            isNewAlarm = false;
            mTitle.setText(mData.getTitle());
            mContent.setText(mData.getContent());

        } else {
            isNewAlarm = true;
            mData = new ReminderItem();

        }




        List<Map<String, String>> mapList = new ArrayList<>();
        mAlarmTime = new HashMap<>();
        mAlarmDate = new HashMap<>();
        mAlarmRepeat = new HashMap<>();


        mRepeatMode = 0;
        mAlertTime = Calendar.getInstance();


        if (mData != null) {
            mTitle.setText(mData.getTitle());
            mContent.setText(mData.getContent());
            mAlertTime.setTimeInMillis(mData.getTimeInMillis());
            mRepeatMode = mData.getFrequency();

            mTime = TIME_FORMAT.format(mAlertTime.getTime());
            mDate = DATE_FORMAT.format(mAlertTime.getTime());
        } else {
            mData = new ReminderItem();
            Calendar current = Calendar.getInstance();
            mTime = TIME_FORMAT.format(current.getTime());
            mDate = DATE_FORMAT.format(current.getTime());
            mAlertTime.setTimeInMillis(current.getTimeInMillis());

            mData.setTimeInMillis(current.getTimeInMillis());
            mData.setFrequency(mRepeatMode);
        }

        mAlarmTime.put(ITEM_TITLE, TIME_SETTING);
        mAlarmTime.put(ITEM_CONTENT, mTime);
        mAlarmDate.put(ITEM_TITLE, DATE_SETTING);
        mAlarmDate.put(ITEM_CONTENT, mDate);
        mAlarmRepeat.put(ITEM_TITLE, REPEAT_SETTING);
        mAlarmRepeat.put(ITEM_CONTENT, REPEAT_MODES[mRepeatMode]);

        mapList.add(mAlarmTime);
        mapList.add(mAlarmDate);
        mapList.add(mAlarmRepeat);

        mAdapter = new SimpleAdapter(getContext(), mapList, android.R.layout.simple_list_item_2,
                new String[]{ITEM_TITLE, ITEM_CONTENT}, new int[]{
                android.R.id.text1, android.R.id.text2});
        ListView listView = root.findViewById(R.id.alert_settings);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case TIME_POSITION:
                        TimePickerDialog timePicker = getTimePicker();
                        timePicker.show();
                        break;
                    case DATE_POSITION:
                        DatePickerDialog datePicker = getDatePicker();
                        datePicker.show();
                        break;
                    case REPEAT_POSITION:
                        createRepeatDialog().show();
                        break;
                    default:
                        Log.e(this.getClass().getName(), "Out of bounds setting position.");
                        break;
                }
            }
        });

        return root;
    }


    @Override
    public void onPause() {
        super.onPause();
        promptSave();
    }


    private TimePickerDialog getTimePicker() {
        return new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mAlertTime.set(Calendar.HOUR_OF_DAY, hour);
                mAlertTime.set(Calendar.MINUTE, minute);
                mAlertTime.set(Calendar.SECOND, 0);
                mTime = TIME_FORMAT.format(mAlertTime.getTime());
                mAlarmTime.put(ITEM_CONTENT, mTime);
                mData.setTimeInMillis(mAlertTime.getTimeInMillis());
                mAdapter.notifyDataSetChanged();
            }
        }, mAlertTime.get(Calendar.HOUR_OF_DAY), mAlertTime.get(Calendar.MINUTE), false);
    }


    private DatePickerDialog getDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mAlertTime.set(Calendar.YEAR, year);
                        mAlertTime.set(Calendar.MONTH, month);
                        mAlertTime.set(Calendar.DAY_OF_MONTH, day);
                        mDate = DATE_FORMAT.format(mAlertTime.getTime());
                        mAlarmDate.put(ITEM_CONTENT, mDate);
                        mData.setTimeInMillis(mAlertTime.getTimeInMillis());
                        mAdapter.notifyDataSetChanged();
                    }
                }, mAlertTime.get(Calendar.YEAR), mAlertTime.get(Calendar.MONTH),
                mAlertTime.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePicker;
    }


    private AlertDialog createRepeatDialog() {
        final int prevRepeat = mRepeatMode;
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.repeat)
                .setSingleChoiceItems(REPEAT_MODES, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mRepeatMode = i;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mAlarmRepeat.put(ITEM_CONTENT, REPEAT_MODES[mRepeatMode]);
                        mData.setFrequency(mRepeatMode);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mRepeatMode = prevRepeat;
                        mData.setFrequency(mRepeatMode);
                    }
                })
                .create();
    }


    private void createAlarm(int id) {
        Intent alarm = new Intent(getContext(), AlarmService.class);
        alarm.putExtra(ReminderParams.ID, id);
        alarm.setAction(AlarmService.CREATE);
        getActivity().startService(alarm);
    }


    private void promptSave() {
        mData.setTitle(mTitle.getText().toString());
        mData.setContent(mContent.getText().toString());
        if (!mData.getTitle().equals("") && !mData.getContent().equals("")) {
            saveAlert(mData);
        }
    }

    private void saveAlert(final ReminderItem item) {
        if (item.getId() > 0) {
            Intent cancelPrevious = new Intent(getContext(),
                    AlarmService.class);
            cancelPrevious.putExtra(ReminderParams.ID, item.getId());
            cancelPrevious.setAction(AlarmService.CANCEL);
            getActivity().startService(cancelPrevious);
            ContentValues values = new ContentValues();
            values.put(ReminderContract.Alerts.TITLE, item.getTitle());
            values.put(ReminderContract.Alerts.CONTENT, item.getContent());
            values.put(ReminderContract.Alerts.TIME, item.getTimeInMillis());
            values.put(ReminderContract.Alerts.FREQUENCY, item.getFrequency());
            Uri uri = ContentUris.withAppendedId(ReminderContract.Alerts.CONTENT_URI, item.getId());
            mContentResolver.update(uri, values, null, null);
            createAlarm(item.getId());
        } else {
            ContentValues values = new ContentValues();
            values.put(ReminderContract.Alerts.TYPE, ReminderType.ALERT.getName());
            values.put(ReminderContract.Alerts.TITLE, item.getTitle());
            values.put(ReminderContract.Alerts.CONTENT, item.getContent());
            values.put(ReminderContract.Alerts.TIME, item.getTimeInMillis());
            values.put(ReminderContract.Alerts.FREQUENCY, item.getFrequency());
            Uri uri = mContentResolver.insert(ReminderContract.Alerts.CONTENT_URI,
                    values);
            if (uri != null) {
                createAlarm(Integer.parseInt(uri.getLastPathSegment()));
            }
        }
    }


}
