package com.app.feut.feut;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private String title[] = {"Thuis", "Boodschappen", "Chat", "Instellingen"};

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : return HomeFragment.getInstance();
            case 1 : return GroceriesFragment.getInstance();
            case 2 : return ChatFragment.getInstance();
            case 3 : return SettingsFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
