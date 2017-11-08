package com.example.sungh.pettie.Fourm;

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
 * Created by sungh on 2017/11/8.
 */

public class CommentActivity extends AppCompatActivity {
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

        String path_json = "http://140.131.114.167/Comment_qry.php?PostNo="+position_no;
        String result_json = null;

        /* This program downloads a URL and print its contents as a string.*/
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
                CommentGson[] CommentGsons = gson.fromJson(result_json, CommentGson[].class);
//                    StringBuilder sb = new StringBuilder();


                for (CommentGson com : CommentGsons) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("ComNo", com.getComNo());
                    map.put("ComContent", com.getComContent());
                    map.put("ComTime", com.getComTime());
                    map.put("PostNo", com.getComNo());
                    map.put("UserAccount",  com.getUserAccount());

                    mAndroidMapList.add(map);
                    Log.d("maparry", map.toString());
//                        sb.append("活動名稱:").append(act.getActName()).append("\n");
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

            adapter = new SimpleAdapter(CommentActivity.this, mAndroidMapList, R.layout.comment_content,
                    new String[]{"UserAccount", "ComContent", "ComTime"},
                    new int[]{R.id.UserAccount, R.id.ComContent, R.id.ComTime});

            mListView.setAdapter(adapter);

        }

    }
}
