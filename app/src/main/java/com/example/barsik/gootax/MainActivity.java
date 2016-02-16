package com.example.barsik.gootax;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.example.barsik.gootax.adapter.ViewPagerAdapter;

public class MainActivity extends SherlockFragmentActivity


{
    ActionBar myActionBar;
    FragmentManager fragmentManager;
    ViewPager myPager;
    ActionBar.Tab tabs;
    Button lastButton;
    double fromLat;
    double fromLng;
    double toLat;
    double toLng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        lastButton = (Button) findViewById(R.id.routeBtn);
        /**Initialize ActionBar and  Tabs*/

        myActionBar = getSupportActionBar();
        myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        /**Initialize ViewPager*/

        myPager = (ViewPager) findViewById(R.id.myPager);

        /**Initialize FragmentManager*/

        fragmentManager = getSupportFragmentManager();

        /**Create listener changes page and initialize in ViewPager*/

        ViewPager.SimpleOnPageChangeListener myVPListener = new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
                myActionBar.setSelectedNavigationItem(position);
            }
        };
        myPager.setOnPageChangeListener(myVPListener);

        /**Create new object ViewPagerAdapter*/

        ViewPagerAdapter myVPAdapter = new ViewPagerAdapter(fragmentManager);


        /**Set Adapter for ViewPager*/

        myPager.setAdapter(myVPAdapter);

        /**Create listener tabs and choice tabs*/

        ActionBar.TabListener myTabListener = new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected (ActionBar.Tab myTab, FragmentTransaction ft)
            {
                myPager.setCurrentItem(myTab.getPosition());
            }

            @Override
            public void onTabUnselected (ActionBar.Tab myTab, FragmentTransaction ft)
            {

            }
            @Override
            public void onTabReselected(ActionBar.Tab myTab, FragmentTransaction ft)
            {

            }
        };

        /**Create first tab*/

        tabs = myActionBar.newTab().setText("Откуда").setTabListener(myTabListener);
        myActionBar.addTab(tabs);

        /**Create second tab*/

        tabs = myActionBar.newTab().setText("Куда").setTabListener(myTabListener);
        myActionBar.addTab(tabs);
    }

    /**Put date in the final/last page*/
    public void onClickOk(View v)
    {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("from_lat", fromLat);
        intent.putExtra("from_lng", fromLng);
        intent.putExtra("to_lat", toLat);
        intent.putExtra("to_lng", toLng);
        startActivity(intent);
    }

    public void setFromLat(double lat)
    {
        fromLat = lat;
    }

    public void setFromLng(double lng)
    {
        fromLng = lng;
    }
    public void setToLat(double lat)
    {
        toLat = lat;
    }

    public void setToLng(double lng)
    {
        toLng = lng;
    }
}