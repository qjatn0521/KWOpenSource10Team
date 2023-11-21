package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle sa) {
        super.onCreate(sa);
        setContentView(R.layout.activity_splash);

        ImageView loadingIv = findViewById(R.id.splash1);
        ImageView loadingIv2 = findViewById(R.id.splash2);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(loadingIv);
        GlideDrawableImageViewTarget gifImage2 = new GlideDrawableImageViewTarget(loadingIv2);
        Glide.with(this).load(R.drawable.splash_gif_icon).into(gifImage);
        Glide.with(this).load(R.drawable.splash_gif_text).into(gifImage2);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2800);


    }

}
