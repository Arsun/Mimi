package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitomi.tilibrary.transfer.Transferee;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.scau.mimi.R;
import org.scau.mimi.activity.MainActivity;
import org.scau.mimi.adapter.MomentAdapter;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.gson.MessagesInfo;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.ResponseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10313 on 2017/8/2.
 */

public class MomentFragment extends BaseFragment {

    private static final String TAG = "MomentFragment";

    private List<MessagesInfo.Content.Message> mMessageList;

    //Views;
    private RecyclerView rvMoment;
    private TwinklingRefreshLayout trlRefreshMoment;

    //Variables
    private MainActivity mActivity;
    private GestureDetectorCompat mGestureDetectorCompat;
    private View mRootView;
    private int nextPage;


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
        mMessageList = new ArrayList<>();
        nextPage = 1;

    }

    @Override
    protected void initViews(View view) {
        rvMoment = (RecyclerView) view.findViewById(R.id.rv_moment);
        trlRefreshMoment = (TwinklingRefreshLayout) view.findViewById(R.id.trl_refresh_moment);
        rvMoment.setAdapter(new MomentAdapter(mMessageList));
        rvMoment.setLayoutManager(new LinearLayoutManager(getActivity()));

        trlRefreshMoment.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                HttpUtil.requestMessages(0, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.d(TAG, "failed to request messages.");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trlRefreshMoment.finishRefreshing();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        MessagesInfo messagesInfo = ResponseUtil
                                .getMessagesInfo(response);
                        List<MessagesInfo.Content.Message> messages = messagesInfo.content.messageList;
                        mMessageList.addAll(0, messages);
//                        for (int i = 0; i < messages.size(); i++) {
//                            if (i != messages.size() - 1) {
//                                if (messages.get(i).mid != messages.get(i + 1).mid) {
//                                    mMessageList.add(messages.get(i));
//                                }
//                            } else {
//                                mMessageList.add(messages.get(i));
//                            }
//                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rvMoment.getAdapter().notifyDataSetChanged();
                                trlRefreshMoment.finishRefreshing();
                            }
                        });
                    }
                });

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                HttpUtil.requestMessages(nextPage, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.d(TAG, "failed to load more.");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trlRefreshMoment.finishLoadmore();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        MessagesInfo messagesInfo = ResponseUtil.getMessagesInfo(response);
                        nextPage++;
                        mMessageList.addAll(messagesInfo.content.messageList);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rvMoment.getAdapter().notifyDataSetChanged();
                                trlRefreshMoment.finishLoadmore();
                            }
                        });
                    }
                });
            }
        });


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
