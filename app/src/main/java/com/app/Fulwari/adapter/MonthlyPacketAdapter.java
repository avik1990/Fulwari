package com.app.Fulwari.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.Fulwari.R;
import com.app.Fulwari.model.CustomPackData;
import com.app.Fulwari.model.FlowerProductBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MonthlyPacketAdapter extends RecyclerView.Adapter<MonthlyPacketAdapter.MyViewHolder> {
    List<CustomPackData.CustomPackDataModel> customPackDataModels;
    Context mContext;
    SubCategoryAdapter.AdapterPos adapterPos;

    public interface AdapterPos {
        void adapterPosition(int pos);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressbar;
        TextView tv_productname, tv_price;
        ImageView iv_productImg;
        RecyclerView rv_product_details;


        public MyViewHolder(View view) {
            super(view);
            progressbar = view.findViewById(R.id.progressbar);
            iv_productImg = (ImageView) view.findViewById(R.id.iv_chefimage);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_price = (TextView) view.findViewById(R.id.tv_price_orginal);
            rv_product_details = (RecyclerView) view.findViewById(R.id.rv_recyclerview);

        }
    }

    public MonthlyPacketAdapter(Context mContext, List<CustomPackData.CustomPackDataModel> customPackDataModels) {
        this.mContext = mContext;
        this.customPackDataModels = customPackDataModels;
    }

    @Override
    public void onBindViewHolder(final MonthlyPacketAdapter.MyViewHolder holder, int position) {
        CustomPackData.CustomPackDataModel c = customPackDataModels.get(position);
        LinearLayoutManager layoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.rv_product_details.setLayoutManager(layoutManager);

        MonthlyPacketProductDetailsAdapter ca = new MonthlyPacketProductDetailsAdapter(mContext, c.getPacketDetails());
        holder.rv_product_details.setAdapter(ca);

        holder.tv_productname.setText(c.getProduct_name_english());
        holder.tv_price.setText("\u20B9"+c.getProduct_price());

        final ProgressBar progressView=holder.progressbar;
        try {
            Picasso.with(mContext)
                    .load(c.getProduct_photo())
                    .into(holder.iv_productImg, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressView.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + customPackDataModels.size() + "]");
        return customPackDataModels.size();
    }


    @Override
    public MonthlyPacketAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.montly_custom_pack_row, parent, false);
        return new MonthlyPacketAdapter.MyViewHolder(v);
    }
}

