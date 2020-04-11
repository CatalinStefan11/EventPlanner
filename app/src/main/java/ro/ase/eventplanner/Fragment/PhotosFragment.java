package ro.ase.eventplanner.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eventplanner.Activity.NewOfferActivity;
import ro.ase.eventplanner.Adapter.GridImageAdapter;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.FilePaths;
import ro.ase.eventplanner.Util.FileSearch;
import ro.ase.eventplanner.Util.FirebaseMethods;
import ro.ase.eventplanner.Util.FirebaseTag;


public class PhotosFragment extends Fragment {
    private static final String TAG = "PhotosFragment";


    //constants
    private static final int NUM_GRID_COLUMNS = 3;



    //widgets
    private GridView gridView;
    private Spinner directorySpinner;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;
    public static List<String> mSelected = new ArrayList<>();





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photos, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);

        directories = new ArrayList<>();

        Log.d(TAG, "onCreateView: started.");



        ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the photos fragment.");
                getActivity().finish();
            }
        });

        final FirebaseMethods firebaseMethods= FirebaseMethods.getInstance(getContext());

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: save object to firebase.");

                int mStringPosition = NewOfferActivity.mStringPosition;

                if(mStringPosition == 0){
                    firebaseMethods.addNewService(NewOfferActivity.sServiceProvided, mSelected,FirebaseTag.TAG_BALLROOM);
                }else if(mStringPosition == 1){
                    firebaseMethods.addNewService(NewOfferActivity.sServiceProvided, mSelected,FirebaseTag.TAG_PHOTOGRAPHERS);
                }else if(mStringPosition == 2){
                    firebaseMethods.addNewService(NewOfferActivity.sServiceProvided, mSelected,FirebaseTag.TAG_DECORATIONS);
                }
            }
        });


        init();



        return view;
    }



    private void init(){
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


    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);


        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);


        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);


        try{

            mSelectedImage = imgURLs.get(0);
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " +e.getMessage() );
        }


        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));



                if(mSelected.contains(imgURLs.get(position))){
                    mSelected.remove(imgURLs.get(position));
                    view.setAlpha(1.0F);
                    view.setPadding(0,0,0,0);
                    view.setBackgroundColor(Color.WHITE);


                }else{
                    mSelected.add(imgURLs.get(position));
                    view.setAlpha(0.5F);
                    view.setPadding(10,10,10,10);
                    view.setBackgroundColor(Color.BLACK);


                }


            }
        });




    }



}































