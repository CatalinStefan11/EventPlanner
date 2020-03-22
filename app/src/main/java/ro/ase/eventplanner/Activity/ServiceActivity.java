package ro.ase.eventplanner.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;

import com.smarteist.autoimageslider.SliderView;

import ro.ase.eventplanner.Adapter.SliderAdapter;
import ro.ase.eventplanner.Fragment.ViewServiceFragment;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Constants;

public class ServiceActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        getSupportActionBar().hide();

        init();





    }


    private void init(){


        Bundle bundle = getIntent().getExtras();



//        ServiceActivityArgs.fromBundle(bundle).getServiceNameArg(Constants.SERVICE_NAME);
//       Intent intent = getIntent();
//       Bundle bundle = intent.getBundleExtra(Constants.SERVICE_NAME).


        String service_name = bundle.getString(Constants.SERVICE_NAME);
        String service_creator = bundle.getString(Constants.SERVICE_CREATOR);
        String collection_path = bundle.getString(Constants.PATH_TAG);


        ViewServiceFragment fragment = new ViewServiceFragment();
        Bundle args = new Bundle();
        args.putString(Constants.SERVICE_NAME, service_name);
        args.putString(Constants.SERVICE_CREATOR, service_creator);
        args.putString(Constants.PATH_TAG, collection_path);
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_service_fragment));

        transaction.commit();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}