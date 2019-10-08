package com.example.smartwallet.userUI;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwallet.Activity;
import com.example.smartwallet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class activity extends Fragment {

    RecyclerView recyclerView;
    String curr_user_id;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference databaseReference;

    public activity() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        recyclerView = view.findViewById(R.id.activity_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        curr_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("allUsers");

        Query query = FirebaseDatabase.getInstance().getReference().child("allUsers").child(curr_user_id).child("activity").orderByChild("timestamp")
                .limitToLast(10);


        FirebaseRecyclerOptions<Activity> options = new FirebaseRecyclerOptions.Builder<Activity>()
                .setQuery(query,Activity.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Activity,ActivityViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ActivityViewHolder holder, int position, @NonNull final Activity model) {
                Boolean isOwed = Boolean.parseBoolean(model.getIsOwed());
                if(isOwed){
                    holder.setHowMuchOwe("You owe "+model.getAmount(),true);
                }else {
                    holder.setHowMuchOwe("You get back "+model.getAmount(),false);
                }

                if (model.getUserId().equals(curr_user_id)){
                    holder.setWhoPaidWho("You added "+model.getTransactionName());
                }else{
                    databaseReference.child(model.getUserId()).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            holder.setWhoPaidWho(dataSnapshot.getValue(String.class)+" added '"+model.getTransactionName()+"'");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                holder.setDate(sdf.format(new Date(-1*model.getTimestamp())));


            }


            @NonNull
            @Override
            public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_list,parent,false);
                return new ActivityViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);


        return view;
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder{

        View view;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;
        }


        public void setWhoPaidWho(String whoPaidWhat){
            TextView textView = view.findViewById(R.id.whoAddedWhat);
            textView.setText(whoPaidWhat);
        }

        public void setHowMuchOwe(String howMuchOwe,boolean isOwed){
            TextView textView = view.findViewById(R.id.youOweHowMuch);
            textView.setText(howMuchOwe);
            if(isOwed){
                textView.setTextColor(Color.RED);
            }else {
                textView.setTextColor(Color.GREEN);
            }
        }

        public void setDate(String date){
            TextView textView = view.findViewById(R.id.activityDate);
            textView.setText(date);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
