package in.apnacare.android.medicationalertsystem.activity;

/**
 * Created by dell on 26-10-2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;
import in.apnacare.android.medicationalertsystem.Reciever.SampleBootReceiver;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.Services.AlarmService;

import in.apnacare.android.medicationalertsystem.activity.AddMedication;
import in.apnacare.android.medicationalertsystem.utils.Constants;

public class AlarmReciever extends WakefulBroadcastReceiver {
  //  public static Ringtone ringtone;
    private Handler mHandler = new Handler();
    Boolean isPlay;
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    int maxVolume = 50;
    private MediaPlayer mMediaPlayer;
    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        // AddMedication inst = AddMedication.instance();
        //inst.setAlarmText("Alarm! Wake up! Wake up!");

        Bundle action = intent.getExtras();
        String messageExtra = action.getString("extras");
        String Set = "set";
        String Taken = "yes";
        isPlay = false;
        Log.e(Constants.TAG, "Activity alarm reciever on toggle button : " + messageExtra);


        Log.e(Constants.TAG, "starting activity for ring tone ");

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Log.e(Constants.TAG, "on Alarm Reciever in evening time :  " + alarmUri);
        }
         MediaPlayer me = MediaPlayer.create(context, R.raw.alarm1);

          Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        Log.e(Constants.TAG, "Playing Ringtone : " + alarmUri.toString());



        if(messageExtra.equals(Set) ){

            Intent startIntent = new Intent(context, RingtoneServices.class);
            startIntent.putExtra("ringtone-uri", alarmUri);
            Log.e(Constants.TAG, "ringtoneUI"+alarmUri);
            context.startService(startIntent);
               ComponentName comp = new ComponentName(context.getPackageName(),
                        AlarmService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
                setResultCode(Activity.MODE_PRIVATE);


    }
            else if(messageExtra.equals(Taken) )
            {
                Intent stopIntent = new Intent(context, RingtoneServices.class);
                context.stopService(stopIntent);
               //cancelAlarm(context);
                   Log.e(Constants.TAG, "taken pressed or off is press ");
                  /* stopSound(context,alarmUri);
                   // me.pause();
                *//*AudioManager AudiMngr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                AudiMngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //me = MediaPlayer.create(context, R.raw.beep);*//*
                   if(isPlay) {
                       Log.e(Constants.TAG, "onReceive: "+isPlay.toString());
                       stopSound(context,alarmUri);
               }*/
           // me.setVolume(0,0);
                /*float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                me.setVolume(1-log1);*/
               // stopSound(context, getAlarmUri());
                /*if (ringtone.isPlaying())
                {
                    Log.e(Constants.TAG, "Off madu macha ");
                    ringtone.stop();
                }
                else
                {
                    ringtone.stop();
                    Log.e(Constants.TAG, "en gotta maga ");

                }*/
              Log.e(Constants.TAG, "onReceive: ");

            }
        else {

                Log.e(Constants.TAG, "it is not playing e: ");
            }



        }



    public void setAlarm(Context context, int hours, int min) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReciever.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Log.e(Constants.TAG, "setAlarm: "+hours+"min"+min);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hours);
        calendar.set(Calendar.MINUTE, min);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);
       ComponentName receiver = new ComponentName(context, SampleBootReceiver.class.getSimpleName());
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);



        playSound(context, getAlarmUri());

    }
    public void cancelAlarm(Context context) {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        /*ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
*/
        mMediaPlayer.stop();
    }
    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
    public void stopSound(Context context,Uri alert){
        mMediaPlayer = new MediaPlayer();
        try {
            Log.e(Constants.TAG, " before stopSound: "+mMediaPlayer.isPlaying());
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {

                Log.e(Constants.TAG, " before stopSound: "+mMediaPlayer.isPlaying());
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.stop();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
    }

