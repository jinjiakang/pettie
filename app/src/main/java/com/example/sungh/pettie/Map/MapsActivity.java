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

import com.example.sungh.pettie.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

                    // 插所有活動點
                for (MyJsonAry act : ACTacts) {

                    double valueX = Double.parseDouble(act.getMapX());
                    double valueY = Double.parseDouble(act.getMapY());
                    mMap.addMarker(new MarkerOptions()

                            .position(new LatLng(valueX,valueY))
                            .title( act.getActLocation())
                            .snippet(act.getActName()));

                }

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
}




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
