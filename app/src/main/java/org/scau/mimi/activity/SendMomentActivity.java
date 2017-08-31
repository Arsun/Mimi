package org.scau.mimi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hitomi.tilibrary.transfer.Transferee;

import org.scau.mimi.R;
import org.scau.mimi.base.BaseActivity;
import org.scau.mimi.fragment.SendMomentFragment;

public class SendMomentActivity extends BaseActivity {

    //Varibles
    private Transferee mTransferee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransferee = Transferee.getDefault(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTransferee.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected Fragment createFragment() {
        return SendMomentFragment.newInstance();
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SendMomentActivity.class);
        context.startActivity(intent);
    }

    public Transferee getTransferee() {
        return mTransferee;
    }

}
