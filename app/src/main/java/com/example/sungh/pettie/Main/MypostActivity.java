package com.example.sungh.pettie.Main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.sungh.pettie.Fourm.PostGson;
import com.example.sungh.pettie.R;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MypostActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String URL = "http://140.131.114.167/post_qry_person.php?UserAccount=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        StringRequest request = new StringRequest(URL+ AccessToken.getCurrentAccessToken().getUserId(), new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                PostGson[] postGsons = gson.fromJson(s,PostGson[].class);
                mRecyclerView.setAdapter(new MypostActivity.MyAdapter(MypostActivity.this,postGsons));
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MypostActivity.this,"錯誤", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    public class MyAdapter extends RecyclerView.Adapter<MypostActivity.MyAdapter.ViewHolder> {
        private final Context context;
        private PostGson[] data;

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView p_content_Del;
            private EditText text_Info_p_content;
            public ImageView img_Info_p;
            public EditText text_Info_p;
            private TextView p_content_edit;

            public ViewHolder(View v) {
                super(v);
                text_Info_p = (EditText) v.findViewById(R.id.text_Info_p);
                img_Info_p = (ImageView) v.findViewById(R.id.img_Info_p);
                text_Info_p_content = (EditText) v.findViewById(R.id.text_Info_p_content);
                p_content_Del=(TextView)v.findViewById(R.id.p_content_Del);
                p_content_edit=(TextView)v.findViewById(R.id.p_content_edit);
            }
        }

        public MyAdapter(Context context, PostGson[] data) {
            this.context = context;
            this.data = data;

        }

        @Override
        public MypostActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mypost, parent, false);
            MypostActivity.MyAdapter.ViewHolder vh = new MypostActivity.MyAdapter.ViewHolder(v);
            return vh;
        }


        // RecycleView 設置畫面上的內容
        @Override
        public void onBindViewHolder(final MypostActivity.MyAdapter.ViewHolder holder, final int position) {
            final PostGson postGson = data[position];
            holder.text_Info_p.setText(postGson.getPostTitle());
            holder.text_Info_p_content.setText(postGson.getPostContent());
            Glide.with(holder.img_Info_p.getContext()).load("http://imgur.com/"+postGson.getImg_seq()+".jpg").into(holder.img_Info_p);

            holder.p_content_edit.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    final String title = holder.text_Info_p.getText().toString();
                    final String text = holder.text_Info_p_content.getText().toString();

                    if(TextUtils.isEmpty(title)) {
                        holder.text_Info_p.setError("標題沒有文字");
                        return;
                    }else if (TextUtils.isEmpty(text)){
                        holder.text_Info_p_content.setError("內容沒有文字");
                        return;
                    }

                    editContent(text , postGson.getPostNo(), title);
                }
            });

            holder.p_content_Del.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    delContent(postGson.getPostNo(),position);
                }
            });
        }

        private void delContent(final String no, int i) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle("Delete");
                // 承接傳過來的字串，顯示在對話框之中

                // 刪除確認
                dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialoginterface, int i) {

                                final OkHttpClient client = new OkHttpClient();
                                final RequestBody formBody = new FormBody.Builder()
                                        .add("UserAccount", AccessToken.getCurrentAccessToken().getUserId())
                                        .add("PostNo",no)
                                        .build();
                                Log.d("PostNo",no+""+AccessToken.getCurrentAccessToken().getUserId());
                                final Request request = new Request.Builder()
                                        .url("http://140.131.114.167/post_del.php")
                                        .post(formBody)
                                        .build();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        okhttp3.Response response = null;
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
                                Intent intent = new Intent(MypostActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                );
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }

                });
                dialog.show();
        }
        private void editContent(final String text, final String No, final String title) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("確定修改?");
            // 承接傳過來的字串，顯示在對話框之中

            // 編輯確認
            dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialoginterface, int i) {

                            final OkHttpClient client = new OkHttpClient();
                            final RequestBody formBody = new FormBody.Builder()
                                    .add("UserAccount", AccessToken.getCurrentAccessToken().getUserId())
                                    .add("PostNo",No)
                                    .add("PostTitle",title)
                                    .add("PostContent",text)
                                    .build();
                            Log.d("PostNo",No+""+AccessToken.getCurrentAccessToken().getUserId());
                            final Request request = new Request.Builder()
                                    .url("http://140.131.114.167/post_upd.php")
                                    .post(formBody)
                                    .build();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    okhttp3.Response response = null;
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
                            Intent intent = new Intent(MypostActivity.this, MypostActivity.class);
                            startActivity(intent);
                        }
                    }
            );
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                }

            });
            dialog.show();

        }
        // 文章長度
        @Override
        public int getItemCount() {
            return data.length;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MypostActivity.this,LoginActivity.class);
        startActivity(intent);
    }


}