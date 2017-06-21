package com.example.lihui20.testhttp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lihui20.testhttp.R;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.ResHelper.getStringRes;

public class ValidCodeLoginActivity extends Activity {

    EditText phonenumber, code;
    TextView getCodeTextView;
    private static Handler mSuccessHandler;
    private static Handler mFailHandler;

    private static EventHandler eventHandler;
    static Context mContext;
    Button button;
    String phone;
    private boolean startTimer = true;
    ProgressBar login_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phonenumber = (EditText) findViewById(R.id.number);
        code = (EditText) findViewById(R.id.code);
        getCodeTextView = (TextView) findViewById(R.id.timer);
        mContext = this;
        button = (Button) findViewById(R.id.login);
        login_progress = (ProgressBar) findViewById(R.id.login_progress);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                phone = phonenumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast("手机号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(code.getText().toString())) {
                    showToast("验证码不能为空");
                    return;
                }
                //手机号码格式，点击登录按钮开始验证
                SMSSDK.submitVerificationCode("86", phonenumber.getText()
                        .toString(), code.getText().toString());
                login_progress.setVisibility(View.VISIBLE);
            }
        });
        //
        mSuccessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                login_progress.setVisibility(View.GONE);
                switch (msg.what) {
                    case -100:
                        getCodeTextView.setText(msg.arg1 + "秒");
                        if (msg.arg1 == 0) {
                            getCodeTextView.setText("获取验证码");
                            getCodeTextView.setClickable(true);
                            startTimer = true;//可以再次获取验证码，开启倒计时
                        }
                        break;
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:// 3 验证码正确
                        showToast("验证成功");
                        //返回到第一个界面
                        Intent mIntent = new Intent();
                        mIntent.putExtra("phoneNum", phone);
                        // 设置结果，并进行传送
                        setResult(-1, mIntent);//resultCode
                        finish();
                        break;
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:// 2 获取验证码成功
                        startTimer();
                        showToast("验证码已发送");
                        break;
                    default:
                        break;
                }
            }
        };
        //
        mFailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                login_progress.setVisibility(View.GONE);
                switch (msg.what) {
                    case SMSSDK.RESULT_ERROR://
                        showToast((String) msg.obj);
                    case -1000://
                        showToast((String) msg.obj);
                    default:
                        break;
                }
            }
        };


        // eventHandler
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.d("validcode", "110 event---" + event);
                Log.d("validcode", "111 result---" + result);
/*
public static final int RESULT_COMPLETE = -1;
    public static final int RESULT_ERROR = 0;
    public static final int EVENT_GET_SUPPORTED_COUNTRIES = 1;
    public static final int EVENT_GET_VERIFICATION_CODE = 2;
    public static final int EVENT_SUBMIT_VERIFICATION_CODE = 3;
    public static final int EVENT_GET_CONTACTS = 4;
    public static final int EVENT_SUBMIT_USER_INFO = 5;
    public static final int EVENT_GET_FRIENDS_IN_APP = 6;
    public static final int EVENT_GET_NEW_FRIENDS_COUNT = 7;
    public static final int EVENT_GET_VOICE_VERIFICATION_CODE = 8;
        */
                switch (result) {
                    case SMSSDK.RESULT_COMPLETE://=-1
                        Log.d("validcode", "success data---" + data);
                        Log.d("validcode", "success event---" + event);
                        Log.d("validcode", "success result---" + result);
                        // 回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//=3
                            Message msg = mSuccessHandler.obtainMessage();
                            msg.what = event;
                            mSuccessHandler.sendMessage(msg);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//=2
                            Message msg = mSuccessHandler.obtainMessage();
                            msg.what = event;
                            mSuccessHandler.sendMessage(msg);
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        }
                        break;
                    case SMSSDK.RESULT_ERROR://=0
                        //再通过event确认到底是什么错误
                        Log.d("validcode", "fail event---" + event);
                        Log.d("validcode", "fail result---" + result);
                        Message msg = mFailHandler.obtainMessage();
                        // 根据服务器返回的网络错误，给toast提示
                        int status = 0;
                        //1、服务器提供的错误信息
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            Log.d("throwable", "throwable " + throwable);
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            status = object.optInt("status");//错误代码
                            Log.d("validcode", "des " + des);
                            Log.d("validcode", "status " + status);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                msg.what = result;
                                msg.obj = des;
                                mFailHandler.sendMessage(msg);
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                            Log.d("validcode", "e " + e);
                            e.printStackTrace();
                        }
                        //2、如果木有找到资源，默认提示
                        int resId = 0;
                        if (status >= 400) {//400 401 402 403...
                            resId = getStringRes(mContext,
                                    "smssdk_error_desc_" + status);
                        } else {
                            resId = getStringRes(mContext,
                                    "smssdk_network_error");
                        }
                        Log.d("resId", "resId---" + resId);
                        if (resId > 0) {
                            msg.what = -1000;
                            msg.obj = getResources().getString(resId);
                            mFailHandler.sendMessage(msg);
                            return;
                        }
                        break;
                    default:
                        ((Throwable) data).printStackTrace();
                        break;
                }
                Log.d("valid", "2.1");
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        getCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 通过手机号获取验证码
                if (TextUtils.isEmpty(phonenumber.getText().toString())) {
                    showToast("手机号不能为空");
                    return;
                }
                // 开始获取验证码
                Log.d("valid", "1");
                SMSSDK.getVerificationCode("86", phonenumber.getText()
                        .toString());
                Log.d("valid", "3");
            }
        });
    }

    static Toast toast;

    private void startTimer() {
        if (startTimer) {//已经在倒计时中
            startTimer = false;
            getCodeTextView.setClickable(false);

            new Thread() {
                public void run() {
                    for (int i = 60; i > -1; i--) {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Message msg = mSuccessHandler.obtainMessage();
                        msg.what = -100;
                        msg.arg1 = i;
                        mSuccessHandler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }

    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        mSuccessHandler = null;
        mFailHandler = null;
        eventHandler = null;
    }
}
