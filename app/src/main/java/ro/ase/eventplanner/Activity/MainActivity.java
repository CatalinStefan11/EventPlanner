package ro.ase.eventplanner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.shreyaspatil.material.navigationview.MaterialNavigationView;

import ro.ase.eventplanner.R;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private NavController mNavController;
    private MaterialNavigationView mNavigationView;
    private FloatingActionMenu mFloatingActionMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFloatingActionMenu = findViewById(R.id.floating_menu);
        mFloatingActionMenu.setClosedOnTouchOutside(true);
        com.github.clans.fab.FloatingActionButton addAlert = findViewById(R.id.add_alert);
        com.github.clans.fab.FloatingActionButton addNote = findViewById(R.id.add_note);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        View header = mNavigationView.getHeaderView(0);
        TextView textEmail = header.findViewById(R.id.drawer_header_email);
        TextView logOut = header.findViewById(R.id.drawer_header_log_out);

        textEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);

        });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_ballrooms, R.id.nav_photographers, R.id.nav_alarms,
                R.id.nav_tools, R.id.nav_share, R.id.nav_decorations, R.id.fragment_container_view_tag,
                R.id.nav_slideshow, R.id.nav_new_offer)
                .setDrawerLayout(drawer)
                .build();


        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        mNavController.addOnDestinationChangedListener(((controller, destination, arguments) -> {
            if (destination.getId() == R.id.chatFragment) {
                mFloatingActionMenu.setVisibility(View.INVISIBLE);
            } else if (destination.getId() == R.id.viewService) {
                mFloatingActionMenu.setVisibility(View.INVISIBLE);
            } else if (destination.getId() == R.id.nav_new_offer) {
                mFloatingActionMenu.setVisibility(View.INVISIBLE);
            } else {
                mFloatingActionMenu.setVisibility(View.VISIBLE);
            }
        }));


        addNote.setOnClickListener(v -> {
            mFloatingActionMenu.close(false);
            mNavController.navigate(R.id.action_global_noteFragment);
        });

        addAlert.setOnClickListener(v -> {
            mFloatingActionMenu.close(false);
            mNavController.navigate(R.id.action_global_alarmFragment);
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("_id", -1);
        if (id != -1) {
            mNavController.navigate(R.id.nav_alarms);
        }

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
