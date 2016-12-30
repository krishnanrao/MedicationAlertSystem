package in.apnacare.android.medicationalertsystem.preferences;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.activity.AddMedication;
import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.activity.BaseActivity;
import in.apnacare.android.medicationalertsystem.activity.BaseActivity1;
import in.apnacare.android.medicationalertsystem.activity.ViewMedicationActivity;
import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.database.DatabaseHandler;
import in.apnacare.android.medicationalertsystem.model.MedHistoryCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.model.RefillCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class AlarmPreferencesActivity extends BaseActivity implements View.OnClickListener {

    ImageButton deleteButton;
    TextView okButton;
    TextView cancelButton;
    private Alarm alarm;
    private MediaPlayer mediaPlayer;
    private Button save;
    private List<Alarm> alarmList;
    private ListAdapter listAdapter;
    private ListView listView;
    private static final int VIBRATE_PERMISSION = 1;
    private static final int WAKE_LOCK_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("Set Alarm Preferences");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.alarm_preferences);
        save = (Button) findViewById(R.id.saveBtn);
        Bundle bundle = getIntent().getExtras();


        if (bundle != null && bundle.containsKey("alarm")) {
            setMathAlarm((Alarm) bundle.getSerializable("alarm"));
        } else {
            setMathAlarm(new Alarm());
        }
        if (bundle != null && bundle.containsKey("adapter")) {
            setListAdapter((AlarmPreferenceListAdapter) bundle.getSerializable("adapter"));
        } else {
            setListAdapter(new AlarmPreferenceListAdapter(this, getMathAlarm()));
        }


        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
                final AlarmPreference alarmPreference = (AlarmPreference) alarmPreferenceListAdapter.getItem(position);

                AlertDialog.Builder alert;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                switch (alarmPreference.getType()) {
                    case BOOLEAN:
                        CheckedTextView checkedTextView = (CheckedTextView) v;
                        boolean checked = !checkedTextView.isChecked();
                        ((CheckedTextView) v).setChecked(checked);
                        switch (alarmPreference.getKey()) {
                            case ALARM_ACTIVE:
                                alarm.setAlarmActive(checked);
                                break;
                            case ALARM_VIBRATE:
                                alarm.setVibrate(checked);
                                if (checked) {
                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                }
                                break;
                        }
                        alarmPreference.setValue(checked);
                        break;
                    case STRING:

                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());


                        // Set an EditText view to get user input
                        final EditText input = new EditText(AlarmPreferencesActivity.this);

                        input.setText(alarmPreference.getValue().toString());

                        alert.setView(input);
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                alarmPreference.setValue(input.getText().toString());

                                if (alarmPreference.getKey() == AlarmPreference.Key.ALARM_NAME) {
                                    alarm.setAlarmName(alarmPreference.getValue().toString());
                                }

                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }
                        });
                        alert.show();
                        break;
                    case LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] items = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < items.length; i++)
                            items[i] = alarmPreference.getOptions()[i];

                        alert.setItems(items, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (alarmPreference.getKey()) {
                                    case ALARM_TONE:
                                        alarm.setAlarmTonePath(alarmPreferenceListAdapter.getAlarmTonePaths()[which]);
                                        if (alarm.getAlarmTonePath() != null) {
                                            if (mediaPlayer == null) {
                                                mediaPlayer = new MediaPlayer();
                                            } else {
                                                if (mediaPlayer.isPlaying())
                                                    mediaPlayer.stop();
                                                mediaPlayer.reset();
                                            }
                                            try {

                                                mediaPlayer.setVolume(0.2f, 0.2f);
                                                mediaPlayer.setDataSource(AlarmPreferencesActivity.this, Uri.parse(alarm.getAlarmTonePath()));
                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                                                mediaPlayer.setLooping(false);
                                                mediaPlayer.prepare();
                                                mediaPlayer.start();

                                                // Force the mediaPlayer to stop after 3
                                                // seconds...
                                                if (alarmToneTimer != null)
                                                    alarmToneTimer.cancel();
                                                alarmToneTimer = new CountDownTimer(3000, 3000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        try {
                                                            if (mediaPlayer.isPlaying())
                                                                mediaPlayer.stop();
                                                        } catch (Exception e) {

                                                        }
                                                    }
                                                };
                                                alarmToneTimer.start();
                                            } catch (Exception e) {
                                                try {
                                                    if (mediaPlayer.isPlaying())
                                                        mediaPlayer.stop();
                                                } catch (Exception e2) {

                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }

                        });

                        alert.show();
                        break;
                    case MULTIPLE_LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] multiListItems = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < multiListItems.length; i++)
                            multiListItems[i] = alarmPreference.getOptions()[i];

                        boolean[] checkedItems = new boolean[multiListItems.length];
                        for (Alarm.Day day : getMathAlarm().getDays()) {
                            checkedItems[day.ordinal()] = true;
                        }
                        alert.setMultiChoiceItems(multiListItems, checkedItems, new OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, int which, boolean isChecked) {

                                Alarm.Day thisDay = Alarm.Day.values()[which];

                                if (isChecked) {
                                    alarm.addDay(thisDay);
                                } else {
                                    // Only remove the day if there are more than 1
                                    // selected
                                    if (alarm.getDays().length > 1) {
                                        alarm.removeDay(thisDay);
                                    } else {
                                        // If the last day was unchecked, re-check
                                        // it
                                        ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                                    }
                                }

                            }
                        });
                        alert.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();

                            }
                        });
                        alert.show();
                        break;
                    case TIME:
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmPreferencesActivity.this, new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                Calendar newAlarmTime = Calendar.getInstance();
                                newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
                                newAlarmTime.set(Calendar.MINUTE, minutes);
                                newAlarmTime.set(Calendar.SECOND, 0);
                                alarm.setAlarmTime(newAlarmTime);
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }
                        }, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY), alarm.getAlarmTime().get(Calendar.MINUTE), true);
                        timePickerDialog.setTitle(alarmPreference.getTitle());
                        timePickerDialog.show();
                    default:
                        break;
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(Constants.TAG, "SEND_SMS ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.VIBRATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.VIBRATE},
                        VIBRATE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(Constants.TAG, "CALL_PHONE ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WAKE_LOCK)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WAKE_LOCK},
                        WAKE_LOCK_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }


        save.setOnClickListener(this);



    }


    private CountDownTimer alarmToneTimer;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("alarm", getMathAlarm());
        outState.putSerializable("adapter", (AlarmPreferenceListAdapter) getListAdapter());
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mediaPlayer != null)
                mediaPlayer.release();
        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Alarm getMathAlarm() {
        return alarm;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public ListAdapter getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        getListView().setAdapter(listAdapter);

    }

    public ListView getListView() {
        if (listView == null)
            listView = (ListView) findViewById(android.R.id.list);
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }



    @Override
    public void onClick(View v) {

        if(v == save){
            final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
            RefillCollection refillCollection = new RefillCollection();
            MedHistoryCollection medHistory = new MedHistoryCollection();

            AlarmCollectionModel alm = new AlarmCollectionModel(MedicationUsers.getContext());
            Bundle bundle = getIntent().getExtras();
            int med_id ;
            String medname = bundle.getString("medname");
            String dose = bundle.getString("dose");
            String doseSpinner = bundle.getString("doseSpinner");
            String qty = bundle.getString("qty_med");
            String minQty = bundle.getString("minQty");
            String maxQty = bundle.getString("maxQty");
            String pharma = bundle.getString("pharamaname");
            String doc = bundle.getString("doc");
            String mornTime = bundle.getString("mornTime");
            String mornTimeSpinner = bundle.getString("mornTimeSpinner");
            String noonTimeSpinner = bundle.getString("noonTimeSpinner");
            String evenTimeSpinner = bundle.getString("evenTimeSpinner");
            String nightTimeSpinner = bundle.getString("nightTimeSpinner");
            String noonTime = bundle.getString("noonTime");
            String evenTime = bundle.getString("evenTime");
            String nightTime = bundle.getString("nightTime");
            int medid = bundle.getInt("medid");



            if(bundle != null && bundle.containsKey("editMedication")){
                //String medid = bundle.getString("medid");
                Log.e(Constants.TAG, "On alarm Save  click "+medid);
                String previous_morn_value ,previous_noon_value,previous_even_value,previous_night_value;

                previous_morn_value = bundle.getString("previous_morn_value");
                previous_noon_value = bundle.getString("previous_noon_value");
                previous_even_value = bundle.getString("previous_even_value");
                previous_night_value = bundle.getString("previous_night_value");

                String text = bundle.getString("editMedication");
                Log.e(Constants.TAG, "From Edit previous_morn_value "+previous_morn_value);
                Log.e(Constants.TAG, "From Edit previous_noon_value: "+previous_noon_value);
                Log.e(Constants.TAG, "From Edit previous_even_value: "+previous_even_value);
                Log.e(Constants.TAG, "From Edit previous_night_value: "+previous_night_value);
                Log.e(Constants.TAG, "From Edit from: "+text);

                MedicationCollection medicine = new MedicationCollection();
                medicine.setQuantity(Integer.parseInt(qty));
                medicine.setDosage(dose + "-" + doseSpinner);
                medicine.setDoc_name(doc);
                medicine.setMedicationName(medname);
                medicine.setPharmacy_name(pharma);

                medicine.setFromDate(bundle.getString("strDate"));
                medicine.setToDate(bundle.getString("endDate"));

                medicine.setMorningAlarm(mornTimeSpinner);
                medicine.setAfterNoonAlarm(noonTimeSpinner);
                medicine.setEveningAlarm(evenTimeSpinner);
                medicine.setNightAlarm(nightTimeSpinner);
                Log.e(Constants.TAG, "Before saving medicine");
                medicine.updateMedications(medid);

                int amedid = bundle.getInt("medid");
                Log.e(Constants.TAG, "Med id from Edit form "+amedid);
                Log.e(Constants.TAG, "Med id from Global form "+medid);
                refillCollection.setMin_qty(minQty);
                refillCollection.setMax_qty(maxQty);
                Log.e(Constants.TAG, "From Edit "+maxQty);
                refillCollection.updateMinMax(medid);


                alarmList = alm.getAlarmsByMedID(medid);
                Log.e(Constants.TAG, "After editing  "+alarmList.size() +"MedId"+ medid);
                if(alarmList.size() > 0){

                    for(int i=0;i<alarmList.size();i++){



                        alm.updateAlarmMedId(getMathAlarm(),alarmList.get(i).getId());

                        if(!bundle.getString("mornTime").equals("N/A")   ){
                            String setAlarmTime = bundle.getString("mornTime");
                            String setAlarmName = bundle.getString("alarmName");
                            String noontime = bundle.getString("noonTime");
                            String eventime = bundle.getString("evenTime");
                            String nitetime = bundle.getString("nightTime");


                            Log.e(Constants.TAG, "Morning time alarm Activity: "+setAlarmTime+"value for e_enabled "+alarmList.get(i).getEven());
                            StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                            String firsttoken = token.nextToken();
                            String secondtoken = token.nextToken();
                            int hours = Integer.parseInt(firsttoken);
                            int min = Integer.parseInt(secondtoken);
                            Calendar calendar_m = Calendar.getInstance();


                            Log.e(Constants.TAG, "Alaram time in hours : " + Calendar.HOUR_OF_DAY);
                            Log.e(Constants.TAG, "Alaram time in hours : " + hours);
                            Log.e(Constants.TAG, "Alaram time in mins : " + min);
                            calendar_m.set(Calendar.HOUR_OF_DAY, hours);
                            calendar_m.set(Calendar.MINUTE, min);
                            calendar_m.set(Calendar.SECOND, 0);

                            alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                            alarmPreferenceListAdapter.notifyDataSetChanged();




                            DatabaseHandler.init(getApplicationContext());
                            if ( alarmList.get(i).getMorn() > 0 ) {
                        Log.e(Constants.TAG, "Alarm Update db for medid : " + medid);


                                alarm.setAlarmName(setAlarmName);
                                alarm.setMed_id(medid);
                                alarm.setAlarmTime(calendar_m);
                                alarm.setMorn(1);
                                alarm.setNoon(0);
                                alarm.setEven(0);
                                alarm.setNight(0);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                alm.updateAlarmMedId(getMathAlarm(),alarmList.get(i).getId());
                            }
                            if (previous_morn_value.equals("N/A") ) {
                                Log.e(Constants.TAG, "previous_morn_value so new alarm " + previous_morn_value);

                                alarm.setMed_id(medid);
                                alarm.setAlarmName(setAlarmName);
                                alarm.setAlarmTime(calendar_m);
                                alarm.setMorn(1);
                                alarm.setNoon(0);
                                alarm.setEven(0);
                                alarm.setNight(0);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                alm.addAlarm(getMathAlarm());

                            }
                            else{
                                Log.d(Constants.TAG, "Already there");
                            }
                            callMathAlarmScheduleService();
                            Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

                        }

                        if(!bundle.getString("noonTime").equals("N/A") ){
                            String setAlarmTime = bundle.getString("noonTime");
                            String setAlarmName = bundle.getString("alarmName");
                            String noontime = bundle.getString("noonTime");
                            String eventime = bundle.getString("evenTime");
                            String nitetime = bundle.getString("nightTime");

                            alarm.setAlarmName(setAlarmName);

                            Log.e(Constants.TAG, "Noontime alarm Activity: "+setAlarmTime+"value for e_enabled "+alarmList.get(i).getNoon());

                            StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                            String firsttoken = token.nextToken();
                            String secondtoken = token.nextToken();
                            int hours = Integer.parseInt(firsttoken);
                            int min = Integer.parseInt(secondtoken);
                            Calendar calendar_a = Calendar.getInstance();


                            Log.e(Constants.TAG, "Alaram time in hours : " + Calendar.HOUR_OF_DAY);
                            Log.e(Constants.TAG, "Alaram time in hours : " + hours);
                            Log.e(Constants.TAG, "Alaram time in mins : " + min);
                            calendar_a.set(Calendar.HOUR_OF_DAY, hours);
                            calendar_a.set(Calendar.MINUTE, min);
                            calendar_a.set(Calendar.SECOND, 0);

                            alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                            alarmPreferenceListAdapter.notifyDataSetChanged();




                            DatabaseHandler.init(getApplicationContext());
                            if (alarmList.get(i).getNoon() > 0 ) {
                                alarm.setMed_id(medid);
                                alarm.setAlarmTime(calendar_a);
                                alarm.setMorn(0);
                                alarm.setNoon(1);
                                alarm.setEven(0);
                                alarm.setNight(0);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                Log.e(Constants.TAG, "Alarm noon update db for medid : " + medid);
                                alm.updateAlarmMedId(getMathAlarm(),alarmList.get(i).getId());

                            }  if (previous_noon_value.equals("N/A") ) {
                                alarm.setMed_id(medid);
                                alarm.setAlarmTime(calendar_a);
                                alarm.setMorn(0);
                                alarm.setNoon(1);
                                alarm.setEven(0);
                                alarm.setNight(0);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                Log.e(Constants.TAG, "previous_noon_value so new alarm " + previous_noon_value);

                                alm.addAlarm(getMathAlarm());

                            }
                            else{
                                Log.d(Constants.TAG, "Already there");
                            }
                            callMathAlarmScheduleService();
                            Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
                        }

                      if(!bundle.getString("evenTime").equals("N/A") ){
                            String setAlarmTime = bundle.getString("evenTime");
                            String setAlarmName = bundle.getString("alarmName");


                            alarm.setAlarmName(setAlarmName);

                            Log.e(Constants.TAG, "Evening time alarm Activity: "+setAlarmTime +"value for e_enabled"+alarmList.get(i).getEven());




                            StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                            String firsttoken = token.nextToken();
                            String secondtoken = token.nextToken();
                            int hours = Integer.parseInt(firsttoken);
                            int min = Integer.parseInt(secondtoken);
                            Calendar calendar_e = Calendar.getInstance();
                            //int hour = calendar.get(Calendar.HOUR_OF_DAY);

                            Log.e(Constants.TAG, "Alaram time in hours : " + Calendar.HOUR_OF_DAY);
                            Log.e(Constants.TAG, "Alaram time in hours : " + hours);
                            Log.e(Constants.TAG, "Alaram time in mins : " + min);
                            calendar_e.set(Calendar.HOUR_OF_DAY, hours);
                            calendar_e.set(Calendar.MINUTE, min);
                            calendar_e.set(Calendar.SECOND, 0);

                            alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                            alarmPreferenceListAdapter.notifyDataSetChanged();

                            if (alarmList.get(i).getEven() > 0) {
                                Log.e(Constants.TAG, "Alarm Create db for medid : " + medid);
                                alarm.setAlarmTime(calendar_e);
                                alarm.setMed_id(medid);
                                alarm.setMorn(0);
                                alarm.setNoon(0);
                                alarm.setEven(1);
                                alarm.setNight(0);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                Log.e(Constants.TAG, "Alarm noon update db for medid : " + medid);
                                alm.updateAlarmMedId(getMathAlarm(),alarmList.get(i).getId());
                            }  if (previous_even_value.equals("N/A") ){
                              alarm.setMed_id(medid);
                              alarm.setAlarmTime(calendar_e);
                              alarm.setMorn(0);
                              alarm.setNoon(0);
                              alarm.setEven(1);
                              alarm.setNight(0);
                              alarm.setTaken(0);
                              alarm.setSnooze(0);
                              Log.e(Constants.TAG, "previous_even_value so new alarm " + previous_even_value);

                              alm.addAlarm(getMathAlarm());
                            }
                          else{
                              Log.d(Constants.TAG, "Already there");
                          }
                            callMathAlarmScheduleService();
                            Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

                        }
                        if(!bundle.getString("nightTime").equals("N/A")  ){
                            String setAlarmTime = bundle.getString("nightTime");
                            String setAlarmName = bundle.getString("alarmName");
                            alarm.setAlarmName(setAlarmName);
                            StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                            String firsttoken = token.nextToken();
                            String secondtoken = token.nextToken();
                            int hours = Integer.parseInt(firsttoken);
                            int min = Integer.parseInt(secondtoken);
                            Calendar calendar_n = Calendar.getInstance();
                            calendar_n.set(Calendar.HOUR_OF_DAY, hours);
                            calendar_n.set(Calendar.MINUTE, min);
                            calendar_n.set(Calendar.SECOND, 0);
                            alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                            alarmPreferenceListAdapter.notifyDataSetChanged();
                            DatabaseHandler.init(getApplicationContext());
                            if (alarmList.get(i).getNight() > 0) {
                                Log.e(Constants.TAG, "Alarm Create db for medid : " + medid);
                                alarm.setMed_id(medid);
                                alarm.setMorn(0);
                                alarm.setNoon(0);
                                alarm.setEven(0);
                                alarm.setNight(1);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                alarm.setAlarmTime(calendar_n);
                                alm.updateAlarmMedId(getMathAlarm(),alarmList.get(i).getId());
                            }  if (previous_night_value.equals("N/A") ){

                                alarm.setMed_id(medid);
                                alarm.setAlarmTime(calendar_n);
                                alarm.setMorn(0);
                                alarm.setNoon(0);
                                alarm.setEven(0);
                                alarm.setNight(1);
                                alarm.setTaken(0);
                                alarm.setSnooze(0);
                                alm.addAlarm(getMathAlarm());
                            }
                            else {
                                Log.e(Constants.TAG, "Already there ");
                            }
                            callMathAlarmScheduleService();
                            Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

                        }



                    }
                }





            }

            else if(bundle != null && bundle.containsKey("AddMedication")){


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = mdformat.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(qty));
                String endDate = mdformat.format(calendar.getTime());




                medHistory.setMed_name(medname);
                medHistory.setFrom_date(strDate);
                medHistory.setTo_date(endDate);
                medHistory.setM_status(mornTime);
                medHistory.setA_status(noonTime);
                medHistory.setE_status(evenTime);
                medHistory.setN_status(nightTime);
                medHistory.setTaken("0");
                medHistory.setSkip("0");
                medHistory.saveMedHistory();

                refillCollection.setMedicine_name(medname);
                refillCollection.setFrom_date(strDate);
                refillCollection.setTo_date(endDate);
                refillCollection.setMin_qty(minQty);
                refillCollection.setMax_qty(maxQty);
                refillCollection.setTaken_qty(minQty);
                refillCollection.saveRefillDetails();

                MedicationCollection medicine = new MedicationCollection();
                medicine.setQuantity(Integer.parseInt(qty));
                medicine.setDosage(dose + "-" + doseSpinner);
                medicine.setDoc_name(doc);
                medicine.setMedicationName(medname);
                medicine.setPharmacy_name(pharma);
                //medicine.setClinic_name(hosp);
                medicine.setFromDate(strDate);
                medicine.setToDate(endDate);

                medicine.setMorningAlarm(mornTimeSpinner);
                medicine.setAfterNoonAlarm(noonTimeSpinner);
                medicine.setEveningAlarm(evenTimeSpinner);
                medicine.setNightAlarm(nightTimeSpinner);

                med_id =  medicine.saveMedication();


            if(!bundle.getString("mornTime").equals("N/A")){
                String setAlarmTime = bundle.getString("mornTime");
                String setAlarmName = bundle.getString("alarmName");

                alarm.setMed_id(med_id);
                alarm.setMorn(1);
                alarm.setNight(0);
                alarm.setNoon(0);
                alarm.setEven(0);
                alarm.setTaken(0);
                alarm.setSnooze(0);
                alarm.setAlarmName(setAlarmName);
                StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                String firsttoken = token.nextToken();
                String secondtoken = token.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                Calendar calendar_m = Calendar.getInstance();
                //int hour = calendar.get(Calendar.HOUR_OF_DAY);

                Log.e(Constants.TAG, "Alaram time in hours : " + Calendar.HOUR_OF_DAY);
                Log.e(Constants.TAG, "Alaram time in hours : " + hours);
                Log.e(Constants.TAG, "Alaram time in mins : " + min);
                calendar_m.set(Calendar.HOUR_OF_DAY, hours);
                calendar_m.set(Calendar.MINUTE, min);
                calendar_m.set(Calendar.SECOND, 0);
                alarm.setAlarmTime(calendar_m);
                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                alarmPreferenceListAdapter.notifyDataSetChanged();
                alm.addAlarm(getMathAlarm());
                callMathAlarmScheduleService();
                Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

            }
            if(!bundle.getString("noonTime").equals("N/A")){
                String setAlarmTime = bundle.getString("noonTime");
                String setAlarmName = bundle.getString("alarmName");
                String noontime = bundle.getString("noonTime");
                String eventime = bundle.getString("evenTime");
                String nitetime = bundle.getString("nightTime");
                alarm.setMed_id(med_id);
                alarm.setNoon(1);
                alarm.setMorn(0);
                alarm.setNight(0);
                alarm.setEven(0);
                alarm.setTaken(0);
                alarm.setSnooze(0);
                alarm.setAlarmName(setAlarmName);

                StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                String firsttoken = token.nextToken();
                String secondtoken = token.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                Calendar calendar_a = Calendar.getInstance();

                calendar_a.set(Calendar.HOUR_OF_DAY, hours);
                calendar_a.set(Calendar.MINUTE, min);
                calendar_a.set(Calendar.SECOND, 0);
                alarm.setAlarmTime(calendar_a);
                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                alarmPreferenceListAdapter.notifyDataSetChanged();
                DatabaseHandler.init(getApplicationContext());

                alm.addAlarm(getMathAlarm());
                callMathAlarmScheduleService();
                Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

            }
            if(!bundle.getString("evenTime").equals("N/A")){
                String setAlarmTime = bundle.getString("evenTime");
                String setAlarmName = bundle.getString("alarmName");
                String noontime = bundle.getString("noonTime");
                String eventime = bundle.getString("evenTime");
                String nitetime = bundle.getString("nightTime");
                alarm.setMed_id(med_id);
                alarm.setAlarmName(setAlarmName);

                alarm.setMorn(0);
                alarm.setNoon(0);
                alarm.setEven(1);
                alarm.setNight(0);
                alarm.setTaken(0);
                alarm.setSnooze(0);
                Log.e(Constants.TAG, "Evening time alarm Activity: "+setAlarmTime);




                StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                String firsttoken = token.nextToken();
                String secondtoken = token.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                Calendar calendar_e = Calendar.getInstance();
                //int hour = calendar.get(Calendar.HOUR_OF_DAY);

                Log.e(Constants.TAG, "Alaram time in hours : " + Calendar.HOUR_OF_DAY);
                Log.e(Constants.TAG, "Alaram time in hours : " + hours);
                Log.e(Constants.TAG, "Alaram time in mins : " + min);
                calendar_e.set(Calendar.HOUR_OF_DAY, hours);
                calendar_e.set(Calendar.MINUTE, min);
                calendar_e.set(Calendar.SECOND, 0);
                alarm.setAlarmTime(calendar_e);
                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                alarmPreferenceListAdapter.notifyDataSetChanged();




                DatabaseHandler.init(getApplicationContext());
                alm.addAlarm(getMathAlarm());
                callMathAlarmScheduleService();
                Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

            }
            if(!bundle.getString("nightTime").equals("N/A")){
                String setAlarmTime = bundle.getString("nightTime");
                String setAlarmName = bundle.getString("alarmName");

                alarm.setMed_id(med_id);
                alarm.setAlarmName(setAlarmName);
                alarm.setNight(1);
                alarm.setMorn(0);
                alarm.setNoon(0);
                alarm.setEven(0);
                alarm.setTaken(0);
                alarm.setSnooze(0);
                StringTokenizer token = new StringTokenizer(setAlarmTime, ":");
                String firsttoken = token.nextToken();
                String secondtoken = token.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                Calendar calendar_n = Calendar.getInstance();
                calendar_n.set(Calendar.HOUR_OF_DAY, hours);
                calendar_n.set(Calendar.MINUTE, min);
                calendar_n.set(Calendar.SECOND, 0);
                alarm.setAlarmTime(calendar_n);
                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                alarmPreferenceListAdapter.notifyDataSetChanged();




                DatabaseHandler.init(getApplicationContext());
                alm.addAlarm(getMathAlarm());
                callMathAlarmScheduleService();
                Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

            }



            }
            Intent returnIntent = new Intent(this, ViewMedicationActivity.class);

            startActivity(returnIntent);
            finish();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
             case VIBRATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(Constants.TAG, "onRequestPermissionsResult: "+grantResults.length);
                    finish();
                    startActivity(getIntent());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
            case WAKE_LOCK_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();
                    startActivity(getIntent());

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(Constants.TAG, "onRequestPermissionsResult: "+grantResults.length);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }



            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}