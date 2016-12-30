package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 23-10-2016.
 */

public class PharmacyCollection {

    int p_id;
    private String pharmacy_name, pharmacy_location, pharmacy_number,pharmacy_email,pharmacy_city;
    /*private Date ;*/

    public PharmacyCollection() {

    }

    public PharmacyCollection(int p_id, String p_name,String pharmacy_city ,String plocation,String pharmacy_email, String p_number) {
        this.p_id = p_id;
        this.pharmacy_name = p_name;
        this.pharmacy_city = pharmacy_city;
        this.pharmacy_location = plocation;
        this.pharmacy_email = pharmacy_email;
        this.pharmacy_number = p_number;

    }

    public void setPharmacy_email(String pharmacy_email) {
        this.pharmacy_email = pharmacy_email;
    }

    public String getPharmacy_email() {
        return pharmacy_email;
    }

    public void setP_id(int id) {   this.p_id = id ;}
    public int getP_id() {
        return this.p_id;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public void setPharmacyname(String pharmacy_name) {

        this.pharmacy_name = pharmacy_name;
    }

    public void setPharmacy_city(String pharmacy_city) {
        this.pharmacy_city = pharmacy_city;
    }

    public String getPharmacy_city() {
        return pharmacy_city;
    }

    public String getPharmacyLocation() {
        return this.pharmacy_location;
    }

    public void setPharmacy_location(String location) {
        this.pharmacy_location = location;
    }

    public String getPharmacyNumber() {
        return this.pharmacy_number;
    }

    public void setPharmacyNumber(String pharmacy_number) {

        this.pharmacy_number = pharmacy_number;
    }


    public void savePharmacy() {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG,"INSIDE SAVE pharmacy:");
        try {

            data.put(PharmacyCollectionModel.PHARMACY_NAME, this.pharmacy_name);
            data.put(PharmacyCollectionModel.PHARMACY_CITY,this.pharmacy_city);
            data.put(PharmacyCollectionModel.PHARMACY_LOCATION, this.pharmacy_location);
            data.put(PharmacyCollectionModel.PHARMACY_EMAIL, this.pharmacy_email);
            data.put(PharmacyCollectionModel.PHARMACY_NUMBER, this.pharmacy_number);
            PharmacyCollectionModel pharma = new PharmacyCollectionModel(MedicationUsers.getContext());
            //med.open();
            pharma.addPharmacy(data);
        } catch (Exception e) {
            Log.e("INSIDE SAVE pharmacy:", this.pharmacy_name);
            Log.e("GOT SAVEEXCEPTION ", "savepharmacy: " + e.toString());
        }

    }
    public void updatePharmacy(int med_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updatePharmacy: "+med_id);

        try {
            data.put(PharmacyCollectionModel.PHARMACY_NAME, this.pharmacy_name);
            data.put(PharmacyCollectionModel.PHARMACY_LOCATION, this.pharmacy_location);
            data.put(PharmacyCollectionModel.PHARMACY_EMAIL, this.pharmacy_email);
            data.put(PharmacyCollectionModel.PHARMACY_NUMBER, this.pharmacy_number);;
            Log.e(Constants.TAG, "Pharmacy Number: "+this.pharmacy_number);
            PharmacyCollectionModel pharma = new PharmacyCollectionModel(MedicationUsers.getContext());
            //med.open();
            pharma.updatePharmacy(data,med_id);
        }
        catch (Exception e) {
            Log.e("INSI Update MEDICATION:", this.pharmacy_name);
            Log.e(Constants.TAG, "updateMedication: "+ e.toString());
        }

    }
}
