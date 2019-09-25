package com.example.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    Toolbar toolbar;
    TabLayout tabLayout;
    TabItem users;
    TabItem history;
    TabItem activity;
    ViewPager viewPager;
    PagerController pagerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Smart Wallet");

        tabLayout = findViewById(R.id.tablayout);
        users = findViewById(R.id.users);
        history = findViewById(R.id.history);
        activity = findViewById(R.id.activity);

        viewPager = findViewById(R.id.viewPager);

        pagerController = new PagerController(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerController);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = mFirebaseAuth.getCurrentUser();
        if (currUser==null) {
            goToLogin();
        }
    }

    private void goToLogin() {
        Intent tologin = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(tologin);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.all_users){
            Intent allUsers = new Intent(MainActivity.this,allUsers.class);
            startActivity(allUsers);
        }

        if (item.getItemId() == R.id.menu_settings) {
            Intent menuSettings = new Intent(MainActivity.this, settings.class);
            startActivity(menuSettings);
        }

        if(item.getItemId() == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            goToLogin();
        }

        return true;
    }
}
