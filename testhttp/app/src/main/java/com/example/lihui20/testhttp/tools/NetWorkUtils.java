package com.example.lihui20.testhttp.tools;

/**
 * Created by lihui20 on 2017/4/24.
 */

public class NetWorkUtils {
    public static boolean isOpen() {
        return open;
    }

    public static void setOpen(boolean open) {
        NetWorkUtils.open = open;
    }

    private static boolean open = true;

}
