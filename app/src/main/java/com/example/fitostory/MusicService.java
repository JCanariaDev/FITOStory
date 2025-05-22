package com.example.fitostory;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
    private static MediaPlayer bgMusic;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_BG_MUSIC = "bgMusic";

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isBG = sharedPreferences.getBoolean(PREF_BG_MUSIC, false);

        if (isBG) {
            if (bgMusic == null) {
                Toast.makeText(this, "Music Service onCreate", Toast.LENGTH_SHORT).show();
                bgMusic = MediaPlayer.create(this, R.raw.multo);
                bgMusic.setLooping(true);
                bgMusic.start();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "PAUSE":
                    if (bgMusic != null && bgMusic.isPlaying()) {
                        bgMusic.pause();
                    }
                    break;
                case "RESUME":
                    if (bgMusic != null) {
                        bgMusic.start();
                    }
                    break;
                case "STOP":
                    stopMusic();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void stopMusic() {
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.release();
            bgMusic = null;
        }
    }
}
