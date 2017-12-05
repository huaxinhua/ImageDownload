package com.mmm.imagedownload.api;

import com.mmm.imagedownload.bean.GankIo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by 马明明 on 2017/12/5.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public interface Api {
    @GET("data/{category}/{pageSize}/{page}")
    Observable<GankIo> getCategoryData(@Path("category") String category, @Path("pageSize") int pageSize, @Path("page") int page);

}
