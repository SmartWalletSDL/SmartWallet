package com.example.smartwallet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smartwallet.userUI.activity;
import com.example.smartwallet.userUI.history;
import com.example.smartwallet.userUI.users;

public class PagerController extends FragmentPagerAdapter {
    int tabcount;

    public PagerController(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new users();
            case 1:
                return new history();
            case 2:
                return new activity();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
