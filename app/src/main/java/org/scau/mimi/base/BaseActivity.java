package org.scau.mimi.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.scau.mimi.R;

/**
 * Created by 10313 on 2017/7/31.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        loadData();
        initVariables();
        initViews();

        addFragment();

    }

    protected abstract Fragment createFragment();

    protected abstract void initVariables();

    protected abstract void initViews();

    protected abstract void loadData();

    private void addFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fl_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fl_fragment_container, fragment)
                    .commit();
        }
    }

}