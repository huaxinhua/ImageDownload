package com.mmm.imagedownload.utils;

import com.blankj.utilcode.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.mmm.imagedownload.common.Constant;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 马明明 on 2017/12/6.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class ImageDownloadManager {

    public static void DownloadOneImage(String url){
        final String img_name = System.currentTimeMillis() + ".jpg";
        OkGo.get(url).execute(new FileCallback(Constant.DowmloadPath, img_name) {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                ToastUtils.showShortToast(img_name+"已保存到图虫下载");
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showShortToast("下载失败->" + e.toString());
            }
        });

    }
}
