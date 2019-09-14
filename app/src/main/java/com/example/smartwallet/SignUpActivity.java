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

public class SignUpActivity extends AppCompatActivity {

    private EditText emailId, password;
    private Button btnSignup;
    private TextView tvsignup;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        btnSignup = findViewById(R.id.signUp);
        tvsignup = findViewById(R.id.toLoginText);

        btnSignup.setOnClickListener(new View.OnClickListener() {
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
                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).
                            addOnCompleteListener(SignUpActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this,
                                                        "SignUp failed! Try again",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Intent home = new Intent
                                                        (SignUpActivity.this,MainActivity.class);
                                                startActivity(home);
                                            }
                                        }
                                    });
                }

            }
        });

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tologin = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(tologin);
            }
        });

    }
}
