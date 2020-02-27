package ro.ase.eventplanner.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ro.ase.eventplanner.R;

/**
 * Created by User on 5/28/2017.
 */

public class InformationsFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int  CAMERA_REQUEST_CODE = 5;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        Log.d(TAG, "onCreateView: started.");




        return view;
    }

//    private boolean isRootTask(){
//        if(((NewOfferActivity)getActivity()).getTask() == 0){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == CAMERA_REQUEST_CODE){
//            Log.d(TAG, "onActivityResult: done taking a photo.");
//            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");
//
//            Bitmap bitmap;
//            bitmap = (Bitmap) data.getExtras().get("data");
//
//            if(isRootTask()){
//                try{
//                    Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
//                    Intent intent = new Intent(getActivity(), NextActivity.class);
//                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
//                    startActivity(intent);
//                }catch (NullPointerException e){
//                    Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
//                }
//            }else{
//               try{
//                   Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
//                   Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
//                   intent.putExtra(getString(R.string.selected_bitmap), bitmap);
//                   intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
//                   startActivity(intent);
//                   getActivity().finish();
//               }catch (NullPointerException e){
//                   Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
//               }
//            }
//
//        }
    }
}

































