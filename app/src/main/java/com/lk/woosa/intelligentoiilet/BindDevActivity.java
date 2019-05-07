package com.lk.woosa.intelligentoiilet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BindDevActivity extends AppCompatActivity {
    private Button btn_bind;
    private TextView text_devid;
    private Spinner sp_floor;
    private Spinner sp_sex;
    private int pos_id;
    public final static int RESULT_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_dev);
        setTitle(getResources().getText(R.string.title_activity_bind_dev));
        final TextView dev_id = findViewById(R.id.text_dev_id);
        dev_id.setText(getIntent().getExtras().getString("dev_id"));
        pos_id = getIntent().getExtras().getInt("pos_id");

        btn_bind = findViewById(R.id.view_btn_bind);
        btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_devid = findViewById(R.id.text_dev_id);
                sp_floor = findViewById(R.id.spinner_floor);
                sp_sex = findViewById(R.id.spinner_sex);
                Intent intent_list=new Intent();
                intent_list.setClass(BindDevActivity.this, MainActivity.class);
                intent_list.putExtra("pos_id", pos_id);
                intent_list.putExtra("dev_id", text_devid.getText());
                intent_list.putExtra("dev_floor", sp_floor.getSelectedItem().toString());
                intent_list.putExtra("dev_sex", sp_sex.getSelectedItem().toString());
                setResult(RESULT_CODE, intent_list);
                finish();
                Toast.makeText(getApplicationContext(), "成功绑定设备"+text_devid.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
