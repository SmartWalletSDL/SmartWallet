package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;


public class add_bill extends AppCompatActivity implements friendsSuggestedList.onSomeEventListener{

    AppCompatEditText usersNames;
    ListView suggestedUsers;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    boolean back=false;
    ChipGroup chipGroup,tags;
    Set<String> hashSet = new HashSet<String>();
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceHistory;
    FirebaseUser curr_user;
    String curr_user_id;

    TextView paidBy,andSplit;
    Button you,equally,theyOwe;

    TextInputEditText description,total;

    ListView theyOweListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> theyOweItems;
    int lastChipChecked;
    Random random;

    ImageView imageIcon;


    String[] array;
    TextView save;
    Button dollar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        chipGroup = findViewById(R.id.friendsNamesChips);
        final int[] positionSelected = {2};

        theyOweItems = new ArrayList<>();
        theyOweItems.add("Paid by you and split equally");
        theyOweItems.add("You owe the full amount");
        theyOweItems.add("They owe the full amount");
        theyOweItems.add("Paid by the other person and split equally");

        array = new String[]{"man", "stud", "beard", "boy"};
        random = new Random();

        imageIcon = findViewById(R.id.imageView3);
        imageIcon.setImageResource(R.drawable.food);

        tags = findViewById(R.id.chipGroup);

        description = findViewById(R.id.descriptionText);
        total = findViewById(R.id.totalText);


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
        databaseReferenceHistory = FirebaseDatabase.getInstance().getReference().child("allUsers");


