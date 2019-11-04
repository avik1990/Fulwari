package com.app.mbazzar.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.mbazzar.ProductCart;
import com.app.mbazzar.R;
import com.app.mbazzar.model.CartDeleteAction;
import com.app.mbazzar.model.MyCart;
import com.app.mbazzar.retrofit.api.ApiServices;
import com.app.mbazzar.utils.ConnectionDetector;
import com.app.mbazzar.utils.Preferences;
import com.app.mbazzar.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private List<MyCart.CartDatum> moviesList;
    private List<MyCart.CartDatum> moviesList1;
    private List<MyCart.CartDatum> mArrayList;
    Context mContext;
    private int amount = 0;
    MyCart.CartDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    String cartstringjson = "";
    String cart_id = "";
    ConnectionDetector cd;
    CartDeleteAction cartDeleteAction;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_productname, tv_size, tv_producttype, tv_price, tv_packetsize, tv_position;
        ImageView iv_sub, iv_add;
        TextView et_qty;
        ImageView iv_product;
        ProgressBar progressbar;
        TextView tv_id, tv_unitprice, tv_discount, tv_quantity;
        Button btn_delete;

        public MyViewHolder(View view) {
            super(view);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_producttype = (TextView) view.findViewById(R.id.tv_producttype);
            tv_size = (TextView) view.findViewById(R.id.tv_size);
            tv_position = (TextView) view.findViewById(R.id.tv_position);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            iv_sub = (ImageView) view.findViewById(R.id.iv_sub);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            et_qty = view.findViewById(R.id.et_qty);
            iv_product = (ImageView) view.findViewById(R.id.iv_product);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
            btn_delete = view.findViewById(R.id.btn_delete);
            //  btn_update = view.findViewById(R.id.btn_update);
            tv_unitprice = view.findViewById(R.id.tv_unitprice);
            tv_quantity = view.findViewById(R.id.tv_quantity);
            tv_packetsize = view.findViewById(R.id.tv_packetsize);

            // this.myCustomEditTextListener = myCustomEditTextListener;
            //et_qty.addTextChangedListener(myCustomEditTextListener);
        }
    }

    public CartAdapter(List<MyCart.CartDatum> moviesList, Context mContext) {
        this.moviesList = moviesList;
        this.moviesList1 = moviesList;
        this.mArrayList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        movie = moviesList.get(position);
        holder.tv_productname.setText(movie.getProductName());
        holder.tv_id.setText(movie.getCartId());
        holder.et_qty.setText(String.valueOf(movie.quantity));

        holder.tv_packetsize.setText(movie.packetSize);
        holder.tv_unitprice.setText("\u20A8" + ". " + String.valueOf(movie.unitPrice));
        //holder.tv_quantity.setText("Packet      : " + String.valueOf(movie.quantity) + " Pc(s)");
        if (!movie.discount.equalsIgnoreCase("0")) {
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_discount.setVisibility(View.VISIBLE);
            //holder.tv_price.setText("\u20B9" + " " + movie.original_price + " " + movie.discount + " off");
            holder.tv_discount.setText(movie.discount + "% off");
            holder.tv_price.setText("\u20A8" + ". " + movie.original_price);
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tv_price.setVisibility(View.GONE);
            holder.tv_discount.setVisibility(View.GONE);
        }


        holder.tv_position.setText(movie.getCartId());

        try {
            Picasso.with(mContext)
                    .load(movie.getProductPhoto())
                    .into(holder.iv_product, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.iv_product.setImageResource(R.mipmap.ic_launcher);
                        }
                    });
        } catch (Exception e) {
        }

        try {
            if (!holder.et_qty.getText().equals("0") && !holder.et_qty.getText().toString().isEmpty()) {
                amount = Integer.parseInt(holder.et_qty.getText().toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("You want to remove this item.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (cd.isConnected()) {
                                    dialog.dismiss();
                                    deletefromcart(holder.tv_position.getText().toString().trim());
                                } else {
                                    Utility.showToastShort(mContext, "No Internet Connection");
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });


        holder.iv_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_sub.isPressed()) {
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
                        //Utility.showToastShort(mContext, holder.tv_position.getText().toString().trim());
                        holder.et_qty.setText(String.valueOf(amount));
                        Log.d("Position-", "" + holder.tv_position.getText().toString());
                        updateCart(holder.tv_position.getText().toString().trim(), holder.et_qty.getText().toString().trim(), "0");
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
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        amount += 1;
                        holder.et_qty.setText(String.valueOf(amount));
                        if (amount != 0) {
                        }
                        Log.d("Position+", "" + holder.tv_position.getText().toString());
                        //Utility.showToastShort(mContext, holder.tv_position.getText().toString().trim());
                        updateCart(holder.tv_position.getText().toString().trim(), "1", "1");
                    }
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    private void deletefromcart(String cart_id) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.GetCartDeleteAction(Preferences.get_userId(mContext), cart_id, Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        ((ProductCart) mContext).LoadCartProduct();
                    } else {
                        Utility.showToastShort(mContext, cartDeleteAction.getMsg());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void updateCart(String cart_id, String quantity, String isCartAdd) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.UpdateMyCart(Preferences.get_userId(mContext), cart_id, Preferences.get_UniqueId(mContext), "1", isCartAdd);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        Utility.showToastShort(mContext, cartDeleteAction.getMsg());
                        ((ProductCart) mContext).LoadCartProduct();
                    } else {
                        Utility.showToastShort(mContext, cartDeleteAction.getMsg());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
