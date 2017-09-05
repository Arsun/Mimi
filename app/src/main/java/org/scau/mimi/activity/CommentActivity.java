package org.scau.mimi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import org.scau.mimi.R;
import org.scau.mimi.adapter.CommentAdapter;
import org.scau.mimi.fragment.CommentFrament;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10313 on 2017/9/4.
 */

public class CommentActivity extends AppCompatActivity {


    //Views
    private RecyclerView rvComment;
    //Test
    private TextView tvTest;
    private CollapsingToolbarLayout ctbl;

    //Variables
    //Test
    private List<String> mList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        loadData();
        initVariables();
        initViews();
    }

    private Fragment createFragment() {
        return CommentFrament.newInstance();
    }
    private void initVariables() {

    }

    private void initViews() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_comment_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//        tvTest = (TextView) view.findViewById(R.id.tv_test);
//        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//        tvTest.setText(s + s + s + s + s + s + s + s);
        rvComment = (RecyclerView)findViewById(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        initList();
        rvComment.setAdapter(new CommentAdapter(mList));

//        ctbl = (CollapsingToolbarLayout)findViewById(R.id.ctbl_comment_collapsing_toolbar);
//        ctbl.setTitleEnabled(false);
//        ctbl.setExpandedTitleGravity(Gravity.TOP|Gravity.CENTER);
//        ctbl.setCollapsedTitleGravity(Gravity.CENTER);


    }

    private void loadData() {

    }

    private void initList() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add("hhhhh " + i);
        }
    }
}
