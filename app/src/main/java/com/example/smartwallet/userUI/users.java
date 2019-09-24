package com.example.smartwallet.userUI;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwallet.R;
import com.example.smartwallet.TopUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class users extends Fragment {
    Button addUser;
    RecyclerView listView;

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseRecyclerAdapter adapter;
    String curr_user_id;

    public users() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        // Inflate the layout for this fragment
        addUser = view.findViewById(R.id.addUser);

        user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user_id = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("allUsers");
        Query query = FirebaseDatabase.getInstance().getReference().child("allUsers").child(curr_user_id).child("topUI");

        listView = view.findViewById(R.id.usersRecyclerList);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),com.example.smartwallet.allUsers.class);
                startActivity(intent);
            }
        });


        FirebaseRecyclerOptions<TopUI> options = new FirebaseRecyclerOptions.Builder<TopUI>()
                .setQuery(query,TopUI.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<TopUI,TopUIViewHolder>(options){

            @NonNull
            @Override
            public TopUIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistlayout,parent,false);
                return new TopUIViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final TopUIViewHolder holder, int position, @NonNull TopUI model) {

                databaseReference.child(getRef(position).getKey()).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.getValue(String.class);
                        holder.setUsername(userName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });

                final String user_id = getRef(position).getKey();

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),com.example.smartwallet.users_history.class);
                        intent.putExtra("User_id",user_id);
                        Log.e("User_id",user_id);
                        startActivity(intent);
                    }
                });

                holder.setPrevTransactionName(model.getPrevTransactionName());
                holder.setisOwed(model.getIsOwed());
                holder.setTotal(model.getTotal());
            }


        };

        listView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class TopUIViewHolder extends RecyclerView.ViewHolder{

        View view;


        public TopUIViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setUsername(String username) {
            TextView textView = view.findViewById(R.id.friend_list_username);
            textView.setText(username);
        }

        public void setPrevTransactionName(String transactionName) {
            TextView textView = view.findViewById(R.id.friend_list_prev);
            textView.setText(transactionName);
        }

        public void setisOwed(String isOwedString) {
            int isOwed = Integer.parseInt(isOwedString);
            TextView owed = view.findViewById(R.id.owed);
            if(isOwed==0){
                owed.setText(R.string.you_are_owed);
            }else if(isOwed==1){
                owed.setText("you owe");
            }else{
                owed.setText("settled up");
            }
        }

        public void setTotal(String totalString){
            TextView total = view.findViewById(R.id.friend_total);
            total.setText(totalString);
        }
    }
}
