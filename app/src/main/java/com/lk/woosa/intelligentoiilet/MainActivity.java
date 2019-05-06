package com.lk.woosa.intelligentoiilet;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IGetMessageCallBack {
    private MyServiceConnection serviceConnection;
    private IBinder binder;
    private final static int REQUEST_CODE = 101;

    private List<myBean> myBeanList = new ArrayList<>();
    private viewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mqtt service
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MainActivity.this);
        final Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        ListView listView = findViewById(R.id.listview);
        viewAdapter adapter = new viewAdapter(MainActivity.this,R.layout.view_adapter, myBeanList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView text = findViewById(R.id.text_dev_id);
                Intent intent_list=new Intent();
                intent_list.setClass(MainActivity.this, BindDevActivity.class);
                intent_list.putExtra("dev_id", myBeanList.get(position).getText());
                intent_list.putExtra("pos_id", position);

                Toast.makeText(getApplicationContext(), myBeanList.get(position).getText(), Toast.LENGTH_SHORT).show();

                startActivityForResult(intent_list, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == REQUEST_CODE && resultCode == BindDevActivity.RESULT_CODE) {
            Bundle bundle=data.getExtras();
            String strDevID = bundle.getString("dev_id");
            String strDevFloor = bundle.getString("dev_floor");
            String strDevSex = bundle.getString("dev_sex");
            int pos_id = bundle.getInt("pos_id");
            Log.i("onActivityResult: ",strDevID);
            Log.i("onActivityResult: ",strDevFloor);
            Log.i("onActivityResult: ",strDevSex);

            myBeanList.remove(pos_id);
            adapter.notifyDataSetChanged();


            Parcel data_bind=Parcel.obtain();
            data_bind.writeString("CMD_SET_BIND_DEV");
            data_bind.writeString(strDevID);
            data_bind.writeString(strDevFloor);
            data_bind.writeString(strDevSex);
            Parcel reply=Parcel.obtain();
            try {
                binder.transact(IBinder.LAST_CALL_TRANSACTION, data_bind, reply, 0);
            } catch (RemoteException e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private class MyServiceConnection implements ServiceConnection {
        private MQTTService mqttService;
        private IGetMessageCallBack IGetMessageCallBack;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = iBinder;
            mqttService = ((MQTTService.CustomBinder)iBinder).getService();
            mqttService.setIGetMessageCallBack(IGetMessageCallBack);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

        public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
            this.IGetMessageCallBack = IGetMessageCallBack;
        }
    }

    //mqtt service
    @Override
    public void setMessage(String message) {
        ListView listView = findViewById(R.id.listview);
        myBean bean1 = new myBean(message,R.drawable.bind);
        myBeanList.add(bean1);
        adapter = new viewAdapter(MainActivity.this,R.layout.view_adapter,myBeanList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.manage:
                myBeanList.clear();

                Parcel data=Parcel.obtain();
                data.writeString("CMD_GET_INIT_DATA");
                Parcel reply=Parcel.obtain();
                try {
                    binder.transact(IBinder.LAST_CALL_TRANSACTION, data, reply, 0);
                } catch (RemoteException e) {
                    //TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //从service里读数据
                //Log.i("Main<<<<<<<<",reply.readString());
                //Log.i("Main<<<<<<<<<",reply.readInt()+"");
                break;
            case R.id.add:
                Intent intent_list=new Intent();
                intent_list.setClass(MainActivity.this, MyAdapterActivity.class);
                startActivity(intent_list);
                break;
            case R.id.delete:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
