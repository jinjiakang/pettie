package com.example.sungh.pettie.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sungh.pettie.Main.LoginActivity;
import com.example.sungh.pettie.R;
import com.facebook.AccessToken;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PettieACTFormActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button mButtonActSet;
    String url;
    Button mButtonWhere;
    EditText mEditActName;
    DatePicker mPicActDate;
    EditText mEditActContent;
    EditText mEditActWhere;
    String cdate;
    TimePicker mPicActTime;
    private GoogleMap mMap;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pettie_actform);
        // 判斷權限
        if (AccessToken.getCurrentAccessToken() == null) {
            Toast.makeText(PettieACTFormActivity.this, "請先登入...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(PettieACTFormActivity.this, LoginActivity.class);
            startActivity(intent);


        }
        mButtonWhere = (Button) findViewById(R.id.button_where);
        mButtonActSet = (Button) findViewById(R.id.activity_set);
        mEditActName = (EditText) findViewById(R.id.activity_name);
        mEditActWhere = (EditText) findViewById(R.id.activity_where);
        mPicActDate = (DatePicker) findViewById(R.id.activity_date);
        mPicActTime = (TimePicker) findViewById(R.id.activity_time);
        mEditActContent = (EditText) findViewById(R.id.activity_content);

        url = "http://140.131.114.167/act_ins.php";

        mButtonWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsDialog( mEditActWhere.getText().toString());
            }
        }
        );


        mButtonActSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mEditActName.getText().toString())) {
                    mEditActName.setError("請輸入名稱");
                    return;
                }else if(TextUtils.isEmpty(mEditActWhere.getText().toString())){
                    mEditActWhere.setError("請輸入地點");
                    return;
                }
                PostPage();
                Intent pettie_form_act_intent = new Intent();
                pettie_form_act_intent.setClass(PettieACTFormActivity.this, PettieACTActivity.class);
                startActivity(pettie_form_act_intent);
            }
        });
    }


    private void openOptionsDialog(String mWhere) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View content = inflater.inflate(R.layout.activity_maps, null);
        Button btnAdd1 = (Button) content.findViewById(R.id.button);
        final EditText et = (EditText) content.findViewById(R.id.editText);
        et.setText(mWhere);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    goLocation(et);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // btnAdd1 has been clicked

            }

        });
        dialog.setView(content);
        dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                // 當使用者按下確定鈕後所執行的動作
                // 這邊先刪除Fragment 的 Googlemap 因為它是獨立id 不能重複
                FragmentTransaction fm = getSupportFragmentManager()
                        .beginTransaction();
                fm.remove(getSupportFragmentManager()
                        .findFragmentByTag("FragmentMap"));
                fm.commit();
            }
        });

        dialog.show();

    }



    private void PostPage() {

        final OkHttpClient client = new OkHttpClient();
        int year = mPicActDate.getYear();
        int month = mPicActDate.getMonth()+1;
        int day = mPicActDate.getDayOfMonth();
        int hour =  mPicActTime.getHour();
        int min = mPicActTime.getMinute();
        cdate = year + "/" + month + "/" + day + " " + hour +":"+ min +":"+00;

        String STlat = String.valueOf(lat);
        String STlng = String.valueOf(lng);

        Log.d("MAPXY: ",STlat+","+STlng);
        Log.d("insertDate",cdate);
        final RequestBody formBody = new FormBody.Builder()
                .add("ActName", mEditActName.getText().toString())
                .add("ActLocation", mEditActWhere.getText().toString())
                .add("ActDate", cdate)
                .add("ActContent", mEditActContent.getText().toString())
                .add("MapX",STlat)
                .add("MapY",STlng)
                .add("UserAccount", AccessToken.getCurrentAccessToken().getUserId())
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("testOKhttp","POST輸出: " + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


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
    public void goLocation(EditText et) throws IOException {


        String location = et.getText().toString();
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);

        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();

         lat = address.getLatitude();
         lng = address.getLongitude();

        goToLocationZoom(lat, lng, 15);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(location));

        // setMarker(locality, lat, lng);

    }
}
