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

import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.SectionsPagerAdapter;

public class MyServicesFragment extends Fragment {


    private ViewPager mViewPager;
    private FirebaseAuth mFirebaseAuth;
    private View mRoot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.fragment_slideshow, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();


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
        adapter.addFragment(new MyBallroomsFragment());
        adapter.addFragment(new MyPhotographersFragment());

        mViewPager = mRoot.findViewById(R.id.viewpager_container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);





        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){



                }else if( position == 0){


                }
                Log.d("VIEW_PAGER", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) mRoot.findViewById(R.id.tabsBottom);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.my_ballrooms));
        tabLayout.getTabAt(1).setText(getString(R.string.my_photographers));
        tabLayout.getTabAt(1).setText(getString(R.string.my_decorations));



    }


}