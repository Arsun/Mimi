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
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.scau.mimi.R;
import org.scau.mimi.activity.MainActivity;
import org.scau.mimi.activity.SignUpActivity;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.database.User;
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
    private TextView tvGoToSignUp;

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
        tvGoToSignUp = (TextView) view.findViewById(R.id.tv_login_go_to_sign_up);

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

        tvGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.actionStart(getActivity());
                getActivity().finish();
            }
        });


    }

    @Override
    protected void loadData() {
    }

    private void login() {
        if (TextUtil.isTextVaild(mAccount) && TextUtil.isTextVaild(mPassword)) {

            cpbLogin.setProgress(START_LOGIN);
            //TODO：处理登陆后输入框禁止输入的交互，如果登录失败记得重新允许输入
            etAccount.setEnabled(false);
            etPassword.setEnabled(false);

            HttpUtil.login(mAccount, mPassword, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                     User user;
                    if (DataSupport.count(User.class) == 0) {
                        user = new User();
                    } else {
                        user = DataSupport.findFirst(User.class);
                    }
                    user.setSecret(ResponseUtil.getLoginInfo(response).content.secret);
                    user.save();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cpbLogin.setProgress(FINISH_LOGIN);
                            MainActivity.actionStart(getActivity());
                        }
                    });
                }
            });

        } else {
            Toast.makeText(getActivity(), "请正确输入账户和密码", Toast.LENGTH_SHORT).show();
        }

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }
}
