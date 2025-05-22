package com.example.fitostory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class SplashActivity extends AppCompatActivity {
    static final String STATE_DARK_MODE = "Dark Mode";

    private boolean isDarkMode;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_THEME = "isDarkTheme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpTheme(savedInstanceState);

        ImageView logoImage = findViewById(R.id.logoImage);
        Glide.with(this)
                .load(R.drawable.logo)  // Load image from resources or URL
                .apply(RequestOptions.circleCropTransform())  // Apply circular transformation
                .into(logoImage);
        // Load the fade-in animation

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImage.startAnimation(fadeIn);

        // Delay for a few seconds then start the main activity
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();  // Close splash activity
        }, 7000);  // 7-second delay
    }

    public void setUpTheme(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean(PREF_THEME, false);
        //switchDarkMode.setChecked(isDarkTheme);

        if(savedInstanceState != null){
            isDarkMode = savedInstanceState.getBoolean(STATE_DARK_MODE, isDarkTheme);
            //switchDarkMode.setChecked(isDarkMode);
        }
        if (isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putBoolean(STATE_DARK_MODE, isDarkMode);
        super.onSaveInstanceState(outState);
    }
}