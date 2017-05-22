package com.ntu.powerranger.walkera;

/**
 * Created by nicho on 3/17/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import facade.stateControl;
import service.PedometerService;

public class Maintab1landing extends Fragment{

    //private static final String ARG_SECTION_NUMBER = "section_number";

    View rootView;
    TextView stepCountView;
    private String stepCount;
    public static int stepCounts;
    private ImageView imgV;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            stepCount = intent.getStringExtra("stepscount");
            //stepCount = "10028";
            stateControl.saveData(rootView.getContext(), "stepscount", stepCount);
            stepCountView.setText(stepCount+" / 10000");
            //stepCount = "10000";
            stepCountView.setText(stepCount);
            stepCounts = Integer.parseInt(stepCount);
            if(stepCounts >= 0)
            {
                imgV = (ImageView) rootView.findViewById(R.id.tree);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.flower);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.footstep5);
                imgV.setVisibility(View.VISIBLE);
            }
            if(stepCounts >= 2000)
            {
                imgV = (ImageView) rootView.findViewById(R.id.bird);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.hdb);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.footstep4);
                imgV.setVisibility(View.VISIBLE);
            }
            if(stepCounts >= 4000)
            {
                imgV = (ImageView) rootView.findViewById(R.id.ferrisWheel);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.gardensbythebay);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.footstep3);
                imgV.setVisibility(View.VISIBLE);
            }
            if(stepCounts >= 6000)
            {
                imgV = (ImageView) rootView.findViewById(R.id.mbs);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.hotel);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.footstep2);
                imgV.setVisibility(View.VISIBLE);
            }

            if(stepCounts >=8000)
            {
                imgV = (ImageView) rootView.findViewById(R.id.esplande);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.merlion);
                imgV.setVisibility(View.VISIBLE);
                imgV = (ImageView) rootView.findViewById(R.id.footstep);
                imgV.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.maintab1landing, container, false);
        stepCountView = (TextView) rootView.findViewById(R.id.count);
        stepCountView.setText(stepCount+" / 10000");

        LocalBroadcastManager.getInstance(rootView.getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("com.ntu.powerranger.walkera.pedometerService.update"));

        return rootView;
    }

    @Override
    public void onDestroy()
    {
        LocalBroadcastManager.getInstance(rootView.getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();

    }


}
