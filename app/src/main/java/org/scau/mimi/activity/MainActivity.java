package org.scau.mimi.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.jaeger.library.StatusBarUtil;
import com.joaquimley.faboptions.FabOptions;
import com.nineoldandroids.view.ViewHelper;

import org.jetbrains.annotations.NotNull;
import org.scau.mimi.R;
import org.scau.mimi.fragment.ChatFragment;
import org.scau.mimi.fragment.MomentFragment;
import org.zackratos.ultimatebar.UltimateBar;

import client.yalantis.com.foldingtabbar.FoldingTabBar;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity {


    //Views;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
//    private FabOptions foMenu;
    private FoldingTabBar ftbMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
//        ultimateBar.setHintBar();

        initVariables();
        initViews();
        loadData();

//        StatusBarUtil.setColor(this, 0x454e5f, 122);
//        StatusBarUtil.setTransparent(this);
    }

    private void initViews() {
        actionBar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        ftbMenu = (FoldingTabBar) findViewById(R.id.ftb_main_menu);

        if (actionBar != null)
            actionBar.hide();

        drawerLayout.setScrimColor(0x00);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
//                    ViewHelper.setAlpha(mMenu,1f);

                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {
                    ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });

        ftbMenu.setOnFoldingItemClickListener(new FoldingTabBar.OnFoldingItemSelectedListener() {
            @Override
            public boolean onFoldingItemSelected(@NotNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.activity_main_menu_chat:
                        replaceFragment(new ChatFragment());
                        break;
                    case R.id.activity_main_menu_moment:
                        replaceFragment(new MomentFragment());
                        break;
                    case R.id.activity_main_menu_settings:
                        replaceFragment(new ChatFragment());
                        break;
                    case R.id.activity_main_menu_personal:
                        replaceFragment(new MomentFragment());
                        break;
                }
                return true;
            }
        });

        addDefaultFragment();

//        new QBadgeView(this).bindTarget(ftbMenu)
//                .setBadgeNumber(3).setBadgeGravity(Gravity.START | Gravity.TOP);


    }


    private void initVariables() {

    }

    private void loadData() {

    }

    private void addDefaultFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_fragment_container, new MomentFragment())
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, fragment)
                .commit();
    }
}
