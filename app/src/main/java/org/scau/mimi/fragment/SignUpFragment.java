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
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.scau.mimi.R;
import org.scau.mimi.activity.LoginActivity;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.ResponseUtil;
import org.scau.mimi.util.TextUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10313 on 2017/8/22.
 */

public class SignUpFragment extends BaseFragment {

    private static final String TAG = "SignUpFragment";

    //Constants
    private static final int START_SIGN_UP = 1;
    private static final int FINISH_SIGN_UP = 100;

    //Varibles
    private View mRootView;
    private String mAccount = "";
    private String mUsername = "";
    private String mPassword = "";
    private String mConfirmPassword = "";


    //Views
    private TextInputLayout tilAccount;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;
    private EditText etAccount;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private CircularProgressButton cpbSignUp;
    private TextView tvGoToLogin;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
            loadData();
            initVariables();
            initViews(mRootView);
        }


        return mRootView;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(View view) {
        tilAccount = (TextInputLayout) view.findViewById(R.id.til_sign_up_account);
        tilUsername = (TextInputLayout) view.findViewById(R.id.til_sign_up_username);
        tilPassword = (TextInputLayout) view.findViewById(R.id.til_login_password);
        tilPassword = (TextInputLayout) view.findViewById(R.id.til_sign_up_confirm_password);
        etAccount = (EditText) view.findViewById(R.id.et_sign_up_account);
        etUsername = (EditText) view.findViewById(R.id.et_sign_up_username);
        etPassword = (EditText) view.findViewById(R.id.et_sign_up_password);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_sign_up_confirm_password);
        tvGoToLogin = (TextView) view.findViewById(R.id.tv_sign_up_go_to_login);
        cpbSignUp = (CircularProgressButton) view.findViewById(R.id.cpb_sign_up);

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
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUsername = s.toString();
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
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mConfirmPassword = s.toString();
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(getActivity());
            }
        });

        cpbSignUp.setIndeterminateProgressMode(true);
        cpbSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtil.isTextVaild(mUsername) && TextUtil.isTextVaild(mPassword) && TextUtil.isTextVaild(mConfirmPassword) && TextUtil.isTextVaild(mAccount)) {
                    cpbSignUp.setProgress(START_SIGN_UP);
                    HttpUtil.signUp(mAccount
                            , mUsername
                            , mPassword
                            , mConfirmPassword
                            , new Callback() {
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
                                    cpbSignUp.setProgress(FINISH_SIGN_UP);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "请正确输入您的注册信息！", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void loadData() {

    }


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }
}
