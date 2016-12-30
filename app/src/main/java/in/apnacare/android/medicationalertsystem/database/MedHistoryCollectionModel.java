package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.MedHistoryCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 14-11-2016.
 */

public class MedHistoryCollectionModel {

    public String[] allColumns = {};

    public static final String MH_ID = "mh_id";
    public static final String MEDICINE_NAME  = "med_name";
    public static final String FROM_DATE  = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String M_TIME = "m_status";
    public static final String A_TIME = "a_status";
    public static final String E_TIME = "e_status";
    public static final String N_TIME = "n_status";
    public static final String TAKEN = "taken";
    public static final String SKIP = "skip";


    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public MedHistoryCollectionModel(Context context) {
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


    public long addMedHistoryCollection(ContentValues data) {
        long insertId = 0;
        Log.e(Constants.TAG,"inside add Doctor method"+data.toString());
        try {
            this.open();
            Log.e("data",data.toString());
            insertId = database.insert(Constants.TABLE_MEDHISTORY, null, data);
            Log.e("Inserted ","After inserting ");
            Log.e("Returning ","Returning InsertID "+insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "TABLE_MEDHISTORY() Exception: " + e.toString());

        }

        return insertId;

    }

    public ArrayList<MedHistoryCollection> getMedHistory() {
        ArrayList<MedHistoryCollection> med_history_collection = new ArrayList<MedHistoryCollection>();

        Cursor cursor = database.query(Constants.TABLE_MEDHISTORY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedHistoryCollection medHistoryCollection = cursorToContact(cursor);
            med_history_collection.add(medHistoryCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "DoctorDetails: "+ cursor.toString());
        return med_history_collection;

    }
    public ArrayList<MedHistoryCollection>  getMedByFromDate(String from_date) {

        Log.e("Inside get Doctor", "getDoctorByID: "+from_date );

        ArrayList<MedHistoryCollection> medHistory = new ArrayList<MedHistoryCollection>();
        String whereClause = null;

        if(from_date != null){
            whereClause = " from_date = '"+from_date+"'";
        }

        Cursor cursor = database.query(Constants.TABLE_MEDHISTORY,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedHistoryCollection medHistoryCollection = cursorToContact(cursor);
            medHistory.add(medHistoryCollection);
            cursor.moveToNext();
        }
        Log.e(Constants.TAG, "getPharmacyByID count: "+cursor.getCount());
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "getPharmacyByID: "+medHistory.toString());
        return medHistory;
    }

    public long updateTaken(ContentValues data, int medication_id) {
        long res = 0;

        try {

            res = database.update(Constants.TABLE_MEDHISTORY, data, "mh_id = '" + medication_id + "'", null);

            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "updateMedication() Exception: " + e.toString());
        }

        return res;
    }
    public long updateSkipped(ContentValues data, int medication_id) {
        long res = 0;

        try {
            this.open();

            res = database.update(Constants.TABLE_MEDHISTORY, data, "medication_id = '" + medication_id + "'", null);

            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "updateMedication() Exception: " + e.toString());
        }

        return res;
    }
    public ArrayList<MedHistoryCollection> getTookMedication() {
        ArrayList<MedHistoryCollection> medgiver = new ArrayList<MedHistoryCollection>();
        this.open();
        String selection = TAKEN + " = 1 ";
        Log.e(Constants.TAG, "Taken tag " + selection);
        Log.v(Constants.TAG, "Taken medhistory selection: " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDHISTORY, null, selection, null, null, null, null);

        Log.v(Constants.TAG, "Night CURSOR: " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedHistoryCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Night getMedications: " + medgiver.toString());
        return medgiver;

    }
    public ArrayList<MedHistoryCollection> getSkippedMedication() {
        ArrayList<MedHistoryCollection> medgiver = new ArrayList<MedHistoryCollection>();
        this.open();

        String selection = SKIP + " = 1 "; //
        Log.e(Constants.TAG, "Selection skipped " + selection);
        Log.v(Constants.TAG, " Medication history selection: " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDHISTORY, null, selection, null, null, null, null);

        Log.v(Constants.TAG, "Night CURSOR: " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedHistoryCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Night getMedications: " + medgiver.toString());
        return medgiver;

    }

    private MedHistoryCollection cursorToContact(Cursor cursor) {


        MedHistoryCollection med_history_collection = new MedHistoryCollection();

        med_history_collection.setMh_id(cursor.getInt(0));
        med_history_collection.setMed_name(cursor.getString(1));
        med_history_collection.setFrom_date(cursor.getString(2));
        med_history_collection.setTo_date(cursor.getString(3));
        med_history_collection.setM_status(cursor.getString(4));
        med_history_collection.setA_status(cursor.getString(5));
        med_history_collection.setE_status(cursor.getString(6));
        med_history_collection.setN_status(cursor.getString(7));
        med_history_collection.setTaken(cursor.getString(8));
        med_history_collection.setSkip(cursor.getString(9));



        return med_history_collection;
    }
}
