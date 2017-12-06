package com.mmm.imagedownload;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mmm.imagedownload.ui.fragment.HomeFragment;
import com.mmm.imagedownload.ui.fragment.MeFragment;


import butterknife.Bind;
import butterknife.ButterKnife;

import static com.mmm.imagedownload.R.id.rb_home;

public class MainActivity extends BaseActivity {
    @Bind(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @Bind(rb_home)
    RadioButton rbHome;
    @Bind(R.id.rb_me)
    RadioButton rbMe;
    @Bind(R.id.radio)
    RadioGroup radio;
    private HomeFragment homeFragment;
    private MeFragment meFragment;
    private Fragment mFragment;//当前显示的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rbHome.setChecked(true);
        homeFragment = new HomeFragment();
        meFragment = new MeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.realtabcontent,homeFragment).commit();
        mFragment = homeFragment;
        // 初始化页面设置
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        switchFragment(homeFragment);
                        break;
                    case R.id.rb_me:
                        switchFragment(meFragment);
                        break;
                }
            }
        });

    }


    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if(mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.realtabcontent,fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }



    @Override
    protected void onResume() {
        rbHome.setChecked(true);
        rbMe.setChecked(false);
        super.onResume();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //  不保存Activity状态，解决宿主Activity被系统回收后，重启程序时Fragment重叠的问题。
        //  super.onSaveInstanceState(outState);
    }
}
