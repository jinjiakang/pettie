package com.example.sungh.pettie.Fourm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.sungh.pettie.Add.AddActivity;
import com.example.sungh.pettie.Map.PettieACTActivity;
import com.example.sungh.pettie.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForumActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SimpleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private final List<HashMap<String, String>> mAndroidPostList = new ArrayList<>();
    private static String URL = "http://140.131.114.167/post_qry_all.php";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // 實作toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //實作 fab
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ForumActivity.this  ,AddActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                PostGson[] postGsons = gson.fromJson(s,PostGson[].class);
                mRecyclerView.setAdapter(new MyAdapter(ForumActivity.this,postGsons));
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ForumActivity.this,"錯誤", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);



        // 判斷 fab 隱藏時機
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    fab.hide();
                } else{
                    fab.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }

        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final Context context;
        private PostGson[] data;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView text_UserName;
            public TextView text_Class;
            public ImageView img_Info;
            public TextView text_Info;
            public ViewHolder(View v) {
                super(v);
                text_Info = (TextView) v.findViewById(R.id.text_Info);
                text_UserName = (TextView) v.findViewById(R.id.text_UserName);
                text_Class = (TextView) v.findViewById(R.id.text_Class);
                img_Info = (ImageView) v.findViewById(R.id.img_Info);
            }
        }

        public MyAdapter(Context context, PostGson[] data) {
            this.context = context;
            this.data = data;
            
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // RecycleView 設置畫面上的內容
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            PostGson postGson = data[position];
            holder.text_UserName.setText(postGson.getUserAccount());
            holder.text_Info.setText(postGson.getPostContent());
            holder.text_Class.setText(postGson.getTypes());
            Glide.with(holder.img_Info.getContext()).load("http://imgur.com/"+postGson.getImg_seq()+".jpg").into(holder.img_Info);
        }
        // 文章長度
        @Override
        public int getItemCount() {
            return data.length;
        }



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fourm) {
            // Handle the camera action

        } else if (id == R.id.nav_activity) {
            Intent pettie_act_intent = new Intent();
            pettie_act_intent.setClass(ForumActivity.this, PettieACTActivity.class);
            startActivity(pettie_act_intent);

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
