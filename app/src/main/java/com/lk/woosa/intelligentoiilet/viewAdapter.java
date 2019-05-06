package com.lk.woosa.intelligentoiilet;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class viewAdapter extends ArrayAdapter {
    private final int ImageId;

    class ViewHolder{
        ImageView fruitImage;
        TextView fruitDevID;
        LinearLayout fruitLayout;
        //Button fruitBtn;
    }

    public viewAdapter(Context context, int headImage, List<myBean> obj){
        super(context,headImage,obj);
        ImageId = headImage;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        myBean myBean = (myBean) getItem(position);
        View view ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(ImageId,parent,false);
            viewHolder.fruitImage = view.findViewById(R.id.img_dev);
            viewHolder.fruitDevID = view.findViewById(R.id.text_dev_id);
            viewHolder.fruitLayout = view.findViewById(R.id.ll_view);
            //viewHolder.fruitBtn = view.findViewById(R.id.view_btn_bind);
            view.setTag(viewHolder);
        } else {
            view =convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        //viewHolder.fruitImage.setImageResource(myBean.getImageID());
        viewHolder.fruitDevID.setText(myBean.getText());
        viewHolder.fruitLayout.setTag(position);
        return view;
    }
}
