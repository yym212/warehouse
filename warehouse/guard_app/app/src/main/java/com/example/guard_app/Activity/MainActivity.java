package com.example.guard_app.Activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import com.example.guard_app.Fragment.HomeFragment;
import com.example.guard_app.Fragment.InfoFragment;
import com.example.guard_app.Fragment.MyFragment;
import com.example.guard_app.Adapter.PagerMainAdapter;
import com.example.guard_app.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager vp;
    private RadioGroup rg;


    //将id定义成整型数组
    private int[] rbs = {R.id.rb_home,R.id.rb_info,R.id.rb_my};

    //定义一个fragment集合
    private List<Fragment> mFragments;


    @Override
    protected int getLayoutID(){
        return R.layout.activity_main;
    }

    @Override
    protected void initView(){
        vp=f(R.id.vp);
        rg=f(R.id.rg);
    }

    @Override
    protected void initData(){
        mFragments=new ArrayList<>();
        HomeFragment home = new HomeFragment();
        InfoFragment info = new InfoFragment();
        MyFragment my = new MyFragment();
        mFragments.add(home);
        mFragments.add(info);
        mFragments.add(my);

        //设置填充器
        vp.setAdapter(new PagerMainAdapter(getSupportFragmentManager(),mFragments));
        //设置缓存页面数
        vp.setOffscreenPageLimit(3);
    }

    @Override
    protected void initListener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0;i<rbs.length;i++){
                    if (rbs[i]!=checkedId) continue;
                    //加载滑动
                    /*setCurrentItem(int item, boolean true);
                    参数为true，换页速度块，参数为false换页速度慢
                    * */
                    vp.setCurrentItem(i,true);
                }
            }
        });

        //当页面滑动时被调用
        /*
        *   第一个参数，position，代表当前正在显示的第一个页面的索引，如果positionOffset不等于０，那么第position+1个页面将被显示。
            第二个参数，positionOffset，代表position页面的偏移，取值范围是[0,1)。这是一个偏移百分比。
            第三个参数，positionOffsetPixels，和第二个意义差不多，只不过单位是像素，偏移的像素值。
        */
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            /*
            *   当新页面将被选中时调用，动画不是必需完成的。
                将被选中时，动画不是必需完成，也就是说可能动画还在继续的时候，这个方法就被调用了
                */
            @Override
            public void onPageSelected(int position) {
                rg.check(rbs[position]);
            }

            /*
            *   当滑动状态改变时被调用，有助于发现当用户开始拖拽，当页面自动的安放在当前页面，或当页面完全停止
            * */
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //设置一个默认页
        rg.check(rbs[0]);
    }
}
