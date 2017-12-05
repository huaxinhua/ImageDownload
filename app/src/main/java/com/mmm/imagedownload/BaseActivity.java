package com.mmm.imagedownload;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by 马明明 on 2017/12/5.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class BaseActivity extends AppCompatActivity {
    private Window window;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式通知栏解决方案
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 因为EMUI3.1系统与这种沉浸式方案API有点冲突，会没有沉浸式效果。
                // 所以这里加了判断，EMUI3.1系统不清除FLAG_TRANSLUCENT_STATUS
                if (!isEMUI3_1()) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);

                //6.0以上设置通知栏字体颜色为黑色，根据App风格决定
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                }
            }
        }
    }

    public static boolean isEMUI3_1() {
        if ("EmotionUI_3.1".equals(getEmuiVersion())) {
            return true;
        }
        return false;
    }

    private static String getEmuiVersion() {
        Class<?> classType = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, "ro.build.version.emui");
        } catch (Exception e) {
        }
        return "";
    }


/**
 * CoordinatorLayout+AppBarLayout并给AppBarLayout设置paddingtop之后的滑动问题
 * 给Activity添加如下flag即可
 * this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
 *部分带虚拟按键的华为手机出现虚拟按键挡住底部布局的问题(EMUI3.1问题)
 * Activity的onCreate方法中加上下面一段代码就可以完美解决这个问题
 if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
 ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_container_layout), new android.support.v4.view.OnApplyWindowInsetsListener() {
@Override public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
return insets.consumeSystemWindowInsets();
}
});
 }
 * 参考链接 https://juejin.im/post/5a25f6146fb9a0452405ad5b
 */
}

