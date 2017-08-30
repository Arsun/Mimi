package org.scau.mimi.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.scau.mimi.R;
import org.scau.mimi.adapter.PictureFragmentPagerAdapter;

import static org.scau.mimi.R.id.vp;


/**
 * Created by 10313 on 2017/8/29.
 */

public class PictureActivity extends AppCompatActivity {

    //Constants
    private static final String SHARED_ELEMENT_PHOTO = "shared_element_photo";
    //extra
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_THUMBNAIL_SIZE = "extra_thumbnail_size";
    private static final String EXTRA_POSITION = "extra_position";

    //Varibles
    private String[] mPicUrls;
    private int[] mSize;
    private int mPosition;

    //Views
    private ViewPager vpPicturePager;
    private ImageView ivThumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        loadData();
        initVariables();
        initViews();

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }

    private void initVariables() {
        mPicUrls = getIntent().getStringArrayExtra(EXTRA_URL);
        mSize = getIntent().getIntArrayExtra(EXTRA_THUMBNAIL_SIZE);
        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
    }

    private void initViews() {
        vpPicturePager = (ViewPager) findViewById(R.id.vp_picture);
        ivThumbnail = (ImageView) findViewById(R.id.iv_picture_thumbnail);

        vpPicturePager.setAdapter(new PictureFragmentPagerAdapter(getSupportFragmentManager(), mPicUrls));

        if (addTransitionListener()) {
            displayThumbnail();
            ViewCompat.setTransitionName(ivThumbnail, SHARED_ELEMENT_PHOTO);
        } else {
            vpPicturePager.setCurrentItem(mPosition);
            ivThumbnail.setVisibility(View.GONE);
            vpPicturePager.setVisibility(View.VISIBLE);
        }

    }

    private void displayThumbnail() {
        Glide.with(this)
                .load(mPicUrls[mPosition])
                .override(mSize[0], mSize[1])
                .priority(Priority.IMMEDIATE)
                .dontAnimate()
                .into(ivThumbnail);
    }

    private void loadData() {

    }

    public static void actionStart(Activity activity, ImageView imageView, String[] urls, int[] size, int position) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, new Pair<View, String>(imageView, SHARED_ELEMENT_PHOTO));

        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(EXTRA_URL, urls);
        intent.putExtra(EXTRA_THUMBNAIL_SIZE, size);
        intent.putExtra(EXTRA_POSITION, position);

        ActivityCompat.startActivity(activity, intent, options.toBundle());

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        Transition transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    vpPicturePager.setCurrentItem(mPosition);
                    ivThumbnail.setVisibility(View.GONE);
                    vpPicturePager.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                    vpPicturePager.setCurrentItem(mPosition);
                    ivThumbnail.setVisibility(View.GONE);
                    vpPicturePager.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

            return true;
        }

        return false;
    }

}
