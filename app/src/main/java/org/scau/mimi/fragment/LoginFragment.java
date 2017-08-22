package org.scau.mimi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.scau.mimi.R;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.ResponseUtil;
import org.scau.mimi.util.TextUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10313 on 2017/8/1.
 */

public class LoginFragment extends BaseFragment {

    //Log
    private static final String TAG = "LoginFragment";

    //Constants
    private static final int START_LOGIN = 1;
    private static final int FINISH_LOGIN = 100;


    //Variables;
    private String mAccount;
    private String mPassword;
    private int mProgress;

    //Views;
    private EditText etAccount;
    private EditText etPassword;
    private CircularProgressButton cpbLogin;
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
    }

    @Override
    protected void initViews(View view) {
        cpbLogin = (CircularProgressButton) view.findViewById(R.id.btn_login);
        etAccount = (EditText) view.findViewById(R.id.et_login_account);
        etPassword = (EditText) view.findViewById(R.id.et_login_password);
        tilAccount = (TextInputLayout) view.findViewById(R.id.til_login_account);
        tilPassword = (TextInputLayout) view.findViewById(R.id.til_login_password);

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAccount = s.toString();
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword = s.toString();
            }
        });

        cpbLogin.setIndeterminateProgressMode(true);
        cpbLogin.setOnClickListener(new View.OnClickListener() {
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
        if (TextUtil.isTextVaild(mAccount) && TextUtil.isTextVaild(mPassword)) {

            cpbLogin.setProgress(START_LOGIN);
            HttpUtil.login(mAccount, mPassword, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String data = ResponseUtil.getString(response);
                    Log.d(TAG, data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cpbLogin.setProgress(FINISH_LOGIN);
                        }
                    });
                }
            });

        } else {
            Toast.makeText(getActivity(), "请正确输入账户和密码", Toast.LENGTH_SHORT).show();
        }

    }
}
