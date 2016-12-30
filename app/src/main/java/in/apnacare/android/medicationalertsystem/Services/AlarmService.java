package in.apnacare.android.medicationalertsystem.Services;

/**
 * Created by dell on 26-10-2016.
 */

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import in.apnacare.android.medicationalertsystem.R;

import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.activity.AlarmActivity;
import in.apnacare.android.medicationalertsystem.activity.RingtoneServices;
import in.apnacare.android.medicationalertsystem.alert.AlarmAlertBroadcastReciever;
import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.database.DatabaseHandler;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

public class AlarmService extends Service {

@Override
public IBinder onBind(Intent intent) {

    return null;
}

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        Log.e(Constants.TAG, "Inside Alarm service ");
        Log.d(this.getClass().getSimpleName(),"onCreate()");
        super.onCreate();
    }

    private Alarm getNext(){
        Set<Alarm> alarmQueue = new TreeSet<Alarm>(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm lhs, Alarm rhs) {
                int result = 0;
                long diff = lhs.getAlarmTime().getTimeInMillis() - rhs.getAlarmTime().getTimeInMillis();
                if(diff>0){
                    return 1;
                }else if (diff < 0){
                    return -1;
                }
                Log.e(Constants.TAG, "Inside Alarm Service alarmQueuesresult "+result);
                return result;
            }
        });

        DatabaseHandler.init(getApplicationContext());
        List<Alarm> alarms = DatabaseHandler.getAll();


        for(Alarm alarm : alarms){
            if(alarm.getAlarmActive())
                alarmQueue.add(alarm);
            Log.e(Constants.TAG, "service getNext alarm "+alarmQueue.iterator());

        }


        if(alarmQueue.iterator().hasNext()){
            return alarmQueue.iterator().next();
        }else{
            return null;
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        DatabaseHandler.deactivate();
        super.onDestroy();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getSimpleName(),"onStartCommand()");
        Alarm alarm = getNext();
        Log.e(Constants.TAG, "Alarms service start command");
        if(null != alarm){
            alarm.schedule(getApplicationContext());
         }else{
            Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReciever.class);
            myIntent.putExtra("alarm", new Alarm());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
          }
        return START_NOT_STICKY;
    }

}
