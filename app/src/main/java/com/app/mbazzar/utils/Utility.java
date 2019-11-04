package com.app.mbazzar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.Toast;

import com.app.mbazzar.AboutUs;
import com.app.mbazzar.ChangePasswordActivity;
import com.app.mbazzar.ContactUs;
import com.app.mbazzar.Feedbackactivity;
import com.app.mbazzar.LoginActivity;
import com.app.mbazzar.MyOrderActivity;
import com.app.mbazzar.model.MyProfileActivity;
import com.app.mbazzar.OrderCancellation;
import com.app.mbazzar.Privacy;
import com.app.mbazzar.R;
import com.app.mbazzar.Refund;
import com.app.mbazzar.Replacement;
import com.app.mbazzar.ShippingContent;
import com.app.mbazzar.TermsCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static ConnectionDetector cd;
    public static String PACKAGE_NAME;

    public static void openNavDrawer(int id, final Context mContext) {

        cd = new ConnectionDetector(mContext);

        if (id == R.id.nav_aboutus) {
            if (cd.isConnected()) {
                if (!(mContext instanceof AboutUs)) {
                    Intent profileintent = new Intent(mContext, AboutUs.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_privacypolicy) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Privacy)) {
                    Intent profileintent = new Intent(mContext, Privacy.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
            //showLogoutAlert(mContext, "Are you sure?", "Logout");
        }

        if (id == R.id.nav_terms) {
            if (cd.isConnected()) {
                if (!(mContext instanceof TermsCondition)) {
                    Intent profileintent = new Intent(mContext, TermsCondition.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_contacts) {
            //showLogoutAlert(mContext, "Are you sure?", "Logout");
        }
        if (id == R.id.nav_ordercancellation) {
            if (cd.isConnected()) {
                if (!(mContext instanceof OrderCancellation)) {
                    Intent profileintent = new Intent(mContext, OrderCancellation.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_replacement) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Replacement)) {
                    Intent profileintent = new Intent(mContext, Replacement.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_refund) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Refund)) {
                    Intent profileintent = new Intent(mContext, Refund.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_shareapp) {
            PACKAGE_NAME = mContext.getApplicationContext().getPackageName();
            shareAll(mContext, "", "", mContext.getResources().getString(R.string.share_pkg) + PACKAGE_NAME + "&hl=en");
        }

        if (id == R.id.nav_feedback) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Feedbackactivity)) {
                    Intent profileintent = new Intent(mContext, Feedbackactivity.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_rateapp) {
            final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        if (id == R.id.nav_contacts) {
            if (cd.isConnected()) {
                if (!(mContext instanceof ContactUs)) {
                    Intent profileintent = new Intent(mContext, ContactUs.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_shipping) {
            if (cd.isConnected()) {
                if (!(mContext instanceof ShippingContent)) {
                    Intent profileintent = new Intent(mContext, ShippingContent.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_myprofile) {
            if (cd.isConnected()) {
                if (Preferences.getisVerified(mContext)) {
                    if (!(mContext instanceof MyProfileActivity)) {
                        Intent profileintent = new Intent(mContext, MyProfileActivity.class);
                        mContext.startActivity(profileintent);
                    }
                } else {
                    showToastShort(mContext, "Not Accessible by Guest");
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_myorders) {
            if (cd.isConnected()) {
                if (Preferences.getisVerified(mContext)) {
                    if (!(mContext instanceof MyOrderActivity)) {
                        Intent profileintent = new Intent(mContext, MyOrderActivity.class);
                        mContext.startActivity(profileintent);
                    }
                } else {
                    showToastShort(mContext, "Not Accessible by Guest");
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_changepass) {
            if (cd.isConnected()) {
                if (Preferences.getisVerified(mContext)) {
                    if (!(mContext instanceof ChangePasswordActivity)) {
                        Intent profileintent = new Intent(mContext, ChangePasswordActivity.class);
                        mContext.startActivity(profileintent);
                    }
                } else {
                    showToastShort(mContext, "Not Accessible by Guest");
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_logout) {
            showLogoutAlert(mContext, "Are you sure?", "Logout");
        }
    }

    public static void showLogoutAlert(final Context context, String msg, String title) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent profileintent = new Intent(context, LoginActivity.class);
                context.startActivity(profileintent);
                ((Activity) context).finishAffinity();
                cleardata(context);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void cleardata(Context mContext) {
        //DatabaseHandler db = new DatabaseHandler(mContext);
        SharedPreferences settings = mContext.getSharedPreferences("Kppref", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        // db.drop_all_data();
    }

    public static void CallContactNo(Context mContext) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Preferences.get_contactNo(mContext)));
        mContext.startActivity(callIntent);
    }

    public static void shareAll(Context mContext, String heading, String sub, String links) {
        //String title = heading;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, links);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    public static void showToastShort(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String getFormasssttedDate(String normal_date) {
        Log.d("DateFormat", normal_date);
        String anni = normal_date;
        String formated_date = "";
        if (anni.length() > 6) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date;
            try {
                date = originalFormat.parse(anni);
                formated_date = targetFormat.format(date);  // 20120821
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            formated_date = anni;
        }
        return formated_date;
    }

}
