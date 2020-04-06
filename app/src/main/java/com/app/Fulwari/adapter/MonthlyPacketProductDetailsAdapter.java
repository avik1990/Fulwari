package com.app.Fulwari.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.Fulwari.R;
import com.app.Fulwari.model.CustomPackData;

import java.util.List;

public class MonthlyPacketProductDetailsAdapter extends RecyclerView.Adapter<MonthlyPacketProductDetailsAdapter.MyViewHolder> {
    List<CustomPackData.PacketDetails> packetDetails;
    Context mContext;

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_flowerName, tv_qty;

        public MyViewHolder(View view) {
            super(view);
            tv_flowerName = (TextView) view.findViewById(R.id.flowerName);
            tv_qty = (TextView) view.findViewById(R.id.qty);


        }
    }

    public MonthlyPacketProductDetailsAdapter(Context mContext, List<CustomPackData.PacketDetails> packetDetails) {
        this.mContext = mContext;
        this.packetDetails = packetDetails;
    }

    @Override
    public void onBindViewHolder(final MonthlyPacketProductDetailsAdapter.MyViewHolder holder, int position) {
        CustomPackData.PacketDetails c = packetDetails.get(position);

        holder.tv_flowerName.setText(c.getFlower_name());
        holder.tv_qty.setText(c.getFlower_quantity());


    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + packetDetails.size() + "]");
        return packetDetails.size();
    }


    @Override
    public MonthlyPacketProductDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_product_details_row, parent, false);
        return new MonthlyPacketProductDetailsAdapter.MyViewHolder(v);
    }
}

