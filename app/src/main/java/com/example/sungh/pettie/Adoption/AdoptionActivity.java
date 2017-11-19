package com.example.sungh.pettie.Adoption;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sungh.pettie.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdoptionActivity extends AppCompatActivity {
    ArrayList<String> image_list, place_list, sex_list, shelter_list;
    CatLoadingView mView;
    GridView adoption_gridview;
    private AlertDialog.Builder builder;
    private final List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);

        image_list = new ArrayList<>();
        place_list = new ArrayList<>();
        sex_list = new ArrayList<>();
        shelter_list = new ArrayList<>();
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
//                showCustomViewDialog(view);
                openOptionsDialog(position);
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



//    public boolean checkUri(String uri){
//
//        try {
//            URL url = new URL(uri);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.connect();
//            conn.setConnectTimeout(300);
//            InputStream input = conn.getInputStream();
//            return true;
//
//        } catch (IOException e) {
//            return false;
//        }
//    }

    public class MyRunWork extends AsyncTask<String, Integer, Boolean>{

        String path_json = "http://data.coa.gov.tw/Service/OpenData/AnimalOpenData.aspx?$top=300";
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
//                    image_list.add(adoption.getAlbum_file());
//                    place_list.add(adoption.getAnimal_place());
//                    shelter_list.add(adoption.getShelter_name());
//
//                    switch (adoption.getAnimal_sex()){
//                        case "F":
//                            sex_list.add("母");
//                            break;
//                        case "M":
//                            sex_list.add("公");
//                            break;
//
//                    }


                        HashMap<String, String> map = new HashMap<>();
                        map.put("AnimalPlace", adoption.getAnimal_place());
                        map.put("AnimalImg", adoption.getAlbum_file());
                        map.put("AnimalID", adoption.getAnimal_id());
                        map.put("AnimalKind", adoption.getAnimal_kind());
                        switch (adoption.getAnimal_sex()){
                            case "F":
                                map.put("AnimalSex", "母");
                                break;
                            case "M":
                                map.put("AnimalSex", "公");
                                break;
                            case "N":
                                map.put("AnimalSex", "未輸入");
                                break;
                        }

                        switch (adoption.getAnimal_bodytype()){
                            case "MINI":
                                map.put("AnimalBodyType", "迷你");
                                break;
                            case "SMALL":
                                map.put("AnimalBodyType", "小型");
                                break;
                            case "MEDIUM":
                                map.put("AnimalBodyType", "中型");
                                break;
                            case "BIG":
                                map.put("AnimalBodyType", "大型");
                                break;
                        }

                        map.put("AnimalColour", adoption.getAnimal_colour());

                        switch (adoption.getAnimal_age()){
                            case "CHILD":
                                map.put("AnimalAge", "幼年");
                                break;
                            case "ADULT":
                                map.put("AnimalAge", "成年");
                                break;
                        }

                        switch (adoption.getAnimal_sterilization()){
                            case "T":
                                map.put("AnimalSterilization", "是");
                                break;
                            case "F":
                                map.put("AnimalSterilization", "否");
                                break;
                            case "N":
                                map.put("AnimalSterilization", "未輸入");
                                break;
                        }

                        switch (adoption.getAnimal_bacterin()){
                            case "T":
                                map.put("AnimalBacterin", "是");
                                break;
                            case "F":
                                map.put("AnimalBacterin", "否");
                                break;
                            case "N":
                                map.put("AnimalBacterin", "未輸入");
                                break;
                        }

                        map.put("AnimalFoundPlace", adoption.getAnimal_foundplace());

                        switch (adoption.getAnimal_status()){
                            case "NONE":
                                map.put("AnimalStatus", "未公告");
                                break;
                            case "OPEN":
                                map.put("AnimalStatus", "開放認養");
                                break;
                            case "ADOPTED":
                                map.put("AnimalStatus", "已認養");
                                break;
                            case "OTHER":
                                map.put("AnimalStatus", "其他");
                                break;
                            case "DEAD":
                                map.put("AnimalStatus", "死亡");
                                break;
                        }


                        map.put("AnimalOpenDate", adoption.getAnimal_opendate());
                        map.put("AnimalUpdate", adoption.getAnimal_update());
                        map.put("AnimalShelterName", adoption.getShelter_name());
                        map.put("AnimalShelterAddress", adoption.getShelter_address());
                        map.put("AnimalShelterTel", adoption.getShelter_tel());


                        mAndroidMapList.add(map);







                    Log.d("img_list", adoption.getAlbum_file());
                    Log.d("getShelter_name", adoption.getShelter_name());

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
            adoption_gridview.setAdapter(new ImageListAdapter(AdoptionActivity.this, mAndroidMapList));
            mView.dismiss();

            super.onPostExecute(aBoolean);
        }
    }

    public class ImageListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<HashMap<String, String>> MyDatas = new ArrayList<>();

        private ArrayList<String> imageUrls, animal_place, animal_sex, animal_shelter;

        public ImageListAdapter(Context context, List<HashMap<String, String>> data) {
            this.context = context;
            this.MyDatas = data;

            inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return MyDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return MyDatas.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.adoption_gridview_items, parent, false);
                holder = new ViewHolder();
                holder.textView_info = (TextView) row.findViewById(R.id.adoption_info_txv);
                holder.textView2 = (TextView) row.findViewById(R.id.adoption_info_txv2);
                holder.textView3 = (TextView) row.findViewById(R.id.adoption_info_txv3);
                holder.image = (ImageView) row.findViewById(R.id.adoption_imgview);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }


            holder.textView_info.setText(MyDatas.get(position).get("AnimalPlace"));
            holder.textView2.setText(MyDatas.get(position).get("AnimalSex"));
            holder.textView3.setText(MyDatas.get(position).get("AnimalShelterName"));

            Glide
                .with(context)
                .load(MyDatas.get(position).get("AnimalImg"))
                .error(R.drawable.nopic)
                .into(holder.image);
            return row;
        }

        class ViewHolder {
            TextView textView_info;
            TextView textView2;
            TextView textView3;
            ImageView image;
        }
    }

    private void openOptionsDialog(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        Context context = AdoptionActivity.this;

        View content =  inflater.inflate(R.layout.dialog_adoption, null);
        ImageView imageView = (ImageView) content.findViewById(R.id.animal_photo);
        TextView animal_id_txv = (TextView) content.findViewById(R.id.animal_id_txv);
        TextView animal_kind_txv = (TextView) content.findViewById(R.id.animal_kind_txv);
        TextView animal_sex_txv = (TextView) content.findViewById(R.id.animal_sex_txv);
        TextView animal_bodytype_txv = (TextView) content.findViewById(R.id.animal_bodytype_txv);
        TextView animal_colour_txv = (TextView) content.findViewById(R.id.animal_colour_txv);
        TextView animal_age_txv = (TextView) content.findViewById(R.id.animal_age_txv);
        TextView animal_sterilization_txv = (TextView) content.findViewById(R.id.animal_sterilization_txv);
        TextView animal_bacterin_txv = (TextView) content.findViewById(R.id.animal_bacterin_txv);
        TextView animal_foundplace_txv = (TextView) content.findViewById(R.id.animal_foundplace_txv);
        TextView animal_status_txv = (TextView) content.findViewById(R.id.animal_status_txv);
        TextView animal_opendate_txv = (TextView) content.findViewById(R.id.animal_opendate_txv);
        TextView animal_update_txv = (TextView) content.findViewById(R.id.animal_update_txv);
        TextView animal_shelter_name_txv = (TextView) content.findViewById(R.id.animal_shelter_name_txv);
        TextView animal_shelter_address_txv = (TextView) content.findViewById(R.id.animal_shelter_address_txv);
        TextView animal_shelter_tel_txv = (TextView) content.findViewById(R.id.animal_shelter_tel_txv);



        dialog.setView(content);
        dialog.setTitle("動物詳細資訊");
        Glide
            .with(context)
            .load(mAndroidMapList.get(position).get("AnimalImg"))
            .error(R.drawable.nopic)
            .into(imageView);
        animal_id_txv.setText("ID：" + mAndroidMapList.get(position).get("AnimalID"));
        animal_kind_txv.setText("種類：" + mAndroidMapList.get(position).get("AnimalKind"));
        animal_sex_txv.setText("性別：" + mAndroidMapList.get(position).get("AnimalSex"));
        animal_bodytype_txv.setText("體型：" + mAndroidMapList.get(position).get("AnimalBodyType"));
        animal_colour_txv.setText("毛色：" + mAndroidMapList.get(position).get("AnimalColour"));
        animal_age_txv.setText("年紀：" + mAndroidMapList.get(position).get("AnimalAge"));
        animal_sterilization_txv.setText("是否絕育：" + mAndroidMapList.get(position).get("AnimalSterilization"));
        animal_bacterin_txv.setText("是否施打狂犬病疫苗：" + mAndroidMapList.get(position).get("AnimalBacterin"));
        animal_foundplace_txv.setText("尋獲地：" + mAndroidMapList.get(position).get("AnimalFoundPlace"));
        animal_status_txv.setText("狀態：" + mAndroidMapList.get(position).get("AnimalStatus"));
        animal_opendate_txv.setText("開放認養時間：" + mAndroidMapList.get(position).get("AnimalOpenDate"));
        animal_update_txv.setText("資料更新時間：" + mAndroidMapList.get(position).get("AnimalUpdate"));
        animal_shelter_name_txv.setText("收容所名稱：" + mAndroidMapList.get(position).get("AnimalShelterName"));
        animal_shelter_address_txv.setText("收容所住址：" + mAndroidMapList.get(position).get("AnimalShelterAddress"));
        animal_shelter_tel_txv.setText("收容所電話：" + mAndroidMapList.get(position).get("AnimalShelterTel"));




        // 承接傳過來的字串，顯示在對話框之中
//        dialog.setMessage(mAndroidMapList.get(position).get("ActName"));
        // 設定 PositiveButton 也就是一般 確定 或 OK 的按鈕
        dialog.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialoginterface, int i) {
                // 當使用者按下確定鈕後所執行的動作
            }
        } );
        dialog.show();

    }

}