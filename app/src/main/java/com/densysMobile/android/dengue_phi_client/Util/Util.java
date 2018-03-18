package com.densysMobile.android.dengue_phi_client.Util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.densysMobile.android.dengue_phi_client.Model.Record;
import com.densysMobile.android.dengue_phi_client.Model.ptDateAdd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sineth on 1/26/2017.
 */

public class Util {
    public static final void makeToast(View view, String message, final int DURATION) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Click", null).show();
    }

    public static final void makeToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static List<Record> getDummyRecordData() {
        List<Record> l = new ArrayList<Record>();
        Record record;
        for (int i = 0; i < 10; i++) {
            record = new Record();
            record.setBht("BHT" + i);
            record.setDistrict("Col");
            record.setGnName("s");
            record.setHospitalName("dvd");
            record.setMohDesc("mog");
            record.setPatientVisitCount("1");
            record.setPhiDesc("dsvsvs");
            record.setPtName("sine");
            record.setPtSex("male");
            ptDateAdd PtDateAdd = new ptDateAdd();
            PtDateAdd.setDate("2012");
            PtDateAdd.setTimezone("Europe/Paris");
            PtDateAdd.setTimezone_type("3");
            record.setPtDateAdd(PtDateAdd);
            record.setRecid("" + i);
            l.add(record);
        }
        return l;
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    public static void notificationService(String content, String title, Context context, int i) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context).setSmallIcon(com.densysMobile.android.dengue_phi_client.R.drawable.design_dengue)
                .setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle())
                .setContentText(content)
                .setProgress(0, 0, true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i, notification.build());
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static String md5(String md5) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

        }
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
