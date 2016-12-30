package in.apnacare.android.medicationalertsystem.activity;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.Provider;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.utils.Constants;

/**
 * Created by dell on 21-10-2016.
 */

public class RingtoneServices extends Service {
    @Nullable
    MediaPlayer mediasong;
    private Ringtone ringtone;
    int count = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

  /*  public int onStartCommand(Intent intent){



       return START_NOT_STICKY;
    };*/

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        Log.e(Constants.TAG, "onStartCommand ringtone Rciever ");
        Bundle action = intent.getExtras();
        String med = action.getString("ringtone-uri");

        Log.e(Constants.TAG, "onStartCommand: "+med);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Log.e(Constants.TAG, "on Alarm Reciever in evening time :  " + alarmUri);
        }
        //Uri ringtoneUri = Uri.parse(intent.getExtras().getString("ringtone-uri"));
        mediasong = MediaPlayer.create(this, R.raw.bells);
        this.ringtone = RingtoneManager.getRingtone(this, alarmUri);
        //ringtone.play();
        for(int i=count; i< 5 ;i++){

            mediasong.start();
            count++;
            Log.e(Constants.TAG, "onStartCommand: "+count);

        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy()
    {
        //ringtone.stop();
        mediasong.stop();
    }



}
