//package ro.ase.eventplanner.Activity;
//
//
//import android.content.ContentResolver;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.DialogInterface;
//
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.Navigation;
//
//
//import javax.annotation.Nullable;
//
//import ro.ase.eventplanner.Activity.ui.tools.ToolsFragmentDirections;
//import ro.ase.eventplanner.Util.ReminderContract;
//import ro.ase.eventplanner.Model.ReminderItem;
//import ro.ase.eventplanner.R;
//import ro.ase.eventplanner.Util.ReminderType;
//
//import static androidx.navigation.Navigation.findNavController;
//
//
//public class NoteActivity extends AppCompatActivity {
//    private EditText mTitle, mContent;
//    private ContentResolver mContentResolver;
//    private ReminderItem mData;
//    private boolean isNewNote;
//    NoteActivityArgs mArgs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_or_edit_note);
//
//
//        mArgs = NoteActivityArgs.fromBundle(NoteActivity.this.getIntent().getExtras());
//
//        mData = mArgs.getData();
//
//
//        mContentResolver = getContentResolver();
//
//
//
//        mContent = findViewById(R.id.note_content);
//        mTitle = findViewById(R.id.note_title);
//
//
//        ActionBar actionBar = getSupportActionBar();
//
//        if (mData != null) {
//            isNewNote = false;
//            mTitle.setText(mData.getTitle());
//            mContent.setText(mData.getContent());
//            setActionBarTitle(actionBar, this.getString(R.string.action_bar_edit_note));
//        } else {
//            isNewNote = true;
//            mData = new ReminderItem();
//            setActionBarTitle(actionBar, this.getString(R.string.action_bar_create_note));
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        promptSave();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if(!isNewNote) {
//            getMenuInflater().inflate(R.menu.menu_create_or_edit_note, menu);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_del_note:
//                deleteDialog(mData).show();
//                break;
//
//            case android.R.id.home:
//                promptSave();
//                break;
//
//            default:
//                break;
//        }
//
//        return true;
//    }
//
//    private AlertDialog deleteDialog(final ReminderItem item) {
//        return new AlertDialog.Builder(this)
//                .setTitle("Confirm")
//                .setMessage("Do you want to delete?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int i) {
//                        deleteNote(item);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int i) {
//                        dialog.dismiss();
//
//                    }
//                })
//                .create();
//    }
//
//    private AlertDialog saveDialog(final ReminderItem item) {
//
//        return new AlertDialog.Builder(this)
//                .setTitle("Confirm")
//                .setMessage("Do you want to save?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        if(!item.getTitle().equalsIgnoreCase(" "))
//                        {
//                            saveNote(item);
//                        }
//                        terminateActivity();
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        terminateActivity();
//                        dialog.dismiss();
//                    }
//                })
//                .create();
//    }
//
//    private void terminateActivity() {
//        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_NoteGraph_pop);
//
//
//    }
//
//    private void promptSave() {
//        mData.setTitle(mTitle.getText().toString());
//        mData.setContent(mContent.getText().toString());
//        saveDialog(mData).show();
//    }
//
//    private void setActionBarTitle(ActionBar actionBar, String title) {
//        if (actionBar != null) {
//            actionBar.setTitle(title);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//    }
//
//    private void saveNote(ReminderItem item) {
//        if (item.getId() > 0) {
//            ContentValues values = new ContentValues();
//            values.put(ReminderContract.Notes.TITLE, item.getTitle());
//            values.put(ReminderContract.Notes.CONTENT, item.getContent());
//            Uri uri = ContentUris.withAppendedId(ReminderContract.Notes.CONTENT_URI, item.getId());
//            mContentResolver.update(uri, values, null, null);
//        } else {
//            ContentValues values = new ContentValues();
//            values.put(ReminderContract.Notes.TYPE, ReminderType.NOTE.getName());
//            values.put(ReminderContract.Notes.TITLE, item.getTitle());
//            values.put(ReminderContract.Notes.CONTENT, item.getContent());
//            mContentResolver.insert(ReminderContract.Notes.CONTENT_URI, values);
//        }
//    }
//
//    private void deleteNote(ReminderItem item) {
//        if (item != null) {
//            Uri uri = ContentUris.withAppendedId(ReminderContract.Notes.CONTENT_URI, item.getId());
//            mContentResolver.delete(uri, null, null);
//        }
//        terminateActivity();
//    }
//}
