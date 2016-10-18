package com.example.arthome.newexchangeworld.ExchangeAPI;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SSD on 2016/10/15.
 */
public class RestClient
{
    private static final String BASE_URL = "http://exwd.csie.org:43002/";
    private ExchangeService apiService;

    public RestClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ExchangeService.class);
    }

    public ExchangeService getExchangeService()
    {
        return apiService;
    }
}
