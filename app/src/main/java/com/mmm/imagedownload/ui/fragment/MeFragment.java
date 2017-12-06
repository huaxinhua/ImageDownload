package com.mmm.imagedownload.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.blankj.utilcode.utils.SPUtils;
import com.mmm.imagedownload.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.mmm.imagedownload.common.Config.spUtils;

/**
 * Created by 马明明 on 2017/12/5.
 * 邮箱：609860164@qq.com
 * 公司：江苏诚通网路科技有限公司
 * Copyright © 2017 IASK Corporation, All Rights Reserved
 * 类注释：
 */

public class MeFragment extends Fragment {
    @Bind(R.id.switch_toast)
    Switch switchToast;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        switchToast.setChecked(spUtils.getBoolean("toast_switch"));
        initData();
        return view;

    }

    private void initData() {
        switchToast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    spUtils.putBoolean("toast_switch", true);
                    Log.i("---------->", "true");
                } else {
                    spUtils.putBoolean("toast_switch", false);
                    Log.i("---------->", "false");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
