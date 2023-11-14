package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class SplashActivity extends AppCompatActivity {
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
