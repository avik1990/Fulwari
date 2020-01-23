package com.app.Fulwari.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.Fulwari.R;
import com.app.Fulwari.model.FlowerProductBean;

import java.util.List;

public class CustomPacketAdapter extends RecyclerView.Adapter<CustomPacketAdapter.MyViewHolder> {
    List<FlowerProductBean.Packet> countryList;
    Context mContext;
    SubCategoryAdapter.AdapterPos adapterPos;

    public interface AdapterPos {
        void adapterPosition(int pos);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_productname, tv_mon,rv_sun,tv_tue,tv_wed,tv_thu,tv_fri,tv_sat;


        public MyViewHolder(View view) {
            super(view);
            tv_mon = (TextView) view.findViewById(R.id.mon_value);
            tv_productname = (TextView) view.findViewById(R.id.product_name);
            rv_sun = (TextView) view.findViewById(R.id.sun_value);
            tv_tue = (TextView) view.findViewById(R.id.tue_value);
            tv_wed = view.findViewById(R.id.wed_value);
            tv_thu = view.findViewById(R.id.thu_value);
            tv_fri = view.findViewById(R.id.fri_value);
            tv_sat = view.findViewById(R.id.sat_value);
        }
    }

    public CustomPacketAdapter(Context mContext, List<FlowerProductBean.Packet> countryList) {
        this.mContext = mContext;
        this.countryList = countryList;
    }

    @Override
    public void onBindViewHolder(final CustomPacketAdapter.MyViewHolder holder, int position) {
        FlowerProductBean.Packet c = countryList.get(position);
        holder.rv_sun.setText(c.getSunday());
        holder.tv_mon.setText(c.getMonday());
        holder.tv_tue.setText(c.getTuesday());
        holder.tv_wed.setText(c.getWednesday());
        holder.tv_thu.setText(c.getTuesday());
        holder.tv_fri.setText(c.getFriday());
        holder.tv_sat.setText(c.getSaturday());
        holder.tv_productname.setText(c.getFlower_name());

    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + countryList.size() + "]");
        return countryList.size();
    }


    @Override
    public CustomPacketAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_packet_row, parent, false);
        return new CustomPacketAdapter.MyViewHolder(v);
    }
}

