package in.apnacare.android.medicationalertsystem.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;

import in.apnacare.android.medicationalertsystem.Mandrill.EmailMessage;
import in.apnacare.android.medicationalertsystem.Mandrill.MandrillMessage;
import in.apnacare.android.medicationalertsystem.Mandrill.Recipient;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.Services.AlarmServiceBroadcastReciever;
import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.database.CareTakerCollectionModel;
import in.apnacare.android.medicationalertsystem.database.DatabaseHandler;
import in.apnacare.android.medicationalertsystem.database.ManageUserCollectionModel;
import in.apnacare.android.medicationalertsystem.model.CareTakerCollection;
import in.apnacare.android.medicationalertsystem.model.ManageUserCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.HapticFeedbackConstants;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class AlarmAlertActivity extends Activity implements OnClickListener, TextToSpeech.OnInitListener {

    private Alarm alarm;
    private MediaPlayer mediaPlayer;
    private NotificationManager alarmNotificationManager;

    Button takeMed,SnoozeAlarm;

    private Vibrator vibrator;
    public static String name;
    private boolean alarmActive;
    String uName;

    TextToSpeech ttsAlertAlarm;

    private TextView problemView;

    int snoozeCounter ;
    private int MY_DATA_CHECK_CODE = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_alert);

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");
        takeMed = (Button) findViewById(R.id.Button_clear);
        SnoozeAlarm = (Button) findViewById(R.id.Button_snooze);

        takeMed.setText("Taken");

