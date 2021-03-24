package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.create_keyboard1.Adapter.PageAdapter;
import com.example.create_keyboard1.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Background extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabgradient;
    TabItem tabimage;
    TabItem tabsport;
    TabItem tabflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        getSupportActionBar().hide();



        tabLayout = findViewById(R.id.tablayout);
        tabgradient = findViewById(R.id.gradient);
        tabimage = findViewById(R.id.imagef);
        tabsport = findViewById(R.id.sport);
        tabflag = findViewById(R.id.flags);
        viewPager = findViewById(R.id.viewPager);


        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {


                } else if (tab.getPosition() == 2) {

                }
                else {


                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
        });


    }
}