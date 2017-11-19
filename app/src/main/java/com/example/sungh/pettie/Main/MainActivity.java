package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sungh.pettie.Adoption.AdoptionActivity;
import com.example.sungh.pettie.Fourm.ForumActivity;
import com.example.sungh.pettie.Map.MapsActivity;
import com.example.sungh.pettie.R;

public class MainActivity extends AppCompatActivity {

    private TextView bt1;
    private TextView bt2;
    private TextView bt3;

//    private int[] image = {
//            R.drawable.fourm, R.drawable.adoption, R.drawable.fourm,
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         bt1 = (TextView)findViewById(R.id.textView);
        bt2 = (TextView)findViewById(R.id.textView2);
        bt3 = (TextView)findViewById(R.id.textView3);

        // 設定gridview
//        List<Map<String, Object>> items = new ArrayList<>();
//        for (int i = 0; i < image.length; i++) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("image", image[i]);
////            item.put("text", imgText[i]);
//            items.add(item);
//        }
//        SimpleAdapter adapter = new SimpleAdapter(this,
//                items, R.layout.home_gridview_items, new String[]{"image", "text"},
//                new int[]{R.id.image, R.id.text});
//
//        GridView gridView = (GridView)findViewById(R.id.gridview);
//        gridView.setNumColumns(1);
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        Intent intent = new Intent();
//                        intent.setClass(MainActivity.this, ForumActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 1:
//                        Intent adoption_intent = new Intent();
//                        adoption_intent.setClass(MainActivity.this, AdoptionActivity.class);
//                        startActivity(adoption_intent);
//                        break;
//                    case 2:
//                        Intent map_intent = new Intent();
//                        map_intent.setClass(MainActivity.this, MapsActivity.class);
//                        startActivity(map_intent);
//                        break;
//
//                }
//            }
//        });
//
//
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void goFourm(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ForumActivity.class);
        startActivity(intent);

    }

    public void goAdopt(View view) {
        Intent adoption_intent = new Intent();
        adoption_intent.setClass(MainActivity.this, AdoptionActivity.class);
        startActivity(adoption_intent);
    }

    public void goMap(View view) {
        Intent map_intent = new Intent();
        map_intent.setClass(MainActivity.this, MapsActivity.class);
        startActivity(map_intent);
    }
}
