package com.mmm.imagedownload.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mmm.imagedownload.ImageListActivity;
import com.mmm.imagedownload.R;
import com.mmm.imagedownload.api.Api;
import com.mmm.imagedownload.bean.GankIo;
import com.mmm.imagedownload.utils.HttpManager;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.BuildConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 马明明 on 2017/12/5.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class HomeFragment extends Fragment {
    @Bind(R.id.et_url)
    EditText etUrl;
    @Bind(R.id.bt_analysis)
    Button btAnalysis;
    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    private View view;
    private Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        view = inflater.inflate(R.layout.fragment_main, container, false);
        CheckPermission();
        initData();
        ButterKnife.bind(this, view);
        return view;

    }

    private void CheckPermission() {
        Acp.getInstance(getActivity()).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE).build(), new AcpListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> permissions) {
                ToastUtils.showShortToast("拒绝权限将不能下载图片");
            }
        });
    }

    private void initData() {
        Api api = HttpManager.getInstance().getApiService();
        api.getCategoryData("福利", 20, 1)
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
                .subscribe(new Consumer<GankIo>() {
                    @Override
                    public void accept(GankIo gankIo) throws Exception {
                        Glide.with(getActivity()).load(gankIo.getResults().get(3).getUrl()).into(iv);
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.bt_analysis)
    public void onClick() {
        if (etUrl.getText().toString().startsWith("https://")) {
            ImageListActivity.skip(getActivity(), etUrl.getText().toString());
        } else {
            ToastUtils.showShortToast("链接格式不正确");
        }
    }
}
