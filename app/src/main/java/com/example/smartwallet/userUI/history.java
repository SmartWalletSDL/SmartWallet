package com.example.smartwallet.userUI;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.smartwallet.R;

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


    public history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        pieChartView = view.findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, Color.BLUE).setLabel("Q1:$10"));
        pieData.add(new SliceValue(25,Color.GRAY).setLabel("Q2:$4"));
        pieData.add(new SliceValue(10,Color.RED).setLabel("Q3:$18"));
        pieData.add(new SliceValue(60,Color.MAGENTA).setLabel("Q4:$28"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasCenterCircle(true).setCenterText1("150").setCenterText2FontSize(30).setCenterText2("Total RS").setCenterText2FontSize(10);
        pieChartView.setPieChartData(pieChartData);

        return view;


    }

}
