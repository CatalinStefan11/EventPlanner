package ro.ase.eventplanner.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import ro.ase.eventplanner.Adapter.SliderAdapter;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.CallbackGetServiceByName;
import ro.ase.eventplanner.Util.Constants;
import ro.ase.eventplanner.Util.FirebaseMethods;

public class ViewServiceFragment extends Fragment {

    private SliderView sliderView;
    private SliderAdapter adapter;
    private TextView textName;
    private TextView textLocation;
    private TextView textDescription;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_view_service, container, false);

        sliderView = root.findViewById(R.id.imageSlider);
        textName = root.findViewById(R.id.display_name);
        textLocation = root.findViewById(R.id.location);
        textDescription = root.findViewById(R.id.description);

        initUI();


        return root;


    }

    @Override
    public void onResume() {
        initUI();
        super.onResume();
    }


    private void initUI() {

        Bundle bundle = this.getArguments();
        String service_name = bundle.getString(Constants.SERVICE_NAME);
        String service_creator = bundle.getString(Constants.SERVICE_CREATOR);
        String collection_path = bundle.getString(Constants.PATH_TAG);


        FirebaseMethods fb = FirebaseMethods.getInstance(getContext());
        fb.getServiceByName(new CallbackGetServiceByName() {
            @Override
            public void onGetServiceById(ServiceProvided serviceProvided) {
                adapter = new SliderAdapter(getContext(), serviceProvided.getImages_links());
                sliderView.setSliderAdapter(adapter);
                textName.setText(serviceProvided.getName());
                textLocation.setText(serviceProvided.getLocation());
                textDescription.setText(serviceProvided.getDescription());

            }
        }, service_name, service_creator, collection_path);


        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(false);


    }



}