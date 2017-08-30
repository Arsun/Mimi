package org.scau.mimi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.nineoldandroids.view.ViewHelper;

import org.jetbrains.annotations.NotNull;
import org.scau.mimi.R;
import org.scau.mimi.adapter.LocationAdapter;
import org.scau.mimi.fragment.ChatFragment;
import org.scau.mimi.fragment.MomentFragment;
import org.scau.mimi.gson.MessagesInfo;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.ResponseUtil;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.yalantis.com.foldingtabbar.FoldingTabBar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //Views;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private FoldingTabBar ftbMenu;
    private ImageView ivAddtionButtonLeft;
    private TextView tvLogOut;
    private SweetSheet sweetSheet;
    private RelativeLayout rlHomeLayout;

    //Variables
    private int mOriginFtbMenuTop;
    private List<MessagesInfo.Content.Message.Location> mLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setTransparentBar(Color.parseColor("#000000"), 127);
//        ultimateBar.setHintBar();下拉通知才显示，平时隐藏状态栏
//        ultimateBar.setColorBarForDrawer(Color.parseColor("#4a4a52"), 50);

//        ultimateBar.setHintBar();

        loadData();
        initVariables();
        initViews();


//        StatusBarUtil.setColor(this, 0x454e5f, 122);
//        StatusBarUtil.setTransparent(this);
    }

    private void initViews() {
        actionBar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        ftbMenu = (FoldingTabBar) findViewById(R.id.ftb_main_menu);
        ivAddtionButtonLeft = (ImageView) findViewById(R.id.fab_addtion_button_left);
        tvLogOut = (TextView) findViewById(R.id.tv_main_menu_log_out);

        //设置抽屉菜单
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.logOut(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = ResponseUtil.getString(response);
                        LogUtil.d(TAG, "log out: " + data);
                        LoginActivity.actionStart(MainActivity.this);
                        finish();
                    }
                });
            }
        });

        rlHomeLayout = (RelativeLayout) findViewById(R.id.rl_main_home_layout);

        if (actionBar != null) {
            actionBar.hide();
        }

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

        ftbMenu.post(new Runnable() {
            @Override
            public void run() {
                mOriginFtbMenuTop = ftbMenu.getTop();
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


        //设置选择地点菜单-------------------------------------------------------------------------------------------
//        sweetSheet = new SweetSheet(rlHomeLayout);
//
//        CustomDelegate customDelegate = new CustomDelegate(true,
//                CustomDelegate.AnimationType.DuangLayoutAnimation);
//        View selectLocationView = LayoutInflater.from(this).inflate(R.layout.layout_select_location, null, false);
//        customDelegate.setCustomView(selectLocationView);
//        sweetSheet.setDelegate(customDelegate);
//
//        selectLocationView.findViewById(R.id.ib_close_select_location)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (sweetSheet.isShow())
//                            sweetSheet.dismiss();
//                    }
//                });
//
//
//
//        final RecyclerView rvLocation = (RecyclerView) selectLocationView.findViewById(R.id.rv_select_location);
//        rvLocation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rvLocation.setAdapter(new LocationAdapter(mLocations));


        //-----------------------------------------------------------------------------------------------------------

        ivAddtionButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendMomentActivity.actionStart(MainActivity.this);
//                HttpUtil.requestLocations(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        mLocations.clear();
//                        mLocations.addAll(
//                                ResponseUtil.getLocations(response)
//                        );
//                        rvLocation.getAdapter().notifyDataSetChanged();
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (!sweetSheet.isShow())
//                                    sweetSheet.show();
//                            }
//                        });
//                    }
//                });
            }
        });

        addDefaultFragment();

    }


    private void initVariables() {

        mLocations = new ArrayList<>();

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

    public void setFtbMenuTopAndBottom(int newTop, int newBottom) {
        ftbMenu.setTop(newTop);
        ftbMenu.setBottom(newBottom);
    }

    public int getFtbMenuTop() {
        return ftbMenu.getTop();
    }

    public int getFtbMenuBottom() {
        return ftbMenu.getBottom();
    }

    public int getOriginFtbMenuTop() {
        return mOriginFtbMenuTop;
    }

    public boolean isFtbMenuInScreen() {
        int width;
        int height;
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        width = point.x;
        height = point.y;

        Rect rect = new Rect(0, 0, width, height);

        if (!ftbMenu.getLocalVisibleRect(rect))
            return false;

        return true;
    }

    public void ftbMenuScrollTo(int y) {

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

//    private List<MessagesInfo.Content.Message.Location> initLocationList() {
//        for (i = 0; i < 5; i++) {
//        }
//    }
}
