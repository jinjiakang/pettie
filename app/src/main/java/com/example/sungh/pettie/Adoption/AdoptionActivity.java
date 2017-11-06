package com.example.sungh.pettie.Adoption;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.sungh.pettie.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roger.catloadinglibrary.CatLoadingView;

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
    CatLoadingView mView;
    GridView adoption_gridview;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);

        image_list = new ArrayList<>();
        adoption_gridview = (GridView)findViewById(R.id.Adoption_GV);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");

        new MyRunWork().execute();


        adoption_gridview.setNumColumns(2);

        adoption_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCustomViewDialog(view);
            }
        });


    }

    private void showCustomViewDialog(View view){
        builder=new AlertDialog.Builder(this);

        LinearLayout loginDialog= (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_adoption,null);
        builder.setView(loginDialog);

        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

//    class RunWork extends Thread {
//
//        String path_json = "http://data.coa.gov.tw/Service/OpenData/AnimalOpenData.aspx";
//        String result_json = null;
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        String run(String url) throws IOException {
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = okHttpClient.newCall(request).execute();
//            return response.body().string();
//        }
//
//        @Override
//        public void run() {
//            try {
//
//                result_json = run(path_json);
//
//                Gson gson = new Gson();
//                Adoption[] adoptions = gson.fromJson(result_json, Adoption[].class);
//
//                //取出json資料裡的圖片URL　並存入陣列
//                for(Adoption adoption :adoptions){
//                    image_list.add(adoption.getAlbum_file());
//
//                }
//
//
//                //先取出陣列內的URL並開啟連線傳回bitmap
//                //然後再將取回來的值放入map中
//
//                for(int i = 0; i < 30; i++){
//                    String image_url = image_list.get(i);
//                    Log.d("image_url",image_url);
//                    String image_uri_ok = checkUri(image_url);
//                    Bitmap bitmap = getBitmapImage(image_uri_ok);
//                    item.add(new ImageItem(bitmap,"image#" + i));
//                    Log.d("bitmap",bitmap.toString());
//                    Log.d("item", "item.size() = " + item.size());
//                }
//
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }


//    public Bitmap getBitmapImage(String src){
//        try {
//
//            imageLoader = ImageLoader.getInstance();
//            Bitmap bmp = imageLoader.loadImageSync(src);
//            return bmp;
//
//        }catch(Exception e){
//            e.printStackTrace();
//
//            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_search);
//            return bm;
//        }
//    }
//
//    public String checkUri(String uri){
//
//        try {
//            URL url = new URL(uri);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream input = conn.getInputStream();
//            return uri;
//        } catch (IOException e) {
//            return "drawable://" + R.drawable.totoro;
//        }
//    }

    public class MyRunWork extends AsyncTask<String, Integer, Boolean>{

        String path_json = "http://data.coa.gov.tw/Service/OpenData/AnimalOpenData.aspx?$top=100";
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
        protected Boolean doInBackground(String... strings) {
            try {

                result_json = run(path_json);

                Gson gson = new Gson();
                Adoption[] adoptions = gson.fromJson(result_json, Adoption[].class);

                //取出url

                //取出json資料裡的圖片URL　並存入陣列
                for(Adoption adoption :adoptions){
                    image_list.add(adoption.getAlbum_file());
                    Log.d("img_list", adoption.getAlbum_file());

                }


                //先取出陣列內的URL並開啟連線傳回bitmap
                //然後再將取回來的值放入map中

//                for(int i = 0; i < 30; i++){
//                    String image_url = image_list.get(i);
//                    Log.d("image_url",image_url);
//
////                    String image_uri_ok = checkUri(image_url);
////                    Bitmap bitmap = getBitmapImage(image_uri_ok);
//                    item.add(new ImageItem(bitmap,"image#" + i));
//                    Log.d("bitmap",bitmap.toString());
//                    Log.d("item", "item.size() = " + item.size());
//                }

            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            adoption_gridview.setAdapter(new ImageListAdapter(AdoptionActivity.this, image_list));
            mView.dismiss();

            super.onPostExecute(aBoolean);
        }
    }

    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private ArrayList<String> imageUrls;

        public ImageListAdapter(Context context, ArrayList<String> imageUrls) {
            super(context, R.layout.adoption_gridview_items, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.adoption_gridview_items, parent, false);
            }

            Glide
                .with(context)
                .load(imageUrls.get(position))
                .error(R.drawable.totoro)
                .into((ImageView) convertView);

            return convertView;
        }
    }







//    public Bitmap getBitmapFromURL(String src){
//        try {
//            URL url = new URL(src);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream input = conn.getInputStream();
//            Bitmap bitmap_from_url = BitmapFactory.decodeStream(input);
//            return bitmap_from_url;
//
//
//        } catch (IOException e) {
//            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_search);
//            return bm;
//        }
//    }


    // test

//    private Bitmap getBitmap(String src) {
//        try {
//
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream inputstream = connection.getInputStream();
//
//            //第一次 decode,只取得圖片長寬,還未載入記憶體
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(inputstream, null, opts);
//
//            inputstream.close();
//
//
//            Log.d("log1","kk");
//
//            //取得動態計算縮圖長寬的 SampleSize (2的平方最佳)
//            int sampleSize = 4;
//
//            Log.d("log2", sampleSize +"");
//
//            //第二次 decode,指定取樣數後,產生縮圖
//            inputstream = connection.getInputStream();
//            opts = new BitmapFactory.Options();
//            opts.inSampleSize = sampleSize;
//
//            Log.d("log3", sampleSize + "");
//
//            Bitmap bmp = BitmapFactory.decodeStream(inputstream, null, opts);
//
//            Log.d("bmp", bmp.toString());
//            inputstream.close();
//
//            return bmp;
//
//        } catch (Exception e) {
//            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_search);
//            return bm;
//        }
//    }
//
//    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
//
//        int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
//        int roundedSize;
//
//        if (initialSize <= 8) {
//            roundedSize = 1;
//            while (roundedSize < initialSize) {
//                roundedSize <<= 1;
//            }
//        } else {
//            roundedSize = (initialSize + 7) / 8 * 8;
//        }
//
//        return roundedSize;
//
//}
//
//    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
//
//        double w = options.outWidth;
//        double h = options.outHeight;
//
//        int lowerBound = (maxNumOfPixels == -1) ? 1 :
//                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
//        int upperBound = (minSideLength == -1) ? 128 :
//                (int) Math.min(Math.floor(w / minSideLength),
//                        Math.floor(h / minSideLength));
//
//        if (upperBound < lowerBound) {
//            // return the larger one when there is no overlapping zone.
//            return lowerBound;
//        }
//
//        if ((maxNumOfPixels == -1) &&
//                (minSideLength == -1)) {
//            return 1;
//        } else if (minSideLength == -1) {
//            return lowerBound;
//        } else {
//            return upperBound;
//        }
//    }
}