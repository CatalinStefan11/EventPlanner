package ro.ase.eventplanner.Fragment;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Permissons;
import ro.ase.eventplanner.Util.SectionsPagerAdapter;

public class NewOfferFragment extends Fragment {


    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    private ViewPager mViewPager;
    public static ImageLoader sImageLoader;
    public static ServiceProvided sServiceProvided = new ServiceProvided();
    public static int mStringPosition;
    private FirebaseAuth mFirebaseAuth;
    private View mRoot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.fragment_slideshow, container, false);


        mFirebaseAuth = FirebaseAuth.getInstance();
        sServiceProvided.setCreator(mFirebaseAuth.getCurrentUser().getUid());
        sImageLoader = ImageLoader.getInstance();
        sImageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

        if(checkPermissionsArray(Permissons.PERMISSIONS)){
            setupViewPager();
        }else{
            verifyPermissions(Permissons.PERMISSIONS);
        }


        return mRoot;
    }

    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    /**
     * setup viewpager for manager the tabs
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter =  new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new InformationsFragment());
        adapter.addFragment(new PhotosFragment());

        mViewPager = mRoot.findViewById(R.id.viewpager_container);
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

        TabLayout tabLayout = (TabLayout) mRoot.findViewById(R.id.tabsBottom);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.information));
        tabLayout.getTabAt(1).setText(getString(R.string.photos));



    }




    public void verifyPermissions(String[] permissions){

        ActivityCompat.requestPermissions(
                getActivity(),
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions){

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }


    public boolean checkPermissions(String permission){

        int permissionRequest = ActivityCompat.checkSelfPermission(getContext(), permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else{
            return true;
        }
    }
}