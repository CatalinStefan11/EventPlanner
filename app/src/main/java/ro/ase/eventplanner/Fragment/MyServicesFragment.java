package ro.ase.eventplanner.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.SectionsPagerAdapter;

public class MyServicesFragment extends Fragment {


    private ViewPager mViewPager;
    private View mRoot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.fragment_my_services, container, false);


        setupViewPager();

        return mRoot;
    }

    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter =  new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyBallroomsFragment());
        adapter.addFragment(new MyPhotographersFragment());
        adapter.addFragment(new MyDecorationsFragment());

        mViewPager = mRoot.findViewById(R.id.viewpager_container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);


        TabLayout tabLayout = (TabLayout) mRoot.findViewById(R.id.tabsBottom);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.my_ballrooms));
        tabLayout.getTabAt(1).setText(getString(R.string.my_photographers));
        tabLayout.getTabAt(2).setText(getString(R.string.my_decorations));

    }

}