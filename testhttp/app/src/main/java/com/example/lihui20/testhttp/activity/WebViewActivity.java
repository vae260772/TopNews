package com.example.lihui20.testhttp.activity;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lihui20.testhttp.R;

public class WebViewActivity extends Activity {
    WebView webView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        intent = getIntent();
        String url = intent.getStringExtra("url");
        init(url);
    }

    private void init(String url) {
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //WebView加载web资源
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    //
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
