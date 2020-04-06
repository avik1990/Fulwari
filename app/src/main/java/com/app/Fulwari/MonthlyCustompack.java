package com.app.Fulwari;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.Fulwari.adapter.MonthlyPacketAdapter;
import com.app.Fulwari.model.CustomPackData;
import com.app.Fulwari.model.ProductList;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.CircularTextView;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonthlyCustompack extends AppCompatActivity implements View.OnClickListener,MonthlyPacketAdapter.UpdateCartCount{
    RecyclerView rv_recyclerview;
    TextView tv_notFound;
    ProgressDialog pDialog;
    Context context;

    ImageView btn_menu, btn_back;
    ImageView iv_cart;
    CircularTextView tv_cartcount;
    SearchView v_searcview;
    ImageView iv_search;
    EditText searchEditText;
    TextView tv_pagename;
    ImageView iv_home;
    String sub_category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_custompack);
        rv_recyclerview=findViewById(R.id.rv_recyclerview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_recyclerview.setLayoutManager(layoutManager);
        tv_notFound=findViewById(R.id.not_found);
        context=this;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        getPredefineCategoryData();

        initView();
    }

    private void initView() {
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
        tv_pagename.setText("Ready Monthly Pack");
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
                //Utility.showToastShort(context, s.toString());
                getSearchedProduct(s.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

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

        Call<ProductList> call = redditAPI.Getsearch_food_list1(Preferences.get_dashCatId(context), Preferences.get_userId(context), Preferences.get_UniqueId(context), sub_category_id, searchString);
        call.enqueue(new Callback<ProductList>() {

            @Override
            public void onResponse(Call<ProductList> call, retrofit2.Response<ProductList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    ProductList   productModel = response.body();
                    if (productModel.getAck() == 1) {
                        if (productModel.getProductData().size() > 0) {
                            Intent i = new Intent(context, SearchedProductPage.class);
                            i.putExtra("searchQuery", searchString);
                            i.putExtra("scat_id", sub_category_id);
                            i.putExtra("cat_id", Preferences.get_dashCatId(context));
                            startActivity(i);
                        }
                    } else {
                        Utility.showToastShort(context, productModel.getMsg());
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

    private void getPredefineCategoryData() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<CustomPackData> call = redditAPI.getMonthlyCustomPacks( Preferences.get_userId(context), Preferences.get_UniqueId(context));
        call.enqueue(new Callback<CustomPackData>() {

            @Override
            public void onResponse(Call<CustomPackData> call, retrofit2.Response<CustomPackData> response) {
                Log.d("String", "" + response);
                pDialog.dismiss();
                CustomPackData customPackData=response.body();
                if(customPackData.getAck()==1){
                    tv_notFound.setVisibility(View.GONE);
                    rv_recyclerview.setVisibility(View.VISIBLE);
                    setDataToAdapter(customPackData);
                }
                else {
                    tv_notFound.setVisibility(View.VISIBLE);
                    rv_recyclerview.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<CustomPackData> call, Throwable t) {
                pDialog.dismiss();
                tv_notFound.setVisibility(View.VISIBLE);
                rv_recyclerview.setVisibility(View.GONE);
            }
        });
    }
  void setDataToAdapter(CustomPackData customPackData){
      MonthlyPacketAdapter ca = new MonthlyPacketAdapter(context, customPackData.getCustomPackDataModels(),this);
      rv_recyclerview.setAdapter(ca);
  }

    @Override
    public void updateCartCount() {
        tv_cartcount.setText(Preferences.get_Cartount(context));

    }


    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(this));

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
            Intent i = new Intent(context, ProductCart.class);
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
            Intent i = new Intent(context, Dashboard.class);
            startActivity(i);
            finishAffinity();
        }
    }
}
