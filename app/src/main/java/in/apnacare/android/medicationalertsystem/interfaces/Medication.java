package in.apnacare.android.medicationalertsystem.interfaces;

import java.util.List;

import in.apnacare.android.medicationalertsystem.fromapi.MedicationApi;
import in.apnacare.android.medicationalertsystem.fromapi.UserApi;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dell on 29-12-2016.
 */

public interface Medication {

    @GET("api/v1/medication")
    Call<List<MedicationApi>> getMedication();

    @GET("api/v1/appuser/{id}")
    Call<List<MedicationApi>> getAppUser(@Path("id") String id );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.ROOT_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}
