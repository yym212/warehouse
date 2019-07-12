package com.example.guard_app.Activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.guard_app.Adapter.ViewAdapter;
import com.example.guard_app.Bean.listBean;
import com.example.guard_app.R;
import com.github.mikephil.charting.charts.LineChart;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Graph extends AppCompatActivity {

    private LineChart lineChart;
    private DynamicLineChartManager dynamicLineChartManager;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    private ListView listView;
    private List<listBean> mData = null;
    private Context mContext;
    private ViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = (LineChart) findViewById(R.id.qChart);
        showGraph();
        addData();
        adapter = new ViewAdapter((LinkedList<listBean>) mData,mContext);
        listView.setAdapter(adapter);

    }

    private void addData(){
        listView= (ListView)findViewById(R.id.list);
        mContext = Graph.this;
        mData = new LinkedList<listBean>();
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
        mData.add(new listBean(R.mipmap.fly,"编号","经度","纬度"));
    }


    private void showGraph(){

        //折线名字
        names.add("经度");
        names.add("纬度");
        names.add("高度");

        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);

        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour);
        dynamicLineChartManager.setYAxis(100, 0, 10);

        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.add((int) (Math.random() * 50) + 10);
                            list.add((int) (Math.random() * 80) + 10);
                            list.add((int) (Math.random() * 100));
                            dynamicLineChartManager.addEntry(list);
                            list.clear();
                        }
                    });
                }
            }
        }).start();
    }


}
