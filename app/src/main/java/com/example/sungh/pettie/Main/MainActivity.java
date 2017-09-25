package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.sungh.pettie.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int[] image = {
            R.drawable.totoro, R.drawable.totoro, R.drawable.totoro,
            R.drawable.totoro
    };
    private String[] imgText = {
            "論壇", "領養資訊", "寵物地圖", "其它"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button HomeFragBtn = (Button)findViewById(R.id.HomeFragBT);
//        Button AdoptionBtn = (Button)findViewById(R.id.AdoptionBT);
//        Button PetmapBtn = (Button)findViewById(R.id.PetmapBT);

        // 設定gridview
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", image[i]);
            item.put("text", imgText[i]);
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,
                items, R.layout.adoption_gridview_items, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});

        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setNumColumns(2);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ForumActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent adoption_intent = new Intent();
                        adoption_intent.setClass(MainActivity.this, AdoptionActivity.class);
                        startActivity(adoption_intent);
                        break;
                    case 2:
                        break;

                }
            }
        });

//        HomeFragBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this  , ForumActivity.class);
//                startActivity(intent);
//
//            }
//
//        });
//
//        AdoptionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, AdoptionActivity.class);
//                startActivity(intent);
//            }
//
//        });
//
//        PetmapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 之後做連結 會分開
//                Intent intent = new Intent();
////                intent.setClass(MainActivity.this  ,PetmapFragment.class);
//                startActivity(intent);
//            }
//
//        });



    }




}
