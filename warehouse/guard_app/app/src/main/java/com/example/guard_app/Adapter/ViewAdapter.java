package com.example.guard_app.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guard_app.Bean.listBean;
import com.example.guard_app.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewAdapter extends BaseAdapter {

    private LinkedList<listBean> mData;
    private Context mContext;

    public ViewAdapter(LinkedList<listBean> mData,Context mContext){
        this.mData=mData;
        this.mContext = mContext;

    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list,parent,false);
        ImageView img_icon = (ImageView)convertView.findViewById(R.id.headimage);
        TextView tv_1 = convertView.findViewById(R.id.tv_1);
        TextView tv_2 = convertView.findViewById(R.id.tv_2);
        TextView tv_3 = convertView.findViewById(R.id.tv_3);
        img_icon.setImageResource(mData.get(position).getImageID());
        tv_1.setText(mData.get(position).getTv_1());
        tv_2.setText(mData.get(position).getTv_2());
        tv_3.setText(mData.get(position).getTv_3());
        return convertView;
    }
}
