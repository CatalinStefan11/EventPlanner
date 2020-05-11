package ro.ase.eventplanner.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eventplanner.Adapter.ReminderAdapter;
import ro.ase.eventplanner.Adapter.ReminderViewHolder;
import ro.ase.eventplanner.Model.ReminderItem;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.AlarmService;
import ro.ase.eventplanner.Util.ReminderContract;
import ro.ase.eventplanner.Util.ReminderParams;
import ro.ase.eventplanner.Util.ReminderType;

public class MyAlarmsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private TextView mEmptyView;
    private RecyclerView mRecyclerView;
    private ReminderAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private List<ReminderItem> mReminderItems;
    private ReminderViewHolder.OnClickListener mOnItemClickListener;
    private ReminderViewHolder.OnLongClickListener mOnItemLongClickListener;
    private View mMView;

    public interface EditListener {
        void navigateToNoteEdit(RecyclerView.ViewHolder holder);
    }

    private EditListener mEditListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mEditListener = new EditListener() {
            @Override
            public void navigateToNoteEdit(RecyclerView.ViewHolder holder) {

                int position = holder.getAdapterPosition();
                ReminderItem item = mAdapter.getItemAtPosition(position);
                if (item.getType().getName().equalsIgnoreCase(ReminderType.ALERT.getName())) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", item);
                    Navigation.findNavController(mMView)
                            .navigate(R.id.action_global_alarmFragment, bundle);
                }

            }
        };

        mOnItemClickListener = new ReminderViewHolder.OnClickListener() {
            @Override
            public void onClick(ReminderViewHolder holder) {
                mEditListener.navigateToNoteEdit(holder);
                mAdapter.notifyItemChanged(holder.getAdapterPosition());
            }
        };

        mOnItemLongClickListener = new ReminderViewHolder.OnLongClickListener() {
            @Override
            public boolean onLongClick(ReminderViewHolder holder) {
                int position = holder.getAdapterPosition();
                ReminderItem item = mAdapter.getItemAtPosition(position);
                deleteDialog(item).show();
                return false;
            }
        };



        mReminderItems = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (getContext() == null) {
            return null;
        }
        return new CursorLoader(getContext(), ReminderContract.Alerts.CONTENT_URI, null, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mReminderItems.clear();
        for (boolean i = data.moveToFirst(); i; i = data.moveToNext()) {
            ReminderItem item = new ReminderItem(data);
            mReminderItems.add(item);
        }
        update();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        getLoaderManager().restartLoader(0, null, this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mMView = inflater.inflate(R.layout.fragment_my_alarms, container, false);
        mRecyclerView = mMView.findViewById(R.id.reminder_list);
        mEmptyView = mMView.findViewById(R.id.empty);
        mRefreshLayout = mMView.findViewById(R.id.refresh_layout);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRefreshLayout.setOnRefreshListener(this);

        update();
        return mMView;
    }

    public void onResume() {
        super.onResume();
        mRefreshLayout.setRefreshing(true);
        restartLoader();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void update() {
        if (mReminderItems == null || mReminderItems.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mRefreshLayout.setRefreshing(false);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new ReminderAdapter(mReminderItems, mOnItemClickListener,
                    mOnItemLongClickListener);
            mAdapter.setHasStableIds(true);
            mRecyclerView.swapAdapter(mAdapter, false);
        }
        mRefreshLayout.setRefreshing(false);
    }

    private AlertDialog deleteDialog(final ReminderItem item) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.confirm)
                .setMessage(R.string.delete_prompt)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        deleteSelected(item);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                })
                .create();

    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    private void deleteSelected(ReminderItem item) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return;
        }

        Uri uri;
        int id = item.getId();
        ReminderType type = item.getType();

        if (type.equals(ReminderType.ALERT)) {
            Intent delete = new Intent(getContext(), AlarmService.class);
            delete.putExtra(ReminderParams.ID, id);
            delete.setAction(AlarmService.DELETE);
            getContext().startService(delete);

        }
    }


    @Override
    public void onRefresh() {
        restartLoader();
    }
}