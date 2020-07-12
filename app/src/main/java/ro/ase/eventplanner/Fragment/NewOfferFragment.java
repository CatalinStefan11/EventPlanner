package ro.ase.eventplanner.Fragment;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.materialspinner.MaterialSpinner;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


import ro.ase.eventplanner.Adapter.GridImageAdapter;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.FilePaths;
import ro.ase.eventplanner.Util.FileSearch;
import ro.ase.eventplanner.Util.FirebaseMethods;
import ro.ase.eventplanner.Util.FirebaseTag;
import ro.ase.eventplanner.Util.Permissons;


public class NewOfferFragment extends Fragment {


    private static final String TAG = "NewOfferFragment";

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private EditText mTextInfoName;
    private EditText mTextInfoDescription;
    private EditText mTextInfoLocation;
    private MaterialSpinner mSpinnerService;
    private int mServiceStringPosition = 0;
    private FirebaseAuth mFirebaseAuth;
    private  ImageLoader mImageLoader;
    private ServiceProvided mServiceProvided;


    //constants
    private static final int NUM_GRID_COLUMNS = 3;


    private GridView gridView;
    private Spinner directorySpinner;


    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private List<String> mSelected = new ArrayList<>();
    private TextView mSaveText;
    private View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        directories = new ArrayList<>();


        if (!checkPermissionsArray(Permissons.PERMISSIONS)) {
            verifyPermissions(Permissons.PERMISSIONS);
        }

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_offer, container, false);

        initUi();


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


        Log.d(TAG, "onCreateView: started.");

        ImageView shareClose = mView.findViewById(R.id.ivCloseShare);

        shareClose.setOnClickListener(v -> {
            Log.d(TAG, "onClick: closing the photos fragment.");
            Navigation.findNavController(getView()).popBackStack();
        });

        final FirebaseMethods firebaseMethods = FirebaseMethods.getInstance(getContext());

        mSaveText.setOnClickListener(v -> {
            if (isValid()) {
                mServiceProvided = new ServiceProvided(mTextInfoName.getText().toString(),
                        mTextInfoDescription.getText().toString(),
                        mTextInfoLocation.getText().toString(),
                        mFirebaseAuth.getCurrentUser().getUid());

                if (mServiceStringPosition == 0) {
                    firebaseMethods.addNewService(getContext(), mServiceProvided, mSelected, FirebaseTag.TAG_BALLROOM);
                } else if (mServiceStringPosition == 1) {
                    firebaseMethods.addNewService(getContext(), mServiceProvided, mSelected, FirebaseTag.TAG_PHOTOGRAPHERS);
                } else if (mServiceStringPosition == 2) {
                    firebaseMethods.addNewService(getContext(), mServiceProvided, mSelected, FirebaseTag.TAG_DECORATIONS);
                }
                Navigation.findNavController(getView()).popBackStack();
            }
        });


        init();


        return mView;
    }


    private boolean isValid() {

        if (mTextInfoName.getText().toString().isEmpty()) {
            mTextInfoName.setError("Error! Name is empty!");
            return false;
        } else if (mTextInfoDescription.getText().toString().isEmpty()) {
            mTextInfoDescription.setError("Error! Description is empty.");
            return false;
        } else if (mTextInfoLocation.getText().toString().isEmpty()) {
            mTextInfoLocation.setError("Error! Location is empty.");
            return false;
        } else if(mSelected.size() == 0){
            Toast.makeText(getContext(), "Error! You need to select at least one image!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void initUi() {
        mTextInfoName = mView.findViewById(R.id.text_service_name);
        mTextInfoDescription = mView.findViewById(R.id.text_service_description);
        mTextInfoLocation = mView.findViewById(R.id.text_service_location);
        mSpinnerService = mView.findViewById(R.id.spinner_service_type);
        mSaveText = mView.findViewById(R.id.tvNext);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spinnerServicesArray));
        mSpinnerService.setAdapter(adapter);


        gridView = mView.findViewById(R.id.gridView);
        gridView.setSmoothScrollbarEnabled(true);
        gridView.setNestedScrollingEnabled(true);
        directorySpinner = mView.findViewById(R.id.spinnerDirectory);

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
    }


    private void init() {
        FilePaths filePaths = new FilePaths();

        //check for other folders indide "/storage/emulated/0/pictures"
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            Log.d(TAG, "init: directory: " + directories.get(i));
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index);
            directoryNames.add(string);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected: " + directories.get(position));
                setupGridView(directories.get(position));
                mSelected = new ArrayList<>();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setupGridView(String selectedDirectory) {

        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);

        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mImageLoader, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));

            if (mSelected.contains(imgURLs.get(position))) {
                mSelected.remove(imgURLs.get(position));
                view.setAlpha(1.0F);
                view.setPadding(0, 0, 0, 0);
                view.setBackgroundColor(Color.WHITE);
            } else {
                mSelected.add(imgURLs.get(position));
                view.setAlpha(0.5F);
                view.setPadding(10, 10, 10, 10);
                view.setBackgroundColor(Color.BLACK);
            }
        });

    }


    private void verifyPermissions(String[] permissions) {

        ActivityCompat.requestPermissions(
                getActivity(),
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    private boolean checkPermissionsArray(String[] permissions) {


        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }


    private boolean checkPermissions(String permission) {

        int permissionRequest = ActivityCompat.checkSelfPermission(getContext(), permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
}