package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity {

    private Toolbar toolbar;
    private Button accountSettings, emailSettings, notificationSettings, passcode, deleteAccount;
    private ProgressDialog deleteAccountProgress;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountSettings = findViewById(R.id.accountSettings);
        emailSettings = findViewById(R.id.emailSettings);
        notificationSettings = findViewById(R.id.notificationSettings);
        passcode = findViewById(R.id.passcode);
        deleteAccount = findViewById(R.id.deleteAccount);
        deleteAccountProgress = new ProgressDialog(this);

        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        emailSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        notificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(settings.this);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Deleting your account will permenently delete your account from system.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccountProgress.setTitle("Deleting User.");
                        deleteAccountProgress.setMessage("Please Wait, while we delete your account.");
                        deleteAccountProgress.setCanceledOnTouchOutside(false);
                        deleteAccountProgress.show();

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    deleteAccountProgress.dismiss();
                                    Toast.makeText(settings.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent loginActivity = new Intent(settings.this, LoginActivity.class);
                                    startActivity(loginActivity);
                                }
                                else {
                                    deleteAccountProgress.hide();
                                    Toast.makeText(settings.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

}
