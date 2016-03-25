package me.amiee.nicetab.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import me.amiee.nicetab.NiceTabLayout;
import me.amiee.nicetab.NiceTabStrip;

public class MainActivity extends AppCompatActivity implements NiceTabStrip.OnIndicatorColorChangedListener {
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toast.makeText(getApplicationContext(), "MainAct", Toast.LENGTH_SHORT).show();
        //layout資料夾activity_main.xml 裡的main_toolbar
        //下面三行若註解掉 上方bar會完全消失
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.showOverflowMenu();


        //set for navigation drawer menu click responed
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //menuItem.getTitle()
                int id= menuItem.getItemId();

                if(id==R.id.all){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MyItemActivity.class);
                    startActivity(intent);
                }
                else if(id==R.id.my){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MyItemActivity.class);
                    startActivity(intent);
                }
                else if(id==R.id.setting){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MyItemActivity.class);
                    startActivity(intent);
                }


                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
        //drawer

        //hamburger bar icon
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout, mToolbar, R.string.open , R.string.close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super .onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super .onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //hamburger bar icon

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fl, DemoFragment.newInstance());
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github:
                Uri uri = Uri.parse(getString(R.string.github_url));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onIndicatorColorChanged(NiceTabLayout tabLayout, int color) {
//        tabLayout.setBackgroundColor(color);
    }
}
