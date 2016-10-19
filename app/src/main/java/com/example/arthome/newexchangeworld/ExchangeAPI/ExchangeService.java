package com.example.arthome.newexchangeworld.ExchangeAPI;

import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by SSD on 2016/10/15.
 */
public interface ExchangeService {

    // GET api/user?uid={int}&identity={string}
        @GET("api/user")
        Call<UserModel> getUserInfo(@Query("uid") int intUID, @Query("identity") String strIdentity);

    @POST("api/authenticate/login")
    Call<AuthenticationModel> getToken(@Body FaceBookUser faceBookUser);
}
