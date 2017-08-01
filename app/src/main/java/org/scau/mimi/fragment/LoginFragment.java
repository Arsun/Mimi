package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.scau.mimi.R;
import org.scau.mimi.base.BaseFragment;

/**
 * Created by 10313 on 2017/8/1.
 */

public class LoginFragment extends BaseFragment {

    //Log
    private static final String TAG = "LoginFragment";

    //Variables;
    private String mAccount;
    private String mPassword;
    private int mProgress;

    //Views;
    private EditText etAccount;
    private EditText etPassword;
    private CircularProgressButton btnLogin;
    private TextInputLayout tilAccount;
    private TextInputLayout tilPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initVariables();
        initViews(view);
        return view;
    }

    @Override
    protected void initVariables() {
        mAccount = "";
        mPassword = "";
        mProgress = 0;

    }

    @Override
    protected void initViews(View view) {
        btnLogin = (CircularProgressButton) view.findViewById(R.id.btn_login);
        etAccount = (EditText) view.findViewById(R.id.et_login_account);
        etPassword = (EditText) view.findViewById(R.id.et_login_password);
        tilAccount = (TextInputLayout) view.findViewById(R.id.til_login_account);
        tilPassword = (TextInputLayout) view.findViewById(R.id.til_login_password);

        btnLogin.setIndeterminateProgressMode(true);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    @Override
    protected void loadData() {

    }

    private void login() {
        mPassword = etPassword.getText().toString();
        mAccount = etAccount.getText().toString();
        if (mPassword.equals("") || mAccount.equals(""))
            Toast.makeText(getActivity(), "请正确输入用户名和密码", Toast.LENGTH_SHORT).show();
        else
            btnLogin.setProgress(1);
    }
}
