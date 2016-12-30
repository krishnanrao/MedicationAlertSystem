package in.apnacare.android.medicationalertsystem.utils;

import java.io.File;

/**
 * Created by dell on 03-10-2016.
 */

public class Constants {

    public static String TABLE_MEDICATION = "medications";
    public static String TABLE_PHARMACY = "pharmacy";
    public static String TABLE_DOCTOR = "doctor";
    public static String TABLE_CARETAKER = "caretaker";
    public static String TABLE_MEDHISTORY = "medicationHistory";
    public static String TABLE_REFILL = "refill";
    public static String TABLE_USER = "user";
    public static final String FOLDER_NAME = "Apna_Care";
    public static final String MODULE_FOLDER = "MedicationAlertSystem";
    public static final String PROFILE_FOLDER = Constants.FOLDER_NAME + File.separator + Constants.MODULE_FOLDER + File.separator + "caretaker" + File.separator;
    public static final String TAG = "MedApp.class";
    public static final int MODE_PRIVATE = 0;
    public static final String SETTINGS_FILE_NAME = "in.apnacare.android.medicationalertsystem.Preferences";
    public static final String ROOT_API_URL = "http://192.168.1.10/";

    }
