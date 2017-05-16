package com.example.sungh.pettie.Main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sungh.pettie.Add.AddActivity;
import com.example.sungh.pettie.Home.HomeFragment;
import com.example.sungh.pettie.Message.MessageFragment;
import com.example.sungh.pettie.Nearby.NearbyFragment;
import com.example.sungh.pettie.News.NewsFragment;
import com.example.sungh.pettie.R;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        // 做切換layout 使用 navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener

                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = HomeFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = NewsFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, AddActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.action_item4:
                                selectedFragment = MessageFragment.newInstance();
                                break;
                            case R.id.action_item5:
                                selectedFragment = NearbyFragment.newInstance();
                                break;
                        }
                            // 判斷不會是空值
                        if (selectedFragment != null) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, selectedFragment);
                            transaction.commit();
                        }
                        return true;
                    }

                });


        // 預設 navigation view fragment_home
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
        //  實作 BottomNavigationViewHelper 新增helper (BottomNavigationViewHelper) 使它不會因為超過三個改變它的大小
        BottomNavigationView bottomNavigationFixViews = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 點選有鍵盤的呼叫並覆蓋
    }

    // 使用array方式存取menu的值
    private void addDrawerItems() {
        String[] osArray = { "收藏", "綜合", "狗", "貓", "其他" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        // 之後可以做新增連結
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show(); // 暫時先用toast做看看
            }
        });
    }

    //使用拖拉的部分
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override // 將畫面的 menu 呼叫出來圖案會出來
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    // 將在menu_main做的item 呼叫出來
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        getMenuInflater().inflate(R.menu.menu_main, menu);
 
//        MenuItem menuSearchItem = menu.findItem(R.id.activity_main);
//        SearchView searchView = (SearchView) menuSearchItem.getActionView();
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(true);

        return true;
    }
    // 使item 裡menu部分能夠點選將  mDrawerToggle 呼叫出來
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);


    }



}

