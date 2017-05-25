package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sungh.pettie.R;

/**
 * Created by Pettie on 2017/5/25.
 */

public class NewpersonalActivity  extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpersonal);
        // 註冊按鈕
        Button mEmailRegistInButton = (Button) findViewById(R.id.email_regist_in_button);
        mEmailRegistInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NewpersonalActivity.this, PersonalActivity.class);
                startActivity(intent);


            }
        });
        // 取消按鈕
        Button mEmailCancelButton = (Button) findViewById(R.id.email_sancel_button);
        mEmailCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NewpersonalActivity.this,MainActivity.class);
                startActivity(intent);


            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
