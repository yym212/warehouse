package com.example.guard_app.Activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {
    protected BaseActivity act;

    /*
    *   Class.getName()：以String的形式，返回Class对象的“实体”名称；
        Class.getSimpleName()：获取源代码中给出的“底层类”简称。
    * */
    protected final String TAG=getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act=this;
        setContentView(getLayoutID());
        initView();
        initData();
        initListener();

    }
    @LayoutRes
    protected abstract int getLayoutID();
    protected abstract void initListener();
    protected abstract void initView();
    protected abstract void initData();

    /*
    @SuppressWarnings("unchecked") 告诉编译器忽略 unchecked 警告信息
    * */
    @SuppressWarnings("unchecked")

    //通过泛型来简化findViewById
    protected <E>E f(int id){
        return (E)findViewById(id);
    }

}
