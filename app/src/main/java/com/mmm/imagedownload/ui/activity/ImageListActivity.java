package com.mmm.imagedownload.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.mmm.imagedownload.BaseActivity;
import com.mmm.imagedownload.R;
import com.mmm.imagedownload.adapter.ImageListAdapter;
import com.mmm.imagedownload.adapter.ShowImagesAdapter;
import com.mmm.imagedownload.common.Config;
import com.mmm.imagedownload.common.Constant;
import com.mmm.imagedownload.widget.ShowImagesDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ImageListActivity extends BaseActivity {
    @Bind(R.id.rv_img)
    RecyclerView rvImg;
    @Bind(R.id.bt_download)
    FloatingActionButton btDownload;
    private int flag;
    private String url;
    private Element doc;
    private String path;
    private Context context;
    private List<String> imageList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        ButterKnife.bind(this);
        context = this;
        url = getIntent().getStringExtra("url");
        rvImg.setLayoutManager(new LinearLayoutManager(context));
        getDeviceDensity();
        initData();
    }

    private void getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Config.EXACT_SCREEN_HEIGHT = metrics.heightPixels;
        Config.EXACT_SCREEN_WIDTH = metrics.widthPixels;
    }


    private void initData() {
        OkGo.get(url).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                doc = Jsoup.parse(s);
                Elements img = doc.getElementsByClass("post-content");
                for (Element element : img.select("img")) {
                    imageList.add(element.attr("src"));
                }

                if (imageList != null) {
                    ToastUtils.showShortToast("解析成功");
                }

                Log.i("----------->", "imageList:" + imageList);

                CommonAdapter adapter = new ImageListAdapter(context, R.layout.item_img, imageList);
                rvImg.setAdapter(adapter);
                adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        //ImageDetailsActivity.skip(ImageListActivity.this,imageList.get(position));
                        new ShowImagesDialog(ImageListActivity.this, imageList,position).show();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        DownLoadImage(position);
                        return false;
                    }
                });

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showShortToast("解析失败->" + e.toString());
            }
        });
    }

    /**
     * 单张下载
     *
     * @param position
     */
    private void DownLoadImage(int position) {
        OkGo.get(imageList.get(position)).execute(new FileCallback(Constant.DowmloadPath, System.currentTimeMillis() + ".jpg") {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                ToastUtils.showShortToast("下载完成");
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showShortToast("下载失败->" + e.toString());
            }
        });


    }


    /**
     * 图集下载
     */
    @OnClick(R.id.bt_download)
    public void onClick() {
        for (int i = 0; i < imageList.size(); i++) {
            final String img_name = System.currentTimeMillis() + i + ".jpg";
            OkGo.get(imageList.get(i)).execute(new FileCallback(Constant.DowmloadPath, img_name) {
                @Override
                public void onSuccess(File file, Call call, Response response) {
                    ToastUtils.showShortToast(img_name + "下载完成");
                }
            });
        }
        Log.i("------->", "图集：" + imageList.size() + "张");

    }


    public static void skip(Activity activity, String url) {
        Intent intent = new Intent(activity, ImageListActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }


}
