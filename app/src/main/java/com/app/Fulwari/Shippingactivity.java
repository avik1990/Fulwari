package com.app.Fulwari;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.Fulwari.retrofit.api.ApiServices;

import com.app.Fulwari.model.CartDeleteAction;
import com.app.Fulwari.model.ZipCodeVerify;
import com.app.Fulwari.model.ZipCodemodel;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Shippingactivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    EditText et_name;
    EditText et_email;
    EditText et_phoneno;
    Button btn_placeorder;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_name, user_lastname, user_email, user_phone, user_address, user_city, user_state, user_pincode;
    CartDeleteAction registration;
    TextView tv_pagename;
    EditText et_address;
    EditText et_city;
    EditText et_state;
    EditText et_pincode, et_lname;
    FrameLayout cartvie;
    ImageView btn_menu, btn_back;
    String quick_delivery;
    ZipCodemodel zipCodemodel;
    List<String> list_text = new ArrayList<>();
    private ZipCodeVerify zipCodeVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        quick_delivery = getIntent().getExtras().getString("quick_delivery");

        initViews();

    }


    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        FlexboxLayout container = dialogView.findViewById(R.id.v_container);
        Button btn_proceed = dialogView.findViewById(R.id.btn_proceed);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);


        inflatelayout(container);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        /**/
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        btn_proceed.setVisibility(View.GONE);
       // btn_cancel.setVisibility(View.GONE);

    }

    private void inflatelayout(FlexboxLayout container) {
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(5, 5, 5, 5);
        for (int i = 0; i < list_text.size(); i++) {
            final TextView tv = new TextView(getApplicationContext());
            tv.setText(list_text.get(i));
            tv.setHeight(70);
            tv.setTextSize(16.0f);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setBackground(getResources().getDrawable(R.drawable.rounded_corner_flex));
            tv.setId(i + 1);
            tv.setLayoutParams(buttonLayoutParams);
            tv.setTag(i);
            tv.setPadding(20, 10, 20, 10);
            container.addView(tv);
        }

    }
    private void fetchZipCode() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ZipCodemodel> call = redditAPI.GetZipCodeList();
        call.enqueue(new Callback<ZipCodemodel>() {

            @Override
            public void onResponse(Call<ZipCodemodel> call, retrofit2.Response<ZipCodemodel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    list_text.clear();
                    zipCodemodel = response.body();
                    if (zipCodemodel.getAck() == 1) {
                        if (zipCodemodel.getZipData().size() > 0) {
                            //list_text.clear();
                            for (int i = 0; i < zipCodemodel.getZipData().size(); i++) {
                                list_text.add(zipCodemodel.getZipData().get(i).getAvailableZipcode());
                            }
                            if(list_text.contains(et_pincode.getText().toString()))
                                postShippingDetails();
                            else
                                showCustomDialog();
                        }
                    } else {
                        Utility.showToastShort(mContext, zipCodemodel.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ZipCodemodel> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void initViews() {
        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });
        cartvie = findViewById(R.id.cartvie);
        cartvie.setVisibility(View.GONE);
        tv_pagename = findViewById(R.id.tv_pagename);
        et_name = findViewById(R.id.et_name);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_phoneno = findViewById(R.id.et_phoneno);
        btn_placeorder = findViewById(R.id.btn_placeorder);
        tv_pagename.setText("Shipping Details");
        btn_placeorder.setOnClickListener(this);

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setVisibility(View.VISIBLE);

        et_address = findViewById(R.id.et_address);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_pincode = findViewById(R.id.et_pincode);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        et_name.setText(Preferences.get_firstName(mContext));
        et_lname.setText(Preferences.get_lastName(mContext));
        et_email.setText(Preferences.get_userEmail(mContext));
        et_phoneno.setText(Preferences.get_userPhone(mContext));
       // et_pincode.setEnabled(false);

        et_address.setText(Preferences.get_address(mContext));
        et_city.setText(Preferences.get_city(mContext));
        et_state.setText(Preferences.get_state(mContext));

        et_pincode.setText(Preferences.get_Zip(mContext));
        VerifyZipCodeparsejson(Preferences.get_Zip(mContext));
        et_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6) {
                    VerifyZipCodeparsejson(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        if (v == btn_placeorder) {
            if (et_name.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter First Name");
                return;
            }

            if (et_lname.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Last Name");
                return;
            }

            if (et_email.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Email");
                return;
            }

            if (!Utility.isValidEmail(et_email.getText().toString())) {
                Utility.showToastShort(mContext, "Please Enter Valid Email");
                return;
            }

            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Phone No.");
                return;
            }


            if (et_address.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Address");
                return;
            }

            if (et_city.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter City");
                return;
            }

            if (et_state.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter State");
                return;
            }

            if (et_pincode.getText().toString().isEmpty()) {

                Utility.showToastShort(mContext, "Please Enter Pin Code");
                return;
            }

            user_lastname = et_lname.getText().toString().trim();
            user_address = et_address.getText().toString().trim();
            user_city = et_city.getText().toString().trim();
            user_state = et_state.getText().toString().trim();
            user_pincode = et_pincode.getText().toString().trim();

            user_name = et_name.getText().toString().trim();
            user_email = et_email.getText().toString().trim();
            user_phone = et_phoneno.getText().toString().trim();

            fetchZipCode();


        } else if (v == btn_back) {
            onBackPressed();
            finish();
        }
    }

    private void postShippingDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<CartDeleteAction> call = redditAPI.PostShipping(user_name, user_lastname, user_email, user_phone, user_address, user_city, user_state, user_pincode, Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext), quick_delivery);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck().equals("1")) {
                        Preferences.set_Cartount(mContext, "0");
                        getMessage();
                    } else {
                        Utility.showToastShort(mContext, registration.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void VerifyZipCodeparsejson(final String zipcode) {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ZipCodeVerify> call = redditAPI.VerifyZipCode(zipcode);
        call.enqueue(new Callback<ZipCodeVerify>() {

            @Override
            public void onResponse(Call<ZipCodeVerify> call, retrofit2.Response<ZipCodeVerify> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    zipCodeVerify = response.body();
                    if (zipCodeVerify.getAck().equals("1")) {
                        btn_placeorder.setText("Place Order");
                        btn_placeorder.setEnabled(true);
                       // Utility.showToastShort(mContext, zipCodeVerify.getMsg());
                    } else {
                        btn_placeorder.setText("Service Not Available");
                        btn_placeorder.setEnabled(false);
                        Utility.showToastShort(mContext, zipCodeVerify.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ZipCodeVerify> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void getMessage() {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);


        Call<CartDeleteAction> call = redditAPI.GetCartThankyouMessage(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck().equals("1")) {
                        showDialog(mContext, registration.getMsg());
                    } else {
                        Utility.showToastShort(mContext, registration.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    public void showDialog(Context activity, String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_thankyou_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView et_pincode = dialog.findViewById(R.id.et_pincode);
        et_pincode.setText(message);
        FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.setUniqueKey(mContext, false);
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });

        dialog.show();
    }

}
