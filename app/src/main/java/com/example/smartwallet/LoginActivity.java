package com.example.smartwallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    Toolbar toolbar;
    private EditText emailId, password;
    private Button btnLogin;
    private TextView toLoginPage, toForgotPassword;
    private ProgressDialog loginProgress;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.logIn);
        toLoginPage = findViewById(R.id.toSignupText);
        toForgotPassword = findViewById(R.id.forgotPassword);
        loginProgress = new ProgressDialog(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this,"You are logged in",
                            Toast.LENGTH_SHORT).show();
                    goToMain();
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

                else{
                    loginProgress.setTitle("Login User.");
                    loginProgress.setMessage("Please Wait, while we check your credentials.");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    loginUser(email,pwd);
                }
            }
        });

        toLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tosignup = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(tosignup);
                finish();
            }
        });

        toForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toResetPassword = new Intent(LoginActivity.this,resetPassword.class);
                startActivity(toResetPassword);
                finish();
            }
        });

    }

    private void loginUser(String email, String pwd) {
        mFirebaseAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    loginProgress.dismiss();
                                    if (mFirebaseAuth.getCurrentUser().isEmailVerified()){
                                        goToMain();
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    loginProgress.hide();
                                    Toast.makeText(LoginActivity.this, "Login error! Try again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    private void goToMain() {
        Intent tomain = new Intent(LoginActivity.this,MainActivity.class);
        tomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(tomain);
        finish();
    }
}
