package com.zhour.zhoursecurity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhour.zhoursecurity.R;
import com.zhour.zhoursecurity.Utils.Utility;
import com.zhour.zhoursecurity.models.VisitorModel;

import java.util.ArrayList;

/**
 * Created by madhu on 29-Jul-17.
 */

public class InviteSearchAdapter extends BaseAdapter {
    private Context parent;
    private ArrayList<VisitorModel> visitorModels;
    private LayoutInflater layoutInflater;


    public InviteSearchAdapter(Context parent, ArrayList<VisitorModel> visitorModels) {
        this.parent = parent;
        this.visitorModels = visitorModels;
        layoutInflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return visitorModels.size();
    }

    @Override
    public Object getItem(int position) {
        return visitorModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_invite_serach_item, viewGroup, false);
            holder = new ViewHolder();

            holder.iv_clock = convertView.findViewById(R.id.iv_clock);
            holder.iv_calander = convertView.findViewById(R.id.iv_calander);
            holder.tv_visitor_name = convertView.findViewById(R.id.tv_visitor_name);
            holder.tv_contact_number = convertView.findViewById(R.id.tv_contact_number);
            holder.tv_resident_number = convertView.findViewById(R.id.tv_resident_number);

            holder.tv_visitor_details = convertView.findViewById(R.id.tv_visitor_details);
            holder.tv_resident_details = convertView.findViewById(R.id.tv_resident_details);
            holder.tv_resident_name = convertView.findViewById(R.id.tv_resident_name);
            holder.tv_resident_flat = convertView.findViewById(R.id.tv_resident_flat);

            /*FIRST AND LAST NAME */
            holder.tv_invitenote = convertView.findViewById(R.id.tv_invitenote);
            holder.tv_invitenote.setTypeface(Utility.setRobotoRegular(parent));
            holder.tv_inviteType = convertView.findViewById(R.id.tv_inviteType);
            holder.tv_invitenote.setTypeface(Utility.setRobotoRegular(parent));
            holder.tv_invitenote.setTypeface(Utility.setRobotoRegular(parent));
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.tv_date.setTypeface(Utility.setRobotoRegular(parent));
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_time.setTypeface(Utility.setRobotoRegular(parent));
            holder.tv_visitor_name.setTypeface(Utility.setRobotoRegular(parent));
            holder.tv_resident_number.setTypeface(Utility.setRobotoRegular(parent));

            holder.tv_resident_details.setTypeface(Utility.setRobotoBold(parent));
            holder.tv_visitor_details.setTypeface(Utility.setRobotoBold(parent));

            holder.iv_clock = convertView.findViewById(R.id.iv_clock);
            holder.iv_calander = convertView.findViewById(R.id.iv_calander);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        VisitorModel visitorModel = visitorModels.get(position);

        holder.tv_invitenote.setText(Utility.capitalizeFirstLetter(visitorModel.getInvitenote()));
        holder.tv_date.setText(visitorModel.getEventdate());
        holder.tv_visitor_name.setText(visitorModel.getVisitorname());
        holder.tv_contact_number.setText(visitorModel.getContactnumber());

        holder.tv_resident_name.setText(visitorModel.getResidentname());
        holder.tv_resident_number.setText(visitorModel.getResidentcontact1());
        holder.tv_resident_flat.setText("Flat No: " + visitorModel.getFlat());

        String inviteType = visitorModel.getInvitetype() + " Invitation";
        /*SET EVENT TIME */

        if (!Utility.isValueNullOrEmpty(visitorModel.getEventtime())) {
            holder.tv_time.setText(Utility.getTime(visitorModel.getEventtime()));
        }


        holder.tv_inviteType.setText(inviteType);
        return convertView;
    }

    private class ViewHolder {

        private TextView tv_invitenote;
        private ImageView iv_clock;
        private ImageView iv_calander;
        private TextView tv_date;
        private TextView tv_time;
        private TextView tv_inviteType;
        private TextView tv_visitor_name;
        private TextView tv_contact_number;

        private TextView tv_visitor_details;
        private TextView tv_resident_details;
        private TextView tv_resident_name;
        private TextView tv_resident_number;
        private TextView tv_resident_flat;

    }
}
