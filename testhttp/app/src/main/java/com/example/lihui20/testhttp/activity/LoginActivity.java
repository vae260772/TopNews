package com.example.lihui20.testhttp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends Activity {


    public Handler mSuccessHandler;
    // UI references.

    TextView tv;
    Button register;
    Context mContext;
    LinearLayout loginlinearLayout1, loginlinearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //2套布局
        SharedPreferences sp = this.getSharedPreferences("loginstate", MODE_PRIVATE);
        String loginuser = sp.getString("loginuser", "");
        if (TextUtils.isEmpty(loginuser)) {
            setContentView(R.layout.activity_login2);
            initViews(R.layout.activity_login2);
        } else {
            setContentView(R.layout.activity_login_sucess);
            initViews(R.layout.activity_login_sucess);

        }
        mSuccessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:// 验证码正确
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) msg.obj;
                        String country = (String) phoneMap.get("country");
                        String phone = (String) phoneMap.get("phone");
                        Log.d("lihui", " country---" + country);
                        Log.d("lihui", " phone---" + phone);
                        //持久化，保存起来
                        SharedPreferences sp = mContext.getSharedPreferences("loginstate", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("loginuser", phone);
                        editor.commit();
                        initViews(R.layout.activity_login_sucess);

                        break;
                    default:
                        break;
                }
            }
        }

        ;
        // Set up the login form.


        mContext = this;

//        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    //    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //    attemptLogin();
//            }
//        });

    }

    public void initViews(int resId) {
        switch (resId) {
            case R.layout.activity_login2:
                setContentView(R.layout.activity_login2);
                loginlinearLayout1 = (LinearLayout) findViewById(R.id.ll);
                register = (Button) findViewById(R.id.register);
                tv = (TextView) findViewById(R.id.tv);
                //验证码登录
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ValidCodeLoginActivity.class);
                        startActivityForResult(intent, 0);//requestCode
                    }
                });
                //注册
                register.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //打开注册页面
                        RegisterPage registerPage = new RegisterPage();
                        CustomEventHandler customEventHandler = new CustomEventHandler();
                        //注册完成之后回调操作
                        registerPage.setRegisterCallback(customEventHandler);
                        registerPage.show(mContext);
                    }
                });
                break;
            case R.layout.activity_login_sucess:
                setContentView(R.layout.activity_login_sucess);
                loginlinearLayout2 = (LinearLayout) findViewById(R.id.ll);
                TextView userTv = (TextView) this.findViewById(R.id.user);
                Button cancelLogin = (Button) this.findViewById(R.id.login);
                SharedPreferences sp = this.getSharedPreferences("loginstate", MODE_PRIVATE);
                String loginuser = sp.getString("loginuser", "");
                userTv.setText(loginuser + "用户，登录成功！");
                cancelLogin.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //重新设置布局
                        initViews(R.layout.activity_login2);
                        //登录账户清空
                        SharedPreferences sp = mContext.getSharedPreferences("loginstate", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("loginuser", "");
                        editor.commit();
                    }
                });
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 0:
                    String phoneNum = data.getStringExtra("phoneNum");
                    //持久化，保存起来
                    SharedPreferences sp = this.getSharedPreferences("loginstate", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("loginuser", phoneNum);
                    editor.commit();
                    initViews(R.layout.activity_login_sucess);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }


    public class CustomEventHandler extends EventHandler {

        @Override
        public void beforeEvent(int i, Object o) {
            super.beforeEvent(i, o);
            Log.d("lihui", "180  i---" + i);

        }

        public void afterEvent(int event, int result, Object data) {
            // 解析注册结果
            Log.d("lihui", "186 event---" + event);
            Log.d("lihui", "187 result---" + result);
            Log.d("lihui", "187 data---" + result);

            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Message msg = mSuccessHandler.obtainMessage();
                    msg.what = event;
                    msg.obj = data;
                    mSuccessHandler.sendMessage(msg);
                }
            }
        }

        private boolean isPasswordValid(String password) {
            //TODO: Replace this with your own logic
            return password.length() > 4;
        }


        /**
         * Represents an asynchronous login/registration task used to authenticate
         * the user.
         */
//        public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//            private final String mEmail;
//            private final String mPassword;
//
//            UserLoginTask(String email, String password) {
//                mEmail = email;
//                mPassword = password;
//            }
//
//            @Override
//            protected Boolean doInBackground(Void... params) {
//                // TODO: attempt authentication against a network service.
//
//                try {
//                    // Simulate network access.
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    return false;
//                }
//
//
//                // TODO: register the new account here.
//                return true;
//            }
//
//            @Override
//            protected void onPostExecute(final Boolean success) {
//                mAuthTask = null;
//                //showProgress(false);
//
//                if (success) {
//                    finish();
//                } else {
//                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                    mPasswordView.requestFocus();
//                }
//            }
//
//            @Override
//            protected void onCancelled() {
//                mAuthTask = null;
//                // showProgress(false);
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(getCurrentViewbgColor())) {
            return;
        }
   //   updateBgColor(getCurrentViewbgColor());
    }

    private void updateBgColor(String viewbgcolor_key) {
        /*
                <item>white</item>
        <item>gary</item>
        <item>green</item>
        <item>yellow</item>
         */
        switch (viewbgcolor_key) {
            case "brown":
                if (loginlinearLayout1 != null) {
                    loginlinearLayout1.setBackgroundResource(R.color.setbrown);
                }
                if (loginlinearLayout2 != null) {
                    loginlinearLayout2.setBackgroundResource(R.color.setbrown);
                }
                break;
            case "black":
                if (loginlinearLayout1 != null) {

                    loginlinearLayout1.setBackgroundResource(android.R.color.black);
                }
                if (loginlinearLayout2 != null) {

                    loginlinearLayout2.setBackgroundResource(R.color.setbrown);
                }
                break;
        }
    }

    private String getCurrentViewbgColor() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String viewbgcolor_key = settings.getString(getString(R.string.viewbgcolor_key), "");
        return viewbgcolor_key;
    }
}
