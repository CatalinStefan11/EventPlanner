package ro.ase.eventplanner.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ro.ase.eventplanner.Adapter.RecyclerServiceAdapter;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.FirebaseTag;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private AppBarConfiguration mAppBarConfiguration;

    private static final String TAG = "MainActivity";
    private NavController mNavController;
    private NavigationView mNavigationView;
    private static int restore_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);








        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_ballrooms, R.id.nav_photographers, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_decorations, R.id.fragment_container_view_tag)
                .setDrawerLayout(drawer)
                .build();





        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_add_service){
            startActivity(new Intent(MainActivity.this, NewOfferActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putBundle("state", mNavController.saveState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bundle bundle = savedInstanceState.getBundle("state");
        mNavController.restoreState(bundle);
    }


}
