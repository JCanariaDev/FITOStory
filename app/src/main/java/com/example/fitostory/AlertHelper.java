package com.example.fitostory;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class AlertHelper {
    private static TextToSpeech textToSpeech;
    private static boolean isTtsInitialized = false;

    /**
     * Shows an animated AlertDialog with TextToSpeech
     *
     * @param context The activity context
     * @param message The message to display and speak
     * @param isSuccess Whether this is a success or error message
     */
    public static void showAnimatedAlert(Context context, String message, boolean isSuccess) {
        // Initialize TextToSpeech if not already initialized
        initTTS(context);

        // Build custom alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_for_log_sign, null);

        // Set the message
        TextView alertMessage = dialogView.findViewById(R.id.alert_message);
        alertMessage.setText(message);

        // Set icon based on success/failure
        TextView alertIcon = dialogView.findViewById(R.id.alert_icon);
        if (isSuccess) {
            alertIcon.setText("✓");
            alertIcon.setTextColor(context.getResources().getColor(R.color.success_color));
        } else {
            alertIcon.setText("⚠");
            alertIcon.setTextColor(context.getResources().getColor(R.color.error_color));
        }

        builder.setView(dialogView);
        builder.setCancelable(true);

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Apply animation to dialog content
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.alert_animation);
        dialogView.startAnimation(animation);

        // Speak the message
        speakMessage(message);

        // Auto dismiss after delay
        dialogView.postDelayed(alertDialog::dismiss, 3000);
    }

    private static void initTTS(Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    isTtsInitialized = true;
                    textToSpeech.setLanguage(Locale.US);
                }
            });
        }
    }

    private static void speakMessage(String message) {
        if (isTtsInitialized && textToSpeech != null) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, "alert_id");
        }
    }

    /**
     * Should be called in onDestroy method of activities using this helper
     */
    public static void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
            isTtsInitialized = false;
        }
    }
}