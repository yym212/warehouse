package com.example.guard_app.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.guard_app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class lineChart extends AppCompatActivity {

    private LineChart mLineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        mLineChart = findViewById(R.id.chart);
        mLineData();//折线图显示
    }

    //折线图
    private void mLineData(){
        //设置x轴和y轴的点
        List<Entry> entries = new ArrayList<>();
        for (int i = 0;i<12;i++){
            entries.add(new Entry(i,new Random().nextInt(300)));
        }

        //数据赋值到线条
        LineDataSet dataSet = new LineDataSet(entries,"Label");

        //设置数据刷新图表
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.invalidate();//refresh

        //设置样式
        YAxis rightAxis = mLineChart.getAxisRight();

        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);
        YAxis leftAxis = mLineChart.getAxisLeft();
        //设置图表左边的y轴禁用
        leftAxis.setEnabled(true);
        //设置x轴
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#1781b5"));
        //xAxis.setTextSize(11f);
        //xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(true);//设置X轴上每个点对应的线
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的显示位置
        //xAxis.setGranularity(1);//禁止放大后x轴标签重绘

        //透明化图列
        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        mLineChart.setDescription(description);

        //X轴标签自定义标签
        List<String> list = new ArrayList<>();
        for (int i = 0;i<12;i++){
            list.add(String.valueOf(i+1).concat("月"));
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(list));
    }
}
