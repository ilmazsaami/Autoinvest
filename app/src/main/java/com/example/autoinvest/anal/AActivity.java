package com.example.autoinvest.anal;

import android.graphics.Color;
import android.os.Bundle;

import com.example.autoinvest.R;
import com.example.autoinvest.drawer.DrawerActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import static com.example.autoinvest.R.id.graph;


public class AActivity extends DrawerActivity {
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_anal);
        initDrawer();

        GraphView view = (GraphView)findViewById(graph);
        view.setTitle("Investment History");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setBackgroundColor(getColor(R.color.lightblue));
        view.addSeries(series);
        view.setTitle("Investments Growth year on year");
        view.setBackgroundColor(R.color.lightblue);
    }
}
