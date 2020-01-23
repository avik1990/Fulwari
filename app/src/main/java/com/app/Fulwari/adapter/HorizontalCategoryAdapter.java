package com.app.Fulwari.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.Fulwari.R;
import com.app.Fulwari.model.PredefinedPackCategoryData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HorizontalCategoryAdapter extends RecyclerView.Adapter<HorizontalCategoryAdapter.ViewHolder> {
    private List<PredefinedPackCategoryData.PredefinedPackList> categoryList=new ArrayList<>();
    Context context;
    Gson gson;
    private BrandSelectedLisener brandSelectedLisener;
  //  ListClickLisener clickLisener;

    public HorizontalCategoryAdapter(Context context, BrandSelectedLisener brandSelectedLisener) {
        this.context = context;
        this.brandSelectedLisener=brandSelectedLisener;
        gson=new Gson();
    }

    public void setData(List<PredefinedPackCategoryData.PredefinedPackList> bandsList){
        this.categoryList.clear();
        this.categoryList.addAll(bandsList);
        notifyDataSetChanged();
    }

    public void resetData(){
        this.categoryList.clear();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        RelativeLayout card_rl;


        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.band_name);
            card_rl = (RelativeLayout) view.findViewById(R.id.card_rl);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(PredefinedPackCategoryData.PredefinedPackList data:categoryList){
                        data.setSelected(false);
                    }
                    categoryList.get(getAdapterPosition()).setSelected(true);
                    brandSelectedLisener.onBrandsSelected(categoryList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
   // android:background="@{brands.isSelect?@drawable/image_background_border:@drawable/image_bg_blank}"

    @Override
    public void onBindViewHolder(final HorizontalCategoryAdapter.ViewHolder holder, int position) {
        PredefinedPackCategoryData.PredefinedPackList c = categoryList.get(position);
        holder.tv_name.setText(c.getCategory_name());
        if(c.isSelected())
            holder.card_rl.setBackground(context.getResources().getDrawable(R.drawable.image_background_border));
        else
            holder.card_rl.setBackground(context.getResources().getDrawable(R.drawable.image_bg_blank));
    }

    @Override
    public int getItemCount() {
       // Log.d("RV", "Item size [" + countryList.size() + "]");
        return categoryList.size();
    }


    @Override
    public HorizontalCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new HorizontalCategoryAdapter.ViewHolder(v);
    }

    public interface BrandSelectedLisener{
        void onBrandsSelected(PredefinedPackCategoryData.PredefinedPackList category);
    }

}

