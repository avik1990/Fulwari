package com.app.Fulwari.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.Fulwari.FlowersCategory;
import com.app.Fulwari.ProductPage;
import com.app.Fulwari.R;
import com.app.Fulwari.model.Category;
import com.app.Fulwari.utils.Preferences;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Avik on 11-01-2017.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category.CategoryDatum> countryList;
    Context mContext;

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_catimage;
        TextView tv_categoryname;
        ProgressBar progressbar;
        CardView card_view;


        public MyViewHolder(View view) {
            super(view);
            iv_catimage = view.findViewById(R.id.iv_catimage);
            tv_categoryname = view.findViewById(R.id.tv_categoryname);
            progressbar = view.findViewById(R.id.progressbar);
            card_view = view.findViewById(R.id.card_view);

            card_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Preferences.set_dashCatId(mContext, countryList.get(getAdapterPosition()).getCategoryId());

                    if(countryList.get(getAdapterPosition()).categoryName.equalsIgnoreCase("Flowers")){
                        Intent a = new Intent(mContext, FlowersCategory.class);
                        // a.putExtra("position", "" + pos);
                        a.putExtra("category_id", countryList.get(getAdapterPosition()).getCategoryId());
                        a.putExtra("from", countryList.get(getAdapterPosition()).categoryName);
                        a.putExtra("sub_category_id", "0");
                        mContext.startActivity(a);
                    }else {
                   /* Intent i = new Intent(mContext, SubCategoryPage.class);
                    i.putExtra("from", holder.tv_categoryname.getText().toString().trim());
                    i.putExtra("cat_id", countryList.get(position).getCategoryId());
                    mContext.startActivity(i);*/
                        Intent a = new Intent(mContext, ProductPage.class);
                        // a.putExtra("position", "" + pos);
                        a.putExtra("category_id", countryList.get(getAdapterPosition()).getCategoryId());
                        a.putExtra("from", countryList.get(getAdapterPosition()).categoryName);
                        a.putExtra("sub_category_id", "0");
                        mContext.startActivity(a);
                    }
                }
            });
        }
    }

    public CategoryAdapter(Context mContext, List<Category.CategoryDatum> countryList) {
        this.mContext = mContext;
        this.countryList = countryList;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Category.CategoryDatum c = countryList.get(position);
        holder.progressbar.setVisibility(View.VISIBLE);
        holder.iv_catimage.setVisibility(View.VISIBLE);
        final ProgressBar progressView = holder.progressbar;
        Picasso.with(mContext)
                .load(c.getCategoryPhoto())
                .into(holder.iv_catimage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.tv_categoryname.setText(c.getCategoryName());




    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + countryList.size() + "]");
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(v);
    }
}
