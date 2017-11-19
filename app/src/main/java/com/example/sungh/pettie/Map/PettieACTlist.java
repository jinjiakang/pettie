package com.example.sungh.pettie.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.sungh.pettie.R;
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

public class PettieACTlist extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    private String position_no;
    public List<HashMap<String, String>> mAndroidMapList= new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_info);
        mListView = (ListView) findViewById(R.id.text_com);
        Intent intent = this.getIntent();
        position_no = intent.getStringExtra("position");
        Log.d("Intent_position",position_no);

        new RunWrok().start();
    }

    private class RunWrok extends Thread{

        String path_json = "http://140.131.114.167/join_qry.php?ActNo="+position_no;
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
                JoinGson[] JoinGsons = gson.fromJson(result_json, JoinGson[].class);
                for (JoinGson join : JoinGsons) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("Name", join.getName());
                    mAndroidMapList.add(map);
                    Log.d("Join", map.toString());

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

            adapter = new SimpleAdapter(PettieACTlist.this, mAndroidMapList, R.layout.list_actactivity,
                    new String[]{ "Name",},
                    new int[]{R.id.UserName});

            mListView.setAdapter(adapter);

        }

    }

    private class JoinGson {

        private String Name;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}
