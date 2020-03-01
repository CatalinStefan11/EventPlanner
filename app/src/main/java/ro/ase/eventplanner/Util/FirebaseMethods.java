package ro.ase.eventplanner.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eventplanner.Model.Ballroom;
import ro.ase.eventplanner.Model.BallroomFirebase;

public class FirebaseMethods {

    private static FirebaseMethods ourInstance = null;
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseFirestore mFirestore;
    private static String userId;
    private static FirebaseStorage mStorageReference;
    private static Context mContext;
    private String TAG = "FirebaseMethods";


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


    public void addNewBallroom(BallroomFirebase ballroomFirebase, final List<String> imageUrls) {


        ballroomFirebase.setImages_links(null);
        mFirestore.collection("ballrooms")
                .add(ballroomFirebase)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        String ballroomdId = documentReference.getId();
                        addBallroomToUSer(ballroomdId);

                        uploadPhotos(FirebaseTag.TAG_BALLROOM, ballroomdId, imageUrls);
                    }
                });


    }


    public void addBallroomToUSer(String ballroomId) {

        DocumentReference userRef = mFirestore.collection("users").document(userId);
        userRef.update("ballrooms", FieldValue.arrayUnion(ballroomId));

    }


    public void uploadPhotos(final String tag, final String offerId, List<String> imageUrls) {


        for (int i = 0; i < imageUrls.size(); i++) {

            Bitmap bm = ImageManager.getBitmap(imageUrls.get(i));
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            final String path = "/" + offerId + "/" + i;
            StorageReference storageReference = mStorageReference.getReference(tag).child(path);
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DocumentReference ballroomRef =
                            mFirestore.collection(tag).document(offerId);
                    ballroomRef.update("images_links", FieldValue.arrayUnion(tag + path));

                }
            });
        }
    }


    public void readBallrooms(final Callbacks myCallback) {

        final List<BallroomFirebase> mList = new ArrayList<>();

        mFirestore.collection("ballrooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                mList.add(document.toObject(BallroomFirebase.class));


                            }
                        myCallback.OnGetAllBallrooms(mList);
                        }else {
                            Log.d(TAG, "Error getting data!!!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });

    }



}



