package com.example.guard_app.Activity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*曲线图管理类*/
public class DynamicLineChartManager {

    private LineChart lineChart;
    private XAxis xAxis;    //  X轴
    private YAxis leftYAxis;    //左侧Y轴
    private YAxis rightAxis;    //右侧Y轴
    private Legend legend;  //图例
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式  
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间


    public  DynamicLineChartManager(LineChart lineChart,List<String> names,List<Integer> colors){
        this.lineChart=lineChart;
        leftYAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initChart();
        initLineDataSet(names,colors);
    }

    //初始化图表
    private void initChart(){

        //是否展示网格线
        lineChart.setDrawGridBackground(false);

        //是否显示边界
        lineChart.setDrawBorders(true);

        //是否可以拖动
        lineChart.setDragEnabled(false);

        //是否有触摸事件
        lineChart.setTouchEnabled(true);

        //设置XY轴动画效果
        lineChart.animateX(1500);
        lineChart.animateY(2500);

        //xy轴的设置
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();

        //X轴设置显示在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(10);

        //保证Y轴从0开始,不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });

        //折线图例标签设置
        legend = lineChart.getLegend();

        //设置显示类
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);

        //显示位置，左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        //是否绘制在图表里
        legend.setDrawInside(false);

    }

    private void initLineDataSet(List<String> names, List<Integer> colors) {

        for (int i = 0; i < names.size(); i++) {
            lineDataSet = new LineDataSet(null, names.get(i));
            lineDataSet.setColor(colors.get(i));
            lineDataSet.setLineWidth(1.5f);
            lineDataSet.setCircleRadius(1.5f);
            lineDataSet.setColor(colors.get(i));

            lineDataSet.setDrawFilled(true);
            lineDataSet.setCircleColor(colors.get(i));
            lineDataSet.setHighLightColor(colors.get(i));
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSet.setValueTextSize(10f);
            lineDataSets.add(lineDataSet);


        }
        //添加一个空的 LineData
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }


    //动态添加数据
    public void  addEntry(List<Integer> numbers){
        if (lineDataSets.get(0).getEntryCount()==0){
            lineData = new LineData(lineDataSets);
            lineChart.setData(lineData);
        }
        if (timeList.size()>11){
            timeList.clear();
        }
        timeList.add(df.format(System.currentTimeMillis()));
        for (int i=0;i<numbers.size();i++){
            Entry entry = new Entry(lineDataSet.getEntryCount(),numbers.get(i));
            lineData.addEntry(entry,i);
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(6);
            lineChart.moveViewToX(lineData.getEntryCount()-5);
        }
    }

    //设置Y轴值
    public void setYAxis(float max,float min,int labelCount){
        if (max<min){
            return;
        }
        leftYAxis.setAxisMaximum(max);
        leftYAxis.setAxisMinimum(min);
        leftYAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    //设置高限制线
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftYAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    //设置低限制线
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftYAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    //设置描述信息
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }

}
