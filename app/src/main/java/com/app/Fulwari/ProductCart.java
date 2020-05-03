package com.app.Fulwari;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.Fulwari.adapter.CartAdapter;
import com.app.Fulwari.model.AddToCart;
import com.app.Fulwari.model.MyCart;
import com.app.Fulwari.model.ZipCodeVerify;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductCart extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView btn_menu, btn_back;
    ConnectionDetector cd;
    TextView tv_pagename;
    ProgressDialog pDialog;
    AddToCart addToCart;
    MyCart myCart;
    public static List<MyCart.CartDatum> listmycart = new ArrayList<>();
    String ProductId = "", PacketId = "", From;
    RecyclerView rl_cart;
    LinearLayout footer, footerBtn;
    TextView tv_totalprice, tv_servicetax, tv_taxpercentage, tv_grandtotdal;
    Button btn_checkout;
    ZipCodeVerify zipCodeVerify;
    FrameLayout cartvie;
    Button btn_backhome;
    TextView tv_orderdetails;
    CheckBox cb_quickdelivery;
    TextView tv_delivery;
    String isQuickDelivery = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        cd = new ConnectionDetector(mContext);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        /*ProductId = getIntent().getStringExtra("ProductId");
        PacketId = getIntent().getStringExtra("PacketId");
        From = getIntent().getStringExtra("From");*/
        btn_backhome = findViewById(R.id.btn_backhome);
        tv_orderdetails = findViewById(R.id.tv_orderdetails);
        btn_backhome.setOnClickListener(this);
        rl_cart = findViewById(R.id.rl_cart);
        rl_cart.setLayoutManager(new LinearLayoutManager(mContext));
        tv_delivery = findViewById(R.id.tv_delivery);
        /*if (From.equals("ProductDetails")) {
            if (cd.isConnected()) {
                AddToCart();
            } else {
                Utility.showToastShort(mContext, getResources().getString(R.string.no_internet_msg));
            }
        } else {*/
        if (cd.isConnected()) {
            LoadCartProduct();
        } else {
            Utility.showToastShort(mContext, getResources().getString(R.string.no_internet_msg));
        }
        //  }
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

        cb_quickdelivery = findViewById(R.id.cb_quickdelivery);
        tv_pagename = findViewById(R.id.tv_pagename);
        tv_totalprice = findViewById(R.id.tv_totalprice);
        tv_servicetax = findViewById(R.id.tv_servicetax);
        tv_taxpercentage = findViewById(R.id.tv_taxpercentage);
        tv_grandtotdal = findViewById(R.id.tv_grandtotdal);
        footer = findViewById(R.id.footer);
        footerBtn = findViewById(R.id.footerBtn);
        btn_checkout = findViewById(R.id.btn_checkout);
        cartvie = findViewById(R.id.cartvie);
        cartvie.setVisibility(View.GONE);
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);
        tv_pagename.setText("My Basket");

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
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
        } else if (v == btn_checkout) {
            if (Preferences.getisVerified(mContext)) {
                Intent i = new Intent(mContext, Shippingactivity.class);
                i.putExtra("quick_delivery",isQuickDelivery);
                startActivity(i);
            } else {
                Preferences.set_checkClicked(mContext, "1");
                Intent i = new Intent(mContext, LoginActivity.class);
                startActivity(i);
            }
        } else if (v == btn_backhome) {
            Intent i = new Intent(mContext, Dashboard.class);
            startActivity(i);
            finishAffinity();
        }
    }


    public void LoadCartProduct() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<MyCart> call = redditAPI.GetMyCart(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<MyCart>() {

            @Override
            public void onResponse(Call<MyCart> call, retrofit2.Response<MyCart> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    myCart = response.body();
                    if (myCart.getAck().equals("1")) {
                        tv_orderdetails.setVisibility(View.VISIBLE);
                        Preferences.set_Cartount(mContext, myCart.getPriceData().getTotal_quantity());
                        rl_cart.setVisibility(View.VISIBLE);
                        listmycart = myCart.getCartData();
                        inflateCartAdapter();
                    } else {
                        tv_orderdetails.setVisibility(View.GONE);
                        footerBtn.setVisibility(View.GONE);
                        footer.setVisibility(View.GONE);
                        Utility.showToastShort(mContext, myCart.getMsg());
                        btn_checkout.setVisibility(View.GONE);
                        rl_cart.setVisibility(View.GONE);
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyCart> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void inflateCartAdapter() {
        CartAdapter mAdapter = new CartAdapter(listmycart, mContext);
        rl_cart.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        tv_totalprice.setText("\u20B9" + " " + myCart.getPriceData().totalPrice);
        tv_servicetax.setText(myCart.getPriceData().total_quantity);
        //code change on 03-05-2020
        tv_taxpercentage.setVisibility(View.GONE);

       // tv_taxpercentage.setVisibility(View.VISIBLE);
        tv_delivery.setVisibility(View.VISIBLE);
        tv_taxpercentage.setText("\u20B9" + " " + myCart.getPriceData().delivery_charge);
        /*if (Double.parseDouble(myCart.getPriceData().totalPrice) >= 150) {
            tv_taxpercentage.setVisibility(View.GONE);
            tv_delivery.setVisibility(View.GONE);
        } else {
            tv_taxpercentage.setVisibility(View.VISIBLE);
            tv_delivery.setVisibility(View.VISIBLE);
            tv_taxpercentage.setText("\u20B9" + " " + myCart.getPriceData().delivery_charge);
        }
*/
        ///////

        tv_grandtotdal.setText("\u20B9" + " " + myCart.getPriceData().grand_total);
        footer.setVisibility(View.VISIBLE);

        cb_quickdelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isQuickDelivery = "1";
                    tv_grandtotdal.setText("\u20B9" + " " + (Double.parseDouble(myCart.getPriceData().grand_total) + 10.00));
                } else {
                    isQuickDelivery = "0";
                    tv_grandtotdal.setText("\u20A8" + " " + myCart.getPriceData().grand_total);
                }
            }
        });


    }
}
