package ro.ase.eventplanner.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ro.ase.eventplanner.Adapter.RatingAdapter;
import ro.ase.eventplanner.Adapter.SliderAdapter;
import ro.ase.eventplanner.Model.Rating;
import ro.ase.eventplanner.Model.ServiceProvided;
import ro.ase.eventplanner.R;
import ro.ase.eventplanner.Util.Constants;


public class ViewServiceFragment extends Fragment implements EventListener<DocumentSnapshot>,
        RatingDialogFragment.RatingListener {

    private SliderView sliderView;
    private SliderAdapter adapter;
    private TextView textName;
    private TextView textLocation;
    private TextView textDescription;

    private MaterialRatingBar mRatingIndicator;
    private TextView mNumRatingsView;
    private RecyclerView mRatingsRecycler;

    private RatingDialogFragment mRatingDialog;
    private ListenerRegistration mServiceRegistration;
    private RatingAdapter mRatingAdapter;

    private FirebaseFirestore mFirestore;
    private DocumentReference mServiceRef;
    private Button startChat;


    private static final String TAG = "ViewServiceFragment";

    private ViewGroup mEmptyView;
    private View mRoot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.try_layout, container, false);

        sliderView = mRoot.findViewById(R.id.service_image_slider);
        textName = mRoot.findViewById(R.id.service_name);
        textLocation = mRoot.findViewById(R.id.service_location);
        textDescription = mRoot.findViewById(R.id.description);


        mRatingIndicator = mRoot.findViewById(R.id.service_rating);
        mNumRatingsView = mRoot.findViewById(R.id.service_num_ratings);
        mRatingsRecycler = mRoot.findViewById(R.id.recycler_ratings);
        mEmptyView = mRoot.findViewById(R.id.view_empty_ratings);
        startChat = mRoot.findViewById(R.id.start_chat);




        mRoot.findViewById(R.id.fab_show_rating_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRatingClicked(v);
            }
        });


        initUI();

        return mRoot;


    }


    @Override
    public void onStart() {
        super.onStart();

        mRatingAdapter.startListening();
        mServiceRegistration = mServiceRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mRatingAdapter.stopListening();

        if (mServiceRegistration != null) {
            mServiceRegistration.remove();
            mServiceRegistration = null;
        }
    }


    public void onAddRatingClicked(View view) {
        mRatingDialog.show(ViewServiceFragment.this.getChildFragmentManager(), RatingDialogFragment.TAG);
    }


    private void initUI() {

        Bundle bundle = this.getArguments();

        String collection_path = bundle.getString(Constants.PATH_TAG);
        String service_id = bundle.getString("service_id");


        mFirestore = FirebaseFirestore.getInstance();
        mServiceRef = mFirestore.collection(collection_path).document(service_id);
        Query raitingsQuery = mServiceRef.collection("ratings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);


        startChat.setOnClickListener(v -> Navigation.findNavController(getView()).
                navigate(R.id.action_global_chatFragment, bundle)
        );



        mRatingAdapter = new RatingAdapter(raitingsQuery) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mRatingsRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRatingsRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                super.onDataChanged();
            }
        };


        mRatingsRecycler.setLayoutManager(new LinearLayoutManager(ViewServiceFragment.this.getContext()));
        mRatingsRecycler.setAdapter(mRatingAdapter);

        mRatingDialog = new RatingDialogFragment();

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(false);


    }


    private void hideKeyboard() {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void onServiceLoaded(ServiceProvided serviceProvided) {
        adapter = new SliderAdapter(getContext(), serviceProvided.getImages_links());
        sliderView.setSliderAdapter(adapter);
        mRatingIndicator.setRating((float) serviceProvided.getAvgRating());
        textName.setText(serviceProvided.getName());
        textLocation.setText(serviceProvided.getLocation());
        textDescription.setText(serviceProvided.getDescription());
        mNumRatingsView.setText("(" + serviceProvided.getNumRatings() + ")");

    }


    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }
        onServiceLoaded(snapshot.toObject(ServiceProvided.class));

    }

    private Task<Void> addRating(final DocumentReference documentReference, final Rating rating) {
        // Create reference for new rating, for use inside the transaction
        final DocumentReference ratingRef = documentReference.collection("ratings")
                .document();

        // In a transaction, add the new rating and update the aggregate totals
        return mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {

                ServiceProvided serviceProvided = transaction.get(documentReference)
                        .toObject(ServiceProvided.class);

                // Compute new number of ratings
                int newNumRatings = serviceProvided.getNumRatings() + 1;

                // Compute new average rating
                double oldRatingTotal = serviceProvided.getAvgRating() *
                        serviceProvided.getNumRatings();
                double newAvgRating = (oldRatingTotal + rating.getRating()) /
                        newNumRatings;

                // Set new restaurant info
                serviceProvided.setNumRatings(newNumRatings);
                serviceProvided.setAvgRating(newAvgRating);

                // Commit to Firestore
                transaction.set(documentReference, serviceProvided);
                transaction.set(ratingRef, rating);

                return null;
            }
        });
    }

    @Override
    public void onRating(Rating rating) {
        // In a transaction, add the new rating and update the aggregate totals
        addRating(mServiceRef, rating)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Rating added");

                        // Hide keyboard and scroll to top
                        hideKeyboard();
                        mRatingsRecycler.smoothScrollToPosition(0);
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Add rating failed", e);

                        // Show failure message and hide keyboard
                        hideKeyboard();
                        Snackbar.make(mRoot.findViewById(android.R.id.content), "Failed to add rating",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}