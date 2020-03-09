package ro.ase.eventplanner.Fragment;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import ro.ase.eventplanner.R;



public class InformationsFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    //constant
    public static EditText mTextInfoName;
    public static EditText mTextInfoDescription;
    public static EditText mTextInfoLocation;
    public static Spinner mSpinnerService;



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d(TAG, "onCreateView: started.");

        mTextInfoName = view.findViewById(R.id.text1_name);
        mTextInfoDescription = view.findViewById(R.id.text3_description);
        mTextInfoLocation = view.findViewById(R.id.text2_location);
        mSpinnerService = view.findViewById(R.id.spinner_service_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spinnerServicesArray));
        mSpinnerService.setAdapter(adapter);

        mSpinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


}

































