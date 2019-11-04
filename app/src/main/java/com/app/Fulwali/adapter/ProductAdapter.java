package com.app.Fulwali.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.Fulwali.R;
import com.app.Fulwali.model.BaseResponse;
import com.app.Fulwali.model.MyCart;
import com.app.Fulwali.model.ProductList;
import com.app.Fulwali.model.SubCategoryDataResponse;
import com.app.Fulwali.retrofit.api.ApiServices;
import com.app.Fulwali.utils.ConnectionDetector;
import com.app.Fulwali.utils.Preferences;
import com.app.Fulwali.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

/**
 * Created by Avik on 11-01-2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    List<ProductList.ProductDatum> countryList;
    Context mContext;
    AdapterPos adapterPos;
    UpdateCartCount updateCartCount;
    private int amount = 0;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    BaseResponse baseResponse;
    String product_id, packet_id;
    MyCart myCart;

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

        ImageView iv_chefimage;
        TextView tv_productname, tv_position, tv_productdetails, tv_dis_percent;
        ProgressBar progressbar;
        RelativeLayout card_view;
        TextView sp_packets, tv_price_orginal, tv_price_discount;
        Button btn_add;
        LinearLayout ll_cart_quantity;
        ImageView iv_sub, iv_add;
        TextView et_qty;
        LinearLayout ll_mainbody;
        LinearLayout fl_layout;

        public MyViewHolder(View view) {
            super(view);
            fl_layout = view.findViewById(R.id.fl_layout);
            iv_chefimage = view.findViewById(R.id.iv_chefimage);
            tv_productname = view.findViewById(R.id.tv_productname);
            progressbar = view.findViewById(R.id.progressbar);
            card_view = view.findViewById(R.id.card_view);
            tv_position = view.findViewById(R.id.tv_position);
            tv_productdetails = view.findViewById(R.id.tv_productdetails);
            sp_packets = view.findViewById(R.id.sp_packets);
            tv_price_orginal = view.findViewById(R.id.tv_price_orginal);
            tv_price_discount = view.findViewById(R.id.tv_price_discount);
            tv_dis_percent = view.findViewById(R.id.tv_dis_percent);
            btn_add = view.findViewById(R.id.btn_add);
            ll_mainbody = view.findViewById(R.id.ll_mainbody);
            ll_cart_quantity = view.findViewById(R.id.ll_cart_quantity);
            iv_sub = view.findViewById(R.id.iv_sub);
            iv_add = view.findViewById(R.id.iv_add);
            et_qty = view.findViewById(R.id.et_qty);
        }
    }

    public ProductAdapter(Context mContext, List<ProductList.ProductDatum> countryList, AdapterPos adapterPos, UpdateCartCount updateCartCount) {
        this.mContext = mContext;
        this.countryList = countryList;
        this.adapterPos = adapterPos;
        this.updateCartCount = updateCartCount;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductList.ProductDatum c = countryList.get(position);
        holder.tv_productname.setText(c.getProductNameEnglish() + " / " + c.getProductNameBengali());
        holder.progressbar.setVisibility(View.VISIBLE);
        holder.tv_position.setText(String.valueOf(position));
        holder.iv_chefimage.setVisibility(View.VISIBLE);
        // holder.tv_productdetails.setText("dsfsdfsdfsdfsd");

        if (!c.getBrand().isEmpty()) {
            holder.tv_productdetails.setVisibility(View.VISIBLE);
            holder.tv_productdetails.setText(c.getBrand());
        } else {
            holder.tv_productdetails.setVisibility(View.GONE);
        }


        holder.ll_mainbody.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapterPos.adapterPosition(position);
            }
        });

        holder.iv_chefimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapterPos.adapterPosition(position);
            }
        });


        ///handle button click
        if (c.isClicked()) {
            holder.btn_add.setVisibility(View.GONE);
            holder.ll_cart_quantity.setVisibility(View.VISIBLE);
        } else {
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.ll_cart_quantity.setVisibility(View.GONE);
        }

        final ProgressBar progressView = holder.progressbar;

        try {
            Picasso.with(mContext)
                    .load(c.getProductPhoto())
                    .into(holder.iv_chefimage, new com.squareup.picasso.Callback() {

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

        if (c.getPackets().size() > 0) {
            holder.fl_layout.setVisibility(View.VISIBLE);
            ///if any item is checked from dropdown
            if (c.getSelectedPos() != -1) {
                holder.sp_packets.setText(c.getPackets().get(c.getSelectedPos()).getPacketSize());
                holder.tv_price_orginal.setText(c.getPackets().get(c.getSelectedPos()).getOriginalPrice());

                if (!c.getPackets().get(c.getSelectedPos()).getDiscount().equals("0%")) {
                    holder.tv_dis_percent.setVisibility(View.VISIBLE);
                    holder.tv_price_orginal.setPaintFlags(holder.tv_price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tv_dis_percent.setText(" " + c.getPackets().get(c.getSelectedPos()).getDiscount() + " off");
                    holder.tv_price_discount.setVisibility(View.VISIBLE);
                    holder.tv_price_discount.setText(c.getPackets().get(c.getSelectedPos()).getPrice());
                } else {
                    holder.tv_dis_percent.setVisibility(View.GONE);
                    holder.tv_price_orginal.setPaintFlags(0);
                    holder.tv_price_discount.setVisibility(View.GONE);
                }

                holder.sp_packets.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String packetSize[] = new String[c.getPackets().size()];
                        for (int i = 0; i < c.getPackets().size(); i++) {
                            packetSize[i] = c.getPackets().get(i).getPacketSize();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(c.getProductNameEnglish()).setItems(packetSize, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int pos) {
                                //Utility.showToastShort(mContext, c.getPackets().get(pos).getPacketSize());
                                holder.sp_packets.setText(c.getPackets().get(pos).getPacketSize());
                                c.setSelectedPos(pos);
                                packet_id = c.getPackets().get(c.getSelectedPos()).getPacketId();
                                if (!c.getPackets().get(pos).getDiscount().equals("0%")) {
                                    holder.tv_dis_percent.setVisibility(View.VISIBLE);
                                    holder.tv_dis_percent.setText(" " + c.getPackets().get(pos).getDiscount());
                                    holder.tv_price_orginal.setText(c.getPackets().get(pos).getOriginalPrice());
                                    holder.tv_price_orginal.setPaintFlags(holder.tv_price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    holder.tv_price_discount.setVisibility(View.VISIBLE);
                                    holder.tv_price_discount.setText(c.getPackets().get(pos).getPrice());
                                } else {
                                    holder.tv_price_orginal.setText(c.getPackets().get(pos).getOriginalPrice());
                                    holder.tv_price_orginal.setPaintFlags(0);
                                    holder.tv_dis_percent.setVisibility(View.GONE);
                                    holder.tv_price_discount.setVisibility(View.GONE);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            } else {
                ///if any item is not checked from dropdown by default first item is default is selected
                holder.sp_packets.setText(c.getPackets().get(0).getPacketSize());
                holder.tv_price_orginal.setText(c.getPackets().get(0).getOriginalPrice());

                if (!c.getPackets().get(0).getDiscount().equals("0%")) {
                    holder.tv_dis_percent.setVisibility(View.VISIBLE);
                    holder.tv_price_orginal.setPaintFlags(holder.tv_price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tv_dis_percent.setText(" " + c.getPackets().get(0).getDiscount());
                    holder.tv_price_discount.setVisibility(View.VISIBLE);
                    holder.tv_price_discount.setText(c.getPackets().get(0).getPrice());
                } else {
                    holder.tv_dis_percent.setVisibility(View.GONE);
                    holder.tv_price_orginal.setPaintFlags(0);
                    holder.tv_price_discount.setVisibility(View.GONE);
                }

                holder.sp_packets.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String packetSize[] = new String[c.getPackets().size()];
                        for (int i = 0; i < c.getPackets().size(); i++) {
                            packetSize[i] = c.getPackets().get(i).getPacketSize();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(c.getProductNameEnglish())
                                .setItems(packetSize, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int pos) {
                                        holder.sp_packets.setText(c.getPackets().get(pos).getPacketSize());
                                        c.setSelectedPos(pos);
                                        if (!c.getPackets().get(pos).getDiscount().equals("0%")) {
                                            holder.tv_dis_percent.setVisibility(View.VISIBLE);
                                            holder.tv_dis_percent.setText(" " + c.getPackets().get(pos).getDiscount());
                                            holder.tv_price_orginal.setText(c.getPackets().get(pos).getOriginalPrice());
                                            holder.tv_price_orginal.setPaintFlags(holder.tv_price_orginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                            holder.tv_price_discount.setVisibility(View.VISIBLE);
                                            holder.tv_price_discount.setText(c.getPackets().get(pos).getPrice());
                                        } else {
                                            holder.tv_price_orginal.setText(c.getPackets().get(pos).getOriginalPrice());
                                            holder.tv_price_orginal.setPaintFlags(0);
                                            holder.tv_dis_percent.setVisibility(View.GONE);
                                            holder.tv_price_discount.setVisibility(View.GONE);
                                        }
                                    }
                                });

                        builder.show();
                    }
                });
            }
        } else {
            holder.fl_layout.setVisibility(View.GONE);
        }

        holder.btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (c.getPackets().size() > 0) {
                    product_id = c.getProductId();
                    if (c.getSelectedPos() == -1) {
                        packet_id = c.getPackets().get(0).getPacketId();
                    } else {
                        packet_id = c.getPackets().get(c.getSelectedPos()).getPacketId();
                    }

                    holder.btn_add.setVisibility(View.GONE);
                    holder.ll_cart_quantity.setVisibility(View.VISIBLE);
                    c.setClicked(true);
                    amount = 1;
                    if (cd.isConnected()) {
                        AddtoCartServices("1");
                    }
                }else {
                    Utility.showToastShort(mContext, "Please select pack size");
                }

            }
        });


        holder.iv_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_sub.isPressed()) {
                        if (c.getPackets().size() > 0) {
                            if (holder.et_qty.getText().toString().equals("1")) {
                                return;
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
                            if (amount != 0) {
                            }
                            holder.et_qty.setText(String.valueOf(amount));
                            //holder.et_qty.setText(String.valueOf(amount));


                            product_id = c.getProductId();

                            if (c.getSelectedPos() == -1) {
                                packet_id = c.getPackets().get(0).getPacketId();
                            } else {
                                packet_id = c.getPackets().get(c.getSelectedPos()).getPacketId();
                            }
                            //Utility.showToastShort(mContext, product_id + ":" + packet_id);
                            if (cd.isConnected()) {
                                AddtoCartServices("0");
                            }
                        }else{
                            Utility.showToastShort(mContext, "Please select pack size");
                        }
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
                        if (c.getPackets().size() > 0) {
                            if (!holder.et_qty.getText().toString().isEmpty()) {
                                amount = Integer.parseInt(holder.et_qty.getText().toString());
                            } else {
                                amount = 0;
                            }
                            amount += 1;
                            holder.et_qty.setText(String.valueOf(amount));
                            if (amount != 0) {
                            }

                            product_id = c.getProductId();

                            if (c.getSelectedPos() == -1) {
                                packet_id = c.getPackets().get(0).getPacketId();
                            } else {
                                packet_id = c.getPackets().get(c.getSelectedPos()).getPacketId();
                            }

                            if (cd.isConnected()) {
                                AddtoCartServices("1");
                            }
                        }else{
                            Utility.showToastShort(mContext, "Please select pack size");
                        }
                    }
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products_dropdown, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

        Call<BaseResponse> call = redditAPI.AddtoCartService(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext), product_id, packet_id, "1", isCartAdd);
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
