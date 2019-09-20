package com.example.androidapp;

import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.Wz_Link.SdkDataListener;
import com.wzzc.Wz_Link.WZ_Link_Inter;
import com.wzzc.commpg.Host_Pkt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button attentionBtn,closeBtn,historyBtn,sendBtn;
    private TextView tempTV,humidityTV,lightTV,peopleTV,fenTV,infoTV;
    private EditText editText;
    private ImageView imageView;
    private Message message;
    private AlertDialog.Builder builder;
    private WZ_Link_Inter wzLinkInter;
    private Host_Pkt hostPkt;
    private ListView tempListView;
    private ListView peopleListView;
    private ArrayAdapter<String> tempAdapter;
    private ArrayAdapter<String> peopleAdapter;
    private List<String> tempList = new ArrayList<>();
    private List<String> peopleList = new ArrayList<>();
    private List<Double> tempAvg = new ArrayList<>();
    private SQLiteDatabase db;
    private TempHelper helper;
    private StringBuffer stringBuffer;
    private ClientThread clientThread;
    private Vibrator vibrator;

    private class DataHandler extends Handler {
        private WeakReference<MainActivity> weakReference;
        public DataHandler(WeakReference<MainActivity> weakReference){
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //温度
                case 0:
                    String tempValue = msg.obj.toString();
                    tempTV.setText(tempValue);
                    //获取当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月-dd日 HH时:mm分:ss秒");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String currentTime = dateFormat.format(currentDate);
                    String tempTime = tempValue+" "+currentTime;

                    //温度+时间 listView显示
                    tempList.add(0,tempTime);
                    tempListView.setAdapter(tempAdapter);
                    tempAdapter.notifyDataSetChanged();

                    db = helper.getWritableDatabase();
                    String sql = "insert into temp_tb(avg,longTime) values(?,?)";

                    double d = Double.parseDouble(tempValue);
                    tempAvg.add(0,d);
                    double num = 0;
                    double currentAvg = 0;

                    if (tempAvg != null && tempAvg.size() > 10){
                        tempAvg.remove(tempAvg.size()-1);
                    }

                    if (tempAvg != null && tempAvg.size() == 10){
                        for (int i = 0;i < tempAvg.size();i++){
                            num += tempAvg.get(i);
                            currentAvg = num / 10;
                            currentAvg = (double) Math.round(currentAvg*100)/100;
                        }

                        if(currentAvg > 30){
                            System.out.println("温度高于30的平均值1: =======================>"+currentAvg);

                            db.execSQL(sql,new Object[]{currentAvg,currentTime});
                            wzLinkInter.senKey("30012",1);
                            fenTV.setText("开");
                            wzLinkInter.senKey("30023",1);
                        }else{
                            System.out.println("温度低于30的平均值2: =======================>"+currentAvg);
                            wzLinkInter.senKey("30012",2);
                            fenTV.setText("关");
                        }

                    }

                    break;

                 //湿度
                case 1:
                    humidityTV.setText(msg.obj.toString());
                    break;

                 //光照
                case 2:
                    Double light_d = Double.parseDouble(msg.obj.toString())*10;

                    lightTV.setText(String.valueOf(light_d));
                    if (light_d < 80){
                        wzLinkInter.ControlDev("30018","1",1);
                    }else if (light_d >= 80){
                        //wzLinkInter.ControlDev("30018","1",9);
                        wzLinkInter.ControlDev("30018","1",2);
                    }
                    break;

                 //人体红外
                case 3:
                    String peopleValue = msg.obj.toString();
                    if (peopleValue.equals("0.1")){
                        peopleTV.setText("有人");
                        imageView.setImageResource(R.drawable.red);
                        wzLinkInter.senKey("30023",2);
                    }else if (peopleValue.equals("0.2")){
                        peopleTV.setText("无人");
                        imageView.setImageResource(R.drawable.green);
                    }

                    if (msg.obj != null){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月-dd日 HH时:mm分:ss秒");
                        Date date = new Date(System.currentTimeMillis());
                        String currentPeople = format.format(date);
                        String peopleTime = peopleTV.getText().toString()+" "+currentPeople;
                        peopleList.add(0,peopleTime);
                        peopleListView.setAdapter(peopleAdapter);
                        peopleListView.deferNotifyDataSetChanged();
                    }

                    break;

                 //风扇状态
                case 4:
                    Log.d("tag", "风扇状态:================ "+msg.obj.toString());
                    if (msg.obj.toString().equals("0.2")){
                        fenTV.setText("关");
                    }else if (msg.obj.toString().equals("0.1")){
                        fenTV.setText("开");
                    }
                    break;

                //PC端传来的消息
                case 5:
                    if (msg.obj!=null){
                        infoTV.setText(msg.obj.toString());
                        wzLinkInter.senKey("30023",2);
                        openMusic();
                    }
                    break;

                //点击发送后更新界面控件
                case 6:
                    editText.setText("");
                    break;
                case 7:
                    Toast.makeText(MainActivity.this,"服务器已断开连接，正在请求重连",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    DataHandler handler = new DataHandler(new WeakReference<MainActivity>(this));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        login();
        //开启线程监听PC端传过来的消息
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start();
    }

    //控件初始化
    private void initView(){
        tempTV = findViewById(R.id.temp);
        humidityTV = findViewById(R.id.humidity);
        lightTV = findViewById(R.id.light);
        peopleTV = findViewById(R.id.people);
        fenTV = findViewById(R.id.fen);
        infoTV = findViewById(R.id.info);
        editText = findViewById(R.id.sendInfo);
        tempListView = findViewById(R.id.tempListView);
        peopleListView = findViewById(R.id.peopleListView);
        attentionBtn = findViewById(R.id.attention);
        closeBtn = findViewById(R.id.close);
        sendBtn = findViewById(R.id.send);
        historyBtn = findViewById(R.id.history);
        imageView = findViewById(R.id.imageView);
        builder = new AlertDialog.Builder(this);
        this.wzLinkInter = new WZ_Link_Inter("123");

        //监听设备发送过来的信息
        wzLinkInter.setSdkDataListener(new DataListener());

        //设置温度的适配器
        tempAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,tempList);
        //设置人体红外的适配器
        peopleAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,peopleList);

        //将数据库的name,version传递过来
        helper = new TempHelper(this);
    }

    public void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isWiFiConnected()){
                    Toast.makeText(MainActivity.this, "请先连接物联网所在的路由wifi", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }).start();
        hostPkt = new Host_Pkt("192.168.10.100",268513265,-1);
        wzLinkInter.login(hostPkt);
        closeBtn.setEnabled(false);
    }


    public void onClick(View view) {
        switch (view.getId()){

            //关注设备
            case R.id.attention:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wzLinkInter.attentionDev();
                    }
                }).start();
                closeBtn.setEnabled(true);
                attentionBtn.setEnabled(false);
                break;

            //取消关注
            case R.id.close:
                wzLinkInter.cancelAttentionVariable();
                tempTV.setText("");
                humidityTV.setText("");
                lightTV.setText("");
                peopleTV.setText("");
                fenTV.setText("");
                attentionBtn.setEnabled(true);
                break;

            //查看历史记录
            case R.id.history:
                showDialog();
                break;

            //向PC端发送消息
            case R.id.send:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (editText.getText()!=null && editText.getText().length() > 0){
                                String value = editText.getText().toString();
                                if (clientThread.outputStream!=null) {
                                        clientThread.outputStream = clientThread.socket.getOutputStream();
                                        clientThread.outputStream.write(value.getBytes("utf-8"));
                                        clientThread.outputStream.flush();
                                        message = new Message();
                                        message.what = 6;
                                        message.obj = value;
                                        handler.sendMessage(message);
                                }
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                            message = new Message();
                            message.what = 7;
                            handler.sendMessage(message);
                            System.out.println("服务器已断开连接。。。");
                        }
                    }
                }).start();

                break;
        }
    }

    private class DataListener implements SdkDataListener{

        @Override
        public void getExit(boolean b) {

        }

        @Override       //s:uiid,s1:rfid,s2:index,s3:value
        public void getDevData(String s, String s1, String s2, String s3) {
            synchronized (this){
                if (s.equals("30008")){
                    if (s2.equals("1")){
                        message = new Message();
                        message.what = 0;
                        message.obj = s3;
                        handler.sendMessage(message);
                    }
                    if (s2.equals("2")){
                        message = new Message();
                        message.what = 1;
                        message.obj = s3;
                        handler.sendMessage(message);
                    }
                }

                if (s.equals("30009")) {
                    if (s2.equals("1")) {
                        message = new Message();
                        message.what = 2;
                        message.obj = s3;
                        handler.sendMessage(message);
                    }
                }

                if (s.equals("30011")) {
                    if (s2.equals("1")) {
                        message = new Message();
                        message.what = 3;
                        message.obj = s3;
                        handler.sendMessage(message);
                    }
                }

                if (s.equals("30012")) {
                    if (s2.equals("1")) {
                        message = new Message();
                        message.what = 4;
                        message.obj = s3;
                        handler.sendMessage(message);
                    }
                }

            }
        }

        @Override
        public void getLandState(String s) {

        }

        @Override
        public void getAttState(boolean b) {

        }

        @Override
        public void getConnState(boolean b) {

        }

        @Override
        public void getDevUiid(int i, String s, String s1) {

        }

        @Override
        public void getControlState(String s, boolean b) {

        }

        @Override
        public void getGroup(Short aShort) {

        }

        @Override
        public void onSearchFinish(Integer integer) {

        }

        @Override
        public void onSearching(String s, Integer integer) {

        }
    }

    //弹出对话框，并查询数据
    private void showDialog(){
        stringBuffer = new StringBuffer();
        db = helper.getReadableDatabase();
        String sql = "select * from temp_tb order by id desc limit 5";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor != null){
            while (cursor.moveToNext()){
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                Double avg = cursor.getDouble(1);
                String longTime = cursor.getString(2);
                stringBuffer.append("序号:"+id+" "+"报警值:"+avg+" "+"报警时间:"+longTime+"\n");
            }
        }
        cursor.close();
        db.close();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("历史记录")
                .setMessage(stringBuffer.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }


    //判断是否连接上wifi
    public boolean isWiFiConnected(){
        ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info.isConnected()){
            return true;
        }
        return false;
    }

    //设置声音
    public void openMusic(){
        //初始化系统声音
        RingtoneManager ringtoneManager = new RingtoneManager(getApplicationContext());

        //获取系统声音的默认路径
        //TYPE_RINGTONE         电话铃声
        //TYPE_ALARM            闹钟铃声
        //TYPE_NOTIFICATION     通知提示音
        Uri uri = ringtoneManager.getDefaultUri(ringtoneManager.TYPE_NOTIFICATION);

        //通过Uri来获取提示音的实例对象
        Ringtone ringtone = ringtoneManager.getRingtone(getApplicationContext(),uri);

        //播放
        ringtone.play();
    }

    //手机振动 只支持api26以上的
    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void phoneVibrator(){
        //获取系统的Vibrator服务
        vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        //控制手机振动两秒,振动幅度为255
        vibrator.vibrate(VibrationEffect.createOneShot(2000,255));
    }*/


    //设置竖屏显示
    /*@Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }*/

    //判断是否退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wzLinkInter.exit();
                    System.exit(0);
                }
            };
            builder.setMessage("是否退出程序?")
                    .setPositiveButton("确定",listener)
                    .setNegativeButton("取消",null);
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
