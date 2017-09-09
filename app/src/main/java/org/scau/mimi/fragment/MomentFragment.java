package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.scau.mimi.R;
import org.scau.mimi.SingleInstance.MessageLab;
import org.scau.mimi.activity.MainActivity;
import org.scau.mimi.adapter.MomentAdapter;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.gson.MessagesInfo;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.NetworkUtil;
import org.scau.mimi.util.ResponseUtil;
import org.scau.mimi.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10313 on 2017/8/2.
 */

public class MomentFragment extends BaseFragment {

    private static final String TAG = "MomentFragment";

    private MessageLab mMessageLab;

    //Views;
    private RecyclerView rvMoment;
    private TwinklingRefreshLayout trlRefreshMoment;

    //Variables
    private MainActivity mActivity;
    private GestureDetectorCompat mGestureDetectorCompat;
    private View mRootView;
    private long mTimeAfter;
    private long mTimeBefore;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_moment, container, false);
            loadData();
//            initList();
            initVariables();
            initViews(mRootView);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    protected void initVariables() {
        mMessageLab = MessageLab.getInstance();
        mTimeAfter = new Date().getTime();
        mTimeBefore = mTimeAfter;
        LogUtil.d(TAG, "initTimeAfter: " + mTimeAfter);

    }

    @Override
    protected void initViews(View view) {
        rvMoment = (RecyclerView) view.findViewById(R.id.rv_moment);
        trlRefreshMoment = (TwinklingRefreshLayout) view.findViewById(R.id.trl_refresh_moment);
        rvMoment.setAdapter(new MomentAdapter(mMessageLab.getMessageList()));
        rvMoment.setLayoutManager(new LinearLayoutManager(getActivity()));

        trlRefreshMoment.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                LogUtil.d(TAG, "onRefresh: " + mTimeAfter);
                if (NetworkUtil.isNetworkAvailable(getActivity())) {

                    if (mMessageLab.getMessageList().size() != 0) {
                        HttpUtil.requestMessagesAfter(mTimeAfter, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                LogUtil.d(TAG, "failed to request messages.");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toastWhenOnUiThread("加载失败");
                                        trlRefreshMoment.finishRefreshing();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                MessagesInfo messagesInfo = ResponseUtil
                                        .getMessagesInfo(response);
                                final List<MessagesInfo.Content.Message> messages = messagesInfo.content.messageList;
                                mMessageLab.addMessages(0, messages);
                                if (mMessageLab.getSize() != 0) {
                                    mTimeAfter = mMessageLab.getTimeAfter();
                                    mTimeBefore = mMessageLab.getTimeBefore();
                                    LogUtil.d(TAG, "newTimeAfter: " + mTimeAfter);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (messages.size() != 0) {
                                            rvMoment.getAdapter().notifyItemRangeInserted(0, messages.size());
                                        }
                                        trlRefreshMoment.finishRefreshing();
                                    }
                                });
                            }
                        });
                    } else {
                        HttpUtil.requestMessagesBefore(mTimeBefore, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toastWhenOnUiThread("加载失败");
                                        trlRefreshMoment.finishRefreshing();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                MessagesInfo messagesInfo = ResponseUtil
                                        .getMessagesInfo(response);
                                final List<MessagesInfo.Content.Message> messages = messagesInfo.content.messageList;
                                mMessageLab.addMessages(0, messages);
                                if (mMessageLab.getSize() != 0) {
                                    mTimeAfter = mMessageLab.getTimeAfter();
                                    mTimeBefore = mMessageLab.getTimeBefore();
                                    LogUtil.d(TAG, "newTimeAfter: " + mTimeAfter);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (messages.size() != 0) {
                                            trlRefreshMoment.setEnableLoadmore(true);
                                            rvMoment.getAdapter().notifyItemRangeInserted(
                                                    mMessageLab.getSize() - 1, messages.size());
                                        }
                                        trlRefreshMoment.finishRefreshing();

                                    }
                                });
                            }
                        });
                    }

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toastWhenOnUiThread("网络不可用");
                            trlRefreshMoment.finishRefreshing();
                        }
                    });
                }

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtil.d(TAG, "onLoadMore: " + mTimeBefore);
                if (NetworkUtil.isNetworkAvailable(getActivity())) {

                    HttpUtil.requestMessagesBefore(mTimeBefore, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtil.d(TAG, "failed to load more.");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toastWhenOnUiThread("加载失败");
                                    trlRefreshMoment.finishLoadmore();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            MessagesInfo messagesInfo = ResponseUtil.getMessagesInfo(response);
                            final List<MessagesInfo.Content.Message> messages = messagesInfo.content.messageList;
                            mMessageLab.addMessages(mMessageLab.getSize() - 1,messages);
                            if (mMessageLab.getSize() != 0) {
                                mTimeAfter = mMessageLab.getTimeAfter();
                                mTimeBefore = mMessageLab.getTimeBefore();
                                LogUtil.d(TAG, "newTimeBefore: " + mTimeBefore);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (messages.size() != 0) {
                                        rvMoment.getAdapter().notifyItemRangeInserted(
                                                mMessageLab.getSize() - 1, messages.size());
                                    }
                                    trlRefreshMoment.finishLoadmore();
                                }
                            });
                        }
                    });

                } else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toastWhenOnUiThread("网络不可用");
                            trlRefreshMoment.finishLoadmore();
                        }
                    });

                }
            }
        });

        if (mMessageLab.getSize() == 0) {
            trlRefreshMoment.setEnableLoadmore(false);
        }
        trlRefreshMoment.startRefresh();


//        trlRefreshMoment.onLoadMore(trlRefreshMoment);


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

    public void backToTop() {
        int position = ((LinearLayoutManager)rvMoment.getLayoutManager())
                .findLastVisibleItemPosition();
        if (position <= 20) {
            rvMoment.smoothScrollToPosition(0);
        } else {
            rvMoment.scrollToPosition(0);
        }
    }

    public static MomentFragment newInstance() {
        MomentFragment fragment = new MomentFragment();
        return fragment;
    }

//    private void initList() {
//        mMessageList = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            Moment moment = new Moment();
//            moment.setLikeNumber(56);
//            mMessageList.add(moment);
//        }
//
//        Log.d(TAG, TAG + "initList: " + mMessageList.size());
//    }


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
