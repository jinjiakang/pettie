package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sungh.pettie.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button HomeFragBtn = (Button)findViewById(R.id.HomeFragBT);
        Button AdoptionBtn = (Button)findViewById(R.id.AdoptionBT);
        Button PetmapBtn = (Button)findViewById(R.id.PetmapBT);
        HomeFragBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this  , ForumActivity.class);
                startActivity(intent);

            }

        });

        AdoptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setClass(MainActivity.this  ,AdoptionViewpager.class);
                startActivity(intent);
            }

        });

        PetmapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 之後做連結 會分開
                Intent intent = new Intent();
//                intent.setClass(MainActivity.this  ,PetmapFragment.class);
                startActivity(intent);
            }

        });



    }




}
