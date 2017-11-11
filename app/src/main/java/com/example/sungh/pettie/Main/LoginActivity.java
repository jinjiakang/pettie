package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sungh.pettie.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity {
    private ImageView mImgPhoto;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    private TextView mTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        mImgPhoto = (ImageView) findViewById(R.id.mImgPhoto);
        mTextDescription = (TextView)findViewById(R.id.fb_name);

        if (accessToken.getCurrentAccessToken() != null) {
            Log.d("FACEBOOK", "Facebook getUserId: " + AccessToken.getCurrentAccessToken().getUserId());
            new RunWrok().start();

        }
        // log out 的動作
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Log.d("onlogout", "onLogout catched");
                    mImgPhoto.setImageResource(0);
                    mTextDescription.setText("");
                    LoginManager.getInstance().logOut();

                }
            }
        };
        accessTokenTracker.startTracking();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();

                Log.d("FB_ACC",accessToken.toString());

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            //當RESPONSE回來的時候
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                Log.d("FB_S","complete");
                                Log.d("FB_S",object.optString("name"));
                                Log.d("FB_S",object.optString("link"));
                                Log.d("FB_S",object.optString("id"));
                                Profile profile = Profile.getCurrentProfile();
                                Uri userPhoto = profile.getProfilePictureUri(300, 300);

                                Glide.with(LoginActivity.this)
                                        .load(userPhoto.toString())
                                        .crossFade()
                                        .into(mImgPhoto);
                                Log.d("FB_S",userPhoto.toString());
                                // 延緩四秒
                                try {
                                    Thread.sleep(4000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mTextDescription.setText(object.optString("name"));
                                postProfile(object.optString("id"),object.optString("name"),userPhoto.toString());
                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                Log.d("FB_C","CANCEL");
                mImgPhoto.clearFocus();
                mTextDescription.setText("");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB_E" ,error.toString());
            }

        });

    }

    private void postProfile(String id, String name, String s) {
        final OkHttpClient client = new OkHttpClient();
        final RequestBody formBody = new FormBody.Builder()
                .add("UserAccount",id)
                .add("Img_user",s)
                .add("Name",name)
                .build();

        final Request request = new Request.Builder()
                .url("http://140.131.114.167/user_ins.php")
                .post(formBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("DelPost","DELPOST輸出: " + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "連不上伺服器", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }).start();


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    public void goHome(View view) {

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void myPost(View view){
        // 權限判斷
        if (AccessToken.getCurrentAccessToken() == null) {
            Toast.makeText(LoginActivity.this, "請先登入...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MypostActivity.class);
        startActivity(intent);
    }


    class RunWrok extends Thread{
        String path_json ="http://140.131.114.167/user_Img.php?UserAccount="+ AccessToken.getCurrentAccessToken().getUserId();
        String result_json = null;

        /* This program downloads a URL and print its contents as a string.*/
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        Runnable task = new Runnable()
        {
            @Override
            public void run() {
                //使用 gson 解析 json 資料
                Gson gson = new Gson();
                user_Img[] Img = gson.fromJson(result_json, user_Img[].class);
                for(user_Img user_Img :Img){
                    Glide.with(LoginActivity.this)
                            .load(user_Img.getImg_user())
                            .crossFade()
                            .into(mImgPhoto);
                    mTextDescription.setText(user_Img.getName());
                }

            }
        };

        @Override
        public void run()
        {
            try {
                //1.抓資料
                result_json = run(path_json);
                runOnUiThread(task);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public class user_Img {
            public String getImg_user() {
                return Img_user;
            }
            private String Img_user;

            public String getName() {
                return Name;
            }

            private String Name;
        }
    }
}

