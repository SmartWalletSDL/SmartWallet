package com.example.smartwallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText emailId, password, name;
    private Button btnSignup;
    private TextView tvsignup;
    private ProgressDialog regProgress;
    FirebaseAuth mFirebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");

        mFirebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.signupName);
        emailId = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        btnSignup = findViewById(R.id.signUp);
        tvsignup = findViewById(R.id.toLoginText);
        regProgress = new ProgressDialog(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = name.getText().toString();
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();


                if(!nm.isEmpty() && !email.isEmpty() && !pwd.isEmpty()){
                    regProgress.setTitle("Registering User.");
                    regProgress.setMessage("Please Wait, while we create your account.");
                    regProgress.setCanceledOnTouchOutside(false);
                    regProgress.show();
                    registerUser(nm,email,pwd);
                }

                else if (nm.isEmpty()){
                    name.setError("Enter Name");
                    name.requestFocus();
                }

                else if (email.isEmpty()) {
                    emailId.setError("Enter email");
                    emailId.requestFocus();
                }
                else if (pwd.isEmpty()) {
                    password.setError("Enter password");
                    password.requestFocus();
                }

            }
        });

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tologin = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(tologin);
                finish();
            }
        });

    }

    private void registerUser(final String usernm, final String email, String pwd) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).
                addOnCompleteListener(SignUpActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    regProgress.dismiss();
                                    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = currUser.getUid();

                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("allUsers").child(uid);
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("Username",usernm);
                                    map.put("EmailId",email);
                                    databaseReference.setValue(map);

                                    goToMain();
                                }
                                else {
                                    regProgress.hide();
                                    Toast.makeText(SignUpActivity.this, "SignUp failed! Try again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    private void goToMain() {
        Intent tomain = new Intent(SignUpActivity.this,MainActivity.class);
        tomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(tomain);
        finish();
    }
}
