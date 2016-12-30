package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.util.Log;

import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.RefillCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 15-11-2016.
 */

public class RefillCollection {

    int r_id;
    private String medicine_name, min_qty, max_qty,from_date,to_date,taken_qty;

    public RefillCollection()
    {

    }

    public RefillCollection(int r_id,String medicine_name,String min_qty,String max_qty,String from_date,String to_date,String taken_qty){

        this.r_id = r_id;
        this.medicine_name = medicine_name;
        this.min_qty = min_qty;
        this.max_qty = max_qty;
        this.from_date = from_date;
        this.to_date = to_date;
        this.taken_qty =taken_qty;
    }

    public int getR_id() {
        return r_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getMax_qty() {
        return max_qty;
    }

    public String getMin_qty() {
        return min_qty;
    }

    public String getTaken_qty() {
        return taken_qty;
    }


    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;

    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public void setMin_qty(String min_qty) {
        this.min_qty = min_qty;
    }

    public void setMax_qty(String max_qty) {
        this.max_qty = max_qty;
    }

    public void setTaken_qty(String taken_qty) {
        this.taken_qty = taken_qty;
    }

    public void saveRefillDetails() {
        ContentValues data = new ContentValues();

        Log.e(Constants.TAG, "Inside saveRefillDetails: "+data.toString());

        try {
            data.put(RefillCollectionModel.MEDICINE_NAME, this.medicine_name);
            data.put(RefillCollectionModel.FROM_DATE, this.from_date);
            data.put(RefillCollectionModel.TO_DATE, this.to_date);
            data.put(RefillCollectionModel.MIN_QTY, this.min_qty);
            data.put(RefillCollectionModel.MAX_QTY, this.max_qty);
            data.put(RefillCollectionModel.TAKEN_QTY,this.taken_qty);
            RefillCollectionModel refill_collection = new RefillCollectionModel(MedicationUsers.getContext());
            //med.open();
            refill_collection.addRefillCollection(data);
        } catch (Exception e) {
            Log.e(Constants.TAG, "In save Refil details"+this.medicine_name);
            Log.e(Constants.TAG, "Save RefilDetails: " + e.toString());
        }

    }

    public void updateRefQty(int rh_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateRefil: "+rh_id);

        try {


            data.put(RefillCollectionModel.TAKEN_QTY, this.taken_qty);;

            RefillCollectionModel rfcm = new RefillCollectionModel(MedicationUsers.getContext());

            rfcm.updateRefillQty(data,rh_id);
        }
        catch (Exception e) {
            Log.e("INSI Update refil:", this.min_qty);
            Log.e(Constants.TAG, "update Refil: "+ e.toString());
        }

    }

    public void updateMinMax(int rh_id){

        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateRefil: "+rh_id);

        try {


            data.put(RefillCollectionModel.MIN_QTY, this.min_qty);;
            data.put(RefillCollectionModel.MAX_QTY, this.max_qty);;
            Log.e(Constants.TAG, "max qty updated: "+this.taken_qty);
            RefillCollectionModel rfcm = new RefillCollectionModel(MedicationUsers.getContext());

            rfcm.updateRefillQty(data,rh_id);
        }
        catch (Exception e) {
            Log.e("INSI Update refil:", this.min_qty);
            Log.e(Constants.TAG, "update Refil: "+ e.toString());
        }
    }
}
