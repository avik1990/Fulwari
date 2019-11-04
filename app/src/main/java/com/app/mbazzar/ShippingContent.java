package com.app.mbazzar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mbazzar.model.Privacymodel;
import com.app.mbazzar.retrofit.api.ApiServices;
import com.app.mbazzar.utils.CircularTextView;
import com.app.mbazzar.utils.ConnectionDetector;
import com.app.mbazzar.utils.Preferences;
import com.app.mbazzar.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShippingContent extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView btn_menu, btn_back;
    ConnectionDetector cd;
    TextView tv_pagename;
    TextView tv_title, tv_desc;
    Privacymodel aboutUs;
    ProgressDialog pDialog;
    CircularTextView tv_cartcount;
    ImageView iv_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initView();
    }


    private void initView() {
        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });
        tv_pagename = findViewById(R.id.tv_pagename);
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);


        if (cd.isConnected()) {
            parsejson();
        } else {
            Utility.showToastShort(mContext, getString(R.string.no_internet_msg));
        }

        ImageView iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.CallContactNo(mContext);
            }
        });
    }

    private void setData() {
        tv_title.setText(aboutUs.getAboutData().contentTitle);
        tv_desc.setText(aboutUs.getAboutData().getContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));
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


    private void parsejson() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Privacymodel> call = redditAPI.GetshippingData();
        call.enqueue(new Callback<Privacymodel>() {

            @Override
            public void onResponse(Call<Privacymodel> call, retrofit2.Response<Privacymodel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    aboutUs = response.body();
                    if (aboutUs != null) {
                        setData();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Privacymodel> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


}
