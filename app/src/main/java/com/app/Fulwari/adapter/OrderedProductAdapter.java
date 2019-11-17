package com.app.Fulwari.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.Fulwari.ProductCart;
import com.app.Fulwari.R;
import com.app.Fulwari.model.CartDeleteAction;
import com.app.Fulwari.model.MyCart;
import com.app.Fulwari.model.MyOrdersDetailsModel;
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

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.MyViewHolder> {
    private List<MyOrdersDetailsModel.CartDatum> moviesList;
    Context mContext;
    private int amount = 0;
    MyOrdersDetailsModel.CartDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    String cartstringjson = "";
    String cart_id = "";
    ConnectionDetector cd;
    CartDeleteAction cartDeleteAction;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_productname, tv_size, tv_producttype, tv_price, tv_packetsize, tv_position;
        TextView et_qty;
        ImageView iv_product;
        ProgressBar progressbar;
        TextView tv_id, tv_unitprice, tv_quantity;

        public MyViewHolder(View view) {
            super(view);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_producttype = (TextView) view.findViewById(R.id.tv_producttype);
            tv_size = (TextView) view.findViewById(R.id.tv_size);
            tv_position = (TextView) view.findViewById(R.id.tv_position);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            et_qty = view.findViewById(R.id.et_qty);
            iv_product = (ImageView) view.findViewById(R.id.iv_product);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
            //  btn_update = view.findViewById(R.id.btn_update);
            tv_unitprice = view.findViewById(R.id.tv_unitprice);
            tv_quantity = view.findViewById(R.id.tv_quantity);
            tv_packetsize = view.findViewById(R.id.tv_packetsize);

            // this.myCustomEditTextListener = myCustomEditTextListener;
            //et_qty.addTextChangedListener(myCustomEditTextListener);
        }
    }

    public OrderedProductAdapter(List<MyOrdersDetailsModel.CartDatum> moviesList, Context mContext) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_ordered, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        movie = moviesList.get(position);
        holder.tv_productname.setText(movie.getProductName());
        holder.tv_id.setText(movie.getCartId());
        holder.et_qty.setText("Quantity: "+String.valueOf(movie.quantity));
        holder.tv_price.setText("Subtotal: "+"\u20A8" + ". " + movie.originalPrice);

        holder.tv_packetsize.setText(movie.packetSize);
        holder.tv_unitprice.setText("Unit Price: "+"\u20A8" + ". " + String.valueOf(movie.unitPrice));
        //holder.tv_quantity.setText("Packet      : " + String.valueOf(movie.quantity) + " Pc(s)");
       /* if (!movie.discount.equalsIgnoreCase("0")) {
            holder.tv_price.setVisibility(View.GONE);
            holder.tv_discount.setVisibility(View.GONE);
            //holder.tv_price.setText("\u20B9" + " " + movie.original_price + " " + movie.discount + " off");
            holder.tv_discount.setText(movie.discount + "% off");
            holder.tv_price.setText("\u20A8" + ". " + movie.originalPrice);
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tv_price.setVisibility(View.GONE);
            holder.tv_discount.setVisibility(View.GONE);
        }*/


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
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
