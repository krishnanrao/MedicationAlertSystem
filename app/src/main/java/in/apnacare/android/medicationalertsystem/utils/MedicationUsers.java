package in.apnacare.android.medicationalertsystem.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by dell on 03-10-2016.
 */

public class MedicationUsers extends Application {


    public static SharedPreferences preferences;
    public static SharedPreferences.Editor e;

    public static boolean isConnectedToInternet = false;

    private static Context mContext;
    public static ActivityManager activityManager;

    public final static int LOCATION_PERMISSION_REQUEST_CODE = 7896;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mContext = getApplicationContext();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        preferences = getApplicationContext().getSharedPreferences(Constants.SETTINGS_FILE_NAME, Constants.MODE_PRIVATE);
        e = preferences.edit();

        isConnectedToInternet = isConnectedToInternet();

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/robotolight.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/robotolight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/robotolight.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/robotolight.ttf");
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static boolean isConnectedToInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static boolean checkLocationPermission(){
        int status = mContext.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                mContext.getPackageName());

        if (status == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    public static boolean isServiceRunning(Class<?> serviceClass) {
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
