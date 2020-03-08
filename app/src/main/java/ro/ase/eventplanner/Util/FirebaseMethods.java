package ro.ase.eventplanner.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
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

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;
    private String userId;
    private FirebaseStorage mStorageReference;
    private Context mContext;
    public static List<BallroomFirebase> mList = new ArrayList<>();

    public FirebaseMethods(Context context) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mContext = context;
        mStorageReference = FirebaseStorage.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);


        if (mFirebaseAuth.getCurrentUser() != null) {
            userId = mFirebaseAuth.getCurrentUser().getUid();
        }
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

    public List<BallroomFirebase> getAllBallroomsFirebase() {


        mFirestore.collection("ballrooms")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {

                mList = queryDocumentSnapshots.toObjects(BallroomFirebase.class);
            }
        });

//        Task<QuerySnapshot> task = mFirestore.collection("ballrooms").get();
//
//
//        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                            for(QueryDocumentSnapshot document : task.getResult()){
//
//                                Log.d("DOCUMENT",
//                                        document.getId() + " => " + document.getData());
//                                mList.add(document.toObject(BallroomFirebase.class));
//
//
//                            }
//                            Log.d("SIZE FROM INSIDE",  String.valueOf(mList.size()));
//
//                            Log.d("OBJECT", mList.toString());
//                        }
//
//            }
//        });

        return mList;

    }


//        mFirestore.collection("ballrooms")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            for(QueryDocumentSnapshot document : task.getResult()){
//
//                                Log.d("DOCUMENT",
//                                        document.getId() + " => " + document.getData());
//                                mList.add(document.toObject(BallroomFirebase.class));
//
//
//                            }
//                            Log.d("SIZE FROM INSIDE",  String.valueOf(mList.size()));
//
//                            Log.d("OBJECT", mList.toString());
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(mContext, "Error getting data!!!", Toast.LENGTH_LONG).show();
//                    }
//                });


    public List<Ballroom> getBallrooms(List<BallroomFirebase> mList) {

        List<Ballroom> ballrooms = new ArrayList<>();


        for (int i = 0; i < mList.size(); i++) {

            BallroomFirebase blFromDB = mList.get(i);
            List<String> image_lins = blFromDB.getImages_links();

            for (int j = 0; j < image_lins.size(); j++) {
                StorageReference photoReference =
                        mStorageReference.getReference(image_lins.get(j));

                photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("URI:", uri.toString());
                    }
                });

            }
        }
        return ballrooms;

    }


}



