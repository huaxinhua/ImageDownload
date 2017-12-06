package com.mmm.imagedownload.ui.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.mmm.imagedownload.R;
import com.mmm.imagedownload.api.Api;
import com.mmm.imagedownload.bean.GankIo;
import com.mmm.imagedownload.ui.activity.ImageListActivity;
import com.mmm.imagedownload.utils.HttpManager;
import com.mmm.imagedownload.utils.ImageDownloadManager;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.mmm.imagedownload.common.Config.spUtils;

/**
 * Created by 马明明 on 2017/12/5.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class HomeFragment extends Fragment implements View.OnLongClickListener {
    @Bind(R.id.et_url)
    EditText etUrl;
    @Bind(R.id.bt_analysis)
    Button btAnalysis;
    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;


    private View view;
    private Retrofit retrofit;
    private int imageIndex = 0;
    private GankIo gankio;
    private ClipData primaryClip;
    private AlertDialog.Builder builder;
    private Boolean isShow = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        view = inflater.inflate(R.layout.fragment_main, container, false);
        CheckPermission();
        ButterKnife.bind(this, view);
        builder = new AlertDialog.Builder(getContext());
        CheckPaste();
        initData();
        return view;
    }

    private void CheckPaste() {
        Log.i("---------->", "CheckPaste");
        Boolean ToastSwitch = spUtils.getBoolean("toast_switch");
        Log.i("---------->", "" + ToastSwitch);
        if (ToastSwitch) {
            ClipboardManager mClipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            primaryClip = mClipboardManager.getPrimaryClip();
            if (!primaryClip.toString().equals(spUtils.getString("url"))) {
                if (primaryClip.toString().contains("https://tuchong.com")) {
                    builder.setTitle("提示").setMessage("系统检测到您复制了图虫的链接，要打开吗？")
                            .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ToImageListActivity();
                                    spUtils.putString("url", primaryClip.toString());
                                }
                            }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            spUtils.putString("url", primaryClip.toString());
                        }
                    }).setCancelable(false);
                    builder.create().show();
                }
            }

        }
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
        iv.setOnLongClickListener(this);
        Api api = HttpManager.getInstance().getApiService();
        api.getCategoryData("福利", 580, 1)
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
                .subscribe(new Consumer<GankIo>() {
                    @Override
                    public void accept(GankIo gankIo) throws Exception {
                        gankio = gankIo;
                        Glide.with(getActivity()).load(gankIo.getResults().get(0).getUrl()).into(iv);
                        UpdateImage(gankIo);
                    }
                });
    }

    private void UpdateImage(final GankIo gankIo) {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isVisible()) {
                    ToastUtils.showShortToast("正在刷新");
                } else {
                    Log.i("------------>","index:"+(int) (Math.random() * 580));
                    Glide.with(getActivity()).load(gankIo.getResults().get((int) (Math.random() * 580)).getUrl()).into(iv);
                    refresh.setRefreshing(false);
                }
            }
        });
    }


    @OnClick(R.id.bt_analysis)
    public void onClick() {
        ToImageListActivity();
    }

    private void ToImageListActivity() {
        if (etUrl.getText().toString().startsWith("https://")) {
            ImageListActivity.skip(getActivity(), etUrl.getText().toString());
        } else if (!primaryClip.toString().startsWith("https://tuchong.com")) {
            String clip = primaryClip.toString().replace("ClipData { text/plain {T:", "").replace("} }", "");
            ImageListActivity.skip(getActivity(), clip);
        } else {
            ToastUtils.showShortToast("链接格式不正确");
        }
    }

    /**
     * popupWondown
     *
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentview = inflater.inflate(R.layout.popup_process, null);
                contentview.setFocusable(true); // 这个很重要
                contentview.setFocusableInTouchMode(true);
                final PopupWindow popupWindow = new PopupWindow(contentview, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(false);
                contentview.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            popupWindow.dismiss();

                            return true;
                        }
                        return false;
                    }
                });
                popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                Button popupDownload = contentview.findViewById(R.id.popup_download);
                popupDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageDownloadManager.DownloadOneImage(gankio.getResults().get((int) (Math.random() * 100)).getUrl());
                        popupWindow.dismiss();
                    }
                });
                break;
        }
        return false;

    }

    @Override
    public void onStop() {
        super.onStop();
        CheckPaste();
        Log.i("---------->", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            CheckPaste();
        }
    }
}
