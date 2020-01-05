package com.app.Fulwari;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.Fulwari.adapter.FlowerProductAdapter;
import com.app.Fulwari.adapter.HorizontalCategoryAdapter;
import com.app.Fulwari.model.FlowerProductBean;
import com.app.Fulwari.model.PredefinedPackCategoryData;
import com.app.Fulwari.model.PredefinedPackDataBeans;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlowerListActivity extends AppCompatActivity implements View.OnClickListener,
        FlowerProductAdapter.AdapterPos,HorizontalCategoryAdapter.BrandSelectedLisener, FlowerProductAdapter.UpdateCartCount{
    RecyclerView rv_subcategory_listing,rv_product_listing;
    int tag;// tag ==1 means custom  tag==2 means predefined
    ProgressDialog pDialog;
    public static FlowerProductBean productModel;
    public static PredefinedPackDataBeans predefinedPackDataBeans;
    Context mContext;
    TextView tv_notFound;
    PredefinedPackCategoryData predefinedPackCategoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_list);
        rv_subcategory_listing=findViewById(R.id.rv_subcategory_listing);
        mContext=this;
        rv_product_listing=findViewById(R.id.rv_product_listing);
        tv_notFound=findViewById(R.id.not_found);
        rv_product_listing.setLayoutManager(new GridLayoutManager(mContext,2));
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_subcategory_listing.setLayoutManager(layoutManager);
      //  rv_subcategory_listing.setLayoutManager(new GridLayoutManager(mContext,2));
        tag=getIntent().getIntExtra("tag",-1);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        if(tag==1)
          fetchCustomflowerData();
        else
            getPredefineCategoryData();
    }

    @Override
    protected void onDestroy() {
        productModel=null;
        super.onDestroy();
    }



    private void getPredefineFlowerData(String categoryId) {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        // category_id="3";
        //sub_category_id="3";

        Call<PredefinedPackDataBeans> call = redditAPI.getPredefinedPackList(categoryId, Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<PredefinedPackDataBeans>() {

            @Override
            public void onResponse(Call<PredefinedPackDataBeans> call, retrofit2.Response<PredefinedPackDataBeans> response) {
                Log.d("String", "" + response);
                predefinedPackDataBeans = response.body();
                if (predefinedPackDataBeans.getAck() == 1) {
                    if (predefinedPackDataBeans.getProductData() !=null && predefinedPackDataBeans.getProductData().size() > 0) {
                        tv_notFound.setVisibility(View.GONE);
                        rv_product_listing.setVisibility(View.VISIBLE);
                        inflateAdapterForPredefine();
                    }
                } else {
                    Utility.showToastShort(mContext, predefinedPackDataBeans.getMsg());
                    tv_notFound.setVisibility(View.VISIBLE);
                    rv_product_listing.setVisibility(View.GONE);
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PredefinedPackDataBeans> call, Throwable t) {
                pDialog.dismiss();
                tv_notFound.setVisibility(View.VISIBLE);
                rv_product_listing.setVisibility(View.GONE);
            }
        });
    }


    private void getPredefineCategoryData() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<PredefinedPackCategoryData> call = redditAPI.getPredefinedPackCategory( Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<PredefinedPackCategoryData>() {

            @Override
            public void onResponse(Call<PredefinedPackCategoryData> call, retrofit2.Response<PredefinedPackCategoryData> response) {
                Log.d("String", "" + response);
                predefinedPackCategoryData=response.body();
                //productModel = response.body();
                if (predefinedPackCategoryData.getAck() == 1) {
                    if (predefinedPackCategoryData.getPredefinedPackCategoryDataList().size() > 0) {
                        setCategory();
                        getPredefineFlowerData(predefinedPackCategoryData.getPredefinedPackCategoryDataList().get(0).getCategory_id());
                        //inflateAdapter();
                    }
                } else {
                    Utility.showToastShort(mContext, productModel.getMsg());
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PredefinedPackCategoryData> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void setCategory(){
        HorizontalCategoryAdapter ca = new HorizontalCategoryAdapter(this,this);
        rv_subcategory_listing.setAdapter(ca);
        ca.setData(predefinedPackCategoryData.getPredefinedPackCategoryDataList());

    }

    private void fetchCustomflowerData() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        // category_id="3";
        //sub_category_id="3";

        Call<FlowerProductBean> call = redditAPI.GetCustomPackList( Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<FlowerProductBean>() {

            @Override
            public void onResponse(Call<FlowerProductBean> call, retrofit2.Response<FlowerProductBean> response) {
                Log.d("String", "" + response);
                productModel = response.body();
                if (productModel.getAck() == 1) {
                    if (productModel.getProductData().size() > 0) {
                        tv_notFound.setVisibility(View.GONE);
                        rv_product_listing.setVisibility(View.VISIBLE);
                        inflateAdapter();
                    }
                } else {
                    Utility.showToastShort(mContext, productModel.getMsg());
                    tv_notFound.setVisibility(View.VISIBLE);
                    rv_product_listing.setVisibility(View.GONE);

                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FlowerProductBean> call, Throwable t) {
                pDialog.dismiss();
                tv_notFound.setVisibility(View.VISIBLE);
                rv_product_listing.setVisibility(View.GONE);
            }
        });
    }

    private void inflateAdapter() {
        FlowerProductAdapter ca = new FlowerProductAdapter(mContext, productModel.getProductData(), this, this);
        rv_product_listing.setAdapter(ca);
    }

    private void inflateAdapterForPredefine() {
        FlowerProductAdapter ca = new FlowerProductAdapter(mContext, predefinedPackDataBeans.getProductData(), this, this);
        rv_product_listing.setAdapter(ca);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void adapterPosition(int pos) {
        if(tag==1 && productModel.getProductData().get(pos).getPackets().size()>0) {
            Intent a = new Intent(mContext, FlowerCustomDetails.class);
            a.putExtra("position", pos);
            //a.putExtra("category_id", category_id);
            mContext.startActivity(a);
        }
    }

    @Override
    public void updateCartCount() {

    }

    @Override
    public void onBrandsSelected(PredefinedPackCategoryData.PredefinedPackList category) {
        getPredefineFlowerData(category.getCategory_id());
    }
}
