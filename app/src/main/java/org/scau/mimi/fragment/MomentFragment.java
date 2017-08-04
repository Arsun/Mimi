package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.scau.mimi.R;
import org.scau.mimi.base.BaseFragment;

/**
 * Created by 10313 on 2017/8/2.
 */

public class MomentFragment extends BaseFragment {


    //Views;
    private RecyclerView rvMoment;
    private SwipeRefreshLayout srlRefreshMoment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        initViews(view);
        return view;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(View view) {
        rvMoment = (RecyclerView) view.findViewById(R.id.rv_moment);
        srlRefreshMoment = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh_moment);
    }

    @Override
    protected void loadData() {

    }
}
