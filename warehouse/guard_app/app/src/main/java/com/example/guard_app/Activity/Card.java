package com.example.guard_app.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guard_app.R;
import com.example.guard_app.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Card extends Activity implements SwipeFlingAdapterView.onFlingListener,SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener {

    int [] headerIcons = {
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6
    };

    String [] names = {"经纬 Matrice 200 V2 系列","御 Mavic 2 行业版","精灵 4 RTK","经纬 Matrice 200 系列","精灵 Phantom 4 Pro","经纬 Matrice 100"};//设备名称
    String [] location ={"经度:120","经度:50","经度:70","经度:90","经度:125","经度:32"};
    String [] latitude ={"纬度:20","纬度:80","纬度:120","纬度:80","纬度:100","纬度:132"};
    String [] height ={"高度:1200m","高度:1500m","高度:1800m","高度:1600m","高度:1900m","高度:2000m"};
    String [] speed = {"速度：60km/h", "速度：50km/h", "速度：55km/h", "速度：58km/h"};
    String [] direction ={"方向:东北方向","方向:东南方向","方向:西北方向","方向:西北方向",};
    String [] mtime ={"入侵时间:19:00","入侵时间:19:00","入侵时间:19:00","入侵时间:19:00","入侵时间:19:00"};
    String [] company = {"公司:大疆", "公司:零度智控", "公司:极飞", "公司:甄迪科技","公司:亿航","公司:中科","公司:科卫泰","公司:智能鸟","公司:华科尔","公司:普洛特"};//设备的公司
    String [] people = {"联系人:xxx","联系人:xxx","联系人:xxx","联系人:xxx","联系人:xxx","联系人:xxx"};//设备的联系人


    private int cardWidth;
    private int cardHeight;

    private SwipeFlingAdapterView swipeView;
    private InnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initView();
        loadData();
    }

    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));


        swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
        if (swipeView != null) {
            swipeView.setIsNeedSwipe(true);
            swipeView.setFlingListener(this);
            swipeView.setOnItemClickListener(this);

            adapter = new InnerAdapter();
            swipeView.setAdapter(adapter);
        }

        View v = findViewById(R.id.swipeLeft);
        if (v != null) {
            v.setOnClickListener(this);
        }
        v = findViewById(R.id.swipeRight);
        if (v != null) {
            v.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.swipeLeft:
                swipeView.swipeLeft();
                //swipeView.swipeLeft(250);
                break;
            case R.id.swipeRight:
                swipeView.swipeRight();
                //swipeView.swipeRight(250);
        }
    }

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {

    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 3) {
            loadData();
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {

    }

    //设备的排列顺序
    //背景土黄色:代码不规范
    private void loadData() {
        new AsyncTask<Void, Void, List<Talent>>() {
            @Override
            protected List<Talent> doInBackground(Void... params) {
                ArrayList<Talent> list = new ArrayList<>(10);
                Talent talent;
                for (int i = 0; i <10; i++) {
                    talent = new Talent();
                    talent.headerIcon = headerIcons[i % headerIcons.length];
                    talent.fly_name = names[i%names.length];
                    talent.fly_location = location[i%location.length];
                    talent.fly_latitude = latitude[i%latitude.length];
                    talent.fly_height = height[i%height.length];
                    talent.fly_speed = speed[i%speed.length];
                    talent.fly_direction = direction[i%direction.length];
                    talent.fly_time = mtime[i%mtime.length];
                    talent.fly_company = company[i%company.length];
                    talent.fly_people = people[i%people.length];

                    list.add(talent);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Talent> list) {
                super.onPostExecute(list);
                adapter.addAll(list);
            }
        }.execute();
    }

    private class InnerAdapter extends BaseAdapter {

        ArrayList<Talent> objs;

        public InnerAdapter() {
            objs = new ArrayList<>();
        }

        public void addAll(Collection<Talent> collection) {
            if (isEmpty()) {
                objs.addAll(collection);
                notifyDataSetChanged();
            } else {
                objs.addAll(collection);
            }
        }

        public void clear() {
            objs.clear();
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return objs.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < objs.size()) {
                objs.remove(index);
                notifyDataSetChanged();
            }
        }


        @Override
        public int getCount() {
            return objs.size();
        }

        @Override
        public Talent getItem(int position) {
            if(objs==null ||objs.size()==0) return null;
            return objs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // TODO: getView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Talent talent = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_item, parent, false);
                holder  = new ViewHolder();
                convertView.setTag(holder);
                convertView.getLayoutParams().width = cardWidth;
                holder.portraitView = (ImageView) convertView.findViewById(R.id.portrait);
                //holder.portraitView.getLayoutParams().width = cardWidth;
                holder.portraitView.getLayoutParams().height = cardHeight;
                holder.nameView = (TextView) convertView.findViewById(R.id.name);
                //parentView.getLayoutParams().width = cardWidth;
                //holder.jobView = (TextView) convertView.findViewById(R.id.job);
                //holder.companyView = (TextView) convertView.findViewById(R.id.company);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.latitude = (TextView) convertView.findViewById(R.id.latitude);
                holder.height = (TextView) convertView.findViewById(R.id.height);
                holder.speed = (TextView) convertView.findViewById(R.id.speed);
                holder.direction = (TextView) convertView.findViewById(R.id.direction);
                holder.mtime = (TextView) convertView.findViewById(R.id.mtime);
                holder.company = (TextView) convertView.findViewById(R.id.education);
                holder.people = (TextView) convertView.findViewById(R.id.work_year);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.portraitView.setImageResource(talent.headerIcon);

            holder.nameView.setText(String.format("%s", talent.fly_name));
            //holder.jobView.setText(talent.jobName);

            final CharSequence no = "暂无";

            holder.location.setHint(no);
            holder.location.setText(talent.fly_location);
//            holder.cityView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_location,0,0);
//
            holder.latitude.setHint(no);
            holder.latitude.setText(talent.fly_latitude);
//            holder.eduView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_edu,0,0);
//
            holder.height.setHint(no);
            holder.height.setText(talent.fly_height);
//            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_work_year,0,0);

            holder.speed.setHint(no);
            holder.speed.setText(talent.fly_speed);
//            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_work_year,0,0);

            holder.direction.setHint(no);
            holder.direction.setText(talent.fly_direction);
//            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_work_year,0,0);

            holder.mtime.setHint(no);
            holder.mtime.setText(talent.fly_time);
//            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_work_year,0,0);

            holder.company.setHint(no);
            holder.company.setText(talent.fly_company);
//            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_work_year,0,0);

            holder.people.setHint(no);
            holder.people.setText(talent.fly_people);
//            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home01_icon_work_year,0,0);


            return convertView;
        }

    }

    private static class ViewHolder {
        ImageView portraitView;
        TextView nameView;
        TextView location;
        TextView latitude;
        TextView height;
        TextView speed;
        TextView direction;
        TextView mtime;
        TextView company;
        TextView people;
        CheckedTextView collectView;

    }

    public static class Talent {
        public int headerIcon;
        public String fly_name;
        public String fly_location;
        public String fly_latitude;
        public String fly_height;
        public String fly_speed;
        public String fly_direction;
        public String fly_time;
        public String fly_company;
        public String fly_people;

    }

}
