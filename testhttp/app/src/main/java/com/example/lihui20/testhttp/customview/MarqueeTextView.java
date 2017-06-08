package com.example.lihui20.testhttp.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lihui20 on 2017/3/31.
 */

public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
