package com.example.sungh.pettie.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sungh.pettie.Main.ForumActivity;
import com.example.sungh.pettie.Main.MainActivity;
import com.example.sungh.pettie.R;

public class HomeActivity extends AppCompatActivity {

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
                intent.setClass(HomeActivity.this  , ForumActivity.class);
                startActivity(intent);

            }

        });

        AdoptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this  , MainActivity.class);
                startActivity(intent);
            }

        });

        PetmapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 之後做連結 會分開
            }

        });



    }




}
