package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class users_history extends AppCompatActivity {

    FloatingActionButton addbill;
    FirebaseUser curr_user;
    String curr_user_id;

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_history);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("User_id");


        addbill = findViewById(R.id.addBill);
        addbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),add_bill.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.detailedHistoryList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user_id = curr_user.getUid();



        Query query = FirebaseDatabase.getInstance().getReference().child("allUsers").child(curr_user_id).child("history").child(user_id)
                .orderByChild("timestamp").limitToLast(10);

        FirebaseRecyclerOptions<History> options = new FirebaseRecyclerOptions.Builder<History>()
                .setQuery(query,History.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<History,HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder holder, int position, @NonNull History model) {
                holder.setTransactionName(model.getName());
                boolean isOwed = Boolean.parseBoolean(model.getIsOwed());
                if(isOwed){
                    holder.setBorrowedText("you borrowed",model.getTheyPay());
                }else{
                    holder.setBorrowedText("you lent",model.getTheyPay());
                }

//                holder.setAmount(model.getTheyPay());
                holder.setTag(model.getTag());

            }


            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userhistorylistlayout,parent,false);
                return new HistoryViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);


    }


    public static class HistoryViewHolder extends RecyclerView.ViewHolder{
        View view;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;
        }

        public void setTransactionName(String name){
            TextView textView = view.findViewById(R.id.detailedHistoryTransactionName);
            textView.setText(name);
        }

        public void setTag(String tag){
            TextView textView = view.findViewById(R.id.detailedHistoryTransactionTag);
            ImageView imageView = view.findViewById(R.id.imageView2);
            textView.setText("tag: "+tag);
            if(tag.equals("food")){
                imageView.setImageResource(R.drawable.food);
            }else if (tag.equals("shopping")){
                imageView.setImageResource(R.drawable.shopping);
            }else if(tag.equals("movies")){
                imageView.setImageResource(R.drawable.movies);
            }else{
                imageView.setImageResource(R.drawable.others);
            }
        }

        public void setBorrowedText(String borrowedText,String amount){
            TextView textView = view.findViewById(R.id.detailedHistoryTransactionBorrowed);
            TextView textView2 = view.findViewById(R.id.detailedHistoryTransactionAmount);
            textView.setText(borrowedText);
            textView2.setText(amount);
            if(borrowedText.equals("you borrowed")){
                textView.setTextColor(Color.RED);
                textView2.setTextColor(Color.RED);
            }else {
                textView.setTextColor(Color.GREEN);
                textView2.setTextColor(Color.GREEN);
            }
        }

//        public void setAmount(String amount){
//            TextView textView = view.findViewById(R.id.detailedHistoryTransactionAmount);
//            textView.setText(amount);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
