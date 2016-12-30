package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.model.RefillCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 15-11-2016.
 */

public class RefillCollectionModel {
    private int pharmacy_id;
    private String pharmacy_name, pharmacy_location, pharmacy_number;

    public String[] allColumns = {};

    public static final String R_ID = "r_id";
    public static final String MEDICINE_NAME = "medicine_name";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String MIN_QTY = "min_qty";
    public static final String MAX_QTY = "max_qty";
    public static final String TAKEN_QTY = "taken_qty";


    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public RefillCollectionModel(Context context) {
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

    public long addRefillCollection(ContentValues data) {
        long insertId = 0;
        Log.e(Constants.TAG, "inside add Doctor method" + data.toString());
        try {
            this.open();
            Log.e("addRefillCoction data", data.toString());
            insertId = database.insert(Constants.TABLE_REFILL, null, data);
            Log.e(Constants.TAG, "After inserting refil");
            Log.e(Constants.TAG, "Refill Collection InsertID " + insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addRefillCollection() Exception: " + e.toString());

        }

        return insertId;

    }

    public ArrayList<RefillCollection> getRefillCollection() {
        ArrayList<RefillCollection> doc_collection = new ArrayList<RefillCollection>();

        Cursor cursor = database.query(Constants.TABLE_REFILL,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RefillCollection docCollection = cursorToContact(cursor);
            doc_collection.add(docCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "DoctorDetails: " + cursor.toString());
        return doc_collection;

    }

    public ArrayList<RefillCollection> getRefillReminder(String date) {
        ArrayList<RefillCollection> doc_collection = new ArrayList<RefillCollection>();

        String whereClause = null;

        if (doc_collection.toString() != null) {
            whereClause = " from_date = '" + date + "'";
        }

        Cursor cursor = database.query(Constants.TABLE_DOCTOR,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RefillCollection docCollection = cursorToContact(cursor);
            doc_collection.add(docCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "Refil Details: " + cursor.toString());
        return doc_collection;

    }

    public ArrayList<RefillCollection> getRefillByID(int rh_id) {


        ArrayList<RefillCollection> doc = new ArrayList<RefillCollection>();
        String whereClause = null;

        if (rh_id != 0) {
            whereClause = " r_id = '" + rh_id + "'";
        }

        Cursor cursor = database.query(Constants.TABLE_REFILL,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RefillCollection doctorCollection = cursorToContact(cursor);
            doc.add(doctorCollection);
            cursor.moveToNext();
        }
        Log.e(Constants.TAG, "GetRefilBy ID count: " + cursor.getCount());
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "GetRefilBy: " + doc.toString());
        return doc;
    }

    public long updateRefillQty(ContentValues data, int rh_id) {
        long res = 0;
        Log.e(Constants.TAG, "updatePharmacyId" + rh_id);
        Log.e(Constants.TAG, "updatePharmacyData" + data.toString());
        try {
            this.open();

            res = database.update(Constants.TABLE_REFILL, data, "r_id = '" + rh_id + "'", null);
            Log.e(Constants.TAG, "query" + res);
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "UpdateQtyRefil() Exception: " + e.toString());
        }

        return res;
    }

    public void removeByID(String mname) {
        Log.e(Constants.TAG, "inside reifll remove by medName Removing... medname :" + mname);
        database.execSQL("DELETE FROM refill Where medicine_name = '" + mname + "'");

    }

    private RefillCollection cursorToContact(Cursor cursor) {


        RefillCollection refill_collection = new RefillCollection();

        refill_collection.setR_id(cursor.getInt(0));
        refill_collection.setMedicine_name(cursor.getString(1));
        refill_collection.setFrom_date(cursor.getString(2));
        refill_collection.setTo_date(cursor.getString(3));
        refill_collection.setMin_qty(cursor.getString(4));
        refill_collection.setMax_qty(cursor.getString(5));
        refill_collection.setTaken_qty(cursor.getString(6));

        Log.e(Constants.TAG, "cursorToContact refill " + cursor.getString(3));

        return refill_collection;
    }
}
