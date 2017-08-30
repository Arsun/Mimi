package org.scau.mimi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.scau.mimi.fragment.PictureFragment;

import java.util.List;

/**
 * Created by 10313 on 2017/8/29.
 */

public class PictureFragmentPagerAdapter extends FragmentPagerAdapter{

    private String[] mPicUrls;

    public PictureFragmentPagerAdapter(FragmentManager fm, String[] picUrls) {
        super(fm);
        mPicUrls = picUrls;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureFragment.newInstance(mPicUrls[position]);
    }

    @Override
    public int getCount() {
        return mPicUrls.length;
    }
}
