package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Random;

public class allUsers extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    Query query;
    FirebaseRecyclerAdapter adapter;

    private DatabaseReference friendRequestDatabase;
    private String curr_state;
    private FirebaseUser curr_user;
    String curr_user_id,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        toolbar = findViewById(R.id.app_tollbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.users_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendRequestDatabase  = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user_id = curr_user.getUid();


        curr_state = "not_friends";

        query = FirebaseDatabase.getInstance().getReference().child("allUsers");

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query,Users.class)
                .build();

         adapter = new FirebaseRecyclerAdapter<Users,UsersViewHolder>(options) {

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_users_recycler_list,parent,false);
                return new UsersViewHolder(view, new UsersViewHolder.MyClickListener() {
                    @Override
                    public void onAddFriend(int p) {
                        user_id = getRef(p).getKey();
                        if(!curr_user_id.equals(user_id)){
                        friendRequestDatabase.child(curr_user_id).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    friendRequestDatabase.child(user_id).child(curr_user_id).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Button button = view.findViewById(R.id.addFriend);
                                                button.setBackgroundResource(R.drawable.friendrequestsent);
                                                button.setEnabled(false);
                                                Toast.makeText(allUsers.this,"Request Sent Successfully",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });}else{
                            Toast.makeText(allUsers.this,"Stop Requesting To Yourself!",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                holder.setUsername(model.getUsername());
                holder.setEmailId(model.getEmailId());
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

    public static class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View view;
        Button addFriend;
        MyClickListener listener;
        Random random;

        public UsersViewHolder(@NonNull View itemView,MyClickListener listener) {
            super(itemView);

            view = itemView;
            random = new Random();
            addFriend = view.findViewById(R.id.addFriend);
            this.listener = listener;
            addFriend.setOnClickListener(this);
        }

        public void setUsername(String username) {
            TextView textView = view.findViewById(R.id.username);
            textView.setText(username);
            ImageView imageView = view.findViewById(R.id.imageView4);
            int no = random.nextInt(50);
            int resId = getResId("a"+no, R.drawable.class);
            imageView.setImageResource(resId);
        }

        public void setEmailId(String emailId) {
            TextView textView = view.findViewById(R.id.emailid);
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

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
