package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.sungh.pettie.R;

/**
 * Created by Pettie on 2017/5/24.
 */

public class PersonalActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information);





    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
//  先回首頁
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        return true;

    }


}
