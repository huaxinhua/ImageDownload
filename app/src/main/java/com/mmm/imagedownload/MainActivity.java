package com.mmm.imagedownload;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mmm.imagedownload.ui.fragment.HomeFragment;
import com.mmm.imagedownload.ui.fragment.MeFragment;

import java.util.ArrayList;
import java.util.List;

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
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private List<Fragment> mFragment = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rbHome.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        homeFragment = new HomeFragment();
        // 初始化页面设置
        transaction.add(R.id.realtabcontent, homeFragment).commit();
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        Log.i("--------->","rb_home");
                        transaction = fragmentManager.beginTransaction();
                        hideFragments(transaction);
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                            Log.i("--------->","new->rb_home");
                            transaction.add(R.id.realtabcontent, homeFragment).commit();
                        } else {
                            transaction.show(homeFragment);
                            Log.i("--------->","show->rb_home");
                        }
                        break;
                    case R.id.rb_me:
                        Log.i("--------->","rb_me");
                        transaction = fragmentManager.beginTransaction();
                        hideFragments(transaction);
                        if (meFragment == null) {
                            meFragment = new MeFragment();
                            Log.i("--------->","new->rb_me");
                            transaction.add(R.id.realtabcontent, meFragment).commit();
                        } else {
                            transaction.show(meFragment);
                            Log.i("--------->","show->rb_me");
                        }
                        break;
                }
            }
        });

    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
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
