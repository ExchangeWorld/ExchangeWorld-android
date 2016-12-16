package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/12/16.
 */

public class RegisterModel {
    private boolean fb;
    private String identity;
    private String password;
    private String name;
    private String email;
    private String photo_path;

    public RegisterModel(boolean fb, String identity, String name, String email, String photo_path) {
        this.fb = fb;
        this.identity = identity;
        this.name = name;
        this.email = email;
        this.photo_path = photo_path;
    }

    public boolean isFb() {
        return fb;
    }

    public void setFb(boolean fb) {
        this.fb = fb;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
