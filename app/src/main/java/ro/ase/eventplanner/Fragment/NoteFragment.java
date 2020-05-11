package ro.ase.eventplanner.Fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActionOnlyNavDirections;

import ro.ase.eventplanner.Model.ReminderItem;
import ro.ase.eventplanner.R;

import android.net.Uri;
import android.widget.EditText;

import ro.ase.eventplanner.Util.ReminderContract;
import ro.ase.eventplanner.Util.ReminderType;


public class NoteFragment extends Fragment {

    private EditText mTitle, mContent;
    private ContentResolver mContentResolver;
    private ReminderItem mData;
    private boolean isNewNote;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_create_or_edit_note, container, false);


        mContentResolver = getActivity().getContentResolver();


        Bundle bundle = this.getArguments();

        if (bundle != null) {
            mData = bundle.getParcelable("data");
        } else {
            mData = null;
        }

        mContent = root.findViewById(R.id.note_content);
        mTitle = root.findViewById(R.id.note_title);


        if (mData != null) {
            isNewNote = false;
            mTitle.setText(mData.getTitle());
            mContent.setText(mData.getContent());

        } else {
            isNewNote = true;
            mData = new ReminderItem();

        }
        return root;

    }


    @Override
    public void onPause() {
        super.onPause();
        promptSave();
    }


    private void promptSave() {
        mData.setTitle(mTitle.getText().toString());
        mData.setContent(mContent.getText().toString());
        if (!mData.getTitle().equals("") && !mData.getContent().equals("")) {
            saveNote(mData);
        }
    }


    private void saveNote(ReminderItem item) {
        if (item.getId() > 0) {
            ContentValues values = new ContentValues();
            values.put(ReminderContract.Notes.TITLE, item.getTitle());
            values.put(ReminderContract.Notes.CONTENT, item.getContent());
            Uri uri = ContentUris.withAppendedId(ReminderContract.Notes.CONTENT_URI, item.getId());
            mContentResolver.update(uri, values, null, null);
        } else {
            ContentValues values = new ContentValues();
            values.put(ReminderContract.Notes.TYPE, ReminderType.NOTE.getName());
            values.put(ReminderContract.Notes.TITLE, item.getTitle());
            values.put(ReminderContract.Notes.CONTENT, item.getContent());
            mContentResolver.insert(ReminderContract.Notes.CONTENT_URI, values);
        }
    }


}
