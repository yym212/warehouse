package com.example.guard_app.Fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guard_app.Activity.Card;
import com.example.guard_app.Activity.Graph;
import com.example.guard_app.Activity.Table;
import com.example.guard_app.Activity.lineChart;
import com.example.guard_app.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;

    private CardView cardView1,cardView2,cardView3,cardView4;



    /*
        在写程序的时候你可以定义是否可为空指针。通过使用像@NotNull和@Nullable之类的annotation来声明一个方法是否是空指针安全的。
        如果可以传入NULL值，则标记为@Nullable，如果不可以，则标注为@Nonnull
    * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);
        initView();//初始化视图
       // refreshTime();//刷新时间

        return view;
    }

    //初始化
    private void initView(){
        cardView1 = view.findViewById(R.id.lin01);
        cardView2 = view.findViewById(R.id.lin02);
        cardView3 = view.findViewById(R.id.lin03);
        cardView4 = view.findViewById(R.id.lin04);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
       //

    }

    //刷新时间设置
    /*public void refreshTime(){
        RefreshLayout refreshLayout1 = view.findViewById(R.id.refreshLayout1);
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000*//*,false*//*);//false表示刷新失败
            }
        });

        refreshLayout1.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000*//*,false*//*);//false表示加载失败
            }
        });


    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin01:
                Intent intent=new Intent(getActivity(), Graph.class);
                startActivity(intent);
                break;
            case R.id.lin02:
                Intent intent2=new Intent(getActivity(), lineChart.class);
                startActivity(intent2);
                break;
            case R.id.lin03:
                Intent intent3=new Intent(getActivity(), Table.class);
                startActivity(intent3);
                break;
            case R.id.lin04:
                Intent intent4=new Intent(getActivity(), Card.class);
                startActivity(intent4);
                break;
        }
    }




}
