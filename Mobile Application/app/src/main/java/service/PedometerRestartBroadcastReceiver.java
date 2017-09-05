package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nicho on 3/18/2017.
 */

public class PedometerRestartBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(PedometerRestartBroadcastReceiver.class.getSimpleName(), "Pedometer Service Stops");
        context.startService(new Intent(context, PedometerService.class));;
    }
}
