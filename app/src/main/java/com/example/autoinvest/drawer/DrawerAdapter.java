package com.example.autoinvest.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autoinvest.R;

import java.lang.reflect.Array;



public class DrawerAdapter extends ArrayAdapter {

    private Context context;

    public DrawerAdapter(Context context, String[] str) {
        super(context, 0 ,str);
        this.context = context;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textView;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String option = (String)getItem(position);
        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.drawer_item,parent,false);
            vh = new ViewHolder();
            vh.imageView = (ImageView) convertView.findViewById(R.id.drawer_icon);
            vh.textView = (TextView)convertView.findViewById(R.id.drawer_text);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.textView.setText(option);
        Drawable drawable;

        if(option.equals("Account")){
            drawable = context.getDrawable(R.drawable.ic_wallet);
        }else if(option.equals("Analytics")){
            drawable = context.getDrawable(R.drawable.ic_graph);
        }else{
            drawable = context.getDrawable(R.drawable.ic_quit);
        }
        int color = context.getColor(R.color.colorPrimaryDark);
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        vh.imageView.setImageDrawable(drawable);

        return convertView;
    }
}