        tags.invalidate();
        tags.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Log.e("chipId",Integer.toString(checkedId));
            }
        });

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

        lastChipChecked = tags.getCheckedChipId();

        tags.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if(checkedId == View.NO_ID){
                    group.check(lastChipChecked);
                    return;
                }
                lastChipChecked = checkedId;


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hashset", String.valueOf(hashSet));
                Log.e("positionSelected",Integer.toString(positionSelected[0]));
                float youPay=0,theyPay=0;
                float anotherTotal=0,justAnotherTotal=0;
                boolean isOwed = false;
                float totalVal = Float.parseFloat(total.getText().toString());
                if(hashSet.size()==1){
                    switch (positionSelected[0]){
                        case 0:{
                            theyPay = totalVal/2;
                            youPay = totalVal/2;
                            isOwed = false;
                            anotherTotal = theyPay;
                            break;
                        }
                        case 1: {
                            youPay = totalVal;
                            isOwed = true;
                            anotherTotal = youPay;
                            break;
                        }
                        case 2:{
                            theyPay = totalVal;
                            isOwed = false;
                            anotherTotal = theyPay;
                            break;
                        }
                        case 3:{
                            youPay = totalVal/2;
                            theyPay = totalVal/2;
                            isOwed = true;
                            anotherTotal = theyPay;
                            break;
                        }
                    }
                    justAnotherTotal = anotherTotal;
                }else{
                    int people = hashSet.size()+1;
                    youPay = totalVal/people;
                    theyPay = totalVal/people;
                    anotherTotal = totalVal - youPay;
                    justAnotherTotal = theyPay;
                }
                final String descriptionVal = description.getText().toString();
                String tag = "food";
                if(lastChipChecked == findViewById(R.id.Shopping).getId()){
                    tag = "shopping";
                }else if(lastChipChecked == findViewById(R.id.Movies).getId()){
                    tag = "movies";
                }else if(lastChipChecked == findViewById(R.id.Others).getId()){
                    tag = "others";
                }

                final DecimalFormat df = new DecimalFormat("0.00");

                HashMap<String,Object> map = new HashMap<>();
                map.put("isOwed",Boolean.toString(isOwed));
                map.put("youPay",df.format(youPay));
                map.put("theyPay",df.format(theyPay));
                map.put("name",descriptionVal);
                map.put("tag",tag);
                map.put("timestamp", -1*new Date().getTime());

                HashMap<String,Object> friendMap = new HashMap<>();
                friendMap.put("isOwed",Boolean.toString(!isOwed));
                friendMap.put("youPay",df.format(theyPay));
                friendMap.put("theyPay",df.format(youPay));
                friendMap.put("name",descriptionVal);
                friendMap.put("tag",tag);
                friendMap.put("timestamp", -1*new Date().getTime());

                HashMap<String,Object> chart = new HashMap<>();
                chart.put("youPay",df.format((youPay)));
                chart.put("tag",tag);
                chart.put("timestamp",-1*new Date().getTime());

                HashMap<String,Object> activity = new HashMap<>();
                activity.put("transactionName",descriptionVal);
                activity.put("amount",df.format(anotherTotal));
                activity.put("isOwed",Boolean.toString(isOwed));
                activity.put("timestamp",-1*new Date().getTime());
                activity.put("userId",curr_user_id);

                HashMap<String,Object> friendActivity = new HashMap<>();
                friendActivity.put("transactionName",descriptionVal);
                friendActivity.put("amount",df.format(justAnotherTotal));
                friendActivity.put("isOwed",Boolean.toString(!isOwed));
                friendActivity.put("timestamp",-1*new Date().getTime());
                friendActivity.put("userId",curr_user_id);

                String uniqueID = UUID.randomUUID().toString();

                databaseReferenceHistory.child(curr_user_id).child("chart").child(uniqueID).setValue(chart);
                databaseReferenceHistory.child(curr_user_id).child("activity").child(uniqueID).setValue(activity);



                for(final String i:hashSet){
                    databaseReferenceHistory.child(curr_user_id).child("history").child(i).child(uniqueID).setValue(map);
                    databaseReferenceHistory.child(i).child("history").child(curr_user_id).child(uniqueID).setValue(friendMap);
                    databaseReferenceHistory.child(i).child("activity").child(uniqueID).setValue(friendActivity);
                    final boolean finalIsOwed = isOwed;
                    final float finalYouPay = youPay;
                    final float finalTheyPay = theyPay;
                    databaseReferenceHistory.child(curr_user_id).child("topUI").child(i).child("total").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            float totalTopUiValue = Float.parseFloat(dataSnapshot.getValue(String.class));
                            float totalAfterAddition = 0;
                            int isOwedInt = 0;
                            if(finalIsOwed){
                                totalAfterAddition = totalTopUiValue - finalYouPay;
                            }else{
                                totalAfterAddition = totalTopUiValue + finalTheyPay;
                            }

                            if(totalAfterAddition > 0){
                                isOwedInt = 1;
                            }else if(totalAfterAddition == 0){
                                isOwedInt = 2;
                            }


                            float friendTotalAfterAddition = 0;
                            int friendIsOwedInt = 0;
                            if(finalIsOwed){
                                friendTotalAfterAddition = totalTopUiValue + finalYouPay;
                            }else{
                                friendTotalAfterAddition = totalTopUiValue - finalTheyPay;
                            }

                            if(friendTotalAfterAddition > 0){
                                friendIsOwedInt = 1;
                            }else if(friendTotalAfterAddition == 0){
                                friendIsOwedInt = 2;
                            }

                            HashMap<String,Object> map1 = new HashMap<>();
                            map1.put("isOwed",Integer.toString(isOwedInt));
                            map1.put("prevTransactionName",descriptionVal);
                            map1.put("prevTransactionValue",df.format(finalYouPay));
                            map1.put("total",df.format(totalAfterAddition));

                            HashMap<String,Object> friendMap1 = new HashMap<>();
                            friendMap1.put("isOwed",Integer.toString(friendIsOwedInt));
                            friendMap1.put("prevTransactionName",descriptionVal);
                            friendMap1.put("prevTransactionValue",df.format(finalTheyPay));
                            friendMap1.put("total",df.format(friendTotalAfterAddition));

                            databaseReferenceHistory.child(i).child("topUI").child(curr_user_id).updateChildren(friendMap1);

                            databaseReferenceHistory.child(curr_user_id).child("topUI").child(i).updateChildren(map1, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


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
                    int no = random.nextInt(4);
                    int resId = getResources().getIdentifier(array[no],"drawable",getPackageName());
                    chip.setText(name[0]);
                    chip.setChipIconResource(resId);
                    chip.setCloseIconResource(R.drawable.close);
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
