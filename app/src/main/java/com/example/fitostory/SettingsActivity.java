package com.example.fitostory;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    private GestureDetector gestureDetector;

    static final String STATE_DARK_MODE = "Dark Mode";
    static final String STATE_SOUND_EFFECTS= "Sound Effects";
    static final String STATE_TEXT_SIZE = "Text Size";
    static final String STATE_BG_MUSIC = "Bg Music";
    static final String STATE_TIMER = "timer_state";

    private Switch switchDarkMode;
    private Switch switchSoundEffects;
    private TextView switchTextSize;
    private Switch switchBGMusic;
    private TextView textViewTimer;

    private boolean isDarkMode;
    private boolean isSoundEffects;
    private int mTextSize;
    private boolean isBGMusic;
    private int mTimer;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";

    private static final String PREF_THEME = "isDarkTheme";
    private static final String PREF_TEXT_SIZE = "textSize";
    private static final String PREF_SOUND_EFFECTS = "soundEffects";
    private static final String PREF_BG_MUSIC = "bgMusic";
    private static final String PREF_TIMER = "timer";
    MediaPlayer bgMusic;


    ImageView navHome, navLibrary, navQuiz, navSettings;

    //Touch Event Handling
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
    //*************************************************************

    //Context Menus Application
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent i = new Intent(this, FavoritesActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            Intent i = new Intent(this, Profile.class);
            startActivity(i);
            return true;
        }
        return super.onContextItemSelected(item);
    }
    //******************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Toolbar toolbar1 =
                findViewById(R.id.toolbar);
        registerForContextMenu(toolbar1);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchSoundEffects = findViewById(R.id.switchSound);
        switchTextSize = findViewById(R.id.textSize);
        switchBGMusic = findViewById(R.id.switchBGMusic);
        textViewTimer = findViewById(R.id.textTimer);

        setUpTheme(savedInstanceState);
        setUpTextSize(savedInstanceState);
        setUpSound(savedInstanceState);
        setUpBgMusic(savedInstanceState);
        setUpTimer(savedInstanceState);



        navHome = findViewById(R.id.homeBtn);
        navLibrary = findViewById(R.id.navlibraryBtn);
        navQuiz = findViewById(R.id.navQuizBtn);
        navSettings = findViewById(R.id.navSettingsBtn);




        //Touch Event Handling
        /*gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Toast.makeText(getApplicationContext(), "Long press detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Toast.makeText(getApplicationContext(), "Screen tapped", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        //********************************************************************

        //Touch Event Handling and Touch Gestures Integration
        View mainLayout = findViewById(R.id.main);
        // Initialize GestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Toast.makeText(getApplicationContext(), "Long press detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Toast.makeText(getApplicationContext(), "Screen tapped", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() < e2.getX()) {
                    Toast.makeText(getApplicationContext(), "Swiped Right", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), QuizTabActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Swiped Left", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // Attach GestureDetector to the main layout
        mainLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        //*************************************
    }


    public void setUpTheme(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean(PREF_THEME, false);
        switchDarkMode.setChecked(isDarkTheme);
        isDarkMode = isDarkTheme;

        if(savedInstanceState != null){
            isDarkMode = savedInstanceState.getBoolean(STATE_DARK_MODE, isDarkTheme);
            switchDarkMode.setChecked(isDarkMode);
        }
        if (isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                    isDarkMode = true;
                    saveThemePreference(true);
                    /*ImageView[] imageViews = {navHome, navLibrary, navQuiz, navSettings};
                    for (ImageView imageView: imageViews){
                        //Drawable drawable = imageView.getDrawable();
                        /*if (drawable != null) {
                            // Toggle between black and white
                            int currentColor = ((ColorDrawable) drawable).getColor();
                            int newColor = (currentColor == Color.BLACK) ? Color.WHITE : Color.BLACK;
                            drawable.setTint(newColor);
                        }*/
                        //drawable.setTint(Color.WHITE);
                        //imageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));  Tint color for vector asset
                        //imageView.setBackgroundColor(Color.BLACK);
                    //}*/
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    isDarkMode = false;
                    saveThemePreference(false);
                    /*ImageView[] imageViews = {navHome, navLibrary, navQuiz, navSettings};
                    for (ImageView imageView: imageViews){
                        //Drawable drawable = imageView.getDrawable();
                        /*if (drawable != null) {
                            // Toggle between black and white
                            int currentColor = ((ColorDrawable) drawable).getColor();
                            int newColor = (currentColor == Color.BLACK) ? Color.WHITE : Color.BLACK;
                            drawable.setTint(newColor);
                        }*/
                        //imageView.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                        //imageView.setBackgroundColor(Color.WHITE);
                    //}*/
                }
            }
        });
    }

    public void setUpTextSize(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int textSize = sharedPreferences.getInt(PREF_TEXT_SIZE, 12);
        switchTextSize.setText(String.valueOf(textSize));
        mTextSize = textSize;

        if(savedInstanceState != null){
            mTextSize = savedInstanceState.getInt(STATE_TEXT_SIZE, textSize);
            switchTextSize.setText(String.valueOf(mTextSize));
        }
    }

    public void setUpTimer(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int timerInt = sharedPreferences.getInt(PREF_TIMER, 1);
        textViewTimer.setText(String.valueOf(timerInt));
        mTimer = timerInt;

        if(savedInstanceState != null){
            mTimer = savedInstanceState.getInt(STATE_TIMER, timerInt);
            textViewTimer.setText(String.valueOf(mTimer));
        }
    }

    public void setUpSound(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSound = sharedPreferences.getBoolean(PREF_SOUND_EFFECTS, false);
        switchSoundEffects.setChecked(isSound);
        isSoundEffects = isSound;

        if(savedInstanceState != null){
            isSoundEffects = savedInstanceState.getBoolean(STATE_SOUND_EFFECTS, isSoundEffects);
            switchSoundEffects.setChecked(isSoundEffects);
        }

        switchSoundEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    isSoundEffects = true;
                    saveThemeSoundPreference(true);
                } else {
                    isSoundEffects = false;
                    saveThemeSoundPreference(false);
                }
            }
        });
    }

    public void setUpBgMusic(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isBG = sharedPreferences.getBoolean(PREF_BG_MUSIC, false);
        switchBGMusic.setChecked(isBG);
        isBGMusic = isBG;

        if(savedInstanceState != null){
            isBGMusic = savedInstanceState.getBoolean(STATE_BG_MUSIC, isBGMusic);
            switchBGMusic.setChecked(isBGMusic);
        }

        switchBGMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    isBGMusic = true;
                    startService(new Intent(getApplicationContext(), MusicService.class)); // Start music
                    saveThemeBGPreference(true);
                } else {
                    isBGMusic = false;
                    stopService(new Intent(getApplicationContext(), MusicService.class)); // Stop music
                    saveThemeBGPreference(false);
                }
            }
        });

        if (isBG) {
            startService(new Intent(getApplicationContext(), MusicService.class)); // Start music
        } else {
            stopService(new Intent(getApplicationContext(), MusicService.class)); // Stop music
        }
    }


    private void saveThemePreference(boolean isDarkTheme){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_THEME, isDarkTheme);
        editor.apply();
    }
    private void saveThemePreference(int textSize){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_TEXT_SIZE, textSize);
        editor.apply();
    }
    private void saveThemeSoundPreference(boolean isSoundEffects){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_SOUND_EFFECTS, isSoundEffects);
        editor.apply();
    }

    private void saveThemeBGPreference(boolean isBGMusic){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_BG_MUSIC, isBGMusic);
        editor.apply();
    }

    private void saveThemeTimerPreference(int timerInt){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_TIMER, timerInt);
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putBoolean(STATE_DARK_MODE, isDarkMode);
        outState.putInt(STATE_TEXT_SIZE, mTextSize);
        outState.putBoolean(STATE_BG_MUSIC, isBGMusic);
        outState.putBoolean(STATE_SOUND_EFFECTS, isSoundEffects);
        outState.putInt(STATE_TIMER, mTimer);
        super.onSaveInstanceState(outState);
    }


    //App Bar Implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /*int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            switchDarkMode.setChecked(true);
        } else {
            switchDarkMode.setChecked(false);
        }*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_about){
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent i = new Intent(this, FavoritesActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            Intent i = new Intent(this, Profile.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //***********************************************

    public void btnClick(View view) {
        if (view.getId() == R.id.homeBtn){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.navlibraryBtn){
            Intent i = new Intent(this, LibraryActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.navQuizBtn){
            Intent i = new Intent(this, QuizTabActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.navSettingsBtn){
            //Toast.makeText(this, "Nav Settings Button Clicked", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.upButton){
            //TextView textSizeView = findViewById(R.id.textSize);
            if (Integer.parseInt(switchTextSize.getText().toString()) < 30){
                switchTextSize.setText(String.valueOf(Integer.parseInt(switchTextSize.getText().toString()) + 1));
                mTextSize =  Integer.parseInt(switchTextSize.getText().toString());
                saveThemePreference(mTextSize);
            } else {
                Toast.makeText(this, "The text size must be less than or equal to 30", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.downButton){
            //TextView textSizeView = findViewById(R.id.textSize);
            if (Integer.parseInt(switchTextSize.getText().toString()) > 12){
                switchTextSize.setText(String.valueOf(Integer.parseInt(switchTextSize.getText().toString()) - 1));
                mTextSize =  Integer.parseInt(switchTextSize.getText().toString());
                saveThemePreference(mTextSize);
            } else {
                Toast.makeText(this, "The text size must be greater than or equal to 12", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.downButtonTimer){
            if (Integer.parseInt(textViewTimer.getText().toString()) > 1){
                textViewTimer.setText(String.valueOf(Integer.parseInt(textViewTimer.getText().toString()) - 1));
                mTimer =  Integer.parseInt(textViewTimer.getText().toString());
                saveThemeTimerPreference(mTimer);
            } else {
                Toast.makeText(this, "The timer must be greater than or equal to 1", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.upButtonTimer){
            if (Integer.parseInt(textViewTimer.getText().toString()) < 10){
                textViewTimer.setText(String.valueOf(Integer.parseInt(textViewTimer.getText().toString()) + 1));
                mTimer =  Integer.parseInt(textViewTimer.getText().toString());
                saveThemeTimerPreference(mTimer);
            } else {
                Toast.makeText(this, "The timer must be less than or equal to 10", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.accountBtn || view.getId() == R.id.accountTxt){
            Intent i = new Intent(this, Profile.class);
            startActivity(i);
        } else if (view.getId() == R.id.logoutBtn || view.getId() == R.id.logoutTxt){
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction("PAUSE");
        startService(pauseIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeIntent = new Intent(this, MusicService.class);
        resumeIntent.setAction("RESUME");
        startService(resumeIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, MusicService.class);
        stopIntent.setAction("STOP");
        startService(stopIntent);
    }

}