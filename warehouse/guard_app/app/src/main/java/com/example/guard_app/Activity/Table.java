package com.example.guard_app.Activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.PageTableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.example.guard_app.Bean.UserInfo;
import com.example.guard_app.R;

import java.util.ArrayList;

public class Table extends AppCompatActivity {

    private SmartTable table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setStyle();
    }

    private void setStyle(){

        //普通列
        Column<String> id = new Column<>("无人机编号", "id");
        Column<String> longitude = new Column<>("经度", "longitude");
        Column<String> latitude = new Column<>("纬度", "latitude");
        Column<String> altitude = new Column<>("高度", "altitude");
        Column<String> status = new Column<>("状态", "status");
        Column<String> speed = new Column<>("飞行速度", "speed");
        Column<String> scope = new Column<>("飞行范围", "scope");
        Column<String> time = new Column<>("入侵时间", "time");
        //设置该列当字段相同时自动合并
        //id.setAutoMerge(true);

        ArrayList<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));
        list.add(new UserInfo("001",100, 150, 50, "已捕获", "100m/s", "100-200", "12:00:00"));

        //表格数据 datas 是需要填充的数据
        PageTableData<UserInfo> pageTableData = new PageTableData<>("无人机信息表", list, id, longitude, latitude, altitude, status, speed, scope, time);
        //设置每页的行数
        pageTableData.setPageSize(10);

        //设置数据
        table = findViewById(R.id.table);
        table.setTableData(pageTableData);

        //设置是否显示顶部序列号,默认显示ABCD...
        table.getConfig().setShowXSequence(false);

        //设置是否显示左侧序列号(行号)
        table.getConfig().setShowYSequence(true);

        //去除标题
        /*table.getConfig().setShowTableTitle(false);*/
        //标题样式
        table.getConfig().setTableTitleStyle(new FontStyle(50, Color.BLACK));



        /*设置字体和字体颜色*/
        table.getConfig().setContentStyle(new FontStyle(50, Color.BLACK));

        //设置表格偶数行的背景色
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0){
                    return ContextCompat.getColor(getApplicationContext(),R.color.ou);
                }
                return TableConfig.INVALID_COLOR;
            }
        };


        //设置行号的颜色
        ICellBackgroundFormat<Integer> backgroundFormat1 = new BaseCellBackgroundFormat<Integer>() {
            @Override
            public int getBackGroundColor(Integer position) {
                if (position % 2 == 0){
                    return ContextCompat.getColor(getApplicationContext(),R.color.line);
                }
                return TableConfig.INVALID_COLOR;
            }

            //设置行号的字体颜色
            @Override
            public int getTextColor(Integer position) {
                if (position%2==0){
                    return ContextCompat.getColor(getApplicationContext(),R.color.colorAccent);
                }
                return TableConfig.INVALID_COLOR;
            }
        };

        table.getConfig().setContentCellBackgroundFormat(backgroundFormat).setYSequenceCellBgFormat(backgroundFormat1);



        //固定指定列
        id.setFixed(true);

        //Y,X,列标题,统计行序号列
        table.getConfig().setFixedYSequence(true);
        table.getConfig().setFixedYSequence(true);
        table.getConfig().setFixedCountRow(true);
        table.getConfig().setFixedTitle(true);

        //表格放大缩小
        table.setZoom(true);

        FontStyle fontStyle = new FontStyle();

        table.setSortColumn(id,true);


        //点击事件
        table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {

            }
        });

    }
}
