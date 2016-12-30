package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

import static android.R.attr.level;
import static android.content.ContentValues.TAG;

/**
 * Created by dell on 03-10-2016.
 */

public class MedicationCollectionModel {
    private Alarm alarm;
    private int medication_id, quantity, dosage;
    private String medication_name, pharmacy_name, doctor_name, clinic_name;
    private Date fromDate, toDate;
    public String[] allColumns = {};

    public static final String MED_ID = "medication_id";
    public static final String MEDICATION_NAME = "medication_name";
    public static final String QUANTITY = "quantity";
    public static final String DOSAGE = "dosage";
    public static final String PHARMACY_NAME = "pharmacy_name";
    public static final String DOCTOR_NAME = "doctor_name";
   // public static final String CLINIC_NAME = "clinic_name";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String M_TIME = "morning_alarm";
    public static final String A_TIME = "afternoon_alarm";
    public static final String E_TIME = "evening_alarm";
    public static final String N_TIME = "night_alarm";




    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public MedicationCollectionModel(Context context) {
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

    public long addMedications(ContentValues data) {
        long insertId = 0;
        Log.e(Constants.TAG, "inside add medication method" + data.toString());
        Log.e(Constants.TAG, "inside add medication method from date :" + this.fromDate);
        Log.e(Constants.TAG, "inside add medication method to date : " + this.toDate);
        try {
            this.open();
            Log.e("data", data.toString());
            insertId = database.insert(Constants.TABLE_MEDICATION, null, data);
            Log.e("Inserted ", "After inserting ");
            Log.e("Returning ", "Returning InsertID " + insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addMedication() Exception: " + e.toString());

        }

        Log.e(Constants.TAG, "saveMedication:alarm med id "+insertId);
        return insertId;

    }

    public long updateMedication(ContentValues data, int medication_id) {
        long res = 0;

        try {
            this.open();

            res = database.update(Constants.TABLE_MEDICATION, data, "medication_id = '" + medication_id + "'", null);

            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "updateMedication() Exception: " + e.toString());
        }

        return res;
    }

    public ArrayList<MedicationCollection> getMedications() {
        ArrayList<MedicationCollection> medgiver = new ArrayList<MedicationCollection>();

        Cursor cursor = database.query(Constants.TABLE_MEDICATION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicationCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.v(Constants.TAG, "getMedications: " + cursor.toString());
        return medgiver;

    }

    public ArrayList<MedicationCollection> getMorningMedications() {
        ArrayList<MedicationCollection> medgiver = new ArrayList<MedicationCollection>();
        MedicationCollection med = new MedicationCollection();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        String selection = "'" + strDate + "' BETWEEN " + FROM_DATE + " AND " + TO_DATE + " AND " + M_TIME + " != 'N/A' "; //
        //String selection = null;
        Log.e(Constants.TAG, "getMorningQuery " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDICATION, null, selection, null, null, null, null);
        Log.d(Constants.TAG, "Morning Medication : " + cursor.toString());
        Log.e(Constants.TAG, "Morng CURSOR: " + cursor.getCount());


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.e(Constants.TAG, "Morng CURSOR fromdate  index: " + cursor.getColumnIndex(FROM_DATE));
            Log.e(Constants.TAG, "Morng CURSOR To date index: " + cursor.getColumnIndex(TO_DATE));
            Log.e(Constants.TAG, "Morng CURSOR fromdate Value: " + cursor.getString(cursor.getColumnIndex(FROM_DATE)));
            Log.e(Constants.TAG, "Morng CURSOR Todate Value: " + cursor.getString(cursor.getColumnIndex(TO_DATE)));
            MedicationCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Morng getMedications: " + medgiver.toString());
        return medgiver;

    }

    public ArrayList<MedicationCollection> getNoonMedications() {
        ArrayList<MedicationCollection> medgiver = new ArrayList<MedicationCollection>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());

        String selection = "'" + strDate + "' BETWEEN " + FROM_DATE + " AND " + TO_DATE + " AND " + A_TIME + " != 'N/A' "; //
        Log.e(Constants.TAG, "getNoonQuery " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDICATION, null, selection, null, null, null, null);
        Log.v(Constants.TAG, "Noon CURSOR: " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicationCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Noon getMedications: " + medgiver.toString());
        return medgiver;

    }

    public ArrayList<MedicationCollection> getEveningMedications() {
        ArrayList<MedicationCollection> medgiver = new ArrayList<MedicationCollection>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        String selection = "'" + strDate + "' BETWEEN " + FROM_DATE + " AND " + TO_DATE + " AND " + E_TIME + " != 'N/A' "; //
        Log.e(Constants.TAG, "getEveningQuery " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDICATION, null, selection, null, null, null, null);
        Log.v(Constants.TAG, "Evng CURSOR: " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicationCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Evng getMedications: " + medgiver.toString());
        return medgiver;

    }

    public ArrayList<MedicationCollection> getNightMedications() {
        ArrayList<MedicationCollection> medgiver = new ArrayList<MedicationCollection>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        String selection = "'" + strDate + "' BETWEEN " + FROM_DATE + " AND " + TO_DATE + " AND " + N_TIME + " != 'N/A' "; //
        Log.e(Constants.TAG, "getNightQuery " + selection);
        Log.v(Constants.TAG, "Night Medication selection: " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDICATION, null, selection, null, null, null, null);

        Log.v(Constants.TAG, "Night CURSOR: " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicationCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Night getMedications: " + medgiver.toString());
        return medgiver;

    }


    public ArrayList<MedicationCollection> getAlarmStatus() {
        ArrayList<MedicationCollection> medgiver = new ArrayList<MedicationCollection>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        String selection = "'"+ strDate + "' BETWEEN " + FROM_DATE + " AND " + TO_DATE + " AND " + M_TIME + " != 'N/A' "+ " AND " + A_TIME + " != 'N/A' "+ " AND " + E_TIME + " != 'N/A' "+ " AND " + N_TIME + " != 'N/A' "; //

        Log.e(Constants.TAG, "Get Alarm Query " + selection);
        Log.v(Constants.TAG, "Alarm Medication selection: " + selection);
        Cursor cursor = database.query(Constants.TABLE_MEDICATION, null , selection, null, null, null, null);

        Log.v(Constants.TAG, "Alarm CURSOR: " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicationCollection medCollection = cursorToContact(cursor);
            medgiver.add(medCollection);
            cursor.moveToNext();
        }

        cursor.close();
        Log.v(Constants.TAG, "Alarm getMedications: " + medgiver.toString());
        return medgiver;

    }

    public void removeByID(int medId) {
        Log.e("Inside Remove by ID", "Removing... " + medId);
        database.execSQL("DELETE FROM medications Where medication_id = '" + medId + "'");

    }



    public ArrayList<MedicationCollection> getMedicationsByID(int medId) {


        ArrayList<MedicationCollection> med = new ArrayList<MedicationCollection>();
        String whereClause = null;

        if (medId != 0) {
            whereClause = " medication_id = '" + medId + "'";
        }

        Cursor cursor = database.query(Constants.TABLE_MEDICATION,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedicationCollection medCollection = cursorToContact(cursor);
            med.add(medCollection);
            cursor.moveToNext();
        }
        Log.e(TAG, "getMedicationsByID count: " + cursor.getCount());
        // make sure to close the cursor
        cursor.close();
        Log.e("medicationCollectionMod", "getMedicationsByID: " + med.toString());
        return med;
    }

    private MedicationCollection cursorToContact(Cursor cursor) {


        MedicationCollection medCollection = new MedicationCollection();
        medCollection.setM_id(cursor.getInt(0));
        medCollection.setMedicationName(cursor.getString(1));
        medCollection.setQuantity(cursor.getInt(2));
        medCollection.setDosage(cursor.getString(3));
        medCollection.setPharmacy_name(cursor.getString(4));
        medCollection.setDoc_name(cursor.getString(5));

        medCollection.setFromDate(cursor.getString(6));
        medCollection.setToDate(cursor.getString(7));
        medCollection.setMorningAlarm(cursor.getString(8));
        medCollection.setAfterNoonAlarm(cursor.getString(9));
        medCollection.setEveningAlarm(cursor.getString(10));
        medCollection.setNightAlarm(cursor.getString(11));

        return medCollection;
    }
}
