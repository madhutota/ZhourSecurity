package com.zhour.zhoursecurity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.models.VisitorModel;

import java.util.ArrayList;


public class InviteSearchListAdapter extends BaseAdapter {

    ArrayList<VisitorModel> dataList = null;
    Context mContext = null;
    LayoutInflater vi;

    public InviteSearchListAdapter(Context context, ArrayList<VisitorModel> dataList) {
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
            convertView = vi.inflate(R.layout.row_invite_serach, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VisitorModel prjctItem = dataList.get(position);
        String name = prjctItem.getVisitorname();
        holder.tv_title.setText(name);
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
    }
}