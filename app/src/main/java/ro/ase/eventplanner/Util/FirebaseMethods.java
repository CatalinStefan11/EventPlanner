package ro.ase.eventplanner.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eventplanner.Model.Rating;
import ro.ase.eventplanner.Model.ServiceProvided;

public class FirebaseMethods {

    private static FirebaseMethods ourInstance = null;
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseFirestore mFirestore;
    private static String userId;
    private static FirebaseStorage mStorageReference;
    private static Context mContext;


    public static FirebaseMethods getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new FirebaseMethods();
            mContext = context;
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirestore = FirebaseFirestore.getInstance();
            mContext = context;
            mStorageReference = FirebaseStorage.getInstance();


            if (mFirebaseAuth.getCurrentUser() != null) {
                userId = mFirebaseAuth.getCurrentUser().getUid();
            }

        }
        return ourInstance;
    }


    public void addNewService(Context context, ServiceProvided serviceProvided, final List<String> imageUrls,
                              final String path_collection_tag) {

        serviceProvided.setImages_links(null);
        mFirestore.collection(path_collection_tag)
                .add(serviceProvided)
                .addOnSuccessListener(documentReference -> {


                    String serviceId = documentReference.getId();
                    DocumentReference userRef = mFirestore.collection("users")
                            .document(userId);
                    userRef.update(path_collection_tag, FieldValue.arrayUnion(serviceId));

                    uploadPhotos(path_collection_tag, serviceId, imageUrls);

                    Toast.makeText(context, "Upload successfully", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to upload", Toast.LENGTH_LONG).show();
        });

    }


    public void uploadPhotos(final String tag, final String offerId, List<String> imageUrls) {


        for (int i = 0; i < imageUrls.size(); i++) {

            Bitmap bm = ImageManager.getBitmap(imageUrls.get(i));
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            final String path = "/" + offerId + "/" + i;
            StorageReference storageReference = mStorageReference.getReference(tag).child(path);
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(taskSnapshot ->  {
                    DocumentReference ballroomRef =
                            mFirestore.collection(tag).document(offerId);
                    ballroomRef.update("images_links", FieldValue.arrayUnion(tag + path));

            });
        }
    }


//    public void readServices(final CallbackServiceList myCallback, String path_tag) {
//
//        final List<ServiceProvided> mList = new ArrayList<>();
//
//
//        mFirestore.collection(path_tag)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                mList.add(document.toObject(ServiceProvided.class));
//
//
//                            }
//                            myCallback.onGetServices(mList);
//                        } else {
//                            Log.d(TAG, "Error getting data!!!");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(mContext, "Error getting data!!!", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//    }
//
//    public void getServiceByName(final CallbackGetServiceByName myCallback, String service_name, String service_creator, String path_tag) {
//
//        final List<ServiceProvided> mList = new ArrayList<>();
//
//        mFirestore.collection(path_tag).whereEqualTo("name", service_name)
//                .whereEqualTo("creator", service_creator).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<ServiceProvided> serviceProvided = task.getResult().toObjects(ServiceProvided.class);
//                    Log.d("SIZE SERVICEPROVIDED: ", String.valueOf(serviceProvided.size()));
//                    ServiceProvided mService = serviceProvided.get(0);
//                    myCallback.onGetServiceById(mService);
//                } else {
//                    Log.d(TAG, "Error getting data!!!");
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(mContext, "Error getting data!!!", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private Task<Void> addRating(final DocumentReference serviceRef, final Rating rating) {
//        // Create reference for new rating, for use inside the transaction
//        final DocumentReference ratingRef = serviceRef.collection("ratings")
//                .document();
//
//        // In a transaction, add the new rating and update the aggregate totals
//        return mFirestore.runTransaction(new Transaction.Function<Void>() {
//            @Override
//            public Void apply(Transaction transaction)
//                    throws FirebaseFirestoreException {
//
//                ServiceProvided service = transaction.get(serviceRef)
//                        .toObject(ServiceProvided.class);
//
//                // Compute new number of ratings
//                int newNumRatings = service.getNumRatings() + 1;
//
//                // Compute new average rating
//                double oldRatingTotal = service.getAvgRating() *
//                        service.getNumRatings();
//                double newAvgRating = (oldRatingTotal + rating.getRating()) /
//                        newNumRatings;
//
//                // Set new restaurant info
//                service.setNumRatings(newNumRatings);
//                service.setAvgRating(newAvgRating);
//
//                // Commit to Firestore
//                transaction.set(serviceRef, service);
//                transaction.set(ratingRef, rating);
//                return null;
//            }
//        });
//    }


}



