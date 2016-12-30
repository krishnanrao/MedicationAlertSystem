package in.apnacare.android.medicationalertsystem.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.database.DatabaseHandler;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.database.RefillCollectionModel;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.model.RefillCollection;
import in.apnacare.android.medicationalertsystem.preferences.AlarmPreferencesActivity;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

import static in.apnacare.android.medicationalertsystem.utils.MedicationUsers.getContext;

public class EditMedicationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    int multiplerCounter;
    EditText med_name, pharma_name, doc_name, clinic_name, doseTxt,minQty,maxQty;
    Spinner med_qty, med_dose, mor_alarm_time, noon_alarm_time, even_alarm_time, night_alarm_time;
        public String strDate = ""  ;
        public String endDate = "";
    private int alarm_id;
    private ArrayList<MedicationCollection> medList;
    private ArrayList<RefillCollection> refList;
    private List<Alarm> alarmList;
    private String previous_morn_value,previous_noon_value,previous_even_value,previous_night_value,pharmacyName,doctorName;

    Button btnEditMedication, btnDatePickerFrom, btnDatePickerTo;
Spinner pharmacyNameSpinner,doctorSpinner;

    private int mYear, mMonth, mDay, mHour, mMinute;


    public final String EXTRA_MESSAGE = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        mContext = this;
        Fabric.with(this, new Crashlytics());
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("Edit Medicine");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }




        MedicationCollectionModel get = new MedicationCollectionModel(getContext());
        med_name = (EditText) findViewById(R.id.med_name);
        med_qty = (Spinner) findViewById(R.id.qtySpinner);
        med_dose = (Spinner) findViewById(R.id.doseSpinner);
       // pharma_name = (EditText) findViewById(R.id.parmName);
        doc_name = (EditText) findViewById(R.id.docName);
        doseTxt = (EditText) findViewById(R.id.doseTxt);
        minQty = (EditText) findViewById(R.id.minQty);
        maxQty = (EditText) findViewById(R.id.maxQty);

       // clinic_name = (EditText) findViewById(R.id.clinicName);
       /* from_date = (EditText) findViewById(R.id.fromDatePicker);
        to_date = (EditText) findViewById(R.id.toDatePicker);*/
        mor_alarm_time = (Spinner) findViewById(R.id.morningSpinner);
        noon_alarm_time = (Spinner) findViewById((R.id.noonSpinner));
        even_alarm_time = (Spinner) findViewById(R.id.eveningSpinner);
        pharmacyNameSpinner = (Spinner) findViewById(R.id.parmNameSpinner);
        doctorSpinner = (Spinner) findViewById(R.id.docNameSpinner);
        night_alarm_time = (Spinner) findViewById(R.id.nightSpinner);
        btnEditMedication = (Button) findViewById(R.id.editMed);

       /* btnDatePickerFrom = (Button) findViewById(R.id.btn_from_date);
        btnDatePickerTo = (Button) findViewById(R.id.btn_to_date);*/
        DoctorCollectionModel dml = new DoctorCollectionModel(MedicationUsers.getContext());
        ArrayList<DoctorCollection> docList = new ArrayList<DoctorCollection>();
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("N/A");
        docList = dml.getDoctor();
        for(int i=0;i<docList.size();i++){
            spinnerArray.add(docList.get(i).getDoctor_name());
                /*spinnerArray.add("item2");*/

        }

        spinnerArray.add(" + "+" Add Doctor");


        int pharmacySpinnerPosition ,doctorSpinnerPosition;


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
                /*spinnerArray.add("item2");*/

        }


        spinnerArrayPharma.add(" + "+" Add Pharmacy");





        ArrayAdapter<String> adapterPharma = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArrayPharma);

        adapterPharma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pharmacyNameSpinner.setAdapter(adapterPharma);
        editMedication();



        btnEditMedication.setOnClickListener(this);

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
    }


    public void editMedication() {

        medList = new ArrayList<MedicationCollection>();
        MedicationCollectionModel get = new MedicationCollectionModel(getContext());
        RefillCollectionModel refil = new RefillCollectionModel(MedicationUsers.getContext());
        AlarmCollectionModel aml = new AlarmCollectionModel(MedicationUsers.getContext());
        MedicationCollection getCollections = new MedicationCollection();
        Bundle extras = getIntent().getExtras();
        int messageExtra = extras.getInt("id");
       //getAlarmsByMedID

        alarmList = aml.getAlarmsByMedID(messageExtra);
        if(alarmList.size() > 0){

            for(int i=0;i<alarmList.size();i++){


                alarm_id = alarmList.get(i).getId();

                Log.e(Constants.TAG, "Alarm ID " + alarmList.get(i).getId());
                Log.e(Constants.TAG, "Alarm Name " + alarmList.get(i).getAlarmName());
                Log.e(Constants.TAG, "Alarm Time " + alarmList.get(i).getAlarmTime());
                Log.e(Constants.TAG, "Alarm MedId " + alarmList.get(i).getMedAlarmId());
                Log.e(Constants.TAG, "Alarm Tone " + alarmList.get(i).getAlarmTonePath());
            }
        }


        /*Intent in = new Intent();
        int mes = in.getIntExtra(EXTRA_MESSAGE,0);*/
       /* Log.e("insde edit medications", "editMedication: "+messageExtra);*/
        Log.e("inside edit medications", "editMedication: " + messageExtra);

        ArrayAdapter morAdap = (ArrayAdapter) mor_alarm_time.getAdapter(); //cast to an ArrayAdapter
        ArrayAdapter noonAdap = (ArrayAdapter) noon_alarm_time.getAdapter(); //cast to an ArrayAdapter
        ArrayAdapter evenAdap = (ArrayAdapter) even_alarm_time.getAdapter(); //cast to an ArrayAdapter
        ArrayAdapter nightAdap = (ArrayAdapter) night_alarm_time.getAdapter();
        ArrayAdapter qtyAdap = (ArrayAdapter) med_qty.getAdapter(); //cast to an ArrayAdapter
        ArrayAdapter doseAdap = (ArrayAdapter) med_dose.getAdapter(); //cast to an ArrayAdapter
        ArrayAdapter phrmaAdap = (ArrayAdapter) pharmacyNameSpinner.getAdapter();
        ArrayAdapter doctorAdap = (ArrayAdapter) doctorSpinner.getAdapter();

        medList = get.getMedicationsByID(messageExtra);
        refList = refil.getRefillByID(messageExtra);
        Log.e("Exception", "MedByID: " + medList.toString());
        int morSpinnerPosition, noonSpinnerPosition, evenSpinnerPosition, nightSpinnerPosition, qtySpinnerPosition, doseSpinnerPosition,pharmaSpinnerPosition,doctorSpinnerPostion;

        try {
            for (int i = 0; i < medList.size(); i++) {
                if (medList.get(i) != null && refList.get(i) != null) {
                    Log.e("Exception", "MedList " + i + ": " + medList.get(i).toString());
                    med_name.setText(medList.get(i).getM_name());
                    Log.e(Constants.TAG, "Dosage from dB "+medList.get(i).getDosage());
                    StringTokenizer stringTokenizer = new StringTokenizer(String.valueOf(medList.get(i).getDosage()),"-");
                    String firsttoken = stringTokenizer.nextToken();
                    String secondtoken = stringTokenizer.nextToken();
                    doseSpinnerPosition = (doseAdap.getPosition(secondtoken));
                    Log.e(Constants.TAG, "DoseSPinnerspinner second value: " + secondtoken);
                    Log.e(Constants.TAG, "DoseSPinnerspinner first value: " + firsttoken);
                    Log.e(Constants.TAG, "DoseSPinnerValueFronDB: " + medList.get(i).getDosage());
                    Log.e(Constants.TAG, "Dose at position: " + med_dose.getItemAtPosition(doseSpinnerPosition));
                    //med_dose.setSelection(medList.get(i).getDosage());
                    med_dose.setSelection(doseSpinnerPosition);
                    doseTxt.setText(firsttoken);
                    Log.e(Constants.TAG, "DOSETEXT AFTER SETTING "+doseTxt.getText());
                    Log.e(Constants.TAG, "QtyPosition: " + ((ArrayAdapter<String>) med_qty.getAdapter()).getPosition((String.valueOf(medList.get(i).getQuantity()))));

                    qtySpinnerPosition = (qtyAdap.getPosition(String.valueOf(medList.get(i).getQuantity())));
                    Log.e(Constants.TAG, "QtySPinnerPosition: " + qtySpinnerPosition);
                    Log.e(Constants.TAG, "QtySPinnerValueFronDB: " + medList.get(i).getQuantity());
                    Log.e(Constants.TAG, "Qty at position: " + med_qty.getItemAtPosition(qtySpinnerPosition));
                    med_qty.setSelection(qtySpinnerPosition);
                    // med_qty.setSelection(medList.get(i).getQuantity());
                    //even_alarm_time.setSelection(evenSpinnerPosition);

                    Log.e("Exception", "med_qty spinner count " + i + ": " + med_qty.getCount());
                    Log.e("Exception", "med_qty " + i + ": " + medList.get(i).getQuantity());
                    //  med_qty.setSelection(medList.get(i).getQuantity());
                   /* doc_name.setText(medList.get(i).getDoctorName());
                    pharma_name.setText(medList.get(i).getPharmacyName());
                    clinic_name.setText(medList.get(i).getClinicName());*/
                    pharmaSpinnerPosition = (phrmaAdap.getPosition(medList.get(i).getPharmacyName()));
                    pharmacyNameSpinner.setSelection(pharmaSpinnerPosition);

                    doctorSpinnerPostion = (doctorAdap.getPosition(medList.get(i).getDoctorName()));
                    doctorSpinner.setSelection(doctorSpinnerPostion);

                    minQty.setText(refList.get(i).getMin_qty());
                    /*maxQty.setText(refList.get(i).getMax_qty());*/
                    Log.e(Constants.TAG, "Max quantity from refill table "+refList.get(i).getMax_qty());
                    //String strDate,endDate;
                     strDate = medList.get(i).getFromDate().toString();
                     endDate = medList.get(i).getToDate();
                    morSpinnerPosition = morAdap.getPosition(medList.get(i).getMorningAlarmStatus());
                    Log.e(Constants.TAG, "morSpinnerPosition: " + mor_alarm_time.getItemAtPosition(morSpinnerPosition));
                    previous_morn_value =  mor_alarm_time.getItemAtPosition(morSpinnerPosition).toString();
                    mor_alarm_time.setSelection(morSpinnerPosition);
                    Log.e(Constants.TAG, "previous_morn_value: "+previous_morn_value);

                    noonSpinnerPosition = noonAdap.getPosition(medList.get(i).getAfternoonAlarmStatus());
                    Log.e("spinner", "noonSpinnerPosition: " + noon_alarm_time.getItemAtPosition(noonSpinnerPosition));
                    noon_alarm_time.setSelection(noonSpinnerPosition);
                    previous_noon_value = noon_alarm_time.getItemAtPosition(noonSpinnerPosition).toString();
                    Log.e(Constants.TAG, "previous_noon_value: "+previous_noon_value);

                    evenSpinnerPosition = evenAdap.getPosition(medList.get(i).getEveningAlarmStatus());
                    Log.e("spinner", "evenSpinnerPosition: " + evenSpinnerPosition);
                    even_alarm_time.setSelection(evenSpinnerPosition);
                    previous_even_value = even_alarm_time.getItemAtPosition(evenSpinnerPosition).toString();
                    Log.e(Constants.TAG, "previous_even_value: "+previous_even_value);


                    nightSpinnerPosition = nightAdap.getPosition(medList.get(i).getNightAlarmStatus());
                    Log.e("spinner", "nightSpinnerPosition: " + nightSpinnerPosition);
                    night_alarm_time.setSelection(nightSpinnerPosition);
                    previous_night_value = night_alarm_time.getItemAtPosition(nightSpinnerPosition).toString();
                    Log.e(Constants.TAG, "previous_night_value: "+previous_night_value);
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception: " + e.toString());
        }

    }

    public void onClick(View v) {
        if (v == btnEditMedication) {
            Log.e(Constants.TAG, "btn Click");

            Intent setAlarm = new Intent(EditMedicationActivity.this, AlarmPreferencesActivity.class);

            String mornTime = null ;
            String noonTime = null ;
            String evenTime = null ;
            String nightTime = null ;
            if(!mor_alarm_time.getSelectedItem().toString().equals("N/A")){
                StringTokenizer noonToken = new StringTokenizer(mor_alarm_time.getSelectedItem().toString(), ":");
                String firsttoken = noonToken.nextToken();
                String secondtoken = noonToken.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                multiplerCounter+=1;

                mornTime = hours + ":" + min;
            }

            else
            {
                mornTime = "N/A";
            }

            if(!noon_alarm_time.getSelectedItem().toString().equals("N/A")){
                StringTokenizer noonToken = new StringTokenizer(noon_alarm_time.getSelectedItem().toString(), ":");
                String firsttoken = noonToken.nextToken();
                String secondtoken = noonToken.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                if (!DateFormat.is24HourFormat(this)) {


                    if (hours < 12) {
                        hours += 12;
                    }
                    multiplerCounter += 1;
                    noonTime = hours + ":" + min;
                }
                else{
                    multiplerCounter += 1;
                    noonTime = hours + ":" + min;
                }
            }
            else
            {
                noonTime = "N/A";
            }


            if(!even_alarm_time.getSelectedItem().toString().equals("N/A")){
                StringTokenizer noonToken = new StringTokenizer(even_alarm_time.getSelectedItem().toString(), ":");
                String firsttoken = noonToken.nextToken();
                String secondtoken = noonToken.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);
                if (!DateFormat.is24HourFormat(this)) {


                    if (hours < 12) {
                        hours += 12;
                    }
                    multiplerCounter += 1;
                    evenTime = hours + ":" + min;
                }
                else{
                    multiplerCounter += 1;
                    evenTime = hours + ":" + min;
                }
            }
            else
            {
                evenTime = "N/A";
            }
            if(!night_alarm_time.getSelectedItem().toString().equals("N/A")){
                StringTokenizer noonToken = new StringTokenizer(night_alarm_time.getSelectedItem().toString(), ":");
                String firsttoken = noonToken.nextToken();
                String secondtoken = noonToken.nextToken();
                int hours = Integer.parseInt(firsttoken);
                int min = Integer.parseInt(secondtoken);

                if (!DateFormat.is24HourFormat(this)) {


                    if (hours < 12) {
                        hours += 12;
                    }
                    multiplerCounter += 1;
                    nightTime = hours + ":" + min;
                }
                else{
                    multiplerCounter += 1;
                    nightTime = hours + ":" + min;
                }
            }
            else
            {
                nightTime = "N/A";
            }

            // setAlarm.putExtra("alarmTime",noonTime);


            //setAlarm.putExtra("alarmTime",nightTime);
            Bundle extras = getIntent().getExtras();
            int messageExtra = extras.getInt("id");

            Log.e(Constants.TAG, "On edit click "+messageExtra);

            String EditMedication = "editMedication";
            setAlarm.putExtra("medid",messageExtra);
            setAlarm.putExtra("editMedication",EditMedication);
            setAlarm.putExtra("medname",med_name.getText().toString());
            setAlarm.putExtra("dose",doseTxt.getText().toString());
            setAlarm.putExtra("doseSpinner",med_dose.getSelectedItem().toString());
            setAlarm.putExtra("qty_med",med_qty.getSelectedItem().toString());
            setAlarm.putExtra("strDate",strDate);
            setAlarm.putExtra("endDate",endDate);
            setAlarm.putExtra("minQty",minQty.getText().toString());
            setAlarm.putExtra("maxQty",String.valueOf(multiplerCounter * Integer.parseInt(med_qty.getSelectedItem().toString())));
            setAlarm.putExtra("pharamaname",pharmacyNameSpinner.getSelectedItem().toString());
            setAlarm.putExtra("doc",doctorSpinner.getSelectedItem().toString());

            setAlarm.putExtra("mornTime", mornTime);
            setAlarm.putExtra("noonTime", noonTime);
            setAlarm.putExtra("evenTime", evenTime);
            setAlarm.putExtra("nightTime", nightTime);
            setAlarm.putExtra("mornTimeSpinner", mor_alarm_time.getSelectedItem().toString());
            setAlarm.putExtra("noonTimeSpinner", noon_alarm_time.getSelectedItem().toString());
            setAlarm.putExtra("evenTimeSpinner", even_alarm_time.getSelectedItem().toString());
            setAlarm.putExtra("nightTimeSpinner", night_alarm_time.getSelectedItem().toString());
            setAlarm.putExtra("previous_morn_value",previous_morn_value);
            setAlarm.putExtra("previous_noon_value",previous_noon_value);
            setAlarm.putExtra("previous_even_value",previous_even_value);
            setAlarm.putExtra("previous_night_value",previous_night_value);

            // setAlarm.putExtra("alarmTime",mornTxtTime.getSelectedItem().toString());
            setAlarm.putExtra("alarmName",med_name.getText().toString());
            startActivity(setAlarm);


        }
    }
}
