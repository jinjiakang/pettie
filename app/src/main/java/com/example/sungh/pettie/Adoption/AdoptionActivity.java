package com.example.sungh.pettie.Adoption;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.sungh.pettie.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdoptionActivity extends AppCompatActivity {
    ArrayList<String> image_list;
    ArrayList<ImageItem> item ;
    ImageLoader imageLoader;
    Timer timer;
    TimerTask timertask;
    private SwipeRefreshLayout laySwipe;
    private GridViewAdapter gridview_adapter;
    private GridView adoption_gridview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);

        image_list = new ArrayList<>();
        item = new ArrayList<>();
        adoption_gridview = (GridView)findViewById(R.id.Adoption_GV);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);


        RunWork runWork = new RunWork();
        runWork.start();
        try{
            runWork.join();
        }catch (InterruptedException e){
            return ;
        }


        gridview_adapter = new GridViewAdapter(this, R.layout.adoption_gridview_items, item);
        adoption_gridview.setNumColumns(2);
        adoption_gridview.setAdapter(gridview_adapter);

        adoption_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



    }




    class RunWork extends Thread {

        String path_json = "http://data.coa.gov.tw/Service/OpenData/AnimalOpenData.aspx";
        String result_json = null;

        OkHttpClient okHttpClient = new OkHttpClient();
        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        }

        @Override
        public void run() {
            try {




                result_json = run(path_json);

                Gson gson = new Gson();
                Adoption[] adoptions = gson.fromJson(result_json, Adoption[].class);

                //取出json資料裡的圖片URL　並存入陣列
                for(Adoption adoption :adoptions){
                    image_list.add(adoption.getAlbum_file());

                }


                //先取出陣列內的URL並開啟連線傳回bitmap
                //然後再將取回來的值放入map中

                for(int i = 0; i < 100; i++){
                    String image_url = image_list.get(i);
                    Log.d("image_url",image_url);
                    String image_uri_ok = checkUri(image_url);
                    Bitmap bitmap = getBitmapImage(image_uri_ok);
                    item.add(new ImageItem(bitmap,"image#" + i));
                    Log.d("bitmap",bitmap.toString());
                    Log.d("item", "item.size() = " + item.size());
                }

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public Bitmap getBitmapImage(String src){
        try {

            imageLoader = ImageLoader.getInstance();
            Bitmap bmp = imageLoader.loadImageSync(src);
            return bmp;

        }catch(Exception e){
            e.printStackTrace();

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_search);
            return bm;
        }
    }

    public String checkUri(String uri) {

        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            return uri;
        } catch (IOException e) {
            return "drawable://" + R.drawable.totoro;
        }
    }
}
