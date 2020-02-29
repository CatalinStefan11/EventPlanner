package ro.ase.eventplanner.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import ro.ase.eventplanner.Fragment.PhotosFragment;
import ro.ase.eventplanner.Model.BallroomFirebase;

public class FirebaseMethods {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;
    private String userId;
    private FirebaseStorage mStorageReference;
    private Context mContext;

    public FirebaseMethods(Context context){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mContext = context;
        mStorageReference = FirebaseStorage.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null){
            userId = mFirebaseAuth.getCurrentUser().getUid();
        }
    }


    public void addNewBallroom(BallroomFirebase ballroomFirebase,final List<String> imageUrls){


        ballroomFirebase.setImages_links(null);
        mFirestore.collection("ballrooms")
                .add(ballroomFirebase)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String ballroomdId = documentReference.getId();
                addBallroomToUSer(ballroomdId);

                uploadPhotos(PhotosUploadTag.TAG_BALLROOM, ballroomdId, imageUrls);
            }
        });


    }


    public void addBallroomToUSer(String ballroomId){

        DocumentReference userRef = mFirestore.collection("users").document(userId);
        userRef.update("ballrooms", FieldValue.arrayUnion(ballroomId));

    }




    public void uploadPhotos(final String  tag, final String offerId, List<String> imageUrls){



        for(int i = 0; i < imageUrls.size(); i++){

            Bitmap bm = ImageManager.getBitmap(imageUrls.get(i));
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            final String path =  "/" + offerId + "/" + i;
            StorageReference storageReference =  mStorageReference.getReference(tag).child(path);
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
}



