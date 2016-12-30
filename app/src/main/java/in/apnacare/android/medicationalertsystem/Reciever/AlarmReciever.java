package in.apnacare.android.medicationalertsystem.Reciever;

/**
 * Created by dell on 26-10-2016.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.Services.AlarmService;
import in.apnacare.android.medicationalertsystem.activity.AddMedication;
import in.apnacare.android.medicationalertsystem.utils.Constants;

public class AlarmReciever extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
      //  AddMedication inst = AddMedication.instance();
     //   MediaPlayer mediasong;
        //inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)

       String action = intent.getExtras().toString();

        Log.e(Constants.TAG, "on toggle button : "+action);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Log.e(Constants.TAG, "on Alarm Reciever in evening time :  "+alarmUri);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        Log.e(Constants.TAG, "Playing Ringtone : "+ringtone);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
