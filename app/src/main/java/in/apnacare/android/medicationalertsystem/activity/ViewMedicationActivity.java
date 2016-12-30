package in.apnacare.android.medicationalertsystem.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;

import android.support.v7.widget.CardView;

import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;


import in.apnacare.android.medicationalertsystem.BuildConfig;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.app.Config;
import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.ManageUserCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.database.RefillCollectionModel;
import in.apnacare.android.medicationalertsystem.fromapi.MedicationApi;
import in.apnacare.android.medicationalertsystem.interfaces.AppUser;
import in.apnacare.android.medicationalertsystem.interfaces.Medication;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.ManageUserCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.model.RefillCollection;
import in.apnacare.android.medicationalertsystem.fromapi.UserApi;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import in.apnacare.android.medicationalertsystem.utils.NotificationUtils;
import io.fabric.sdk.android.Fabric;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewMedicationActivity extends BaseActivity {

    private static final String TAG = ViewMedicationActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private int availableQty,takenqty,maxQty;
    String  Username;
    Boolean daydiff = false;
    long userInteractionTime = 0;
    private ArrayList<MedicationCollection> medList;
    private ArrayList<MedicationCollection> morningMedList, noonMedList, evenMedList, nightMedList;
    private ArrayList<RefillCollection> refillList;
    MedicationCollectionModel medicationCollectionModel;
    RefillCollectionModel refillCollectionModel;
    private List<Alarm> alarmList;
    CardView morningMedLayout,noonMedLayout,eveningMedLayout,nightMedLayout;
    LinearLayout morningMedLayout1, noonMedLayou1, eveningMedLayout1, nightMedLayout1;
    private static boolean RUN_ONCE = true;
    TextView morMed;
    AlarmCollectionModel alm  = new AlarmCollectionModel(MedicationUsers.getContext());;
    public static String name;
    private ProgressBar bar;
    String dwnload_file_path = "https://apnacare.in/smartpillreminder/app-release.apk";
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    AppUser appuserinterface;
    TextView cur_val;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);
        Fabric.with(this, new Crashlytics());
        setUpNavigation("Smart Pill Reminder");
        medicationCollectionModel = new MedicationCollectionModel(this);
        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        morningMedLayout = (CardView) findViewById(R.id.mornMedCard);
        noonMedLayout = (CardView) findViewById(R.id.noonMedCard);
        eveningMedLayout = (CardView) findViewById(R.id.evenMedCard);
        nightMedLayout = (CardView) findViewById(R.id.nightMedCard);

        Log.e(Constants.TAG, "Default font family of app is "+ Typeface.DEFAULT.toString());


        Medication appuser = Medication.retrofit.create(Medication.class);
        final Call<List<MedicationApi>> call =
                appuser.getMedication();
        Log.e(Constants.TAG, "Calling function api "+call.toString());

        call.enqueue(new Callback<List<MedicationApi>>() {
            @Override
            public void onResponse(Call<List<MedicationApi>> call, Response<List<MedicationApi>> response) {
                Log.e(Constants.TAG, "size of response body "+response.body().size());
                Log.e(Constants.TAG, "at index value 6: "+response.body().get(6));
                Log.e(Constants.TAG, "onResponse: complete strintg "+response.body().toString());
                Log.e(Constants.TAG, "onResponse: complete body "+response.body());
            }
            @Override
            public void onFailure(Call<List<MedicationApi>> call, Throwable t) {

                Log.e(Constants.TAG, "onFailure: "+t.getMessage());
            }
        });




        Calendar c = Calendar.getInstance();
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String currentDateTimeString = df3.format(c.getTime());
        TextView current_date = (TextView) findViewById(R.id.current_date);

        StringTokenizer tk = new StringTokenizer(currentDateTimeString);
        String date1 = tk.nextToken();
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(time);
            Log.e(Constants.TAG, "Time Display: " + sdfs.format(dt));
            // <-- I got result here
            Log.e(Constants.TAG, "String date format "+sdfs.format(dt));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(Constants.TAG, "exception for date tme conversion "+e.toString());
        }


        Log.e(TAG, "Current time: "+ c.getTime() );









        current_date.setText(currentDateTimeString);
        getAll();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                   // txtMessage.setText(message);
                    Log.e(Constants.TAG, "Notification recieveed"+message);
                }
            }
        };

        displayFirebaseRegId();



    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))

        Log.e(Constants.TAG, "displayFirebaseRegId: "+regId);
        else
            Log.e(Constants.TAG, "Firebase Reg Id is not received yet!");

    }





    public void addMedication(View view) {
        DoctorCollectionModel dcm = new DoctorCollectionModel(MedicationUsers.getContext());
        DoctorCollection doctorCollection = new DoctorCollection();
        ArrayList<DoctorCollection> docList = new ArrayList<DoctorCollection>();
        docList = dcm.getDoctor();
        PharmacyCollectionModel collection = new PharmacyCollectionModel(MedicationUsers.getContext());
        ArrayList<PharmacyCollection> pharmaList = new ArrayList<PharmacyCollection>();
        pharmaList = collection.getPharmacy();
        if(pharmaList.size() == 0){


            android.support.v7.app.AlertDialog diaBox = AskOption();
            diaBox.show();
        }
        else{
            Intent intent = new Intent(ViewMedicationActivity.this, AddMedication.class);
            startActivity(intent);
        }
    }

    public void detailMedication(View view) {

        String mornmed;
        String[] separater;
        TextView text = (TextView) findViewById(R.id.mormedtxt);
        mornmed = text.getText().toString();

        Intent intent = new Intent(ViewMedicationActivity.this, MedicationDetailsActivity.class);
        startActivity(intent);
    }

    private android.support.v7.app.AlertDialog AskOption()

    {

        final DoctorCollectionModel docMod = new DoctorCollectionModel(MedicationUsers.getContext());
        android.support.v7.app.AlertDialog myQuittingDialogBox =new android.support.v7.app.AlertDialog.Builder(mContext)
                //set message, title, and icon
                .setTitle("Pharmacy Details")
                .setMessage("Please give Pharmacy information so that the request for refills can be sent automatically.")
                .setIcon(R.drawable.ic_doctor)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code


                        Intent i = new Intent(mContext,ActivityPharmacyModule.class);

                        mContext.startActivity(i);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Add Medication", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(mContext,AddMedication.class);

                        mContext.startActivity(i);
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public void getAll() {

        alarmList = alm.getAlarmCollection();
        int size = alarmList.size();
        Log.e(Constants.TAG, "Alarm size 0"+alarmList.size());
        if(size == 0){
            Log.e(Constants.TAG, "Alarm size 0"+alarmList.size());
        }
        if(size>0){
            for(int i=0 ; i<alarmList.size();i++) {
                Log.e(Constants.TAG, "Alarm ID " + alarmList.get(i).getId());
                Log.e(Constants.TAG, "Alarm Name " + alarmList.get(i).getAlarmName());
                Log.e(Constants.TAG, "Alarm Time " + alarmList.get(i).getAlarmTime());
                Log.e(Constants.TAG, "Alarm MedId " + alarmList.get(i).getMedAlarmId());
                Log.e(Constants.TAG, "Alarm Tone " + alarmList.get(i).getAlarmTonePath());
                Log.e(Constants.TAG, "Alarm Morning " + alarmList.get(i).getMorn());
                Log.e(Constants.TAG, "Alarm Noon " + alarmList.get(i).getNoon());
                Log.e(Constants.TAG, "Alarm Evening " + alarmList.get(i).getEven());
                Log.e(Constants.TAG, "Alarm Night" + alarmList.get(i).getNight());
                Log.e(Constants.TAG, "Alarm taken" + alarmList.get(i).getTaken());
                Log.e(Constants.TAG, "Alarm snooze" + alarmList.get(i).getSnooze());
            }
        }

        ManageUserCollection manageUserCollection = new ManageUserCollection();
        ManageUserCollectionModel mModel = new ManageUserCollectionModel(MedicationUsers.getContext());
        ArrayList<ManageUserCollection> userList = new ArrayList<ManageUserCollection>();
        userList = mModel.getUsers();
        for(int i=0;i<userList.size();i++){
            name = userList.get(i).getUser_name();
            Log.e(Constants.TAG, "User Name "+name);

        }



        medList = medicationCollectionModel.getMedications();
        TextView morMed = (TextView) findViewById(R.id.mormedtxt);
        TextView noonMed = (TextView) findViewById(R.id.noonMedTxt);
        TextView evenMed = (TextView) findViewById(R.id.evenMedTxt);
        TextView nightMed = (TextView) findViewById(R.id.nightMedTxt);
        TextView morTime = (TextView) findViewById(R.id.mormedtime);
        TextView noonTime = (TextView) findViewById(R.id.noonmedtime);
        TextView evenTime = (TextView) findViewById(R.id.evenmedTime);
        TextView nightTime = (TextView) findViewById(R.id.nightmedTime);
        TextView mormedtaken = (TextView) findViewById(R.id.mormedtaken);
        TextView noonmedtaken = (TextView) findViewById(R.id.noonmedtaken);
        TextView evenmedtaken = (TextView) findViewById(R.id.evenmedtaken);
        TextView nightmedtaken = (TextView) findViewById(R.id.nightmedtaken);



        ArrayList<MedicationCollection> med = new ArrayList<>();
        med = medicationCollectionModel.getMedications();
        for(int j=0;j<med.size();j++){
            MedicationCollection mc = new MedicationCollection();
            mc = med.get(j);
            Log.v(Constants.TAG,mc.toString());
        }
        Log.v(Constants.TAG, "Med count: "+medicationCollectionModel.getMedications().size());

        morningMedList = new ArrayList<MedicationCollection>();
        morningMedList = medicationCollectionModel.getMorningMedications();

        noonMedList = new ArrayList<MedicationCollection>();
        noonMedList = medicationCollectionModel.getNoonMedications();

        evenMedList = new ArrayList<MedicationCollection>();
        evenMedList = medicationCollectionModel.getEveningMedications();

        nightMedList = new ArrayList<MedicationCollection>();
        nightMedList = medicationCollectionModel.getNightMedications();


        try {
            String medicineNames = "";
            String medicineTime = "";
            String medtaken = "";
            if (morningMedList.size() == 0) {
                Log.e(Constants.TAG, "Morning: " + morningMedList.size());
                morMed.setText("@string/empty_data" + morningMedList.size());
            }
            Log.e(Constants.TAG, "Morning: " + morningMedList.size());
            for (int i = 0; i < morningMedList.size(); i++) {
                if (morningMedList.get(i) != null) {
                    Log.e("Exception", morningMedList.get(i).toString());
                    String temp = morningMedList.get(i).getM_name();
                    String temp2 = morningMedList.get(i).getMorningAlarmStatus();

                    int temp1 = morningMedList.get(i).getM_id();
                    if (temp != "" && !temp.equals(null)) {
                        medicineNames +=  temp + "\n";
                        medicineTime += temp2 + " am" + "\n";
                    }
                }
            }







            if (morningMedList.size() > 0) {
                morMed.setText(medicineNames);
                morTime.setText(medicineTime);

                morningMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailMedication(v);
                    }
                });
            } else {

                ViewGroup.LayoutParams params = morMed.getLayoutParams();
                params.width = getResources().getDimensionPixelSize(R.dimen.text_view_width);;
                morMed.setLayoutParams(params);
                morMed.setText("Click to add Medication");

                morningMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMedication(v);
                    }
                });
            }

            medicineNames = "";
            medicineTime ="";
            Log.e("Exception", "Noon: " + noonMedList.size());
            for (int i = 0; i < noonMedList.size(); i++) {
                if (noonMedList.get(i) != null) {
                    Log.e("Exception", noonMedList.get(i).toString());
                    String temp = noonMedList.get(i).getM_name();
                    String temp1 = noonMedList.get(i).getAfternoonAlarmStatus();
                    if (temp != "" && !temp.equals(null)) {
                        medicineNames +=  temp  +"\n";
                        medicineTime += temp1 + " pm" + "\n";
                    }
                }
            }
            if (noonMedList.size() > 0) {

                noonMed.setText(medicineNames);
               noonTime.setText(medicineTime);

                noonMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailMedication(v);
                    }
                });
            } else {
                ViewGroup.LayoutParams params = noonMed.getLayoutParams();
                params.width = getResources().getDimensionPixelSize(R.dimen.text_view_width);;
                noonMed.setLayoutParams(params);
                noonMed.setText("Click to add Medication");
                noonMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMedication(v);
                    }
                });
            }


            medicineNames = "";
            medicineTime = "";
            Log.e("Exception", "Evening: " + evenMedList.size());
            if (evenMedList.size() == 0) {
                evenMed.setText("Click here to add");
                Log.e(Constants.TAG, "Evening: " + evenMedList.size());
            } else if (evenMedList.size() > 0) {
                for (int i = 0; i < evenMedList.size(); i++) {
                    if (evenMedList.get(i) != null) {
                        Log.e("Exception", evenMedList.get(i).toString());
                        String temp = evenMedList.get(i).getM_name();
                        String temp1 = evenMedList.get(i).getEveningAlarmStatus();
                        if (temp != "" && !temp.equals(null)) {
                            medicineNames +=  temp  +"\n";
                            medicineTime += temp1 + "pm" + "\n";
                        }
                    }
                }
            }
            if (evenMedList.size() > 0) {
                evenMed.setText(medicineNames);
                evenTime.setText(medicineTime);

                eveningMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailMedication(v);
                    }
                });
            } else {
                ViewGroup.LayoutParams params = evenMed.getLayoutParams();
                params.width = getResources().getDimensionPixelSize(R.dimen.text_view_width);;
                evenMed.setLayoutParams(params);
                evenMed.setText("Click to add Medication");
                eveningMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMedication(v);
                    }
                });
            }

            medicineNames = "";
            medicineTime = "";
            Log.e("Exception", "Night: " + nightMedList.size());
            for (int i = 0; i < nightMedList.size(); i++) {
                if (nightMedList.get(i) != null) {
                    Log.e("Exception", nightMedList.get(i).toString());
                    String temp = nightMedList.get(i).getM_name();
                    String temp1 = nightMedList.get(i).getNightAlarmStatus();


                    if (temp != "" && !temp.equals(null)) {
                        medicineNames +=  temp +"\n";
                        medicineTime += temp1 +"pm" +"\n";
                    }
                }
            }

            if (nightMedList.size() > 0) {
                nightMed.setText(medicineNames);
                nightTime.setText(medicineTime);

                nightMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailMedication(v);
                    }
                });
            } else {
                ViewGroup.LayoutParams params = nightMed.getLayoutParams();
                params.width = getResources().getDimensionPixelSize(R.dimen.text_view_width);;
                nightMed.setLayoutParams(params);
                nightMed.setText("Click to add Medication");
                nightMedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMedication(v);
                    }
                });
            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }
    private void runOnce(){

        Log.e(Constants.TAG, "inside runOnce: before if condition  ");
        if (RUN_ONCE) {
            Log.e(Constants.TAG, "inside runOnce:  ");
            refillCollectionModel = new RefillCollectionModel(MedicationUsers.getContext());
            refillList = new ArrayList<RefillCollection>();
            refillList = refillCollectionModel.getRefillCollection();
            RUN_ONCE = false;
            String medicineNames = "" ;
            String daysdifference ;
            String medno = "";
            for(int i=0;i< refillList.size();i++)
            {

                if(refillList.get(i) != null)
                {

                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();


                    Calendar c = Calendar.getInstance();


                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c.getTime());
                    Log.e(Constants.TAG, "getAll: "+formattedDate);
                    String inputString1 = refillList.get(i).getFrom_date();
                    String inputString2 = refillList.get(i).getTo_date();

                    if(refillList.get(i).getMax_qty() != "" && refillList.get(i).getTaken_qty() != ""){
                        takenqty = Integer.parseInt(refillList.get(i).getTaken_qty());
                        int minqty =  Integer.parseInt(refillList.get(i).getMin_qty());
                        maxQty =  Integer.parseInt(refillList.get(i).getMax_qty());
                        availableQty = (minqty - takenqty);
                     }
                    else
                    {
                        availableQty = 0;
                    }
                    Log.e(Constants.TAG, "Difference in qty: "+availableQty);



                    try {
                        Date date1 = myFormat.parse(formattedDate);
                        Date date2 = myFormat.parse(inputString2);

                        long diff = date2.getTime() - date1.getTime();
                        daysdifference =  String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        Log.e(Constants.TAG, "Refill difference date: "+ daysdifference);
                        Log.e(Constants.TAG, "Difference in qty: "+availableQty);

                        if(Integer.parseInt(daysdifference) < 3 )
                        {
                                daydiff = true;
                            String temp = refillList.get(i).getMedicine_name();
                            String temp1 = String.valueOf(i + 1 );

                            if (temp != "" && !temp.equals(null)) {
                            medicineNames +=  temp1 + "." + temp +".\n";

                                Log.e(Constants.TAG, "runOnce: medicineNames"+medicineNames);

                        }

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e(Constants.TAG, "refil in view exception "+e.toString());
                    }

                    StringTokenizer token = new StringTokenizer(refillList.get(i).getTo_date(), "-");
                    String firstToken = token.nextToken();
                    String secondtoken = token.nextToken();
                    String thirdtoken = token.nextToken();
                    Log.e(Constants.TAG, "getAll: "+thirdtoken);
                    Log.e(Constants.TAG, "Refill Medicinname: "+refillList.get(i).getMedicine_name());
                    Log.e(Constants.TAG, "Refil From Date: "+refillList.get(i).getFrom_date());
                    Log.e(Constants.TAG, "refil to date: "+refillList.get(i).getTo_date());
                    Log.e(Constants.TAG, "refil min qty: "+refillList.get(i).getMin_qty());
                    Log.e(Constants.TAG, "refil max qty: "+refillList.get(i).getMax_qty());

                }
            }
            Log.e(Constants.TAG, "runOnce: medName outside forloop"+medicineNames);
            if(daydiff){
                Log.e(TAG, "day's difference: "+daydiff);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Please Refill these medicines \n"+ medicineNames + "are getting out of stock")// + " you are left with only "+ daysdifference + " days medicine")
                        .setTitle("Refill Reminder");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent pharma = new Intent(mContext,PharmacyDetailsActivity.class);
                        startActivity(pharma);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            RUN_ONCE = false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
         LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));


        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));


        NotificationUtils.clearNotifications(getApplicationContext());

  }

    @Override
    public  void onStart(){
        super.onStart();
        RUN_ONCE = true;
        RefillCollectionModel refill = new RefillCollectionModel(MedicationUsers.getContext());
        ArrayList<RefillCollection> refillList = new ArrayList<RefillCollection>();
        refillList = refill.getRefillCollection();
        if(refillList.size() > 0){
            runOnce();
        }
      if(isNetworkAvailable()) {
            GetXMLTask task = new GetXMLTask();
            String URL = "https://apnacare.in/portal/notification/medAppVersionCheck";
            task.execute(new String[]{URL});
        }
        else{
            Toast.makeText(this, "Please check you internet connection for updates", Toast.LENGTH_SHORT).show();
        }
    }
   @Override
   protected void onPause() {
       LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
       super.onPause();

   }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String downloadText() {
        Log.e(Constants.TAG, "inside download text method ");
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = openHttpConnection();
            Log.e(Constants.TAG, "inside download text method and the IN value is : "+in);
        } catch (IOException e1) {
            return "";
        }

        String str = "";
        if (in != null) {
            InputStreamReader isr = new InputStreamReader(in);
            int charRead;
            char[] inputBuffer = new char[BUFFER_SIZE];
            try {
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    // ---convert the chars to a String---
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    str += readString;
                    inputBuffer = new char[BUFFER_SIZE];
                }
                in.close();
            } catch (IOException e) {
                return "";
            }
        }
        return str;
    }

    private InputStream openHttpConnection() throws IOException {

        Log.e(Constants.TAG, "inside openHttpConnection method ");
        InputStream in = null;
        int response = -1;

        URL url = new URL("https://apnacare.in/portal/notification/medAppVersionCheck");
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                Log.e(Constants.TAG, "Httpurlconnection is Ok "+httpConn.getInputStream());
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.e(Constants.TAG, "inside Http exception text method "+ex.toString());

            throw new IOException("Error connecting");
        }
        return in;
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
                if(stream != null){
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(stream));
                    String s = "";
                    while ((s = buffer.readLine()) != null)
                        output.append(s);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }


        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            android.support.v7.app.AlertDialog accessBox;
            Log.e(Constants.TAG, "onPostExecute: "+output);

            bar.setVisibility(View.GONE);
            Log.e(Constants.TAG, "onPostExecute: build version "+BuildConfig.VERSION_CODE);
            Log.e(Constants.TAG, "onPostExecute: OUTPUT "+output);

            if(BuildConfig.VERSION_CODE < Integer.parseInt(output)){


                accessBox = AskPermission();
                accessBox.show();

            }

            else if(BuildConfig.VERSION_CODE == Integer.parseInt(output))
            {
                Log.e(Constants.TAG, "Same version code: "+output.toString());
            }
            else {
                Log.e(Constants.TAG, "version code of both "+output.toString());
            }

        }
    }


    private android.support.v7.app.AlertDialog AskPermission()

    {

        ManageUserCollectionModel mus = new ManageUserCollectionModel(MedicationUsers.getContext());
        ArrayList<ManageUserCollection> mn = new ArrayList<ManageUserCollection>();
        mn = mus.getUsers();

        for(int i=0;i<mn.size();i++)
        {
            Username = mn.get(i).getUser_name();
        }

        final DoctorCollectionModel docMod = new DoctorCollectionModel(MedicationUsers.getContext());
        android.support.v7.app.AlertDialog myQuittingDialogBox =new android.support.v7.app.AlertDialog.Builder(mContext)
                //set message, title, and icon
                .setTitle("New Version")
                .setMessage("Hi." + Username + " there is a new version available would you like to download it")
                .setIcon(R.drawable.ic_doctor)

                .setPositiveButton("Update", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code



                        try{

                            showProgress(dwnload_file_path);

                            new Thread(new Runnable() {
                                public void run() {
                                    downloadFile();


                                }
                            }).start();



                        }catch (Exception e){
                            Log.e(Constants.TAG,"Install intent exception: "+e.toString());
                        }


                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        myQuittingDialogBox.setCancelable(false);
        myQuittingDialogBox.setCanceledOnTouchOutside(false);
        return myQuittingDialogBox;

    }

    void downloadFile(){

        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,"apnacare_updated.apk");
            Log.e(Constants.TAG, "downloadFile: path "+file);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        if(per == 100){
                            per = 0;
                        }
                        cur_val.setText("Downloading...." + "(" + (int)per + "%)" );

                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();


            runOnUiThread(new Runnable() {
                public void run() {

                    pb.setVisibility(View.GONE);
                    Log.e(Constants.TAG,"file: "+ Environment.getExternalStorageDirectory().toString());
                    File mFile = new File(Environment.getExternalStorageDirectory().toString()+"/apnacare_updated.apk");
                    Log.e(Constants.TAG,"File Size: "+mFile.length());
                    Log.e(Constants.TAG,"File Size: "+mFile);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(Uri.fromFile(mFile),"application/vnd.android.package-archive");
                    installIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(installIntent);
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
            Log.e(Constants.TAG, "downloadFile:exception malformedURLException "+e.toString());
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
            Log.e(Constants.TAG, "downloadFile:exception IOException "+e.toString());
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
            Log.e(Constants.TAG, "downloadFile: final exception "+e.toString());
        }

    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ViewMedicationActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(String file_path){
        dialog = new Dialog(ViewMedicationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file please wait...");
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);

        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }




    @Override
    public void onUserInteraction() {
        userInteractionTime = System.currentTimeMillis();
        super.onUserInteraction();
        Log.e(Constants.TAG,"Interaction"+userInteractionTime);
    }

    @Override
    public void onUserLeaveHint() {
        long uiDelta = (System.currentTimeMillis() - userInteractionTime);

        super.onUserLeaveHint();
        Log.e(Constants.TAG,"Last User Interaction = "+uiDelta);
        if (uiDelta < 100){
            Log.e(Constants.TAG,"Home Key Pressed in activity");


        }
        else {
            Log.e(Constants.TAG, "We are leaving, but will probably be back shortly!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
