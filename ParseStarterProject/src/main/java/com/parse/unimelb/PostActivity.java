package com.parse.unimelb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap currentBitmap;
    ViewPager mViewPager;
    PostTabsAdapter mTabsAdapter;

    //TODO: garbage recycle: newBitMap, currentBitmap in the edit activity
    //TODO: use api to post new photos
    //TODO: Add location information at when taking the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTabsAdapter = new PostTabsAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPager.setAdapter(mTabsAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

//        imageView = (ImageView) findViewById(R.id.imageview_post);
        Intent intent = getIntent();
        if (intent != null) {
            String filePath = intent.getStringExtra("post_img");
            currentBitmap = BitmapFactory.decodeFile(filePath);
//            imageView.setImageBitmap(currentBitmap);
        }
    }

    public class PostTabsAdapter extends FragmentStatePagerAdapter {
        public PostTabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new PostFragment();
                case 1:
                    return new BluetoothFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "POST TO KILOGRAM";
                case 1:
                    return "SEND VIA BLUETOOTH";
            }
            return null;
        }
    }
}



