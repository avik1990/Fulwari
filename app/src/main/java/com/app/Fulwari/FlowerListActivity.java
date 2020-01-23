package com.app.Fulwari;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.Fulwari.adapter.FlowerProductAdapter;
import com.app.Fulwari.adapter.HorizontalCategoryAdapter;
import com.app.Fulwari.model.FlowerProductBean;
import com.app.Fulwari.model.PredefinedPackCategoryData;
import com.app.Fulwari.model.PredefinedPackDataBeans;
import com.app.Fulwari.model.ProductList;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.CircularTextView;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlowerListActivity extends AppCompatActivity implements View.OnClickListener,
        FlowerProductAdapter.AdapterPos,HorizontalCategoryAdapter.BrandSelectedLisener, FlowerProductAdapter.UpdateCartCount{
    RecyclerView rv_subcategory_listing,rv_product_listing;
    int tag;// tag ==1 means predefined  tag==2 means custom
    ProgressDialog pDialog;
    ImageView btn_menu, btn_back;
    public static FlowerProductBean productModel;
    public static PredefinedPackDataBeans predefinedPackDataBeans;
    Context mContext;
    TextView tv_notFound;
    ImageView iv_cart;
    CircularTextView tv_cartcount;
    SearchView v_searcview;
    ImageView iv_search;
    EditText searchEditText;
    TextView tv_pagename;
    ImageView iv_home;
    String sub_category_id;
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
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
        iv_search.setOnClickListener(this);
        v_searcview = findViewById(R.id.v_searcview);
        v_searcview.setOnClickListener(this);
        searchEditText = v_searcview.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setCursorVisible(true);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setCursorVisible(true);
        //tv_pagename.setText(category_name);
        tv_pagename = findViewById(R.id.tv_pagename);
        iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(this);
        sub_category_id = getIntent().getExtras().getString("sub_category_id");

        ImageView closeButton = (ImageView) this.v_searcview.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.VISIBLE);
                tv_pagename.setVisibility(View.VISIBLE);
                v_searcview.setVisibility(View.GONE);
                searchEditText.setText("");
            }
        });
        v_searcview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Utility.showToastShort(mContext, s.toString());
                getSearchedProduct(s.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


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
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));

    }


    private void getSearchedProduct(final String searchString) {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<ProductList> call = redditAPI.Getsearch_food_list1(Preferences.get_dashCatId(mContext), Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext), sub_category_id, searchString);
        call.enqueue(new Callback<ProductList>() {

            @Override
            public void onResponse(Call<ProductList> call, retrofit2.Response<ProductList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    ProductList   productModel = response.body();
                    if (productModel.getAck() == 1) {
                        if (productModel.getProductData().size() > 0) {
                            Intent i = new Intent(mContext, SearchedProductPage.class);
                            i.putExtra("searchQuery", searchString);
                            i.putExtra("scat_id", sub_category_id);
                            i.putExtra("cat_id", Preferences.get_dashCatId(mContext));
                            startActivity(i);
                        }
                    } else {
                        Utility.showToastShort(mContext, productModel.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        productModel=null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        } else if (v == iv_search) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(v_searcview.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            v_searcview.requestFocus();
            iv_search.setVisibility(View.GONE);
            v_searcview.setVisibility(View.VISIBLE);
            tv_pagename.setVisibility(View.GONE);
            searchEditText.setText("");
        }
        else if(v==iv_home){
            Intent i = new Intent(mContext, Dashboard.class);
            startActivity(i);
            finishAffinity();
        }

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
        predefinedPackCategoryData.getPredefinedPackCategoryDataList().get(0).setSelected(true);
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
        tv_cartcount.setText(Preferences.get_Cartount(mContext));

    }

    @Override
    public void onBrandsSelected(PredefinedPackCategoryData.PredefinedPackList category) {
        getPredefineFlowerData(category.getCategory_id());
    }
}
