package com.app.Fulwari.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.Fulwari.R;
import com.app.Fulwari.model.BaseResponse;
import com.app.Fulwari.model.CustomPackData;
import com.app.Fulwari.model.MyCart;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonthlyPacketAdapter extends RecyclerView.Adapter<MonthlyPacketAdapter.MyViewHolder> {
    List<CustomPackData.CustomPackDataModel> customPackDataModels;
    Context mContext;
    SubCategoryAdapter.AdapterPos adapterPos;
    MyCart myCart;
    ProgressDialog pDialog;
    BaseResponse baseResponse;
    UpdateCartCount updateCartCount;
    String product_id;
    private int amount = 0;
    ConnectionDetector cd;


    public interface AdapterPos {
        void adapterPosition(int pos);
    }

    public interface UpdateCartCount {
        void updateCartCount();
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressbar;
        TextView tv_productname, tv_price;
        ImageView iv_productImg;
        RecyclerView rv_product_details;
        Button btn_add;
        LinearLayout ll_cart_quantity;
        ImageView iv_sub, iv_add;
        TextView et_qty;


        public MyViewHolder(View view) {
            super(view);
            progressbar = view.findViewById(R.id.progressbar);
            iv_productImg = (ImageView) view.findViewById(R.id.iv_chefimage);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_price = (TextView) view.findViewById(R.id.tv_price_orginal);
            rv_product_details = (RecyclerView) view.findViewById(R.id.rv_recyclerview);
            iv_sub = view.findViewById(R.id.iv_sub);
            iv_add = view.findViewById(R.id.iv_add);
            et_qty = view.findViewById(R.id.et_qty);
            ll_cart_quantity = view.findViewById(R.id.ll_cart_quantity);
            btn_add = view.findViewById(R.id.btn_add);


        }
    }

    public MonthlyPacketAdapter(Context mContext, List<CustomPackData.CustomPackDataModel> customPackDataModels,UpdateCartCount updateCartCount) {
        this.mContext = mContext;
        this.customPackDataModels = customPackDataModels;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        this.updateCartCount=updateCartCount;
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        cd = new ConnectionDetector(mContext);
    }

    @Override
    public void onBindViewHolder(final MonthlyPacketAdapter.MyViewHolder holder, int position) {
        final CustomPackData.CustomPackDataModel c = customPackDataModels.get(position);
        LinearLayoutManager layoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.rv_product_details.setLayoutManager(layoutManager);

        MonthlyPacketProductDetailsAdapter ca = new MonthlyPacketProductDetailsAdapter(mContext, c.getPacketDetails());
        holder.rv_product_details.setAdapter(ca);

        holder.tv_productname.setText(c.getProduct_name_english());
        holder.tv_price.setText("\u20B9"+c.getProduct_price());

        final ProgressBar progressView=holder.progressbar;
        try {
            Picasso.with(mContext)
                    .load(c.getProduct_photo())
                    .into(holder.iv_productImg, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressView.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {

        }

        ///handle button click
        if (c.isClicked()) {
            holder.btn_add.setVisibility(View.GONE);
            holder.ll_cart_quantity.setVisibility(View.VISIBLE);
        } else {
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.ll_cart_quantity.setVisibility(View.GONE);
        }
        holder.btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ///  if (c.getPackets().size() > 0) {
                product_id = c.getProduct_id();
                   /* if (c.getSelectedPos() == -1) {
                        packet_id = c.getPackets().get(0).getPack_id();
                    } else {
                        packet_id = c.getPackets().get(c.getSelectedPos()).getPack_id();
                    }*/

                holder.btn_add.setVisibility(View.GONE);
                holder.ll_cart_quantity.setVisibility(View.VISIBLE);
                c.setClicked(true);
                amount = 1;
                if (cd.isConnected()) {
                    AddtoCartServices("1");
                }
               /* }else {
                    Utility.showToastShort(mContext, "Please select pack size");
                }*/

            }
        });


        holder.iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_sub.isPressed()) {
                        //if (c.getPackets().size() > 0) {
                        if (holder.et_qty.getText().toString().equals("1")) {
                            holder.btn_add.setVisibility(View.VISIBLE);
                            holder.ll_cart_quantity.setVisibility(View.GONE);
                        }
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        if (amount > 0) {
                            amount -= 1;
                        }
                        if (amount == 0) {
                            amount = 1;
                        }
                        holder.et_qty.setText(String.valueOf(amount));
                        //holder.et_qty.setText(String.valueOf(amount));


                        product_id = c.getProduct_id();

                          /*  if (c.getSelectedPos() == -1) {
                                packet_id = c.getPackets().get(0).getPack_id();
                            } else {
                                packet_id = c.getPackets().get(c.getSelectedPos()).getPack_id();
                            }*/
                        //Utility.showToastShort(mContext, product_id + ":" + packet_id);
                        if (cd.isConnected()) {
                            AddtoCartServices("0");
                        }
                        /*}else{
                            Utility.showToastShort(mContext, "Please select pack size");
                        }*/
                    }
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_add.isPressed()) {
                        // if (c.getPackets().size() > 0) {
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        amount += 1;
                        holder.et_qty.setText(String.valueOf(amount));
                        if (amount != 0) {

                        }

                        product_id = c.getProduct_id();

                           /* if (c.getSelectedPos() == -1) {
                                packet_id = c.getPackets().get(0).getPack_id();
                            } else {
                                packet_id = c.getPackets().get(c.getSelectedPos()).getPack_id();
                            }*/

                        if (cd.isConnected()) {
                            AddtoCartServices("1");
                        }
                        /*}else{
                            Utility.showToastShort(mContext, "Please select pack size");
                        }*/
                    }
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + customPackDataModels.size() + "]");
        return customPackDataModels.size();
    }


    @Override
    public MonthlyPacketAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.montly_custom_pack_row, parent, false);
        return new MonthlyPacketAdapter.MyViewHolder(v);
    }


    private void AddtoCartServices(String isCartAdd) {//packetid blank
        pDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Log.e("Log==",isCartAdd);

        Call<BaseResponse> call = redditAPI.AddtoCartService(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext),
                product_id, "","flower" ,"1", isCartAdd);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                baseResponse = response.body();
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
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
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
                            updateCartCount.updateCartCount();
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
}

