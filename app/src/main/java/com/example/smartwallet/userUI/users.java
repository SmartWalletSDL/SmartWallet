package com.example.smartwallet.userUI;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smartwallet.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class users extends Fragment {
    Button addUser;
    ListView listView;
    CustomAdapter usersAdapter;
    ArrayList<String> users;


    public users() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        // Inflate the layout for this fragment
        addUser = view.findViewById(R.id.addUser);
        listView = view.findViewById(R.id.usersList);
        users = new ArrayList<>();
        users.add("old user");
        usersAdapter= new CustomAdapter();
        listView.setAdapter(usersAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),com.example.smartwallet.users_history.class);
                startActivity(intent);
            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users.add("new user");
                usersAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.userlistlayout,null);
            TextView textView = view.findViewById(R.id.username);
            textView.setText(users.get(position));
            return view;
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
