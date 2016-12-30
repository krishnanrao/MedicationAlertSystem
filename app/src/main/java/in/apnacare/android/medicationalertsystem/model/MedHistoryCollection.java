package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedHistoryCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 14-11-2016.
 */

public class MedHistoryCollection {

    int mh_id;
    private String med_name, from_date, to_date,taken,skip,m_status,a_status,e_status,n_status;

    public MedHistoryCollection()
    {

    }

    public MedHistoryCollection(int id,String m_name,String from_date,String to_date,String m_status,String a_status,String e_status,String n_status,String taken,String skip){
        this.mh_id = id;
        this.med_name = m_name;
        this.from_date = from_date;
        this.to_date = to_date;
        this.m_status = m_status;
        this.a_status = a_status;
        this.e_status = e_status;
        this.n_status = n_status;
        this.taken = taken;
        this.skip = skip;
    }

    public int getMh_id() {
        return mh_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getMed_name() {
        return med_name;
    }

    public String getSkip() {
        return skip;
    }

    public String getTaken() {
        return taken;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getM_status() {
        return m_status;
    }

    public String getA_status() {
        return a_status;
    }

    public String getE_status() {
        return e_status;
    }

    public String getN_status() {
        return n_status;
    }

    public void setMh_id(int mh_id) {
        this.mh_id = mh_id;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public void setM_status(String m_status) {
        this.m_status = m_status;
    }

    public void setA_status(String a_status) {
        this.a_status = a_status;
    }

    public void setE_status(String e_status) {
        this.e_status = e_status;
    }

    public void setN_status(String n_status) {
        this.n_status = n_status;
    }


    public void setTaken(String taken) {
        this.taken = taken;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public void saveMedHistory() {
        ContentValues data = new ContentValues();

        Log.e(Constants.TAG, "Inside saveDoctor: ");

        try {
            data.put(MedHistoryCollectionModel.MEDICINE_NAME, this.med_name);
            data.put(MedHistoryCollectionModel.FROM_DATE, this.from_date);
            data.put(MedHistoryCollectionModel.TO_DATE, this.to_date);
            data.put(MedHistoryCollectionModel.M_TIME, this.m_status);
            data.put(MedHistoryCollectionModel.A_TIME, this.a_status);
            data.put(MedHistoryCollectionModel.E_TIME, this.e_status);
            data.put(MedHistoryCollectionModel.N_TIME, this.n_status);
            data.put(MedHistoryCollectionModel.TAKEN, this.taken);
            data.put(MedHistoryCollectionModel.SKIP, this.skip);


            MedHistoryCollectionModel medHistoryCollectionModel = new MedHistoryCollectionModel(MedicationUsers.getContext());

            medHistoryCollectionModel.addMedHistoryCollection(data);
        } catch (Exception e) {
            Log.e(Constants.TAG, this.med_name);
            Log.e(Constants.TAG, "Save Medication History: " + e.toString());
        }

    }
    public void updateTaken(int med_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateTaken: "+ "mh_id:" + med_id + "Data's: " + this.taken);

        try {

            data.put(MedHistoryCollectionModel.TAKEN, String.valueOf(this.taken));

            MedHistoryCollectionModel med = new MedHistoryCollectionModel(MedicationUsers.getContext());
            med.updateTaken(data,med_id);
        }
        catch (Exception e) {
            Log.e("Inside update taken", this.taken);
            Log.e(Constants.TAG, "updateTaken: "+ e.toString());
        }

    }
    public void updateSkipped(int med_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateMedications: "+ "medID:" + med_id + "Data's: " + this.taken);

        try {

            data.put(MedHistoryCollectionModel.SKIP, String.valueOf(this.skip));

            MedHistoryCollectionModel med = new MedHistoryCollectionModel(MedicationUsers.getContext());
            //med.open();
            med.updateSkipped(data,med_id);
        }
        catch (Exception e) {
            Log.e(Constants.TAG,"Inside update taken"+this.skip);
            Log.e(Constants.TAG, "updateSkipped: "+ e.toString());
        }

    }

}
