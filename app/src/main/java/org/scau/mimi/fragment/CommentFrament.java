package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.scau.mimi.R;
import org.scau.mimi.activity.CommentActivity;
import org.scau.mimi.adapter.CommentAdapter;
import org.scau.mimi.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10313 on 2017/9/4.
 */

public class CommentFrament extends BaseFragment {

    //Views
    private RecyclerView rvComment;
    //Test
    private TextView tvTest;
    private CollapsingToolbarLayout ctbl;

    //Variables
    //Test
    private List<String> mList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        loadData();
        initVariables();
        initViews(view);
        return view;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_comment_toolbar);
//        tvTest = (TextView) view.findViewById(R.id.tv_test);
//        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//        tvTest.setText(s + s + s + s + s + s + s + s);
        rvComment = (RecyclerView) view.findViewById(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        initList();
        rvComment.setAdapter(new CommentAdapter(mList));

        ctbl = (CollapsingToolbarLayout) view.findViewById(R.id.ctbl_comment_collapsing_toolbar);
//        ctbl.setTitle("enenene");

        ((CommentActivity)getActivity()).setSupportActionBar(toolbar);

    }

    @Override
    protected void loadData() {

    }

    public static CommentFrament newInstance() {
        CommentFrament frament = new CommentFrament();

        return frament;
    }

    private void initList() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add("hhhhh");
        }

    }

}
