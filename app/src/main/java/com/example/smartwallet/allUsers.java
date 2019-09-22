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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class allUsers extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    Query query;
    FirebaseRecyclerAdapter adapter;

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

        query = FirebaseDatabase.getInstance().getReference().child("allUsers");

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query,Users.class)
                .build();

         adapter = new FirebaseRecyclerAdapter<Users,UsersViewHolder>(options) {

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_users_recycler_list,parent,false);
                return new UsersViewHolder(view);
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

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View view;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setUsername(String username) {
            TextView textView = view.findViewById(R.id.username);
            textView.setText(username);
        }

        public void setEmailId(String emailId) {
            TextView textView = view.findViewById(R.id.emailid);
            textView.setText(emailId);
        }
    }
}
