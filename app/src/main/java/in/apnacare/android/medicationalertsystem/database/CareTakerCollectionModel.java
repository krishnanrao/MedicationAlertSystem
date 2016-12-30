package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.model.CareTakerCollection;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 29-10-2016.
 */

public class CareTakerCollectionModel {

    public String[] allColumns = {};

    public static final String CT_ID = "care_taker_id";
    public static final String CT_NAME = "care_taker_name";
    public static final String CT_RELATIONSHIP = "care_taker_relationship";
    public static final String CT_EMAIL = "care_taker_email";
    public static final String CT_NUMBER = "care_taker_number";

    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public CareTakerCollectionModel(Context context) {
        med_ctx = context;
        try {
            db = DatabaseHandler.getInstance(med_ctx);
            database = db.getWritableDatabase();
        }
        catch (Exception e){
            Log.e(TAG,e.toString() );
        }
    }

    public void open(){
        try {
            database = db.getWritableDatabase();
        }
        catch (Exception e){
            Log.e(TAG,e.toString() );
        }
    }
    public void close() {
        database.close();
        db.close();
    }

    public long addCareTakerCollection(ContentValues data) {
        long insertId = 0;
        Log.e(Constants.TAG,"inside add caretaker method"+data.toString());
        try {
            this.open();
            Log.e("data",data.toString());
            insertId = database.insert(Constants.TABLE_CARETAKER, null, data);
            Log.e("Inserted ","After inserting ");
            Log.e("Returning ","Returning InsertID "+insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addMedication() Exception: " + e.toString());

        }

        return insertId;

    }



    public ArrayList<CareTakerCollection> getCareTaker() {
        ArrayList<CareTakerCollection> care_taker_collection = new ArrayList<CareTakerCollection>();

        Cursor cursor = database.query(Constants.TABLE_CARETAKER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CareTakerCollection careTakerCollection = cursorToContact(cursor);
            care_taker_collection.add(careTakerCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "DoctorDetails: "+ cursor.toString());
        return care_taker_collection;

    }
    public void removeByID(int caretaker_id){
        Log.e("Inside Remove by ID","Removing... "+caretaker_id);
        database.execSQL("DELETE FROM caretaker Where care_taker_id = '" + caretaker_id + "'");

    }



    public ArrayList<CareTakerCollection> getCareTakerByID(int ctId) {

        Log.e(Constants.TAG, "getCareTakerByID: " + ctId);

        ArrayList<CareTakerCollection> med = new ArrayList<CareTakerCollection>();
        String whereClause = null;

        if (ctId != 0) {
            whereClause = " care_taker_id = '" + ctId + "'";
        }

        Cursor cursor = database.query(Constants.TABLE_CARETAKER,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CareTakerCollection medCollection = cursorToContact(cursor);
            med.add(medCollection);
            cursor.moveToNext();
        }
        Log.e(TAG, "getMedicationsByID count: " + cursor.getCount());
        // make sure to close the cursor
        cursor.close();
        Log.e("CareTakerCollectionMod", "getMedicationsByID: " + med.toString());
        return med;
    }



    public long updateCareTaker(ContentValues data, int care_taker_id) {
        long res = 0;

        try {
            this.open();

            res = database.update(Constants.TABLE_CARETAKER, data, "care_taker_id = '" + care_taker_id + "'", null);

            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "updateCareTaker() Exception: " + e.toString());
        }

        return res;
    }


    private CareTakerCollection cursorToContact(Cursor cursor) {


        CareTakerCollection care_taker_collection = new CareTakerCollection();

        care_taker_collection.setCare_taker_id(cursor.getInt(0));
        care_taker_collection.setCare_taker_name(cursor.getString(1));
        care_taker_collection.setCare_taker_relation(cursor.getString(2));
        care_taker_collection.setCare_taker_email(cursor.getString(3));
        care_taker_collection.setCare_taker_phnumber(cursor.getString(4));



        return care_taker_collection;
    }




}
