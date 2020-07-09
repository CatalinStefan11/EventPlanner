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


import com.dev.materialspinner.MaterialSpinner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ro.ase.eventplanner.R;



public class InformationsFragment extends Fragment implements OnMapReadyCallback{
    private static final String TAG = "InfoFragment";

    //constant
    public static EditText mTextInfoName;
    public static EditText mTextInfoDescription;
    public static EditText mTextInfoLocation;
    public MaterialSpinner mSpinnerService;
    public static int mServiceStringPosition = 0;

    private GoogleMap mMap;

    public static InformationsFragment newInstance() {
        InformationsFragment fragment = new InformationsFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d(TAG, "onCreateView: started.");

//        mTextInfoName = view.findViewById(R.id.text1_name);
//        mTextInfoDescription = view.findViewById(R.id.text3_description);
//        mTextInfoLocation = view.findViewById(R.id.text2_location);
        mSpinnerService = view.findViewById(R.id.spinner_service_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spinnerServicesArray));
        mSpinnerService.setAdapter(adapter);

        mSpinnerService.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
                mServiceStringPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(mServiceStringPosition);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }




}

































