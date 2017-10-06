package com.example.sungh.pettie.Adoption;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.sungh.pettie.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdoptionActivity extends AppCompatActivity {
    ArrayList<String> image_list;
    ArrayList<ImageItem> item ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);

        image_list = new ArrayList<>();
        item = new ArrayList<>();
        GridView adoption_gridview = (GridView)findViewById(R.id.Adoption_GV);

        RunWork runWork = new RunWork();
        runWork.start();
        try{
            runWork.join();
        }catch (InterruptedException e){
            return ;
        }

        GridViewAdapter gridview_adapter = new GridViewAdapter(this, R.layout.adoption_gridview_items, item);
        adoption_gridview.setNumColumns(3);
        adoption_gridview.setAdapter(gridview_adapter);
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

                for(int i = 0; i < 5; i++){
                    String image_url = image_list.get(i);
                    Log.d("image_url",image_url);
                    Bitmap bitmap = getBitmapFromURL(image_url);
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


    public static Bitmap getBitmapFromURL(String src){
        try {
            URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            Bitmap bitmap_from_url = BitmapFactory.decodeStream(input);
            return bitmap_from_url;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
