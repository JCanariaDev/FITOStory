package com.example.fitostory;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {
    String storyTitle;
    int score;

    DataBaseHelper dataBaseHelper;
    StoryClass storyClass;

    TextView creatureNameTextView;
    TextView storyTitleTextView;
    TextView scoreTextView;
    TextView percentTextView;

    Toolbar toolbar;
    private GestureDetector gestureDetector;
    static final String STATE_DARK_MODE = "Dark Mode";
    private boolean isDarkMode;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_THEME = "isDarkTheme";
    private static final String PREF_BG_MUSIC = "bgMusic";
    MediaPlayer bgMusic;

    private boolean isMuted = true; // Initial state

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
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Toolbar toolbar1 =
                findViewById(R.id.toolbar);
        registerForContextMenu(toolbar1);

        setUpTheme(savedInstanceState);
        setUpBGMusic();
        //ImageView imageView = findViewById(R.id.circleImageView);
        /*Glide.with(this)
                .load(R.drawable.rounded_bg)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);*/


        Intent i = getIntent();
        storyTitle = i.getStringExtra("quiz_storyTitle");
        score = i.getIntExtra("score",0);
        dataBaseHelper = new DataBaseHelper(ScoreActivity.this);

        creatureNameTextView = findViewById(R.id.creatureName);
        storyTitleTextView = findViewById(R.id.storyTitle);
        scoreTextView = findViewById(R.id.score);
        percentTextView = findViewById(R.id.textViewPercent);


        ArrayList<StoryClass> storyClasses = dataBaseHelper.getAllStory();
        for (StoryClass sC: storyClasses){
            if (sC.getStoryTitle().equals(storyTitle)){
                storyClass = sC;
            }
        }
        creatureNameTextView.setText(storyClass.getCreatureName());
        storyTitleTextView.setText(storyClass.getStoryTitle());
        scoreTextView.setText(score + "/" +i.getIntExtra("numOfQues", 0));
        //Toast.makeText(this, (score / 10) * 100, Toast.LENGTH_SHORT).show();
        //percentTextView.setText(String.valueOf( (score / 10) * 100));
        percentTextView.setText(String.valueOf( ((double) score / 10) * 100 ) + "%");

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
                    startActivity(new Intent(getApplicationContext(), LibraryActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Swiped Left", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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

    public void setUpBGMusic(){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isBG = sharedPreferences.getBoolean(PREF_BG_MUSIC, false);
        //Toast.makeText(this, "BG Music " + isBG, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putBoolean(STATE_DARK_MODE, isDarkMode);
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
            //Toast.makeText(this, "Nav Library Button Clicked", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.navQuizBtn){
            Intent i = new Intent(this, QuizTabActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.navSettingsBtn){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        /*else if (view.getId() == R.id.switchDarkMode){
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isDarkMode = false;
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isDarkMode = true;
            }
            recreate();
        }*/
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