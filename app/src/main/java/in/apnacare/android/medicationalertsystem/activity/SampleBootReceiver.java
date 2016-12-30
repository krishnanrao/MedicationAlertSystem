package in.apnacare.android.medicationalertsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dell on 04-11-2016.
 */

public class SampleBootReceiver extends BroadcastReceiver {

    AlarmReciever alarm = new AlarmReciever();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
           /* alarm.setAlarm(context);*/
            Log.e("taken", "onReceive: ");
        }
    }
}
