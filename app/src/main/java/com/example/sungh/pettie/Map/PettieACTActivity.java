package com.example.sungh.pettie.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.sungh.pettie.Fourm.ForumActivity;
import com.example.sungh.pettie.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PettieACTActivity extends ForumActivity implements AdapterView.OnItemClickListener {

        private TextView textArray;
        private  ListView mListView;
        private final List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();
     private SimpleAdapter adapter;

    @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pettie_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mListView = (ListView) findViewById(R.id.textArray);
        mListView.setOnItemClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pettie_form_act_intent = new Intent();
                pettie_form_act_intent.setClass(PettieACTActivity.this, PettieACTFormActivity.class);
                startActivity(pettie_form_act_intent);
            }
        });


        new RunWrok().start();


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// 設定每個item 的按鈕
        openOptionsDialog(position);
//        Toast.makeText(this, mAndroidMapList.get(position).get("ActName"),Toast.LENGTH_SHORT).show();
    }

    private void openOptionsDialog(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View content =  inflater.inflate(R.layout.dialog_activity, null);
        TextView DiaActName = (TextView) content.findViewById(R.id.DiaActName);
        TextView DiaActLoc = (TextView) content.findViewById(R.id.DiaActLoc);
        TextView DiaActDate = (TextView) content.findViewById(R.id.DiaActDate);
        TextView DiaActCon = (TextView) content.findViewById(R.id.DiaActCon);

        dialog.setView(content);
        dialog.setTitle("活動內容");
        DiaActName.setText(mAndroidMapList.get(position).get("ActName"));
        DiaActLoc.setText(mAndroidMapList.get(position).get("ActWhere")) ;
        DiaActDate.setText(mAndroidMapList.get(position).get("ActDate"));
        DiaActCon.setText(mAndroidMapList.get(position).get("ActContent"));



        // 承接傳過來的字串，顯示在對話框之中
//        dialog.setMessage(mAndroidMapList.get(position).get("ActName"));
        // 設定 PositiveButton 也就是一般 確定 或 OK 的按鈕
        dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialoginterface, int i) {
                // 當使用者按下確定鈕後所執行的動作
            }
        } );
        dialog.setNegativeButton("刪除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                // 當使用者按下刪除鈕後所執行的動作
                openDeleteDialog("確定嗎?",mAndroidMapList.get(position).get("ActNo"), position);

            }


        });
        dialog.show();

    }

    // 刪除的動作
    private void openDeleteDialog(String sure, final String no, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete");
        // 承接傳過來的字串，顯示在對話框之中
        dialog.setMessage(sure);
        // 刪除確認
        dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialoginterface, int i) {
                // 當使用者按下確定鈕後所執行的動作
                Log.d("REMOVEDATA","a"+no);
                Log.d("Listremoved",""+mAndroidMapList.size());
                mAndroidMapList.remove(position);
                Log.d("Listremoved",""+mAndroidMapList.size());
                adapter.notifyDataSetChanged();

                final OkHttpClient client = new OkHttpClient();
                final RequestBody formBody = new FormBody.Builder()
                        .add("ActNo",no)
                        .build();

                final Request request = new Request.Builder()
                        .url("http://140.131.114.167/act_del.php")
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
                        }
                    }
                }).start();


            }
        }

        );
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                // 當使用者按下刪除鈕後所執行的動作

            }

        });
        dialog.show();

    }


    class RunWrok extends Thread {

            // 可以連看看人怎麼包的 Json
            String path_json = "http://140.131.114.167/act_qry.php";
            String result_json = null;

            /* This program downloads a URL and print its contents as a string.*/
            OkHttpClient client = new OkHttpClient();

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
                    MyJsonAry[] ACTacts = gson.fromJson(result_json, MyJsonAry[].class);
//                    StringBuilder sb = new StringBuilder();


                    for (MyJsonAry act : ACTacts) {

                        HashMap<String, String> map = new HashMap<>();
                        map.put("ActNo", act.getActNo());
                        map.put("ActName", act.getActName());
                        map.put("ActWhere", act.getActLocation());
                        map.put("ActDate", act.getActDate());
                        map.put("ActContent",  act.getActContent());
                        map.put("MapX",  act.getMapX());
                        map.put("MapY", act.getMapY());

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

                adapter = new SimpleAdapter(PettieACTActivity.this, mAndroidMapList, R.layout.list_item,
                        new String[]{"ActName", "ActWhere", "ActDate"},
                        new int[]{R.id.ActName, R.id.ActWhere, R.id.ActDate});

                mListView.setAdapter(adapter);

            }
        }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fourm) {
            Intent pettie_act_intent = new Intent();
            pettie_act_intent.setClass(PettieACTActivity.this, ForumActivity.class);
            startActivity(pettie_act_intent);

        } else if (id == R.id.nav_activity) {


        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
