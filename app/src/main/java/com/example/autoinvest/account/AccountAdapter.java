package com.example.autoinvest.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.autoinvest.R;
import java.util.List;



public class AccountAdapter extends ArrayAdapter {

    private Context context;

    public AccountAdapter(Context context, List<InformationObject> list) {
        super(context, 0,list);
        this.context = context;
    }

    class ViewHolder{
        TextView name;
        TextView value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        InformationObject obj = (InformationObject)getItem(position);
        ViewHolder vh;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.account_item,parent,false);
            vh = new ViewHolder();
            vh.name = (TextView)convertView.findViewById(R.id.name_value);
            vh.value = (TextView)convertView.findViewById(R.id.num_value);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.name.setText(obj.getName());
        vh.value.setText(obj.getValue());
        return convertView;
    }
}
