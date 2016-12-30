package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.activity.ViewMedicationActivity;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 30-09-2016.
 */

public class MedicationCollection {
    private Alarm alarm;
    private int m_id,quantity;
    private String m_name,dosage;
    private String pharmacy_name,doc_name,fromDate,toDate,m_alarm,a_alarm,e_alarm,n_alarm,took,skipped;


    public MedicationCollection()
    {

    }
    public MedicationCollection(int m_id,String m_name,String dosage,int quantity,String pharmacy_name,String doc_name, String fromDate, String toDate,String m_alarm,String a_alarm,String e_alarm,String n_alarm,String took,String skipped)
    {
        this.m_id = m_id;
        this.m_name = m_name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.pharmacy_name = pharmacy_name;
        this.doc_name = doc_name;

        this.fromDate = fromDate;
        this.toDate = toDate;
        this.m_alarm = m_alarm;
        this.a_alarm = a_alarm;
        this.e_alarm = e_alarm;
        this.n_alarm = n_alarm;
        this.took = took;
        this.skipped = skipped;
    }
    public int getM_id(){
            return this.m_id;
        }
    public  int getQuantity(){
        return this.quantity;
    }
    public  void setQuantity(int quantity){
        this.quantity = quantity;

     }

    public void setM_id(int m_id){
        this.m_id = m_id;
    }

    public String getDosage(){
        return this.dosage;
    }

    public void setDosage(String dosage){
        this.dosage= dosage;
    }

    public String getM_name(){
    return this.m_name;
    }
    public String getPharmacyName()
    {
        return this.pharmacy_name;
    }
    public String getDoctorName(){

        return this.doc_name;
    }
    /*public String getClinicName(){
        return this.clinic_name;
    }*/
    public void setMedicationName(String medName){
        Log.e("settingmedName", "medName: " + medName );
        this.m_name = medName;


    }
    public void setPharmacy_name(String pharmacy_name){

        this.pharmacy_name = pharmacy_name;
    }
    public void setDoc_name(String doc_name){
        this.doc_name = doc_name;
    }
   /* public void setClinic_name(String clinic_name){
        this.clinic_name = clinic_name;

    }*/
    public String getTaken(){return this.took;}
    public String getSkipped(){return this.skipped;}
    public void setTaken(String taken){
        this.took = taken;
    }
    public void setSkipped(String skip){
        this.skipped = skip;
    }

    public String getFromDate(){
        return this.fromDate;
    }
    public String getMorningAlarmStatus(){
        return this.m_alarm;
    }
    public String getAfternoonAlarmStatus(){
        return this.a_alarm;
    }
    public String getEveningAlarmStatus(){
        return this.e_alarm;
    }
    public String getNightAlarmStatus(){
        return this.n_alarm;
            }

    public String getToDate(){
        return this.toDate;
    }
    public void setFromDate(String fromDate){
        this.fromDate = fromDate;

    }
    public void setMorningAlarm(String m_alarm){
        this.m_alarm = m_alarm;
    }public void setAfterNoonAlarm(String a_alarm){
        this.a_alarm = a_alarm;
    }public void setEveningAlarm(String e_alarm){
        this.e_alarm = e_alarm;
    }public void setNightAlarm(String n_alarm){
        this.n_alarm = n_alarm;
    }
    public void setToDate(String toDate){
        this.toDate = toDate;
    }


    public int saveMedication()  {
        ContentValues data = new ContentValues();
        int le = 0;
        Log.e(Constants.TAG, "data inside saveMedication: "+data );
        Log.e(Constants.TAG, "data inside saveMedication two date: "+String.valueOf(this.toDate));
        Log.e(Constants.TAG, "data inside saveMedication from date: "+String.valueOf(this.fromDate) );
        Log.e(Constants.TAG, "data inside saveMedication: "+String.valueOf(this.m_alarm) );
        Log.e(Constants.TAG, "data inside saveMedication: "+String.valueOf(this.a_alarm) );
        Log.e(Constants.TAG, "data inside saveMedication: "+String.valueOf(this.e_alarm) );
        Log.e(Constants.TAG, "data inside saveMedication: "+String.valueOf(this.n_alarm) );

        try {
            data.put(MedicationCollectionModel.MEDICATION_NAME, this.m_name);
            data.put(MedicationCollectionModel.QUANTITY, this.quantity);
            data.put(MedicationCollectionModel.DOSAGE, this.dosage);
            data.put(MedicationCollectionModel.PHARMACY_NAME, this.pharmacy_name);
            data.put(MedicationCollectionModel.DOCTOR_NAME, this.doc_name);

            data.put(MedicationCollectionModel.FROM_DATE, String.valueOf(this.fromDate));
            data.put(MedicationCollectionModel.TO_DATE, String.valueOf(this.toDate));
            data.put(MedicationCollectionModel.M_TIME, String.valueOf(this.m_alarm));
            data.put(MedicationCollectionModel.A_TIME, String.valueOf(this.a_alarm));
            data.put(MedicationCollectionModel.E_TIME, String.valueOf(this.e_alarm));
            data.put(MedicationCollectionModel.N_TIME, String.valueOf(this.n_alarm));


            MedicationCollectionModel med = new MedicationCollectionModel(MedicationUsers.getContext());
            //med.open();
            med.open();
           le = (int) med.addMedications(data);
            Log.e(TAG, "Inside save med collection "+le);
           // alarm.setMed_id(this.m_id);

        }
        catch (Exception e) {
            Log.e("INSIDE SAVE MEDICATION:", this.m_name);
            Log.e("GOT SAVEEXCEPTION ", "saveMedication: "+e.toString());
        }
        return le;
    }
    public void updateMedications(int med_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateMedications: "+ "medID:" + med_id + "Data's: " + this.dosage);

        try {
            data.put(MedicationCollectionModel.MEDICATION_NAME, this.m_name);
            data.put(MedicationCollectionModel.QUANTITY, this.quantity);
            data.put(MedicationCollectionModel.DOSAGE, this.dosage);
            data.put(MedicationCollectionModel.PHARMACY_NAME, this.pharmacy_name);
            data.put(MedicationCollectionModel.DOCTOR_NAME, this.doc_name);

            data.put(MedicationCollectionModel.FROM_DATE, String.valueOf(this.fromDate));
            data.put(MedicationCollectionModel.TO_DATE, String.valueOf(this.toDate));
            data.put(MedicationCollectionModel.M_TIME, String.valueOf(this.m_alarm));
            data.put(MedicationCollectionModel.A_TIME, String.valueOf(this.a_alarm));
            data.put(MedicationCollectionModel.E_TIME, String.valueOf(this.e_alarm));
            data.put(MedicationCollectionModel.N_TIME, String.valueOf(this.n_alarm));


            MedicationCollectionModel med = new MedicationCollectionModel(MedicationUsers.getContext());
            //med.open();
            Log.e(TAG, "Inside updateMedications: "+data.toString() + med_id);
            med.updateMedication(data,med_id);
        }
        catch (Exception e) {
            Log.e("INSI Update MEDICATION:", this.m_name);
            Log.e(Constants.TAG, "updateMedication: "+ e.toString());
        }

    }



    @Override
    public String toString(){
        return this.m_name + " | " + this.fromDate + " | " + this.toDate + " | " + this.m_alarm + "|" + this.took + "|"+this.skipped;
    }

}
