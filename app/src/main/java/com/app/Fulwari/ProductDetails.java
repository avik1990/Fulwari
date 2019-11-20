package com.app.Fulwari;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.Fulwari.model.AddToCart;
import com.app.Fulwari.model.BaseResponse;
import com.app.Fulwari.model.MyCart;
import com.app.Fulwari.model.ProductList;
import com.app.Fulwari.model.ProductModel;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.CircularTextView;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetails extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView btn_menu, btn_back, iv_productimg;
    ConnectionDetector cd;
    String category_name;
    TextView tv_pagename;
    TextView tv_title, tv_desc, tv_price;
    Spinner spinner;
    int position = 0;
    String[] items;
    TextView tv_quantity, tv_brand;
    ProgressDialog pDialog;
    String product_id = "";
    RelativeLayout v_spinner;
    AddToCart addToCart;
    MyCart myCart;

    public static ProductList productModel;
    LinearLayout footer;


    android.support.v7.widget.SearchView v_searcview;
    ImageView iv_search;
    EditText searchEditText;
    String category_id = "", st_packet_id = "";

    LinearLayout ll_radios;
    int amount = 1;

    Button btn_backhome;
    CircularTextView tv_cartcount;
    ImageView iv_cart;

    Button btn_add;
    LinearLayout ll_cart_quantity;
    ImageView iv_sub, iv_add;
    TextView et_qty, tv_packsize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        cd = new ConnectionDetector(mContext);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        position = Integer.parseInt(getIntent().getStringExtra("position").trim());
        category_id = getIntent().getExtras().getString("cat_id");

        //Log.d("JsonData", SubCategoryPage.productModel.getProductData().get(position).getProductDetails());

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
        tv_packsize = findViewById(R.id.tv_packsize);
        tv_brand = findViewById(R.id.tv_brand);
        btn_add = findViewById(R.id.btn_add);
        ll_cart_quantity = findViewById(R.id.ll_cart_quantity);
        iv_sub = findViewById(R.id.iv_sub);
        iv_add = findViewById(R.id.iv_add);
        et_qty = findViewById(R.id.et_qty);

        btn_add.setOnClickListener(this);
        iv_sub.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        btn_backhome = findViewById(R.id.btn_backhome);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(this);
        tv_cartcount = findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        btn_backhome.setOnClickListener(this);

        footer = findViewById(R.id.footer);
        tv_pagename = findViewById(R.id.tv_pagename);

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);

        btn_menu.setOnClickListener(this);
        v_spinner = findViewById(R.id.v_spinner);
        v_spinner.setVisibility(View.GONE);
        iv_productimg = findViewById(R.id.iv_productimg);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);

        spinner = findViewById(R.id.spinner);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_price = findViewById(R.id.tv_price);
        v_searcview = findViewById(R.id.v_searcview);
        v_searcview.setOnClickListener(this);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
        iv_search.setOnClickListener(this);
        ll_radios = findViewById(R.id.ll_radios);
        setData();

        /*v_searcview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Utility.showToastShort(mContext, "Hello");
                return false;
            }
        });*/
        searchEditText = v_searcview.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setCursorVisible(true);
        /*searchEditText.setHintTextColor(getResources().getColor(R.color.black));*/

        ImageView closeButton = (ImageView) this.v_searcview.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.VISIBLE);
                v_searcview.setVisibility(View.GONE);
                tv_pagename.setVisibility(View.VISIBLE);
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

        ImageView iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.CallContactNo(mContext);
            }
        });

        //  tv_pagename.setText(SubCategoryPage.productModel.getProductData().get(position).getProductDetails());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));

    }

    private void setData() {
            tv_brand.setVisibility(View.GONE);

        tv_title.setText(ProductPage.productModel.getProductData().get(position).getProductNameEnglish() );
        tv_desc.setVisibility(View.GONE);
        tv_desc.setText(ProductPage.productModel.getProductData().get(position).getProductDetails());
        product_id = ProductPage.productModel.getProductData().get(position).getProductId();

        try {
            Picasso.with(mContext)
                    .load(ProductPage.productModel.getProductData().get(position).getProductPhoto())
                    .into(iv_productimg, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            // progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            //progressView.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {

        }
        inflateRadioGroup();
    }

    private void inflateRadioGroup() {
        if (ProductPage.productModel.getProductData().get(position).getPackets().size() > 0) {
            tv_packsize.setVisibility(View.VISIBLE);
            ll_radios.removeAllViews();
            LayoutInflater layoutInflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < ProductPage.productModel.getProductData().get(position).getPackets().size(); i++) {
                View view2 = layoutInflator.inflate(R.layout.row_radio_views, null);
                final TextView tv_cat = view2.findViewById(R.id.tv_offers);
                ImageView iv_circle = view2.findViewById(R.id.iv_circle);
                LinearLayout ll_border = view2.findViewById(R.id.ll_border);
                RelativeLayout v_main_layout = view2.findViewById(R.id.v_main_layout);
                tv_cat.setText(ProductPage.productModel.getProductData().get(position).getPackets().get(i).getPacketSize());

                TextView tv_offered_price = view2.findViewById(R.id.tv_offered_price);
                TextView tv_discount_percent = view2.findViewById(R.id.tv_discount_percent);
                TextView tv_orginal_price = view2.findViewById(R.id.tv_orginal_price);

                tv_offered_price.setText("\u20B9"+ProductPage.productModel.getProductData().get(position).getPackets().get(i).getPrice());
                tv_discount_percent.setText(ProductPage.productModel.getProductData().get(position).getPackets().get(i).getDiscount() + " off");
                tv_orginal_price.setText("\u20B9"+ProductPage.productModel.getProductData().get(position).getPackets().get(i).getOriginalPrice());
                tv_orginal_price.setPaintFlags(tv_orginal_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                tv_cat.setTextColor(Color.parseColor("#585858"));
                tv_cat.setId(i);
                iv_circle.setId(i);


                if (i == ProductPage.productModel.getProductData().get(position).getPackets().size() - 1) {
                    ll_border.setVisibility(View.GONE);
                } else {
                    ll_border.setVisibility(View.VISIBLE);
                }

                if (!st_packet_id.isEmpty()) {
                    if (ProductPage.productModel.getProductData().get(position).getPackets().get(i).getPacketId().equalsIgnoreCase(st_packet_id)) {
                        iv_circle.setVisibility(View.VISIBLE);
                        iv_circle.setImageResource(R.drawable.ic_check_black_24dp);
                        v_main_layout.setBackgroundColor(Color.parseColor("#dde7f5"));
                    } else {
                        iv_circle.setVisibility(View.INVISIBLE);
                    }
                }

                tv_cat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        st_packet_id = ProductPage.productModel.getProductData().get(position).getPackets().get(tv_cat.getId()).getPacketId();
                        inflateRadioGroup();
                    }
                });
                v_main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        st_packet_id = ProductPage.productModel.getProductData().get(position).getPackets().get(tv_cat.getId()).getPacketId();
                        inflateRadioGroup();
                    }
                });

                iv_circle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        st_packet_id = ProductPage.productModel.getProductData().get(position).getPackets().get(tv_cat.getId()).getPacketId();
                        inflateRadioGroup();
                    }
                });

                ll_radios.addView(view2);
            }
        } else {
            tv_packsize.setVisibility(View.GONE);
        }


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
        } else if (v == btn_backhome) {
            finish();
        } else if (v == btn_add) {
            if (st_packet_id.isEmpty()) {
                Utility.showToastShort(mContext, "Please select pack size");
                return;
            }
            btn_add.setVisibility(View.GONE);
            ll_cart_quantity.setVisibility(View.VISIBLE);
            amount = 1;
            if (cd.isConnected()) {
                AddtoCartServices("1");
            }
        } else if (v == iv_add) {
            if (st_packet_id.isEmpty()) {
                Utility.showToastShort(mContext, "Please select pack size");
                return;
            }
            if (cd.isConnected()) {
                if (iv_add.isPressed()) {
                    if (!et_qty.getText().toString().isEmpty()) {
                        amount = Integer.parseInt(et_qty.getText().toString());
                    } else {
                        amount = 0;
                    }
                    amount += 1;
                    et_qty.setText(String.valueOf(amount));
                    if (amount != 0) {
                    }

                    if (cd.isConnected()) {
                        AddtoCartServices("1");
                    }
                }
            } else {
                Utility.showToastShort(mContext, "No Internet Connection");
            }
        } else if (v == iv_sub) {

            if (st_packet_id.isEmpty()) {
                Utility.showToastShort(mContext, "Please select pack size");
                return;
            }
            if (cd.isConnected()) {
                if (iv_sub.isPressed()) {
                    if (et_qty.getText().toString().equals("1")) {
                        btn_add.setVisibility(View.VISIBLE);
                        ll_cart_quantity.setVisibility(View.GONE);
                    }
                    if (!et_qty.getText().toString().isEmpty()) {
                        amount = Integer.parseInt(et_qty.getText().toString());
                    } else {
                        amount = 0;
                    }
                    if (amount > 1) {
                        amount -= 1;
                        et_qty.setText(String.valueOf(amount));
                    }
                    if (amount == 0) {
                        amount = 1;
                    }
                    if (amount != 0) {
                    }
                    if (cd.isConnected()) {
                        AddtoCartServices("0");
                    }
                }
            } else {
                Utility.showToastShort(mContext, "No Internet Connection");
            }
        }
    }

    private void AddtoCartServices(String isCartAdd) {
        pDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<BaseResponse> call = redditAPI.AddtoCartService(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext), product_id, st_packet_id, "1", isCartAdd);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                Utility.showToastShort(mContext, baseResponse.getMsg());
                LoadCartProduct();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    public void LoadCartProduct() {
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
                    if (response.isSuccessful()) {
                        myCart = response.body();
                        if (myCart.getAck().equals("1")) {
                            Preferences.set_Cartount(mContext, myCart.getPriceData().getTotal_quantity());
                            tv_cartcount.setText(Preferences.get_Cartount(mContext));
                        } else {
                        }
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


    private void getSearchedProduct(final String searchString) {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<ProductList> call = redditAPI.Getsearch_food_list(category_id, Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext), searchString);
        call.enqueue(new Callback<ProductList>() {

            @Override
            public void onResponse(Call<ProductList> call, retrofit2.Response<ProductList> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    productModel = response.body();
                    if (productModel.getAck() == 1) {
                        if (productModel.getProductData().size() > 0) {
                            Intent i = new Intent(mContext, SearchedProductPage.class);
                            i.putExtra("searchQuery", searchString);
                            i.putExtra("cat_id", category_id);
                            startActivity(i);
                            //inflateAdapter();
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


}
