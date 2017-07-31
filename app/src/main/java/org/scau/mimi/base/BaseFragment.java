package org.scau.mimi.base;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by 10313 on 2017/7/31.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract void initVariables();

    protected abstract void initViews(View view);

    protected abstract void loadData();
}