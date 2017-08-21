package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.scau.mimi.R;
import org.scau.mimi.activity.MainActivity;
import org.scau.mimi.adapter.MomentAdapter;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.bean.Moment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10313 on 2017/8/2.
 */

public class MomentFragment extends BaseFragment {

    private static final String TAG = "MomentFragment";

    private List<Moment> mMomentList;

    //Views;
    private RecyclerView rvMoment;
    private TwinklingRefreshLayout trlRefreshMoment;

    //Variables
    private MainActivity mActivity;
    private GestureDetectorCompat mGestureDetectorCompat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        loadData();
        initList();
        initVariables();
        initViews(view);
        return view;
    }

    @Override
    protected void initVariables() {
//        mActivity = (MainActivity)getActivity();
//        mGestureDetectorCompat = new GestureDetectorCompat(mActivity, new MyGestureListener());
    }

    @Override
    protected void initViews(View view) {
        rvMoment = (RecyclerView) view.findViewById(R.id.rv_moment);
        trlRefreshMoment = (TwinklingRefreshLayout) view.findViewById(R.id.trl_refresh_moment);
        rvMoment.setAdapter(new MomentAdapter(mMomentList));
        rvMoment.setLayoutManager(new LinearLayoutManager(getActivity()));

//        rvMoment.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mGestureDetectorCompat.onTouchEvent(event);
//                return false;
//            }
//        });

    }

    @Override
    protected void loadData() {

    }

    private void initList() {
        mMomentList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Moment moment = new Moment();
            moment.setLikeCount(56);
            mMomentList.add(moment);
        }

        Log.d(TAG, TAG + "initList: " + mMomentList.size());
    }


//    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//
//            if (Math.abs(distanceY) > Math.abs(distanceX)) {
//                int btnTop = mActivity.getFtbMenuTop();
//                int btnBottom = mActivity.getFtbMenuBottom();
//
//                boolean isScrollDown = e1.getRawY() < e2.getRawY() ? true : false;
//
//                if (!ifNeedScroll(isScrollDown))
//                    return false;
//
//                if (isScrollDown) {
//                    mActivity.setFtbMenuTopAndBottom(
//                            btnTop - (int) Math.abs(distanceY),
//                            btnBottom + (int) Math.abs(distanceY)
//                    );
//                } else if (!isScrollDown) {
//                    mActivity.setFtbMenuTopAndBottom(
//                            btnTop + (int)Math.abs(distanceY),
//                            btnBottom + (int) Math.abs(distanceY)
//                    );
//                }
//            }
//
//            return super.onScroll(e1, e2, distanceX, distanceY);
//        }
//
//        private boolean ifNeedScroll(boolean isScrollDown) {
//            int nowButtonTop = mActivity.getFtbMenuTop();
//
//            if (isScrollDown && nowButtonTop <= mActivity.getOriginFtbMenuTop())
//                return false;
//
//            if (!isScrollDown) {
//                return mActivity.isFtbMenuInScreen();
//            }
//
//            return true;
//        }
//
//    }
}
