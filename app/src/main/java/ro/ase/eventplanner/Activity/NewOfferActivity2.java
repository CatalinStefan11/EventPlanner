//package ro.ase.eventplanner.Activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//import ro.ase.eventplanner.R;
//
//public class NewOfferActivity extends AppCompatActivity {
//
//
//    private LinearLayout lnrImages;
//    private Button btnAddPhots;
//    private Button btnSaveImages;
//    private ArrayList<String> imagesPathList;
//    private Bitmap yourbitmap;
//    private Bitmap resized;
//    private final int PICK_IMAGE_MULTIPLE =1;
//    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_offer2);
//        lnrImages = (LinearLayout)findViewById(R.id.lnrImages);
//        btnAddPhots = (Button)findViewById(R.id.btnAddPhots);
//        btnSaveImages = (Button)findViewById(R.id.btnSaveImages);
//        btnAddPhots.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (checkPermissionREAD_EXTERNAL_STORAGE(getApplicationContext())) {
//                    Intent intent = new Intent(NewOfferActivity.this,
//                            CustomPhotoGalleryActivity.class);
//                    startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
//                }
//
//            }
//        });
//        btnSaveImages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(imagesPathList !=null){
//                    if(imagesPathList.size()>1) {
//                        Toast.makeText(NewOfferActivity.this,
//                                imagesPathList.size() + " no of images are selected",
//                                Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(NewOfferActivity.this,
//                                imagesPathList.size() + " no of image are selected",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(NewOfferActivity.this,
//                            " no images are selected", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if(requestCode == PICK_IMAGE_MULTIPLE){
//                imagesPathList = new ArrayList<String>();
//                String[] imagesPath = data.getStringExtra("data").split("\\|");
//                try{
//                    lnrImages.removeAllViews();
//                }catch (Throwable e){
//                    e.printStackTrace();
//                }
//                for (int i=0;i<imagesPath.length;i++){
//                    imagesPathList.add(imagesPath[i]);
//                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
//                    ImageView imageView = new ImageView(this);
//                    imageView.setImageBitmap(yourbitmap);
//                    imageView.setAdjustViewBounds(true);
//                    lnrImages.addView(imageView);
//                }
//            }
//        }
//
//    }
//
//    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
//            final Context context) {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        (Activity) context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    showDialog("External storage", context,
//                            Manifest.permission.READ_EXTERNAL_STORAGE);
//
//                } else {
//                    ActivityCompat
//                            .requestPermissions(
//                                    (Activity) context,
//                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
//                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//
//        } else {
//            return true;
//        }
//    }
//
//    public void showDialog(final String msg, final Context context,
//                           final String permission) {
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//        alertBuilder.setCancelable(true);
//        alertBuilder.setTitle("Permission necessary");
//        alertBuilder.setMessage(msg + " permission is necessary");
//        alertBuilder.setPositiveButton(android.R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions((Activity) context,
//                                new String[] { permission },
//                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                    }
//                });
//        AlertDialog alert = alertBuilder.create();
//        alert.show();
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // do your stuff
//                } else {
//                    Toast.makeText(NewOfferActivity.this, "GET_ACCOUNTS Denied",
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions,
//                        grantResults);
//        }
//    }
//}
