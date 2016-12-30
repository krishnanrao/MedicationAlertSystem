package in.apnacare.android.medicationalertsystem.Services;

/**
 * Created by dell on 22-11-2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

public class AlarmServiceBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmServiceBroadcastReciever", "onReceive()");
        AlarmCollectionModel alm = new AlarmCollectionModel(MedicationUsers.getContext());
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }

}