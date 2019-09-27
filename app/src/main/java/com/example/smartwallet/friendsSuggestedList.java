package com.example.smartwallet;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class friendsSuggestedList extends Fragment {

    ListView listView;

    DatabaseReference databaseReference;
    String curr_user_id;


    public friendsSuggestedList() {
        // Required empty public constructor
    }

    public interface onSomeEventListener {
        public void someEvent(String s);
    }

    onSomeEventListener someEventListener;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_friends_suggested_list, container, false);
        listView = view.findViewById(R.id.usersSuggestedList);

        String s = getArguments().getString("seq");

        curr_user_id = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("allUsers").child(curr_user_id).child("Friends");


        final ArrayList<String> arrayList = new ArrayList<String>();
        final ArrayList<String> arrayListKey = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, arrayList);

        databaseReference.orderByValue().startAt(s.toString()).endAt(s.toString()+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayListKey.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    String key = postSnapshot.getKey();
                    arrayList.add(post);
                    arrayListKey.add(key);
                    adapter.notifyDataSetChanged();
                    Log.e("Get Data", post);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Key",arrayListKey.get(position));
                Log.e("Value",arrayList.get(position));

                someEventListener.someEvent(arrayListKey.get(position));
            }
        });

        return view;
    }



}
