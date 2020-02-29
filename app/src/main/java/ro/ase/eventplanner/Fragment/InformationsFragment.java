package ro.ase.eventplanner.Fragment;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import ro.ase.eventplanner.R;


/**
 * Created by User on 5/28/2017.
 */

public class InformationsFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    //constant
    public static EditText mTextInfoName;
    public static EditText mTextInfoDescription;
    public static EditText mTextInfoLocation;



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d(TAG, "onCreateView: started.");

        mTextInfoName = view.findViewById(R.id.text1_name);
        mTextInfoDescription = view.findViewById(R.id.text3_description);
        mTextInfoLocation = view.findViewById(R.id.text2_location);

        return view;
    }


}

































