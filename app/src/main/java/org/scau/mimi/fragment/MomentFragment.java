package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.scau.mimi.R;
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
//    private SwipeRefreshLayout srlRefreshMoment;


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

    }

    @Override
    protected void initViews(View view) {
        rvMoment = (RecyclerView) view.findViewById(R.id.rv_moment);
//        srlRefreshMoment = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh_moment);
        rvMoment.setAdapter(new MomentAdapter(mMomentList));
        rvMoment.setLayoutManager(new LinearLayoutManager(getActivity()));
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
}
