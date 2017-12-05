package com.mmm.imagedownload;

import android.app.Application;

import com.blankj.utilcode.utils.ToastUtils;
import com.blankj.utilcode.utils.Utils;
import com.lzy.okgo.OkGo;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by 马明明 on 2017/12/4.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "38fc1e8c51", false);
    }


    /**
     * 封装QQ、微信、微博的第三方登录和分享
     * https://juejin.im/post/5a23770151882534af25c573
     */
}

