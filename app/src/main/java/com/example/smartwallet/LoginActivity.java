package com.example.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private EditText emailId, password;
    private Button btnLogin;
    private TextView tvlogin;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.loginIn);
        tvlogin = findViewById(R.id.toSignupText);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this,"You are logged in",
                            Toast.LENGTH_SHORT).show();
                    Intent tohome = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(tohome);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Not logged in!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Enter email");
                    emailId.requestFocus();
                }
                else if (pwd.isEmpty()) {
                    password.setError("Enter password");
                    password.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!email.isEmpty() && !pwd.isEmpty()) {
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(LoginActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this,
                                                        "Login error! Try again",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Intent tohome = new Intent
                                                        (LoginActivity.this,MainActivity.class);
                                                startActivity(tohome);
                                            }
                                        }
                                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,
                            "Error occured!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tosignup = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(tosignup);
                finish();
            }
        });

    }
}
