package com.example.arthome.newexchangeworld;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.example.arthome.newexchangeworld.util.CommonAPI;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    private LoginButton loginButton;
    private TextView textView;
    private Button postButton;
    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private CallbackManager callbackManager;
    private AccessToken Fbtoken;
    private String fbID;
    private String EXtoken;
    private String itemName;
    private String itemDescription;
    private ProfileTracker mProfileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.arthome.newexchangeworld",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        textView = (TextView) findViewById(R.id.login_text);
        postButton = (Button) findViewById(R.id.login_post_button);
        itemDescriptionEditText = (EditText) findViewById(R.id.login_item_description_editText);
        itemNameEditText = (EditText) findViewById(R.id.login_item_name_editText);

        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");
        loginButton.setReadPermissions(permissionNeeds);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                if(Profile.getCurrentProfile() == null) {
                    System.out.println(">>>onSuccess");
                    System.out.println(">>>" + loginResult.getAccessToken().getToken());
                    System.out.println(">>>hash=" + loginResult.getAccessToken().getToken().hashCode());
                    System.out.println(">>>id=" + loginResult.getAccessToken().getUserId());
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            if(currentProfile==null && RealmManager.INSTANCE.retrieveUser().size()!=0){   //使用者登出, 且手機資料庫尚有使用者資料
                                RealmManager.INSTANCE.deleteAllUser();
                            }else if(!(currentProfile==null)){
                                User user = new User();
                                user.setIdentity(currentProfile.getId());
                                RealmManager.INSTANCE.createUser(user);
                                CommonAPI.INSTANCE.getExToken(currentProfile.getId(), getApplicationContext());
                                getAndSaveUserInfo(0, currentProfile.getId());
                            }
                        }
                    };
                }
            }

            @Override
            public void onCancel() {
                System.out.println(">>>onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println(">>>onCancel");
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = itemNameEditText.getText().toString();
                itemDescription = itemDescriptionEditText.getText().toString();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




    private void getAndSaveUserInfo(int uid, String strIdentity){
        Call<UserModel> getUserInfo = new RestClient().getExchangeService().getUserInfo(uid,strIdentity);
        getUserInfo.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.code()==200){
                    UserModel userModel = response.body();
                    User realmUser = RealmManager.INSTANCE.retrieveUser().get(0);
                    realmUser.setIdentity(userModel.getIdentity());
                    realmUser.setUserName(userModel.getName());
                    realmUser.setPhotoPath(userModel.getPhoto_path());
                    realmUser.setUid(userModel.getUid());
                    RealmManager.INSTANCE.createUser(realmUser);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println(">>>fail "+t);
            }
        });
    }
}


