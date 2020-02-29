package ro.ase.eventplanner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.ase.eventplanner.R;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mPassword;
    private TextView mForgotPass;
    private TextView mSingUp;
    private Button mButtonSingIn;
    public static FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private ImageView mImgSingIn;
    private ImageView mButtonFacebook;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUI();
        mProgressBar.setVisibility(View.GONE);
        mButtonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailFromText = mEmail.getText().toString();
                String passFromText = mPassword.getText().toString();

                if(isValid(emailFromText,passFromText)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    singIn(emailFromText, passFromText);
                }
            }
        });


        mSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mFirebaseAuth.getCurrentUser();
        if(user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }
    }



    private void singIn(String email, String password){

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initializeUI(){

        mEmail = findViewById(R.id.textViewEmailLogIn);
        mPassword = findViewById(R.id.textViewPassLogIn);
        mForgotPass = findViewById(R.id.textForgottPass);
        mSingUp = findViewById(R.id.textDontHaveAcc);
        mButtonSingIn = findViewById(R.id.btnSignIn);
        mImgSingIn = findViewById(R.id.singInLogo);
        mButtonFacebook = findViewById(R.id.imageFacebook);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressBar2);


    }

    private boolean isValid(String email, String password){

        if(email.isEmpty()){
            mEmail.setError(getString(R.string.errorEmptyEmail));
            return false;
        }
        if(password.isEmpty()){
            mPassword.setError(getString(R.string.errorEmptyPassword));
            return false;
        }

        return true;
    }




}
