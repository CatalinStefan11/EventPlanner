package ro.ase.eventplanner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ro.ase.eventplanner.Model.UserProfile;
import ro.ase.eventplanner.R;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mUsername, mPassword, mEmail;
    private Button mButtonSignUp;
    private TextView mTextSingIn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeUI();


        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputName = mUsername.getText().toString().trim();
                final String inputPw = mPassword.getText().toString().trim();
                final String inputEmail = mEmail.getText().toString().trim();

                if (isValid(inputName, inputPw, inputEmail)) {
                    register(inputName, inputPw, inputEmail);
                }
            }
        });


        mTextSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }


    private void initializeUI() {
        mUsername = findViewById(R.id.textViewUsernameReg);
        mEmail = findViewById(R.id.textViewEmailReg);
        mPassword = findViewById(R.id.textViewPassReg);
        mTextSingIn = findViewById(R.id.textYouHaveAccAlready);
        mButtonSignUp = findViewById(R.id.buttonSignUpReg);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    private void register(final String inputUsername, final String inputPass,
                          final String inputEmail) {

        mFirebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserData(inputEmail, inputUsername, inputPass);
                            Toast.makeText(RegisterActivity.this,
                                    "You've been registered successfully.",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegisterActivity.this,
                                    LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Email already exists.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    private void sendUserData(String email, String username, String password) {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = mFirebaseDatabase.getReference("users");
        UserProfile user = new UserProfile(email, username, password);
        users.push().setValue(user);
    }

    private boolean isValid(String username, String password, String email) {

        if (username.isEmpty()) {
            mUsername.setError("Username is empty.");
            return false;
        }
        if (password.isEmpty()) {
            mPassword.setError("Password is empty.");
            return false;
        }
        if (email.isEmpty()) {
            mEmail.setError("Email is empty.");
            return false;
        }

        return true;
    }

}
