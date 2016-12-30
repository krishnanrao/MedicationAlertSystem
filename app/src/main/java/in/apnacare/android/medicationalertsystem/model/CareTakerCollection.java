package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.database.CareTakerCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 29-10-2016.
 */

public class CareTakerCollection {

    int care_taker_id;
    private String care_taker_name,care_taker_email,care_taker_phnumber,care_taker_relation;

    public CareTakerCollection(){

    }

    public CareTakerCollection(int care_taker_id,String care_taker_name,String care_taker_email,String care_taker_relation,String care_taker_phnumber){
        this.care_taker_id = care_taker_id;
        this.care_taker_name = care_taker_name;
        this.care_taker_email = care_taker_email;
        this.care_taker_relation = care_taker_relation;
        this.care_taker_phnumber = care_taker_phnumber;


    }

    public int getCare_taker_id() {
        return care_taker_id;
    }

    public String getCare_taker_email() {
        return care_taker_email;
    }

    public String getCare_taker_name() {
        return care_taker_name;
    }

    public String getCare_taker_phnumber() {
        return care_taker_phnumber;
    }

    public String getCare_taker_relation() {
        return care_taker_relation;
    }

    public void setCare_taker_id(int care_taker_id) {
        this.care_taker_id = care_taker_id;
    }

    public void setCare_taker_name(String care_taker_name) {
        this.care_taker_name = care_taker_name;
    }

    public void setCare_taker_email(String care_taker_email) {
        this.care_taker_email = care_taker_email;
    }

    public void setCare_taker_relation(String care_taker_relation) {
        this.care_taker_relation = care_taker_relation;
    }

    public void setCare_taker_phnumber(String care_taker_phnumber) {
        this.care_taker_phnumber = care_taker_phnumber;
    }


    public void saveCareTaker(){
        ContentValues data = new ContentValues();
        try{

            data.put(CareTakerCollectionModel.CT_NAME,this.care_taker_name);
            data.put(CareTakerCollectionModel.CT_RELATIONSHIP,this.care_taker_relation);
            data.put(CareTakerCollectionModel.CT_EMAIL,this.care_taker_email);
            data.put(CareTakerCollectionModel.CT_NUMBER,this.care_taker_phnumber);

            CareTakerCollectionModel ctCollectionModel = new CareTakerCollectionModel(MedicationUsers.getContext());
            ctCollectionModel.addCareTakerCollection(data);
        }
        catch (Exception e){
            Log.e(Constants.TAG, "saveCareTaker: "+e.toString());
        }


    }

    public void updateCareTaker(int med_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateCareTaker: "+ "medID:" + med_id + "Data's: " + this.care_taker_relation);

        try {
            data.put(CareTakerCollectionModel.CT_NAME, this.care_taker_name);
            data.put(CareTakerCollectionModel.CT_RELATIONSHIP, this.care_taker_relation);
            data.put(CareTakerCollectionModel.CT_EMAIL, this.care_taker_email);
            data.put(CareTakerCollectionModel.CT_NUMBER, this.care_taker_phnumber);

            CareTakerCollectionModel med = new CareTakerCollectionModel(MedicationUsers.getContext());
            //med.open();
            med.updateCareTaker(data,med_id);
        }
        catch (Exception e) {
            Log.e("INSI Update careTaker:", this.care_taker_email);
            Log.e(Constants.TAG, "CareTakerUpdate: "+ e.toString());
        }

    }
}
