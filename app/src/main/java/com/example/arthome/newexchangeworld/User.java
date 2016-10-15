package com.example.arthome.newexchangeworld;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by arthome on 2016/10/10.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String identity;      //If it's with FB, fb_id as identity
    private String userName;
    private String photoPath;
    private String exToken;


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExToken() {
        return exToken;
    }

    public void setExToken(String exToken) {
        this.exToken = exToken;
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
