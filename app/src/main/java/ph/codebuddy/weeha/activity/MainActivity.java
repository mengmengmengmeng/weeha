package ph.codebuddy.weeha.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.LinearLayout;

import ph.codebuddy.weeha.NonSwipeableViewPager;
import ph.codebuddy.weeha.R;
import ph.codebuddy.weeha.SendLocationsService;
import ph.codebuddy.weeha.adapter.MainPagerAdapter;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class MainActivity extends BaseActivity {

    LinearLayout mapLayout, trackLayout, groupLayout, settingLayout;
    AppCompatImageView map_inactive, track_inactive, group_inactive, setting_inactive,
            map_active, track_active, group_active, setting_active;
    NonSwipeableViewPager mainViewPager;
    MainPagerAdapter mainPagerAdapter;
    AppBarLayout appBarLayout;
    public static int VISIBLE = 0;
    public static int GONE = 8;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tracked_lat", "");
        editor.putString("tracked_lng", "");
        editor.putString("tracked_name", "");
        editor.apply();

        extras = getIntent().getExtras();
        checkIfTracked();

        mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
        trackLayout = (LinearLayout) findViewById(R.id.trackLayout);
        groupLayout = (LinearLayout) findViewById(R.id.groupLayout);
        settingLayout = (LinearLayout) findViewById(R.id.settingLayout);

        map_inactive = (AppCompatImageView) findViewById(R.id.map_inactive);
        map_active = (AppCompatImageView) findViewById(R.id.map_active);
        track_inactive = (AppCompatImageView) findViewById(R.id.track_inactive);
        track_active = (AppCompatImageView) findViewById(R.id.track_active);
        group_inactive = (AppCompatImageView) findViewById(R.id.group_inactive);
        group_active = (AppCompatImageView) findViewById(R.id.group_active);
        setting_inactive = (AppCompatImageView) findViewById(R.id.setting_inactive);
        setting_active = (AppCompatImageView) findViewById(R.id.setting_active);

        mainViewPager = (NonSwipeableViewPager) findViewById(R.id.main_pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        Intent i = new Intent(this, SendLocationsService.class);
        startService(i);

        setUpDisplay();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    public void checkIfTracked(){
        try {
            if(extras.getString("tracked_lat") != null){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tracked_lat", extras.getString("tracked_lat"));
                editor.putString("tracked_lng", extras.getString("tracked_lng"));
                editor.putString("tracked_name", extras.getString("tracked_name"));
                editor.apply();
            }
        } catch (NullPointerException e ) {

        }


    }

    public void goToMapAndLocate(){
        mainViewPager.setCurrentItem(0, false);
        setToolbarName(" Map");
        setPages(VISIBLE, GONE, GONE, VISIBLE, GONE, VISIBLE, GONE, VISIBLE);
        mapLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
    }

    public void setUpDisplay() {
        mainViewPager.setAdapter(mainPagerAdapter);
        mainViewPager.setOffscreenPageLimit(4);

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    setPages(VISIBLE, GONE, GONE, VISIBLE, GONE, VISIBLE, GONE, VISIBLE);
                    mapLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    setToolbarName(" Map");
                }else if(position == 1){
                    setPages(GONE, VISIBLE, VISIBLE, GONE, GONE, VISIBLE, GONE, VISIBLE);
                    trackLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    setToolbarName(" Track");
                }else if(position == 2){
                    setPages(GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE, GONE, VISIBLE);
                    groupLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    setToolbarName(" Group");
                }else if(position == 3){
                    setPages(GONE, VISIBLE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
                    settingLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    setToolbarName(" Setting");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        goToMapAndLocate();

        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewPager.setCurrentItem(0, false);
            }
        });

        trackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewPager.setCurrentItem(1, false);
            }
        });

        groupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewPager.setCurrentItem(2, false);
            }
        });

        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewPager.setCurrentItem(3, false);
            }
        });
    }

    public void setPages(int mapActive, int mapInactive, int trackActive, int trackInactive,
                         int groupActive, int groupInactive, int settingActive, int settingInactive){
        appBarLayout.setExpanded(true, true);

        map_active.setVisibility(mapActive);
        map_inactive.setVisibility(mapInactive);
        track_active.setVisibility(trackActive);
        track_inactive.setVisibility(trackInactive);
        group_active.setVisibility(groupActive);
        group_inactive.setVisibility(groupInactive);
        setting_active.setVisibility(settingActive);
        setting_inactive.setVisibility(settingInactive);

        mapLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        groupLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        trackLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        settingLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.black));
    }
}

