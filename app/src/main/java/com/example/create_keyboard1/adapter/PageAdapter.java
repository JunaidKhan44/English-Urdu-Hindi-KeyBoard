package com.example.create_keyboard1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.create_keyboard1.fragments.FlagsFrag;
import com.example.create_keyboard1.fragments.GradientFrag;
import com.example.create_keyboard1.fragments.ImageFrag;
import com.example.create_keyboard1.fragments.SportsFrag;

public class PageAdapter extends FragmentPagerAdapter {


    private int numOfTabs;

    public PageAdapter(@NonNull FragmentManager fm, int behavior, int numOfTabs) {
        super(fm, behavior);
        this.numOfTabs = numOfTabs;
    }

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GradientFrag();
            case 1:
                return new ImageFrag();
            case 2:
                return new SportsFrag();
            case 3:
                return new FlagsFrag();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}