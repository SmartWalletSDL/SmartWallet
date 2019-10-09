package com.example.smartwallet.userUI;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartwallet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class history extends Fragment {

    PieChartView pieChartView;
    DatabaseReference databaseReference;
    String curr_user_id;
    TextView foodText,shoppingText,moviesText,othersText;

    public history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        curr_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        pieChartView = view.findViewById(R.id.chart);

        foodText = view.findViewById(R.id.food);
        shoppingText = view.findViewById(R.id.shopping);
        moviesText = view.findViewById(R.id.movies);
        othersText = view.findViewById(R.id.others);



        FirebaseDatabase.getInstance().getReference().child("allUsers").child(curr_user_id).child("chart").orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        float food=100,shopping=0,movies=0,others=0;
                        List<SliceValue> pieData = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.child("tag").getValue().equals("food")){
                                food += Float.parseFloat(snapshot.child("youPay").getValue(String.class));
                            }else if(snapshot.child("tag").getValue().equals("shopping")){
                                shopping += Float.parseFloat(snapshot.child("youPay").getValue(String.class));
                            }else if(snapshot.child("tag").getValue().equals("movies")){
                                movies += Float.parseFloat(snapshot.child("youPay").getValue(String.class));
                            }else{
                                others += Float.parseFloat(snapshot.child("youPay").getValue(String.class));
                            }

                        }

                        pieData.add(new SliceValue(food, Color.parseColor("#194AD1")).setLabel("Food"));
                        pieData.add(new SliceValue(shopping,Color.parseColor("#F44336")).setLabel("Shopping"));
                        pieData.add(new SliceValue(movies,Color.parseColor("#4CAF50")).setLabel("Movies"));
                        pieData.add(new SliceValue(others,Color.MAGENTA).setLabel("Others"));

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true);
                        pieChartData.setHasCenterCircle(true).setCenterText1(Float.toString(food+shopping+movies+others)).setCenterText2FontSize(18).setCenterText2("Total RS").setCenterText2FontSize(10);
                        pieChartView.setPieChartData(pieChartData);

                        foodText.setText(Float.toString(food));
                        shoppingText.setText(Float.toString(shopping));
                        moviesText.setText(Float.toString(movies));
                        othersText.setText(Float.toString(others));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






        return view;


    }

}
