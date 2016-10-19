package com.example.arthome.newexchangeworld;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
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
import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmResults;
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
                System.out.println(">>>onSuccess");
                System.out.println(">>>"+loginResult.getAccessToken().getToken());
                System.out.println(">>>hash="+loginResult.getAccessToken().getToken().hashCode());
                System.out.println(">>>id="+loginResult.getAccessToken().getUserId());
                Fbtoken = loginResult.getAccessToken();
                fbID = Fbtoken.getUserId();
                System.out.println(">>onSuccess "+fbID);
                if(RealmManager.INSTANCE.retrieveUser().size()==0) {
                    RealmManager.INSTANCE.createUser(new User());
                }
                getToken(fbID);
                getAndSaveUserInfo(0,fbID);
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

    public class postTask extends AsyncTask<String,String,Integer>{

        private int statusCode;

        @Override
        protected Integer doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://exwd.csie.org:43002/api/goods?token="+params[0]);
            post.addHeader("content-type","application/json");
            try {
                PostModel postModel = new PostModel();
                postModel.setDescription(params[2]);
                postModel.setName(params[1]);
                String jsonString = new Gson().toJson(postModel);
                HttpEntity entity = new StringEntity(jsonString, HTTP.UTF_8);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                entity = response.getEntity();
                jsonString = EntityUtils.toString(entity);
                System.out.println(">>>return String=" + jsonString);
                statusCode = response.getStatusLine().getStatusCode();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer==201){  //post success
                Toast.makeText(getApplicationContext(),"Posted",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Post fail",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getToken(String identity) {
        FaceBookUser faceBookUser = new FaceBookUser();
        faceBookUser.setIdentity(identity);
        Call<AuthenticationModel> call = new RestClient().getExchangeService().getToken(faceBookUser);
        call.enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.code()==201&&response.body().getAuthentication().equals("success")){
                    User realmUser = RealmManager.INSTANCE.retrieveUser().get(0);
                    realmUser.setExToken(response.body().getToken());
                    RealmManager.INSTANCE.createUser(realmUser);
                }else
                    Toast.makeText(getBaseContext(),"取得Token失敗",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Toast.makeText(getBaseContext(),"取得Token失敗",Toast.LENGTH_SHORT).show();
            }
        });
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
                    System.out.println(">>>Saved!!!"+userModel.getName());
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


