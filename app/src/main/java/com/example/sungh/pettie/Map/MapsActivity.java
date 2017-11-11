package com.example.sungh.pettie.Map;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sungh.pettie.Adoption.Adoption;
import com.example.sungh.pettie.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<HashMap<String, String>> mAndroidMapList;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new RunWrok().start();
    }

        class RunWrok extends Thread {
        //設置寵物的位置和活動位置點
        String path_json1 = "http://data.coa.gov.tw/Service/OpenData/AnimalOpenData.aspx?$top=50";// 先抓50筆
            String path_json2 ="http://140.131.114.167/act_qry.php";
        String result_json1 = null;
            String result_json2 = null;
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
                MyJsonAry[] ACTacts = gson.fromJson(result_json2, MyJsonAry[].class);
                Adoption[] adoptions = gson.fromJson(result_json1,Adoption[].class);
                    // 插所有活動點
                for (MyJsonAry act : ACTacts) {

                    double valueX = Double.parseDouble(act.getMapX());
                    double valueY = Double.parseDouble(act.getMapY());
                    mMap.addMarker(new MarkerOptions()

                            .position(new LatLng(valueX,valueY))
                            .title( act.getActLocation())
                            .snippet(act.getActName()));

                }

                for (Adoption ado : adoptions){

                    String location = ado.getShelter_address();

                    try {
                        goLo(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        @Override
        public void run() {
            try {
                //1.抓資料
                result_json1 = run(path_json1);
                result_json2 = run(path_json2);
                Log.d("json1", result_json1);
                runOnUiThread(task);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
}

    private void goLo(String location) throws IOException {
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Log.d("location_eric_list", location+" "+ list );
        if (list.isEmpty()){
            return;
        }else {
            Address address = list.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            Log.d("location_eric", location + lat + lng);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(location));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //開啟定位權限搜尋現在位置
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }




    private void goToLocationZoom(double v, double v1, int i) {
        LatLng ll = new LatLng(v, v1);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, i);
        mMap.moveCamera(update);
    }


    // goLocation() 用來搜尋所輸入的地圖內容位置
    public void goLocation(View view) throws IOException {

        EditText et = (EditText) findViewById(R.id.editText);
        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);

        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();

        double lat = address.getLatitude();
        double lng = address.getLongitude();

        goToLocationZoom(lat, lng, 15);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(location));


    }
}
