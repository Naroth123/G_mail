package com.hammersmith.rothbangelo.g_mail.apps.apps;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hammersmith.rothbangelo.g_mail.R;
import com.hammersmith.rothbangelo.g_mail.apps.apps.Fragment.Fragment_Activity;
import com.hammersmith.rothbangelo.g_mail.apps.apps.Fragment.LoadImageFromURL;

public class MainActivity extends AppCompatActivity {
    private Fragment GG;
    private  Fragment FB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            GG = new Fragment_Activity();
            getSupportFragmentManager().beginTransaction().add(R.id.llLayout, GG).commit();
            
        }
//        else{
//
//            FB = new FB_Fragment();
//
//            getSupportFragmentManager().beginTransaction().add(R.id.llLayout,FB).commit();
//
//
//        }

    }
}
