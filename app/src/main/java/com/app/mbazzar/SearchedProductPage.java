package com.app.mbazzar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mbazzar.adapter.ProductAdapter;
import com.app.mbazzar.adapter.SubCategoryAdapter;
import com.app.mbazzar.model.ProductList;
import com.app.mbazzar.model.ProductModel;
import com.app.mbazzar.retrofit.api.ApiServices;
import com.app.mbazzar.utils.CircularTextView;
import com.app.mbazzar.utils.ConnectionDetector;
import com.app.mbazzar.utils.Preferences;
import com.app.mbazzar.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchedProductPage extends AppCompatActivity implements View.OnClickListener, SubCategoryAdapter.AdapterPos, ProductAdapter.AdapterPos, ProductAdapter.UpdateCartCount {

    Context mContext;
    ImageView btn_menu, btn_back;
    RecyclerView rv_product_listing;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    public static ProductList productModel;
    String category_id;
    TextView tv_pagename;
    CircularTextView tv_cartcount;
    ImageView iv_cart;
    String searchQuery = "",sub_category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        category_id = getIntent().getExtras().getString("cat_id");
        searchQuery = getIntent().getExtras().getString("searchQuery");
        sub_category_id= getIntent().getExtras().getString("scat_id");
        initView();

        if (cd.isConnected()) {
            getSearchedProduct(searchQuery);
        } else {
            Utility.showToastShort(mContext, getString(R.string.no_internet_msg));
        }

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
                    productModel = response.body();
                    if (productModel.getAck() == 1) {
                        if (productModel.getProductData().size() > 0) {
                            inflateAdapter();
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

    private void initView() {
        ImageView iv_home=findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });
        rv_product_listing = findViewById(R.id.rv_product_listing);
        rv_product_listing.setLayoutManager(new LinearLayoutManager(mContext));
        tv_pagename = findViewById(R.id.tv_pagename);
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        tv_pagename.setText("Search Result");
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);

        ImageView iv_phone=findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.CallContactNo(mContext);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));

    }

    private void inflateAdapter() {
        ProductAdapter ca = new ProductAdapter(mContext, productModel.getProductData(), this, this);
        rv_product_listing.setAdapter(ca);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        }
    }


    @Override
    public void adapterPosition(int pos) {
        //Utility.showToastShort(mContext, "" + productModel.getProductData());
        Intent a = new Intent(mContext, ProductDetails.class);
        a.putExtra("position", "" + pos);
        a.putExtra("category_id", category_id);
        mContext.startActivity(a);
    }



    @Override
    public void updateCartCount() {
        tv_cartcount.setText(Preferences.get_Cartount(mContext));
    }
}
