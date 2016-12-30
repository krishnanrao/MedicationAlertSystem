package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 23-10-2016.
 */

public class DoctorCollection {

    int d_id;
    private String doctor_name, hospital_name, hospital_location,doc_hosp_phnumber,doc_email,hosp_city;

    public  DoctorCollection(){

    }
    public DoctorCollection(int d_id, String h_name,String d_name,String doc_city, String dlocation,String doc_email, String dh_number) {
        this.d_id = d_id;
        this.doctor_name = d_name;
        this.hospital_name = h_name;
        this.hospital_location = dlocation;
        this.hosp_city = doc_city;
        this.doc_email = doc_email;
        this.doc_hosp_phnumber = dh_number;

    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public int getD_id() {
        return d_id;
    }

    public void setDoc_email(String doc_email) {
        this.doc_email = doc_email;
    }

    public String getDoc_email() {
        return doc_email;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoc_hosp_phnumber() {
        return doc_hosp_phnumber;
    }

    public void setDoc_hosp_phnumber(String doc_hosp_phnumber) {
        this.doc_hosp_phnumber = doc_hosp_phnumber;
    }

    public void setHospital_location(String hospital_location) {
        this.hospital_location = hospital_location;
    }

    public String getHospital_location() {
        return hospital_location;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDoc_city() {
        return hosp_city;
    }

    public void setDoc_city(String hosp_city) {
        this.hosp_city = hosp_city;
    }


    public void saveDoctor() {
        ContentValues data = new ContentValues();

        Log.e(Constants.TAG, "Inside saveDoctor: "+this.hosp_city);

        try {
            data.put(DoctorCollectionModel.DOCTOR_NAME, this.doctor_name);
            data.put(DoctorCollectionModel.HOSPITAL_NAME, this.hospital_name);
            data.put(DoctorCollectionModel.HOSPITAL_CITY, this.hosp_city);
            data.put(DoctorCollectionModel.HOSPITAL_LOCATION, this.hospital_location);
            data.put(DoctorCollectionModel.DOC_HOSP_NUMBER, this.doc_hosp_phnumber);
            data.put(DoctorCollectionModel.DOCTOR_EMAIL, this.doc_email);


            DoctorCollectionModel doc_collection = new DoctorCollectionModel(MedicationUsers.getContext());
            //med.open();
            doc_collection.addDoctorCollection(data);
        } catch (Exception e) {
            Log.e(Constants.TAG, this.doctor_name);
            Log.e(Constants.TAG, "saveDoctor: " + e.toString());
        }

    }

    public void updateDoctorDetails(int doc_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateDoctor: "+doc_id);

        try {
            data.put(DoctorCollectionModel.DOCTOR_NAME, this.doctor_name);
            data.put(DoctorCollectionModel.HOSPITAL_NAME, this.hospital_name);
            data.put(DoctorCollectionModel.HOSPITAL_CITY, this.hosp_city);
            data.put(DoctorCollectionModel.HOSPITAL_LOCATION, this.hospital_location);
            data.put(DoctorCollectionModel.DOCTOR_EMAIL, this.doc_email);;
            data.put(DoctorCollectionModel.DOC_HOSP_NUMBER, this.doc_hosp_phnumber);;
            Log.e(Constants.TAG, "Doctor Number: "+this.doc_hosp_phnumber);
            DoctorCollectionModel doctorCollectionModel = new DoctorCollectionModel(MedicationUsers.getContext());

            doctorCollectionModel.updateDoctorDetails(data,doc_id);
        }
        catch (Exception e) {
            Log.e("INSI Update Doctor:", this.doctor_name);
            Log.e(Constants.TAG, "updateDoctor: "+ e.toString());
        }

    }




}
