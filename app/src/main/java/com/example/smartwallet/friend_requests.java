package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class friend_requests extends AppCompatActivity {

    Query query;
    FirebaseRecyclerAdapter adapter;

    String curr_user_id;
    private FirebaseUser curr_user;
    private DatabaseReference friendDatabaseReference;
    private DatabaseReference friendRequestDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friend requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView = findViewById(R.id.friend_requests_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user_id = curr_user.getUid();

        friendDatabaseReference = FirebaseDatabase.getInstance().getReference().child("allUsers");
        friendRequestDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_req");

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Friend_req")
                .child(curr_user_id);

        FirebaseRecyclerOptions<Requests> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(query,Requests.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Requests,RequestViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_single_list_layout,parent,false);
                return new RequestViewHolder(view, new RequestViewHolder.MyClickListener() {
                    @Override
                    public void onAddFriend(final int p) {
                        final String user_id = getRef(p).getKey();
                        HashMap<String,String> map = new HashMap<>();
                        map.put("isOwed","2");
                        map.put("prevTransactionName","none");
                        map.put("prevTransactionValue","0");
                        map.put("total","0");
                        friendDatabaseReference.child(user_id).child("topUI").child(curr_user_id).setValue(map);
                        friendDatabaseReference.child(curr_user_id).child("topUI").child(user_id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(friend_requests.this,"Friend Request Accepted!",Toast.LENGTH_LONG).show();
                                    Button button = view.findViewById(R.id.beFriend);
                                    button.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark);
                                    button.setEnabled(false);
                                    friendRequestDatabaseReference.child(user_id).child(curr_user_id).equalTo("sent").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    friendRequestDatabaseReference.child(curr_user_id).child(user_id).equalTo("received").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Requests model) {
                final String user_id = getRef(position).getKey();
                Log.e("User ID",user_id);
                final String[] username = new String[1];
                final String[] emailid = new String[1];
                String request_type = model.getRequest_type();
                boolean received = false;
                if(request_type.equals("received")){
                    received = true;
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                final boolean finalReceived = received;
                friendDatabaseReference.child(user_id).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        username[0] = dataSnapshot.getValue(String.class);
                        if(finalReceived)
                        holder.setUsername(username[0]);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                friendDatabaseReference.child(user_id).child("EmailId").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        emailid[0] = dataSnapshot.getValue(String.class);
                        if (finalReceived)
                        holder.setEmailId(emailid[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(!received){
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }


        };
        recyclerView.setAdapter(adapter);
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

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View view;
        Button addFriend;
        MyClickListener listener;

        public RequestViewHolder(@NonNull View itemView,MyClickListener listener) {
            super(itemView);
            view = itemView;
            addFriend = view.findViewById(R.id.beFriend);
            this.listener = listener;
            addFriend.setOnClickListener(this);
        }

        public void setUsername(String username) {
            TextView textView = view.findViewById(R.id.friend_username);
            textView.setText(username);
        }

        public void setEmailId(String emailId) {
            TextView textView = view.findViewById(R.id.friend_emailid);
            textView.setText(emailId);
        }

        @Override
        public void onClick(View v) {
            listener.onAddFriend(this.getLayoutPosition());
        }

        public interface MyClickListener{
            void onAddFriend(int p);
        }
    }
}
