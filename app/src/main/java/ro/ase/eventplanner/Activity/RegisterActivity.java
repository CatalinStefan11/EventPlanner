package ro.ase.eventplanner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ro.ase.eventplanner.Model.UserProfile;
import ro.ase.eventplanner.R;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mUsername, mPassword, mEmail;
    private Button mButtonSignUp;
    private TextView mTextSingIn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;
    private ProgressBar mProgressBar;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeUI();
        mProgressBar.setVisibility(View.GONE);

        mButtonSignUp.setOnClickListener(v -> {
            final String inputName = mUsername.getText().toString().trim();
            final String inputPw = mPassword.getText().toString().trim();
            final String inputEmail = mEmail.getText().toString().trim();

            if (isValid(inputName, inputPw, inputEmail)) {
                mProgressBar.setVisibility(View.VISIBLE);
                register(inputName, inputPw, inputEmail);
            }
        });


        mTextSingIn.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }


    private void initializeUI() {
        mUsername = findViewById(R.id.textViewUsernameReg);
        mEmail = findViewById(R.id.textViewEmailReg);
        mPassword = findViewById(R.id.textViewPassReg);
        mTextSingIn = findViewById(R.id.textYouHaveAccAlready);
        mButtonSignUp = findViewById(R.id.buttonSignUpReg);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressBar);
    }


    private void register(final String inputUsername, String inputPass,
                          final String inputEmail) {

        mFirebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String userId = task.getResult().getUser().getUid();
                        Toast.makeText(RegisterActivity.this,
                                "You've been registered successfully.",
                                Toast.LENGTH_SHORT).show();

                        mProgressBar.setVisibility(View.GONE);
                        sendUserData(userId, inputEmail, inputUsername);
                        startActivity(new Intent(RegisterActivity.this,
                                LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Email already exists.", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }


                });
    }

    private void sendUserData(String userId, String email, String username) {

        mFirestore = FirebaseFirestore.getInstance();
        UserProfile user = new UserProfile(email, username);

        mFirestore.collection("users")
                .document(userId).set(user)
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
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
