package com.example.arthome.newexchangeworld;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by arthome on 2016/10/10.
 */

public class User extends RealmObject {
    @PrimaryKey
    String facebookID;
    String facebookName;
    String exToken;


    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getExToken() {
        return exToken;
    }

    public void setExToken(String exToken) {
        this.exToken = exToken;
    }

}
