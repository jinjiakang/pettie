package com.example.sungh.pettie.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.sungh.pettie.Map.MyJsonAry;
import com.example.sungh.pettie.R;
import com.facebook.AccessToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sungh on 2017/11/16.
 */

public class MyactActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    private String position_no;
    public List<HashMap<String, String>> mAndroidMapList= new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_info);
        mListView = (ListView) findViewById(R.id.text_com);
        new RunWrok().start();
    }

    private class RunWrok extends Thread{

        String path_json = "http://140.131.114.167/act_person.php?UserAccount="+AccessToken.getCurrentAccessToken().getUserId();
        String result_json = null;

        OkHttpClient client = new OkHttpClient();
        private SimpleAdapter adapter;

        // get 方法 參考 http://square.github.io/okhttp/
        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        Runnable task = new Runnable() {


            @Override
            public void run() {
                Gson gson = new Gson();
                MyJsonAry[] myJsonAries = gson.fromJson(result_json,MyJsonAry[].class);
                for (MyJsonAry my : myJsonAries) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("ActName", my.getActName());
                    map.put("ActDate", my.getActDate());
                    map.put("ActLocation", my.getActLocation());
                    map.put("ActContent", my.getActContent());
                    mAndroidMapList.add(map);
                    Log.d("actarry", map.toString());

                }
                loadListView();

            }
        };

        @Override
        public void run() {
            try {
                //1.抓資料
                result_json = run(path_json);
                Log.d("json1", result_json);

                runOnUiThread(task);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 放將Array 放進ListView 裡面
        private void loadListView() {

            adapter = new SimpleAdapter(MyactActivity.this, mAndroidMapList, R.layout.activity_myact,
                    new String[]{"ActName", "ActLocation", "ActDate", "ActContent"},
                    new int[]{R.id.ActName, R.id.ActWhere, R.id.ActDate, R.id.ActContent});

            mListView.setAdapter(adapter);

        }


    }
}
