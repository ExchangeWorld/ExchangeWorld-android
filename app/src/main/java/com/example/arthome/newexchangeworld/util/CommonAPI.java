package com.example.arthome.newexchangeworld.util;

import android.content.Context;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;

import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/10/30.
 */

public enum  CommonAPI {
    INSTANCE;

    public void getExToken(String identity, final Context context) {
        FaceBookUser faceBookUser = new FaceBookUser();
        faceBookUser.setIdentity(identity);
        Call<AuthenticationModel> call = new RestClient().getExchangeService().getToken(faceBookUser);
        call.enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.code()==201&&response.body().getAuthentication().equals("success")){
                    User realmUser = RealmManager.INSTANCE.retrieveUser().get(0);
                    realmUser.setExToken(response.body().getToken());
                    realmUser.setLastTokenDate(new Date());
                    System.out.println(">>>date is "+ realmUser.getLastTokenDate());
                    RealmManager.INSTANCE.createUser(realmUser);
                }else
                    Toast.makeText(context,"取得Token失敗",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Toast.makeText(context,"取得Token失敗",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
