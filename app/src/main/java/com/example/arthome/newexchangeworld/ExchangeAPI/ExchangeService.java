package com.example.arthome.newexchangeworld.ExchangeAPI;

import com.example.arthome.newexchangeworld.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SSD on 2016/10/15.
 */
public interface ExchangeService {

    // GET api/user?uid={int}&identity={string}
        @GET("api/user")
        Call<UserModel> getUserInfo(@Query("uid") int intUID, @Query("identity") String strIdentity);

}