//        ttsAlertAlarm =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    ttsAlertAlarm.setLanguage(Locale.ENGLISH);
//                }
//            }
//        });

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        Log.e(Constants.TAG, "Audio volume inside alarm activity: "+am);
        ttsAlertAlarm = new TextToSpeech(this, this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        this.setTitle("hai");

        /*switch (alarm.getDifficulty()) {
            case EASY:
                mathProblem = new MathProblem(3);
                break;
            case MEDIUM:
                mathProblem = new MathProblem(4);
                break;
            case HARD:
                mathProblem = new MathProblem(5);
                break;
        }*/

        /*answerString = String.valueOf(mathProblem.getAnswer());
        if (answerString.endsWith(".0")) {
            answerString = answerString.substring(0, answerString.length() - 2);
        }*/

        ManageUserCollection manageUserCollection = new ManageUserCollection();
        ManageUserCollectionModel mModel = new ManageUserCollectionModel(MedicationUsers.getContext());
        ArrayList<ManageUserCollection> userList = new ArrayList<ManageUserCollection>();
        userList = mModel.getUsers();
        for(int i=0;i<userList.size();i++){
            name = userList.get(i).getUser_name();

        }
        //String name =  MedicationUsers.preferences.getString("userName",null);


        StringTokenizer firstname = new StringTokenizer(name," ");
        String fname = firstname.nextToken();
//        String sname = firstname.nextToken();
        problemView = (TextView) findViewById(R.id.textView1);
        problemView.setText("Hi "+ fname +", \nIt's time to take your medication "+ alarm.getAlarmName());// + "alarm id" + alarm.getId() );

        /*if(ttsAlertAlarm.isSpeaking()){
            Log.e(Constants.TAG, "onCreate: "+ ttsAlertAlarm.isSpeaking());
        }
        else
        { speakWords(problemView.getText().toString());
            Log.e(Constants.TAG, "onCreate: "+ ttsAlertAlarm.isSpeaking());
        }*/
        Log.e(Constants.TAG, "Inside create method after setting problemview "+alarm.getAlarmTonePath().toString());

       /* try {
            Log.e(Constants.TAG,"problemView: "+problemView.getText().toString());
            speakWords(problemView.getText().toString());
        }catch (Exception e){
            Log.e(Constants.TAG,"speakWords Exception: "+e.toString());
        }
*/

        Log.e(Constants.TAG, " Alarm Time "+alarm.getAlarmTime());

        if(alarm.getSnooze() >= 0){

            Log.e(Constants.TAG, "Snooze number "+alarm.getSnooze());
            Log.e(Constants.TAG, "Taken number "+alarm.getTaken());


        }


        ((Button) findViewById(R.id.Button_clear)).setOnClickListener(this);


        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(getClass().getSimpleName(), "Incoming call: "
                                + incomingNumber);
                        try {
                            mediaPlayer.pause();
                            Log.e(Constants.TAG, "Inside start alarm while call state ringing ");
                           // speakWords(problemView.getText().toString());
                        } catch (IllegalStateException e) {

                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(getClass().getSimpleName(), "Call State Idle");
                        try {
                            /*String toSpeak = problemView.getText().toString();
                            ttsAlertAlarm.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);*/
                            mediaPlayer.start();

                            Log.e(Constants.TAG, "Inside start alarm while callstateIDle "+alarm.getAlarmTonePath().toString());
                           // speakWords(problemView.getText().toString());
                        } catch (IllegalStateException e) {

                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };



        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        // Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();
       // sendNotification("macha");

        Log.e(Constants.TAG, "Inside On create  before start alarm call ");
        speakWords(problemView.getText().toString());

        startAlarm();

        SnoozeAlarm.setOnClickListener(this);

    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                ttsAlertAlarm = new TextToSpeech(getApplicationContext(), this);
                Log.e(Constants.TAG,"ttsAlarmActivity: "+ttsAlertAlarm.isSpeaking());
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    private void startAlarm() {
       /* String toSpeak = problemView.getText().toString();
        ttsAlertAlarm.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);*/
        Log.e(Constants.TAG, "Inside start alarm before checking alarm path condidtion "+alarm.getAlarmTonePath().toString());
        Log.e(Constants.TAG,"[startAlarm] problemView: "+problemView.getText().toString().replaceAll("\\n",""));
       // speakWords(problemView.getText().toString().replaceAll("\\n",""));
        if (alarm.getAlarmTonePath() != "") {
            mediaPlayer = new MediaPlayer();
            if (alarm.getVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = { 1000, 200, 200, 200 };
                vibrator.vibrate(pattern, 0);
                Log.e(Constants.TAG, "Inside start alarm before while vibrating "+alarm.getAlarmTonePath().toString());
               // speakWords(problemView.getText().toString());
            }
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this,
                        Uri.parse(alarm.getAlarmTonePath()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Log.e(Constants.TAG, "Inside start alarm before speak "+alarm.getAlarmTonePath().toString());

                speakWords(problemView.getText().toString().replaceAll("\\n",""));

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        }


    }
    private void speakWords(String speech) {
        //speak straight away
        //tts.setSpeechRate((float) 0.6);
        //tts.setPitch((float) 1.2);
        if(ttsAlertAlarm != null){

            String[] speechArray = speech.split("");
            Log.e(Constants.TAG, "speakWords speech array length: "+speechArray.length);
          /*for(int i=0;i<speechArray.length;i++){

          }*/

            ttsAlertAlarm.speak(speech,TextToSpeech.QUEUE_FLUSH, null);
            ttsAlertAlarm.speak(speech,TextToSpeech.QUEUE_FLUSH, null);
            ttsAlertAlarm.speak(speech,TextToSpeech.QUEUE_FLUSH, null);
            Log.e(Constants.TAG, "Inside speakWords: "+speech);
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {

        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            if(ttsAlertAlarm != null){
                ttsAlertAlarm.stop();
                ttsAlertAlarm.shutdown();
            }

            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    public Alarm getMathAlarm() {
        return alarm;
    }

    @Override
    public void onClick(View v) {
        ttsAlertAlarm.stop();
        if (!alarmActive)
            return;
        String button = (String) v.getTag();
        Log.e(Constants.TAG, "Snooze Button Tag value "+button);
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (button.equalsIgnoreCase("clear")) {

            AlarmCollectionModel alm = new AlarmCollectionModel(MedicationUsers.getContext());
            ttsAlertAlarm.stop();
            ttsAlertAlarm.shutdown();
            takeMed.setText("Taken");
            alarmActive = false;
            if (vibrator != null)
                vibrator.cancel();
            try {

                ttsAlertAlarm.stop();
                ttsAlertAlarm.shutdown();
                alm.updateTaken(alarm.getId(),1);
                Log.e(Constants.TAG, "Taken value of the database after update: "+alm.getTakenCountByAlarmId(alarm.getId()));
            } catch (IllegalStateException ise) {

            }
            try {

            } catch (Exception e) {

            }

            this.finish();
        }

        if(v == SnoozeAlarm){
            ttsAlertAlarm.stop();
            int snoozeTime = 0;
            int snoozehours = 0;
            Log.e(Constants.TAG, "Inside on click of snooze "+alarm.getAlarmTonePath().toString());

            AlarmCollectionModel alm  = new AlarmCollectionModel(MedicationUsers.getContext());
            Log.e(Constants.TAG, "Snooze is pressed");
            Calendar snooze = Calendar.getInstance();
            snooze.get(Calendar.HOUR);
            snooze.get(Calendar.MINUTE);
            Log.e(Constants.TAG, "Snooze presse on"+snooze.get(Calendar.HOUR)+ "in minutes "+ snooze.get(Calendar.MINUTE));

            if (vibrator != null)
                vibrator.cancel();
            try {

                ttsAlertAlarm.stop();

            } catch (IllegalStateException ise) {

            }
            try {
                ttsAlertAlarm.stop();

            } catch (Exception e) {

            }
            this.finish();

            snoozeTime = snooze.get(Calendar.MINUTE)+1;
            snoozehours = snooze.get(Calendar.HOUR);
            snooze.set(Calendar.MINUTE,snoozeTime);
            Log.e(Constants.TAG, "Snooze presse on  "+snooze.get(Calendar.HOUR)+ " in minutes "+ snooze.get(Calendar.MINUTE));
            String time = snooze.get(Calendar.HOUR) +":"+ snoozeTime;
            snooze.set(Calendar.HOUR,snoozehours);
            snooze.set(Calendar.MINUTE,snoozeTime);


            ArrayList<Alarm> almList = new ArrayList<Alarm>();
            almList = alm.getAlarmsByMedID(alarm.getMedAlarmId());
            for(int i=0;i<almList.size();i++){
                Log.e(Constants.TAG, "Snooze on alarm: "+alarm.getSnooze());
                snoozeCounter = alm.getSnoozeCountByAlarmId(almList.get(i).getId());
                Log.e(Constants.TAG, "only snooze value: "+alm.getSnoozeCountByAlarmId(almList.get(i).getId()));
                snoozeCounter = snoozeCounter + 1;
            }

            if(snoozeCounter <= 2){
                Log.e(Constants.TAG, "Snooze number Before updating "+alarm.getSnooze());
                alarm.setAlarmTime(snooze);
                alarm.setSnooze(snoozeCounter);
                //alm.updateSnooze(alarm.getId(),snoozeCounter);
                alm.updateAlarmMedId(getMathAlarm(),alarm.getId());
                alm.updateSnooze(alarm.getId(),snoozeCounter);
                callMathAlarmScheduleService();
                Log.e(Constants.TAG, "Snooze counter "+snoozeCounter);
                Log.e(Constants.TAG, "Snooze number after updating "+alarm.getSnooze());
                Toast.makeText(this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();

            }
            if(snoozeCounter >= 3){
                alarmActive = false;
                ttsAlertAlarm.stop();
                ttsAlertAlarm.shutdown();
                Log.e(Constants.TAG, "snooze counter is more than 2 "+snoozeCounter);
                ArrayList<CareTakerCollection> careTakerList = new ArrayList<CareTakerCollection>();
                CareTakerCollectionModel careTaker =  new CareTakerCollectionModel(MedicationUsers.getContext());
                ManageUserCollectionModel mUser = new ManageUserCollectionModel(MedicationUsers.getContext());
                ArrayList<ManageUserCollection> mUserList = new ArrayList<ManageUserCollection>();
                mUserList = mUser.getUsers();

                for(int i=0;i<mUserList.size();i++){
                    uName = mUserList.get(i).getUser_name();
                    Log.e(Constants.TAG, "insisde snooze is pressed and is >=2 in for loop "+uName);

                }

               careTakerList =  careTaker.getCareTaker();
                Log.e(Constants.TAG, "Checking internet connection: "+isNetworkAvailable());
                if(careTakerList.size() > 0 && isNetworkAvailable()){


                for(int i=0;i<careTakerList.size();i++){

                    String msg = "Dear, "+careTakerList.get(i).getCare_taker_name()+" we here by inform you that your "+careTakerList.get(i).getCare_taker_relation()+" "+uName+" has skipped the medicine "+alarm.getAlarmName();
                    msg = msg.replaceAll(" ","%20");
                    Toast.makeText(this, "Sending status to your kin", Toast.LENGTH_SHORT).show();
                    GetXMLTask task = new GetXMLTask();
                    String URL ="https://apnacare.in/portal/notification/sms?to="+careTakerList.get(i).getCare_taker_phnumber()+"&message="+msg;

                    task.execute(new String[] { URL });
                    sendEmail(careTakerList.get(i).getCare_taker_email(),careTakerList.get(i).getCare_taker_name(),uName);

                }
                }
                else if(careTakerList.size() <=0){
                    Toast.makeText(this, "Please save your kin details to keep them updated", Toast.LENGTH_SHORT).show();
                }
                else if(!isNetworkAvailable()){
                    Toast.makeText(this, "Please check your internet connection for updating the kin", Toast.LENGTH_SHORT).show();
                }
                this.finish();
            }

        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    protected void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }



    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(ttsAlertAlarm.isLanguageAvailable(new Locale("en","IN")) == TextToSpeech.LANG_AVAILABLE){
                ttsAlertAlarm.setLanguage(new Locale("en","IN"));
            }else if(ttsAlertAlarm.isLanguageAvailable(new Locale("en","IN")) == TextToSpeech.LANG_NOT_SUPPORTED) {
                ttsAlertAlarm.setLanguage(Locale.ENGLISH);
            }
            Log.e(Constants.TAG,"tts init status success");
            for(int i =0 ; i < 15 ; i++){
                speakWords(problemView.getText().toString());
            }

        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            Log.e(Constants.TAG,"tts init status error");
        }
    }


    private class GetXMLTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            Log.e(Constants.TAG, "getOutputFromUrl: "+url);
            try {
                InputStream stream = getHttpConnection(url);
                Log.e(Constants.TAG, "getOutputFromUrl:stream "+stream);
                if(stream != null){
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(stream));
                    String s = "";
                    Log.e(Constants.TAG, "getOutputFromUrl  inside try abd stream !=null "+url);
                    while ((s = buffer.readLine()) != null)
                        output.append(s);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            Log.e(Constants.TAG, "getHttpConnection: Url connection url = "+url);
            Log.e(Constants.TAG, "getHttpConnection: Connection is  = "+connection);
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.setConnectTimeout(5000);
                httpConnection.connect();
                Log.e(Constants.TAG, "getHttpConnection: responseCode "+httpConnection.getResponseCode());
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                    Log.e(Constants.TAG, "getHttpConnection: http is ok "+stream);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(Constants.TAG, "getHttpConnection: exception "+ex.toString());
            }
            Log.e(Constants.TAG, "getHttpConnection: Returning stream  = "+stream);
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            Log.e(Constants.TAG, "onPostExecute: "+output);
        }
    }

    public void sendEmail(String Recipient, String Recipient_name,String UserName){


        MandrillMessage allMessage = new MandrillMessage("D2J_JGYO9LU63X68aTJ7zQ");
        // create your message
        EmailMessage message = new EmailMessage();
        message.setFromEmail("no-reply@apnacare.in");
        message.setFromName("ApnaCare");
        message.setHtml("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "</head>\n" +
                "<body style=\"background: #e2e1e0; font-family: 'Roboto', sans-serif !important;\">\n" +
                "\t<div style=\"display:block; width: 88%; margin: 0 auto; background: #fff; border-radius: 2px; box-shadow: 0 10px 20px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);\">\n" +
                "\t\t<!-- Header -->\n" +
                "\t\t<div style=\"display: block; max-width: 99%; min-height: 50px; border-bottom: 1px solid #E0E0E0; padding: 10px 10px 5px 10px; \">\n" +
                "\t\t\t<div style=\"display: inline-block; float:left;\">\n" +
                "\t\t\t\t<div style=\"font-size: 28px; padding-left: 15px; font-weight: 300; height: 45px; color: #2899dc; line-height: 45px; vertical-align: middle; margin-right: 10px; letter-spacing: 1px\"><img src=\"http://apnacare.in/uploads/smart_pill_reminder.png\" alt=\"Smart Pill Reminder\" title=\"ApnaCare India Pvt Ltd\" style = \"margin:0px 5px 0px 10px; width:50px; height:50px;vertical-align: middle;\" /> Smart Pill Reminder</div>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\n" +
                "\t\t<!-- Main Content -->\n" +
                "\t\t<div style=\"min-height: 200px; padding: 10px 15px; font-size: 14px; line-height: 22px; text-align: justify; margin: 10px 0; border-bottom: 1px solid #E0E0E0\">\n" +
                "            <span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\"></span>\n" +
                "\t\t\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Dear "+ Recipient_name +",</span><br>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">This is to remind you that </b>,</span>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">" + UserName +" has skipped the medication.</span>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">please remind them. </span>\n" +
                "\n" +
                "            <br><br>\n" +
                "            Regards,<br>\n" +
                "            <table>\n" +
                "                <tr>\n" +
                "                    <td style=\"vertical-align: top; font-size: 14px !important;\">\n" +
                "                        <span style=\"font-weight: bold\">ApnaCare India Private Limited</span><br>\n" +
                "#1583, 13th Main, 20th Cross,<br>\n" +
                "B-Block, Sahakar Nagar,<br>\n" +
                "Bengaluru 560092.<br>" +"Phone: <br>Bangalore: +91 (080) 30752584 \n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "\t\t</div>\n" +
                "\n" +
                "\t\t<!-- Footer -->\n" +
                "\t\t<div style=\"display: block; position:relative; bottom: 0; max-width: 99%; min-height: 90px; padding: 5px 10px 0px 10px; \">\n" +
                "\t\t\t<div style=\"width: 50%; display: inline-block; font-size: 12px;\">\n" +
                "\t\t\t\t<span>Copyright @2016 ApnaCare India Pvt. Ltd. All Rights Reserved</span><br><br>\n" +
                "\t\t\t\t<span style=\"font-size: 12px; font-weight: 600\">\n" +
                "\t\t\t\t\t<a href=\"\" style=\"color: #2899dc !important\">Terms and Conditions</a> |\n" +
                "\t\t\t\t\t<a href=\"\" style=\"color: #2899dc !important\">Privacy Policy</a>\n" +
                "\t\t\t\t</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"width: 10%; display: inline-block; text-align: center; vertical-align: top; float: right; font-size: 14px\">\n" +
                "\t\t\t\t<p style=\"margin-bottom: 10px;margin-top:0;padding-top:0\">Powered by ApnaCare</p>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<br><br>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>");

        message.setSubject("Medication Update");


        // add recipients
        in.apnacare.android.medicationalertsystem.Mandrill.Recipient recipient = new Recipient();
        List<Recipient> recipients = new ArrayList<Recipient>();
        recipient.setEmail(Recipient);
        recipient.setName(Recipient_name);
        recipients.add(recipient);



        message.setTo(recipients);

        allMessage.setMessage(message);
        allMessage.send();
    }
}
