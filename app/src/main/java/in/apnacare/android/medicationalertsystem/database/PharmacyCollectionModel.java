package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 23-10-2016.
 */

public class PharmacyCollectionModel {


    public String[] allColumns = {};

    public static final String P_ID = "pharmacy_id";
    public static final String PHARMACY_NAME = "pharmacy_name";
    public static final String PHARMACY_CITY = "pharmacy_city";
    public static final String PHARMACY_LOCATION = "pharmacy_location";
    public static final String PHARMACY_NUMBER = "pharmacy_number";
    public static final String PHARMACY_EMAIL = "pharmacy_email";


    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public PharmacyCollectionModel(Context context) {
        med_ctx = context;
        try {
            db = DatabaseHandler.getInstance(med_ctx);
            database = db.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void open() {
        try {
            database = db.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void close() {
        database.close();
        db.close();
    }

    public long addPharmacy(ContentValues data) {
        long insertId = 0;
        try {
            this.open();
            insertId = database.insert(Constants.TABLE_PHARMACY, null, data);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addMedication() Exception: " + e.toString());
        }

        return insertId;

    }


    public ArrayList<PharmacyCollection> getPharmacy() {
        ArrayList<PharmacyCollection> pharmacy_collection = new ArrayList<PharmacyCollection>();

        Cursor cursor = database.query(Constants.TABLE_PHARMACY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            //cursor is empty
            Log.e(Constants.TAG, "getPharmacyreturns Empty");
        }

        while (!cursor.isAfterLast()) {
            PharmacyCollection pharmaCollection = cursorToContact(cursor);
            pharmacy_collection.add(pharmaCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "cursor Count" + cursor.getCount());
        Log.e("pharmagiver", "PharmacyGiver: " + pharmacy_collection.toString());
        return pharmacy_collection;

    }

    public void removeByID(int pharmacy_id) {
        Log.e("Inside Remove by ID", "Removing... " + pharmacy_id);
        database.execSQL("DELETE FROM pharmacy Where pharmacy_id = '" + pharmacy_id + "'");

    }

    public ArrayList<PharmacyCollection> getPharmaByID(int pharmacy_id) {

        Log.e("Inside get Pharmacy", "getPharmacyByID: " + pharmacy_id);

        ArrayList<PharmacyCollection> pharma = new ArrayList<PharmacyCollection>();
        String whereClause = null;

        if (pharmacy_id != 0) {
            whereClause = " pharmacy_id = '" + pharmacy_id + "'";
        }

        Cursor cursor = database.query(Constants.TABLE_PHARMACY,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PharmacyCollection pharmaCollection = cursorToContact(cursor);
            pharma.add(pharmaCollection);
            cursor.moveToNext();
        }
        Log.e(Constants.TAG, "getPharmacyByID count: " + cursor.getCount());
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "getPharmacyByID: " + pharma.toString());
        return pharma;
    }

    public long updatePharmacy(ContentValues data, int pharmacy_id) {
        long res = 0;
        Log.e(Constants.TAG, "updatePharmacyId" + pharmacy_id);
        Log.e(Constants.TAG, "updatePharmacyData" + data.toString());
        try {
            this.open();

            res = database.update(Constants.TABLE_PHARMACY, data, " pharmacy_id = '" + pharmacy_id + "'", null);
            Log.e(Constants.TAG, "query" + res);
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "updateMedication() Exception: " + e.toString());
        }

        return res;
    }

    private PharmacyCollection cursorToContact(Cursor cursor) {


        PharmacyCollection pharmacy_collection = new PharmacyCollection();
        pharmacy_collection.setP_id(cursor.getInt(0));
        pharmacy_collection.setPharmacyname(cursor.getString(1));
        pharmacy_collection.setPharmacy_city(cursor.getString(2));
        pharmacy_collection.setPharmacy_location(cursor.getString(3));
        pharmacy_collection.setPharmacy_email(cursor.getString(4));
        pharmacy_collection.setPharmacyNumber(cursor.getString(5));


        return pharmacy_collection;
    }
}
