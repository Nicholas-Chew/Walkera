package com.ntu.powerranger.walkera;

/**
 * Created by nicho on 3/17/2017.
 */
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;

import facade.stateControl;


public class Maintab3userdetail extends Fragment implements View.OnClickListener {

    private XYPlot plot;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.maintab3userdetail, container, false);

        // initialize our XYPlot reference:
        plot = (XYPlot)rootView.findViewById(R.id.plot);
        plot.getBackgroundPaint().setColor(Color.WHITE);
        plot.setPlotMargins(0, 0, 0, 0);
        plot.getGraph().getBackgroundPaint().setColor(Color.WHITE);
        plot.getGraph().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraph().getDomainOriginLinePaint().setColor(getResources().getColor(R.color.colorPrimary));
        plot.getGraph().getRangeOriginLinePaint().setColor(getResources().getColor(R.color.colorPrimary));
        plot.getLayoutManager().remove(plot.getLegend());
        plot.getLayoutManager().remove(plot.getDomainTitle());
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).getPaint().setColor(Color.BLACK);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("0"));
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(Color.BLACK);


        ((EditText)rootView.findViewById(R.id.showHeight)).setText("171 cm");
        ((EditText)rootView.findViewById(R.id.showWeight)).setText("78 kg");

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13};
        Number[] series1Numbers = {9822, 8625, 10004, 10204, 7453, 6853, 8124, 5542, 6756};

        Calendar c = Calendar.getInstance();
        int dayDate = c.get(Calendar.DAY_OF_MONTH);
        for(int i = 0; i < 9; i++)
        {
            int temp;
            if(dayDate -(8-i) < 1)
                temp = 31 - dayDate -(8-i);
            else
                temp =  dayDate -(8-i);
            domainLabels[i] = temp;
        }

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, null);


        // create formatter to use for drawing a series using LineAndPointRenderer
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);


        // just for fun, add some smoothing to the lines
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));


        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

        Button logout = (Button) rootView.findViewById(R.id.logOut);
        logout.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logOut:
                stateControl.logout(rootView.getContext());
                Intent myIntent = new Intent(rootView.getContext(), SignupActivity.class);
                rootView.getContext().startActivity(myIntent);
                break;
        }
    }

}
