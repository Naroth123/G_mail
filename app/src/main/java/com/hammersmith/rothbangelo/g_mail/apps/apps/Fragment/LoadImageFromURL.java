package com.hammersmith.rothbangelo.g_mail.apps.apps.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by roat on 9/1/2015.
 */
public class LoadImageFromURL extends AsyncTask<String, String, Bitmap> {
    Button load_img;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
    Activity activity;
    public LoadImageFromURL(Activity activity,ImageView img){
        this.activity = activity;
        img.setImageBitmap(bitmap);
    }
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading Image ....");
        pDialog.show();

    }
    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {

        if(image != null){
//            img.setImageBitmap(image);
            bitmap = image;
            pDialog.dismiss();

        }else{

            pDialog.dismiss();
            Toast.makeText(activity, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

        }
    }
}