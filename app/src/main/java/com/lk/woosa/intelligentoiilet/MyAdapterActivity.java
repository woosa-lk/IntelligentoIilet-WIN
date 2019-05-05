package com.lk.woosa.intelligentoiilet;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterActivity extends ListActivity{
    private List<Map<String, Object>> list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取数据
        list=getData();

        //创建自定义的Adapter对象--内部数据与ListView各项的对应关系在自定义的Adapter中实现
        adapter = new MyAdapter(this);

        //为ListActivity中的ListView设置Adapter
        setListAdapter(adapter);
    }

    //获取List数据对象
    public List<Map<String,Object>> getData(){
        //List对象
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

        //List中存放的Map对象,由多个<键,值>对构成--一个Map对象对应ListView中的一行
        Map map;
        int floor_num;
        int floor_max_num = 22;
        for (floor_num = 1; floor_num <= floor_max_num; floor_num++) {
            String floor = floor_num + "F";
            map = new HashMap<String,Object>();
            map.put("title", floor);
            map.put("man", "男");
            map.put("woman", "女");
            map.put("occupy", "占用数");
            map.put("idle", "空闲数");
            map.put("occupy_man", "0");
            map.put("idle_man", "0");
            map.put("occupy_woman", "0");
            map.put("idle_woman", "0");
            map.put("img", R.drawable.toilet);
            map.put("img_man_status", R.drawable.busy);
            map.put("img_woman_status", R.drawable.busy);
            list.add(map);
        }

        return list;
    }

    //自定义的Adapter类
    /**Android系统更新ListView时需要调用相关的Adapter的方法:
     *      1)更新前首先调用getCount()获取需要更新的行数,然后更新过程逐行进行
     *      2)更新每行时,需要调用getView()获取当前行对应的View对象，
     *            Adapter需要在getView()方法中适时创建View对象，并对View对象填充需要显示的内容
     * */
    public final class MyAdapter extends BaseAdapter {
        //实例化布局对象---用于实例化每行的布局->View对象
        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        //获取ListView的总行数
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //ListView中一行对应的对象组合--容器类
        //使用ViewHolder可以减少findViewById()的使用频率,方便数据访问
        public final class ViewHolder{
            public ImageView img;
            public ImageView img_man_status;
            public ImageView img_woman_status;
            public TextView title;
            public TextView man;
            public TextView woman;
            public TextView occupy;
            public TextView idle;
            public TextView occupy_man;
            public TextView idle_man;
            public TextView occupy_woman;
            public TextView idle_woman;
            public Button viewBtn_check;
        }

        //获取指定的一行所对应的View对象--不存在的话则创建之
        // position--当前要显示的数据的位置(行号)
        // convertView--可利用的以前的View对象(上下滚动时,利用旧View对象显示新内容),
        //              如果此项为空,则需要动态创建新的View对象
        // parent--父控件(上层的ListView)
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            //本行对应的容器对象
            ViewHolder holder = null;

            //如果该行的View为空, 则动态创建新的View
            //利用已有的View显示新数据,可以减少内存占用,优化响应速度
            if (convertView == null) {
                //先创建容器对象,以便后来向其中填充当前行中的控件对象
                holder=new ViewHolder();

                //实例化ListView的一行, root参数为空说明此View的父控件默认为为上层的ListView
                convertView = mInflater.inflate(R.layout.activity_my_adapter, null);
                //获取内部的各个控件对象, 保存到容器对象中, 以后直接取来用即可--每个子控件对象只用一次findViewById()
                holder.img = convertView.findViewById(R.id.img);
                holder.img_man_status = convertView.findViewById(R.id.img_man_status);
                holder.img_woman_status = convertView.findViewById(R.id.img_woman_status);
                holder.title = convertView.findViewById(R.id.title);
                holder.man = convertView.findViewById(R.id.man);
                holder.woman = convertView.findViewById(R.id.woman);
                holder.occupy = convertView.findViewById(R.id.occupy);
                holder.idle = convertView.findViewById(R.id.idle);
                holder.occupy_man = convertView.findViewById(R.id.occupy_man);
                holder.idle_man = convertView.findViewById(R.id.idle_man);
                holder.occupy_woman = convertView.findViewById(R.id.occupy_woman);
                holder.idle_woman = convertView.findViewById(R.id.idle_woman);
                holder.viewBtn_check = convertView.findViewById(R.id.view_btn_check);

                //设置容器对象为ListView当前行的Tag--建立容器类对象与ListView当前行的联系
                convertView.setTag(holder);
            }
            else {   //如果该行的View已经存在,则通过标记获取该行对应的对象
                holder = (ViewHolder)convertView.getTag();
            }

            //设置该行内的控件对象对应的属性，Adapter的作用（List<--->ListView）--- 如果不用ViewHolder则需要频繁使用findViewByID
            holder.img.setBackgroundResource((Integer)list.get(position).get("img"));
            holder.img_man_status.setBackgroundResource((Integer)list.get(position).get("img_man_status"));
            holder.img_woman_status.setBackgroundResource((Integer)list.get(position).get("img_woman_status"));
            holder.title.setText((String)list.get(position).get("title"));
            holder.man.setText((String)list.get(position).get("man"));
            holder.woman.setText((String)list.get(position).get("woman"));
            holder.occupy.setText((String)list.get(position).get("occupy"));
            holder.idle.setText((String)list.get(position).get("idle"));
            holder.occupy_man.setText((String)list.get(position).get("occupy_man"));
            holder.idle_man.setText((String)list.get(position).get("idle_man"));
            holder.occupy_woman.setText((String)list.get(position).get("occupy_woman"));
            holder.idle_woman.setText((String)list.get(position).get("idle_woman"));

            //绑定该行中的Button对象的监听器
            //创建监听器对象时, 用参数传递当前的行号
            //每行中的Button建一个监听器对象,不同对象的position值不同
            holder.viewBtn_check.setOnClickListener(new viewButtonClickListener(position));

            return convertView;//返回当前行对应的View对象
        }
    }

    //重写此方法---点击ListView一行时的回调函数--参数含义同前
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String s=list.get(position).get("title").toString(); //获取该行的Map对象的指定属性的数据内容
        Toast.makeText(MyAdapterActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    //使用内部类实现ListView的每行中按钮的监听函数
    //该监听器类会为ListView的每一行提供一个监听器对象,用来监听该行中按钮的点击事件
    class viewButtonClickListener implements View.OnClickListener {
        //记录按钮所在的行号
        int position;

        //必须使用自定义的构造函数---因为需要在此通过参数记录该监听器对象监听的行号
        public viewButtonClickListener(int pos) {
            position=pos;
        }

        @Override
        public void onClick(View v) {
            //从数据源data中删除数据
            //list.remove(list.get(position));
            //通知适配器更新UI
            //Intent intent_list=new Intent();
            //intent_list.setClass(MyAdapterActivity.this, addDeviceActivity.class);
            //startActivity(intent_list);
            //adapter.notifyDataSetChanged();
        }
    }
}
