package com.zhour.zhoursecurity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.models.SpinnerModel;

import java.util.ArrayList;


public class SpinnerAdapter extends BaseAdapter {

    ArrayList<SpinnerModel> dataList = null;
    Context mContext = null;
    LayoutInflater vi;

    public SpinnerAdapter(Context context, ArrayList<SpinnerModel> dataList) {
        mContext = context;
        this.dataList = dataList;
        vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return dataList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = vi.inflate(R.layout.row_spinner_dialog, null);
            holder = new ViewHolder();

            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        SpinnerModel prjctItem = dataList.get(position);
        String name = prjctItem.getTitle();
        holder.tv_title.setText(name);
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
    }
}