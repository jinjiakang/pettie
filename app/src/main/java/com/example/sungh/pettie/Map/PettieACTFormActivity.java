package com.example.sungh.pettie.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.sungh.pettie.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PettieACTFormActivity extends AppCompatActivity {

    Button mButtonActSet;
    String url;
    Button mButtonWhere;
    EditText mEditActName;
    DatePicker mPicActDate;
    EditText mEditActContent;
    EditText mEditActWhere;
    String cdate;
    TimePicker mPicActTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pettie_actform);
        mButtonWhere = (Button) findViewById(R.id.button_where);
        mButtonActSet = (Button) findViewById(R.id.activity_set);
        mEditActName = (EditText) findViewById(R.id.activity_name);
        mEditActWhere = (EditText) findViewById(R.id.activity_where);
        mPicActDate = (DatePicker) findViewById(R.id.activity_date);
        mPicActTime = (TimePicker) findViewById(R.id.activity_time);
        mEditActContent = (EditText) findViewById(R.id.activity_content);

        url = "http://140.131.114.167/act_ins.php";

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

    private void PostPage() {

        final OkHttpClient client = new OkHttpClient();
        int year = mPicActDate.getYear();
        int month = mPicActDate.getMonth()+1;
        int day = mPicActDate.getDayOfMonth();
        int hour =  mPicActTime.getHour();
        int min = mPicActTime.getMinute();
        cdate = year + "/" + month + "/" + day + " " + hour +":"+ min +":"+00;
        Log.d("insertDate",cdate);
        final RequestBody formBody = new FormBody.Builder()
                .add("ActName", mEditActName.getText().toString())
                .add("ActLocation", mEditActWhere.getText().toString())
                .add("ActDate", cdate)
                .add("ActContent", mEditActContent.getText().toString())
                .add("MapX","123")
                .add("MapY","123")
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


}
