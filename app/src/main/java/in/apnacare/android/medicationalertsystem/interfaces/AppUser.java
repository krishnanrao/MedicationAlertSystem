package in.apnacare.android.medicationalertsystem.interfaces;

import java.util.List;

import in.apnacare.android.medicationalertsystem.fromapi.UserApi;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dell on 29-12-2016.
 */

public interface AppUser {
    @GET("api/v1/appuser")
    public void getAppUsers(Callback<List<UserApi>> response);

    @GET("api/v1/appuser/{id}")
    Call<List<UserApi>> getAppUser(@Path("id") String id );



    @FormUrlEncoded
    @POST("/api/v1/appuser")
    public void insertUser(
            @Field("name") String name,
            @Field("phonenumber") String phonenumber,
            @Field("email") String email,
            @Field("birthdate") String date,
            @Field("birthmonth") String month,
            Callback<Response> callback);




    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.ROOT_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();



}
