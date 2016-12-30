package in.apnacare.android.medicationalertsystem.activity;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.MedHistoryCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.model.RefillCollection;
import in.apnacare.android.medicationalertsystem.preferences.AlarmPreferenceListAdapter;
import in.apnacare.android.medicationalertsystem.preferences.AlarmPreferencesActivity;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class AddMedication extends BaseActivity implements View.OnClickListener {
    final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
    Context mContext;
    AlarmManager alarmManager;
    private static final int WAKE_LOCK_PERMISSION = 11;
    private PendingIntent pendingIntent;
        int medQuantity,mAlarm;
        Button btnSaveMedication,btnCancel;
        EditText  mediName,doseTxt,minQty;
        Spinner mornTxtTime,noonTxtTime,evenTxtTime,nightTxtTime,qtySpinner,doseSpinner,pharmacyNameSpinner,doctorSpinner;
        String morningAlarm, noonAlarm,evenAlarm,nightAlarm,medName,medDose,pharmacyName,doctorName;

        Button mButton;
    private ListAdapter listAdapter;
    private static AddMedication inst;
    private TextView alarmTextView;
    int morSpinnerPosition, noonSpinnerPosition, evenSpinnerPosition, nightSpinnerPosition, qtySpinnerPosition, doseSpinnerPosition,docNameSpinnerPosition,pharmacyNameSpinnerPosition,multiplerCounter;

    public static AddMedication instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

            mContext = this;
            Fabric.with(this, new Crashlytics());
            // Adding Toolbar to Main screen
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
                toolbar.setTitle("Add Medicine");
                setSupportActionBar(toolbar);

                // Adding menu icon to Toolbar
                ActionBar supportActionBar = getSupportActionBar();
                if (supportActionBar != null) {
                    supportActionBar.setDisplayHomeAsUpEnabled(true);
                    supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }

            mButton = (Button) findViewById(R.id.add);

            btnCancel = (Button) findViewById(R.id.cancelMed);
            mediName = (EditText)findViewById(R.id.med_name);
            medName = mediName.getText().toString();
            doctorSpinner = (Spinner)findViewById(R.id.docNameSpinner);
            pharmacyNameSpinner = (Spinner)findViewById(R.id.parmNameSpinner);
            doseTxt = (EditText)findViewById(R.id.doseTxt);


            mornTxtTime = (Spinner) findViewById(R.id.morningSpinner);
            noonTxtTime = (Spinner) findViewById(R.id.noonSpinner);
            evenTxtTime = (Spinner) findViewById(R.id.eveningSpinner);
            nightTxtTime = (Spinner) findViewById(R.id.nightSpinner);

            qtySpinner = (Spinner) findViewById(R.id.qtySpinner);
            doseSpinner = (Spinner) findViewById(R.id.doseSpinner);


            final Calendar calendar = Calendar.getInstance();
            minQty = (EditText) findViewById(R.id.minQty);

            btnSaveMedication = (Button)findViewById(R.id.addMed);

            morningAlarm = mornTxtTime.getSelectedItem().toString();
            noonAlarm = noonTxtTime.getSelectedItem().toString();
            evenAlarm = evenTxtTime.getSelectedItem().toString();
            nightAlarm = nightTxtTime.getSelectedItem().toString();

            Bundle bundle = getIntent().getExtras();

            if(bundle != null ){

                ArrayAdapter doseAdap = (ArrayAdapter) doseSpinner.getAdapter();
                ArrayAdapter qtyAdap = (ArrayAdapter) qtySpinner.getAdapter();
                ArrayAdapter mornAdap = (ArrayAdapter) mornTxtTime.getAdapter();
                ArrayAdapter noonAdap = (ArrayAdapter) noonTxtTime.getAdapter();
                ArrayAdapter evenAdap = (ArrayAdapter) evenTxtTime.getAdapter();
                ArrayAdapter nightAdap = (ArrayAdapter) nightTxtTime.getAdapter();


                doseSpinnerPosition = (doseAdap.getPosition(bundle.getString("doseSpinner")));
                doseSpinner.setSelection(doseSpinnerPosition);

                qtySpinnerPosition = (qtyAdap.getPosition(bundle.getString("Qty")));
                doseSpinner.setSelection(doseSpinnerPosition);

                morSpinnerPosition = (mornAdap.getPosition(bundle.getString("mornTime")));
                mornTxtTime.setSelection(morSpinnerPosition);


                noonSpinnerPosition = (noonAdap.getPosition(bundle.getString("noonTime")));
                noonTxtTime.setSelection(noonSpinnerPosition);

                evenSpinnerPosition = (evenAdap.getPosition(bundle.getString("evenTime")));
                evenTxtTime.setSelection(evenSpinnerPosition);

                nightSpinnerPosition = (nightAdap.getPosition(bundle.getString("nightTime")));
                evenTxtTime.setSelection(nightSpinnerPosition);

                mediName.setText(bundle.getString("MedName"));
                doseTxt.setText(bundle.getString("Dose"));

                minQty.setText(bundle.getString("minQty"));

            }

            DoctorCollectionModel dml = new DoctorCollectionModel(MedicationUsers.getContext());
            ArrayList<DoctorCollection> docList = new ArrayList<DoctorCollection>();
            List<String> spinnerArray =  new ArrayList<String>();
            spinnerArray.add("N/A");
            docList = dml.getDoctor();
            for(int i=0;i<docList.size();i++){
                spinnerArray.add(docList.get(i).getDoctor_name());


            }
            spinnerArray.add(" + "+" Add Doctor");





            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            doctorSpinner.setAdapter(adapter);


            //Pharmacy Spinner
            PharmacyCollectionModel phml = new PharmacyCollectionModel(MedicationUsers.getContext());
            ArrayList<PharmacyCollection> pharmaList = new ArrayList<PharmacyCollection>();
            List<String> spinnerArrayPharma =  new ArrayList<String>();
            spinnerArrayPharma.add("N/A");
            pharmaList = phml.getPharmacy();
            for(int i=0;i<pharmaList.size();i++){
                spinnerArrayPharma.add(pharmaList.get(i).getPharmacy_name());
            }

            spinnerArrayPharma.add(" + "+" Add Pharmacy");





            ArrayAdapter<String> adapterPharma = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArrayPharma);

            adapterPharma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pharmacyNameSpinner.setAdapter(adapterPharma);

            qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    medQuantity = Integer.parseInt(qtySpinner.getSelectedItem().toString());
                    Log.v(Constants.TAG,"MedicationQuantity"+medQuantity);

                    Log.v(Constants.TAG, "QuantitySpinner Position: "+qtySpinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            doseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                     medDose = doseSpinner.getSelectedItem().toString();
                    Log.v(Constants.TAG,"MedicationDose"+medDose);
                    Log.v(Constants.TAG, "DoseSpinner Position: "+doseSpinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            pharmacyNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if(pharmacyNameSpinner.getSelectedItem().toString().equals(" +  Add Pharmacy")){
                        Intent pharma = new Intent(mContext,ActivityPharmacyModule.class);
                        startActivity(pharma);
                    }
                    else{
                        pharmacyName = pharmacyNameSpinner.getSelectedItem().toString();
                        Log.v(Constants.TAG,"MedicationDose"+pharmacyName);
                        Log.v(Constants.TAG, "DoseSpinner Position: "+pharmacyNameSpinner.getSelectedItemPosition());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                    if(doctorSpinner.getSelectedItem().toString().equals(" +  Add Doctor"))
                    {
                        Intent doc = new Intent (mContext,ActivityDoctorModule.class);
                        startActivity(doc);
                    }
                    else {
                        doctorName = doctorSpinner.getSelectedItem().toString();
                        Log.v(Constants.TAG, "MedicationDose" + doctorName);
                        Log.v(Constants.TAG, "DoseSpinner Position: " + doctorSpinner.getSelectedItemPosition());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

            Log.e(Constants.TAG, "Doctor Spinner Size: "+doctorSpinner.getAdapter().getCount());
            Log.e(Constants.TAG, "Pharma Name Spinner Size: "+pharmacyNameSpinner.getAdapter().getCount());
            btnCancel.setOnClickListener(this);
            btnSaveMedication.setOnClickListener(this);

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

        }

    public ListAdapter getListAdapter() {
        return listAdapter;
    }

    @Override
    public void onClick(View v) {

        if( v == btnSaveMedication){


            if(mediName.getText().toString().trim().equals("")  || doseTxt.getText().toString().trim().equals("") || pharmacyNameSpinner.getSelectedItem().toString().trim().equals("") || minQty.getText().toString().trim().equals("")) // || maxQty.getText().toString().trim().equals(""))
            {
                Toast.makeText(mContext,"Please enter medicine Details",Toast.LENGTH_SHORT).show();
            }
            else
            {

                Log.e(Constants.TAG, "Medi name "+mediName.getText());
                Log.e(Constants.TAG, "dose value"+doseTxt.getText());
                Log.e(Constants.TAG, "pharma name "+pharmacyNameSpinner.getSelectedItem().toString());
                try {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = mdformat.format(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_YEAR, medQuantity);
                    String endDate = mdformat.format(calendar.getTime());
                    RefillCollection refillCollection = new RefillCollection();
                    Log.e(Constants.TAG, "Meddose total: " + doseTxt.getText().toString() + "-" + medDose);
                    Log.e(Constants.TAG, "FromDAte in add medication: " + strDate);
                    Log.e(Constants.TAG, "ToDAte: in add medication " + endDate);
                    MedHistoryCollection medHistory = new MedHistoryCollection();

                    Toast.makeText(mContext, "Saving medications's", Toast.LENGTH_SHORT).show();
                    Intent setAlarm = new Intent(AddMedication.this, AlarmPreferencesActivity.class);

                    String mornTime = null ;
                    String noonTime = null ;
                    String evenTime = null ;
                    String nightTime = null ;
                    if(!mornTxtTime.getSelectedItem().toString().equals("N/A")){
                        StringTokenizer noonToken = new StringTokenizer(mornTxtTime.getSelectedItem().toString(), ":");
                        String firsttoken = noonToken.nextToken();
                        String secondtoken = noonToken.nextToken();
                        int hours = Integer.parseInt(firsttoken);
                        int min = Integer.parseInt(secondtoken);
                        multiplerCounter += 1;

                        mornTime = hours + ":" + min;
                    }

                    else
                    {
                        mornTime = "N/A";
                    }

                    if(!noonTxtTime.getSelectedItem().toString().equals("N/A")){
                        StringTokenizer noonToken = new StringTokenizer(noonTxtTime.getSelectedItem().toString(), ":");
                        String firsttoken = noonToken.nextToken();
                        String secondtoken = noonToken.nextToken();
                        int hours = Integer.parseInt(firsttoken);
                        int min = Integer.parseInt(secondtoken);
                        if(hours < 12){
                            hours += 12;
                        }
                        multiplerCounter +=1;
                        noonTime = hours + ":" + min;
                    }
                    else
                    {
                        noonTime = "N/A";
                    }


                    if(!evenTxtTime.getSelectedItem().toString().equals("N/A")){
                        StringTokenizer noonToken = new StringTokenizer(evenTxtTime.getSelectedItem().toString(), ":");
                        String firsttoken = noonToken.nextToken();
                        String secondtoken = noonToken.nextToken();
                        int hours = Integer.parseInt(firsttoken);
                        int min = Integer.parseInt(secondtoken);
                        if(hours < 12){
                            hours += 12;
                        }
                        multiplerCounter +=1;
                        evenTime = hours + ":" + min;
                    }
                    else
                    {
                        evenTime = "N/A";
                    }
                    if(!nightTxtTime.getSelectedItem().toString().equals("N/A")){
                        StringTokenizer noonToken = new StringTokenizer(nightTxtTime.getSelectedItem().toString(), ":");
                        String firsttoken = noonToken.nextToken();
                        String secondtoken = noonToken.nextToken();
                        int hours = Integer.parseInt(firsttoken);
                        int min = Integer.parseInt(secondtoken);
                        if(hours < 12){
                            hours += 12;
                        }
                        multiplerCounter +=1;
                        nightTime = hours + ":" + min;
                    }
                    else
                    {
                        nightTime = "N/A";
                    }



                    setAlarm.putExtra("AddMedication","AddMedication");
                    setAlarm.putExtra("medname",mediName.getText().toString());
                    setAlarm.putExtra("dose",doseTxt.getText().toString());
                    setAlarm.putExtra("doseSpinner",doseSpinner.getSelectedItem().toString());
                    setAlarm.putExtra("qty_med",qtySpinner.getSelectedItem().toString());
                    setAlarm.putExtra("minQty",minQty.getText().toString());
                    setAlarm.putExtra("maxQty",String.valueOf(multiplerCounter*Integer.parseInt(qtySpinner.getSelectedItem().toString())));
                    setAlarm.putExtra("pharamaname",pharmacyNameSpinner.getSelectedItem().toString());
                    setAlarm.putExtra("doc",doctorSpinner.getSelectedItem().toString());
                    Log.e(Constants.TAG, "Refil data: min quantity"+minQty.getText().toString());
                    Log.e(Constants.TAG, "Refil data: max quantity"+multiplerCounter*Integer.parseInt(qtySpinner.getSelectedItem().toString()));
                    setAlarm.putExtra("mornTime", mornTime);
                    setAlarm.putExtra("noonTime", noonTime);
                    setAlarm.putExtra("evenTime", evenTime);
                    setAlarm.putExtra("nightTime", nightTime);
                    Log.e(Constants.TAG, "multiplerCounter while saving  med: "+multiplerCounter*Integer.parseInt(qtySpinner.getSelectedItem().toString()));
                    setAlarm.putExtra("mornTimeSpinner",mornTxtTime.getSelectedItem().toString());
                    setAlarm.putExtra("noonTimeSpinner",noonTxtTime.getSelectedItem().toString());
                    setAlarm.putExtra("evenTimeSpinner",evenTxtTime.getSelectedItem().toString());
                    setAlarm.putExtra("nightTimeSpinner",nightTxtTime.getSelectedItem().toString());
                    // setAlarm.putExtra("alarmTime",mornTxtTime.getSelectedItem().toString());
                    setAlarm.putExtra("alarmName",mediName.getText().toString());
                    startActivity(setAlarm);

                }

                catch (Exception e){
                    Log.e(Constants.TAG, "onClick: "+e.toString());
                }
            }
       }
        else if(v == btnCancel){
            Intent previous = new Intent(this,ViewMedicationActivity.class);
            startActivity(previous);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case WAKE_LOCK_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(Constants.TAG, "onRequestPermissionsResult: " + grantResults.length);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }

        }
    }
}

