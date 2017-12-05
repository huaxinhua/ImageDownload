package com.mmm.imagedownload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mmm.imagedownload.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 马明明 on 2017/12/4.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class ImageListAdapter extends CommonAdapter {

    private int layout;
    private Context context;
    private List<String> mDatas;

    public ImageListAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.layout = layoutId;
        this.mDatas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        ImageView img_list = holder.getView(R.id.img);
        Glide.with(context).load(mDatas.get(position)).into(img_list);
    }


}
