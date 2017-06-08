package com.example.lihui20.testhttp.tools;

import android.app.Fragment;
import android.os.Bundle;

import com.example.lihui20.testhttp.fragment.BaseFragment;
import com.example.lihui20.testhttp.fragment.BaseFragment2;

/**
 * Created by lihui20 on 2017/2/14.
 */
public class FragmentFactory {

    public static android.support.v4.app.Fragment newInstance(String type){
       BaseFragment2 fragment = new BaseFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return  fragment;
    }
}
