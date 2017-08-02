package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.scau.mimi.R;
import org.scau.mimi.base.BaseFragment;

/**
 * Created by 10313 on 2017/8/2.
 */

public class ChatFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void loadData() {

    }
}
