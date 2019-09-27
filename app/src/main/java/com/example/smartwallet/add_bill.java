package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;


public class add_bill extends AppCompatActivity implements friendsSuggestedList.onSomeEventListener{

    AppCompatEditText usersNames;
    ListView suggestedUsers;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    boolean back=false;
    ChipGroup chipGroup;
    Set<String> hashSet = new HashSet<String>();
    DatabaseReference databaseReference;
    FirebaseUser curr_user;
    String curr_user_id;

    TextView paidBy,andSplit;
    Button you,equally,theyOwe;

    ListView theyOweListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> theyOweItems;




    TextView save;
    Button dollar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        chipGroup = findViewById(R.id.friendsNamesChips);
        final int[] positionSelected = {0};

        theyOweItems = new ArrayList<>();
        theyOweItems.add("Paid by you and split equally");
        theyOweItems.add("You owe the full amount");
        theyOweItems.add("They owe the full amount");
        theyOweItems.add("Paid by the other person and split equally");


        save = findViewById(R.id.saveBill);
        dollar = findViewById(R.id.changeCurrencyButton);
        you = findViewById(R.id.you_button);
        equally = findViewById(R.id.you_button2);
        paidBy = findViewById(R.id.textView8);
        andSplit = findViewById(R.id.textView9);
        theyOwe = findViewById(R.id.theyOweButton);

        frameLayout = findViewById(R.id.fragment);
        frameLayout.setVisibility(View.GONE);
        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user_id = curr_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("allUsers").child(curr_user_id).child("Friends");

        usersNames = findViewById(R.id.textInputLayout);
        usersNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersNames.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                suggestedUsers.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        frameLayout.setVisibility(View.VISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("seq", s.toString());
                        back=true;
                        friendsSuggestedList friendList = new friendsSuggestedList();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        friendList.setArguments(bundle);
//                        fragmentTransaction.addToBackStack("backPressed");
                        fragmentTransaction.replace(R.id.fragment,friendList).commit();

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                frameLayout.setVisibility(View.VISIBLE);
            }
        });


        theyOwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("theyOwe","button clicked");
                final Dialog dialog = new Dialog(add_bill.this,R.style.FilterDialogTheme);
                dialog.setContentView(R.layout.they_owe_dialogue);
                dialog.setTitle("How to split bill?");
                theyOweListView = dialog.findViewById(R.id.theyOweDialogueListView);
                adapter = new ArrayAdapter<String>(add_bill.this,android.R.layout.simple_list_item_1,theyOweItems);
                theyOweListView.setAdapter(adapter);
                dialog.show();
                theyOweListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        positionSelected[0] = position;
                        dialog.dismiss();
                        theyOwe.setText(theyOweItems.get(position));
                    }
                });

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hashset", String.valueOf(hashSet));
                Log.e("positionSelected",Integer.toString(positionSelected[0]));

            }
        });



    }

    @Override
    public void onBackPressed() {
        if(back){
            frameLayout.setVisibility(View.GONE);
            back = false;
        }else{
            super.onBackPressed();
        }


    }

    @Override
    public void someEvent(String s) {
        Log.e("userId",s);
        frameLayout.setVisibility(View.GONE);
        usersNames.getText().clear();
        final String[] name = new String[1];
        final Chip chip = new Chip(this);

        if (hashSet.size()==0){
            paidBy.setVisibility(View.GONE);
            you.setVisibility(View.GONE);
            andSplit.setVisibility(View.GONE);
            equally.setVisibility(View.GONE);

            theyOwe.setVisibility(View.VISIBLE);
        }else{
            paidBy.setVisibility(View.VISIBLE);
            you.setVisibility(View.VISIBLE);
            andSplit.setVisibility(View.VISIBLE);
            equally.setVisibility(View.VISIBLE);

            theyOwe.setVisibility(View.GONE);
        }

        databaseReference.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                name[0] = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();
                if(!hashSet.contains(key)){
                    hashSet.add(key);
                    chip.setText(name[0]);
                    chip.setCloseIconResource(R.drawable.ic_launcher_background);
                    chip.setCloseIconEnabled(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hashSet.remove(dataSnapshot.getKey());
                            if (hashSet.size()==1){
                                paidBy.setVisibility(View.GONE);
                                you.setVisibility(View.GONE);
                                andSplit.setVisibility(View.GONE);
                                equally.setVisibility(View.GONE);

                                theyOwe.setVisibility(View.VISIBLE);
                            }else{
                                paidBy.setVisibility(View.VISIBLE);
                                you.setVisibility(View.VISIBLE);
                                andSplit.setVisibility(View.VISIBLE);
                                equally.setVisibility(View.VISIBLE);

                                theyOwe.setVisibility(View.GONE);
                            }
                            chipGroup.removeView(v);
                        }
                    });

                    chipGroup.addView(chip);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


              //  chip.chipIcon = ContextCompat.getDrawable(requireContext(), baseline_person_black_18)

    }
}
