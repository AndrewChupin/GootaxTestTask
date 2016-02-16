package com.example.barsik.gootax.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.barsik.gootax.FromTab;
import com.example.barsik.gootax.ToTab;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_NUMB =2;

    public ViewPagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int arg0)
    {
        switch (arg0)
        {
            case (0):
            {
                FromTab fromTab = new FromTab();
                return fromTab;
            }
            case (1):
            {
                ToTab toTab = new ToTab();
                return  toTab;
            }
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return PAGE_NUMB;
    }
}
