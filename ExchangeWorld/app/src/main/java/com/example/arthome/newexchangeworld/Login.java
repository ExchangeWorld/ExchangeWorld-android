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

import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.PostModel;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Login extends AppCompatActivity {
    private LoginButton loginButton;
    private TextView textView;
    private Button postButton;
    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private CallbackManager callbackManager;
    private AccessToken Fbtoken;
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
//                textView.setText("token = +"+loginResult.getAccessToken().getToken()
//                        +"\n" + profile.getName()+"\n" + profile.getProfilePictureUri(100,100));
                System.out.println(">>>"+loginResult.getAccessToken().getToken());
                System.out.println(">>>hash="+loginResult.getAccessToken().getToken().hashCode());
                System.out.println(">>>id="+loginResult.getAccessToken().getUserId());
                Fbtoken = loginResult.getAccessToken();
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
               new getTokenTask().execute();
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
                HttpEntity entity = new StringEntity(jsonString);
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

    public class getTokenTask extends AsyncTask<String,String,String>{

        private AuthenticationModel authenticationModel;

        @Override
        protected String doInBackground(String... params) {
            FaceBookUser user = new FaceBookUser();
            user.setIdentity(Fbtoken.getUserId());
            String body = new Gson().toJson(user);
            System.out.println(">>>body="+body);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://exwd.csie.org:43002/api/authenticate/login");
            post.addHeader("content-type","application/json");
            try {
                HttpEntity entity = new StringEntity(body);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                entity = response.getEntity();
                String jsonString = EntityUtils.toString(entity);
                System.out.println(">>>return String=" + jsonString);
                authenticationModel = new Gson().fromJson(jsonString, AuthenticationModel.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return authenticationModel.getToken();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EXtoken = s;
            new postTask().execute(s,itemName,itemDescription);
        }
    }
}


