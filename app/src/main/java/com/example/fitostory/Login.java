package com.example.fitostory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Login extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_THEME = "isDarkTheme";

    EditText email;
    EditText psw;

    DataBaseHelper dataBaseHelper;

    private SoundPool soundPool;
    private int goodSound, badSound;
    private boolean soundsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView logoImage = findViewById(R.id.logoImage);
        Glide.with(this)
                .load(R.drawable.logo)  // Load image from resources or URL
                .apply(RequestOptions.circleCropTransform())  // Apply circular transformation
                .into(logoImage);

        dataBaseHelper = new DataBaseHelper(Login.this);

        email = findViewById(R.id.email);
        psw = findViewById(R.id.password);

        setUpTheme();
        initSounds();
    }

    private void initSounds() {
        // Create SoundPool with appropriate builder pattern for newer API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            // For older devices
            soundPool = new SoundPool(3, android.media.AudioManager.STREAM_MUSIC, 0);
        }

        // Load sound effects - replace with your actual sound resource IDs
        goodSound = soundPool.load(this, R.raw.clap, 1);
        badSound = soundPool.load(this, R.raw.bad, 1);

        // Set listener to know when sounds are loaded
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            soundsLoaded = true;
        });
    }

    public void setUpTheme(){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean(PREF_THEME, false);
        if (isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void goToMainActivity(View view) {
        String inputEmail = email.getText().toString().trim();
        String inputPassword = psw.getText().toString().trim();

        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            // Using AlertHelper instead of Toast
            AlertHelper.showAnimatedAlert(this, "Please complete the form", false);
            return;
        }

        UserInfo userInfo = dataBaseHelper.getUser(inputEmail, inputPassword);

        if (userInfo == null) {
            // Using AlertHelper instead of Toast
            //AlertHelper.showAnimatedAlert(this, "Email not found", false);
            showBlobShapedAlert("Email not found!", "", "😵", 0xFFE57373, badSound);
        } else if (userInfo.getUsername() == null) {
            // Using AlertHelper instead of Toast
            //AlertHelper.showAnimatedAlert(this, "Incorrect password", false);
            showBlobShapedAlert("Incorrect password!", "", "😵", 0xFFE57373, badSound);
        } else {
            showBlobShapedAlert("Login successful!", "", "😊", 0xFF81C784, goodSound, inputEmail);
            // Credentials are correct
            /*AlertHelper.showAnimatedAlert(this, "Login successful! Welcome back!", true);

            // Delay the activity transition slightly to allow the alert to be seen
            findViewById(android.R.id.content).postDelayed(() -> {
                /*Intent i = new Intent(this, MainActivity.class);
                SessionManager.getInstance().setUserEmail(inputEmail);
                startActivity(i);*/
           // }, 1500); // 1.5 second delay*/
        }
    }

    private void showBlobShapedAlert(String title, String message, String emoji, int accentColor, int soundId, String inputEmail) {
        // Create a dialog without a title
        Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.blob_shaped_dialog);

        // Make dialog background transparent to show custom shape
        if (customDialog.getWindow() != null) {
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Find views in custom layout
        TextView emojiView = customDialog.findViewById(R.id.custom_dialog_emoji);
        TextView titleView = customDialog.findViewById(R.id.custom_dialog_title);
        TextView messageView = customDialog.findViewById(R.id.custom_dialog_message);
        Button okButton = customDialog.findViewById(R.id.custom_dialog_button);
        View backgroundContainer = customDialog.findViewById(R.id.custom_dialog_container);

        // Configure views
        emojiView.setText(emoji);
        titleView.setText(title);
        messageView.setText(message);

        // Apply accent color to button
        okButton.setBackgroundTintList(ColorStateList.valueOf(accentColor));

        // Create and set the custom blob shape background with accent color
        CustomPathDrawable blobDrawable = new CustomPathDrawable(
                Color.WHITE,  // Fill color
                accentColor,  // Stroke color matching the accent
                dpToPx(this, 2)  // Stroke width
        );
        backgroundContainer.setBackground(blobDrawable);

        // Set button click listener
        okButton.setOnClickListener(v -> {
            // Animate button click
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .withEndAction(customDialog::dismiss)
                                .start();
                    })
                    .start();
            findViewById(android.R.id.content).postDelayed(() -> {
                Intent i = new Intent(this, MainActivity.class);
                SessionManager.getInstance().setUserEmail(inputEmail);
                startActivity(i);
            }, 1500);
        });

        // Apply animation
        if (customDialog.getWindow() != null) {
            customDialog.getWindow().getAttributes().windowAnimations = R.style.BlobDialogAnimation;
        }

        // Play sound effect when dialog appears
        playSound(soundId);

        // Show dialog with entrance animation
        customDialog.show();

        // Add additional entrance animation to dialog contents
        backgroundContainer.setScaleX(0.7f);
        backgroundContainer.setScaleY(0.7f);
        backgroundContainer.setAlpha(0f);

        backgroundContainer.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .start();
    }

    private void playSound(int soundId) {
        if (soundsLoaded) {
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        } else {
            // If sounds aren't loaded yet, wait a bit and try again
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (soundsLoaded) {
                    soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            }, 300);
        }
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void goToSignUp(View view) {
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown TextToSpeech when activity is destroyed
        AlertHelper.shutdown();
    }

    private void showBlobShapedAlert(String title, String message, String emoji, int accentColor, int soundId) {
        // Create a dialog without a title
        Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.blob_shaped_dialog);

        // Make dialog background transparent to show custom shape
        if (customDialog.getWindow() != null) {
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Find views in custom layout
        TextView emojiView = customDialog.findViewById(R.id.custom_dialog_emoji);
        TextView titleView = customDialog.findViewById(R.id.custom_dialog_title);
        TextView messageView = customDialog.findViewById(R.id.custom_dialog_message);
        Button okButton = customDialog.findViewById(R.id.custom_dialog_button);
        View backgroundContainer = customDialog.findViewById(R.id.custom_dialog_container);

        // Configure views
        emojiView.setText(emoji);
        titleView.setText(title);
        titleView.setTextSize(15);
        messageView.setText(message);

        // Apply accent color to button
        okButton.setBackgroundTintList(ColorStateList.valueOf(accentColor));

        // Create and set the custom blob shape background with accent color
        CustomPathDrawable blobDrawable = new CustomPathDrawable(
                Color.WHITE,  // Fill color
                accentColor,  // Stroke color matching the accent
                dpToPx(this, 2)  // Stroke width
        );
        backgroundContainer.setBackground(blobDrawable);

        // Set button click listener
        okButton.setOnClickListener(v -> {
            // Animate button click
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .withEndAction(customDialog::dismiss)
                                .start();
                    })
                    .start();
        });

        // Apply animation
        if (customDialog.getWindow() != null) {
            customDialog.getWindow().getAttributes().windowAnimations = R.style.BlobDialogAnimation;
        }

        // Play sound effect when dialog appears
        playSound(soundId);

        // Show dialog with entrance animation
        customDialog.show();

        // Add additional entrance animation to dialog contents
        backgroundContainer.setScaleX(0.7f);
        backgroundContainer.setScaleY(0.7f);
        backgroundContainer.setAlpha(0f);

        backgroundContainer.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .start();
    }
}