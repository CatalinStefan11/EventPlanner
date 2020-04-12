package ro.ase.eventplanner.Activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ro.ase.eventplanner.Model.ServiceProvided;

import ro.ase.eventplanner.Util.Permissons;


import ro.ase.eventplanner.Fragment.InformationsFragment;
import ro.ase.eventplanner.Fragment.PhotosFragment;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.SectionsPagerAdapter;


/**
 * Created by User on 5/28/2017.
 */

public class NewOfferActivity extends AppCompatActivity {
    private static final String TAG = "NewOfferActivity";


    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;


    private ViewPager mViewPager;
    public static ImageLoader sImageLoader;
    public static ServiceProvided sServiceProvided = new ServiceProvided();
    public static int mStringPosition;

    private Context mContext = NewOfferActivity.this;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);
        Log.d(TAG, "onCreate: started.");

        mFirebaseAuth = FirebaseAuth.getInstance();
        sServiceProvided.setCreator(mFirebaseAuth.getCurrentUser().getUid());
        sImageLoader = ImageLoader.getInstance();
        sImageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        if(checkPermissionsArray(Permissons.PERMISSIONS)){
            setupViewPager();
        }else{
            verifyPermissions(Permissons.PERMISSIONS);
        }



    }



    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    /**
     * setup viewpager for manager the tabs
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter =  new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InformationsFragment());
        adapter.addFragment(new PhotosFragment());

        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);




        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){

                    sServiceProvided.setName(InformationsFragment.mTextInfoName.getText().toString());
                    sServiceProvided.setDescription(InformationsFragment.mTextInfoDescription.getText().toString());
                    sServiceProvided.setLocation(InformationsFragment.mTextInfoLocation.getText().toString());
                    mStringPosition  = InformationsFragment.mServiceStringPosition;
                    Log.d("INFO", sServiceProvided.toString());

                }else if( position == 0){

                    Log.d("INFO", sServiceProvided.toString());
                }
                Log.d("VIEW_PAGER", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);

        tabLayout.setupWithViewPager(mViewPager);
//        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.information));
        tabLayout.getTabAt(1).setText(getString(R.string.photos));



    }



    public int getTask(){
        Log.d(TAG, "getTask: TASK: " + getIntent().getFlags());
        return getIntent().getFlags();
    }

    /**
     * verifiy all the permissions passed to the array
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                NewOfferActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission is it has been verified
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(NewOfferActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }





}