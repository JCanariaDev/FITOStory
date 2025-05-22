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
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Locale;

public class StoryDetailActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    StoryClass storyClass;

    private TextToSpeech textToSpeech;
    private ImageButton listenButton;

    //---------------------
    TextView creatureNameTextView;
    TextView storyTitleTextView;
    TextView storyTextTextView;

    LinearLayout linearLayout;
    //-----------------------

    Toolbar toolbar;
    private GestureDetector gestureDetector;

    static final String STATE_DARK_MODE = "Dark Mode";
    static final String STATE_SOUND_EFFECTS= "Sound Effects";
    static final String STATE_TEXT_SIZE = "Text Size";



    private boolean isDarkMode;
    private boolean isSoundEffects;
    private int mTextSize;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";

    private static final String PREF_THEME = "isDarkTheme";
    private static final String PREF_TEXT_SIZE = "textSize";
    private static final String PREF_SOUND_EFFECTS = "soundEffects";
    private static final String PREF_BG_MUSIC = "bgMusic";
    MediaPlayer bgMusic;

    private boolean isMuted = true; // Initial state

    InitializeClass initializeClass;
    SetUpTwoChoices setUpTwoChoices;

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
        setContentView(R.layout.activity_story_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);

        creatureNameTextView = findViewById(R.id.creatureName);
        storyTitleTextView = findViewById(R.id.storyTitle);
        //storyTextTextView = findViewById(R.id.storyText);
        //linearLayout = findViewById(R.id.storyContentLayout);

        /*dataBaseHelper = new DataBaseHelper(StoryDetailActivity.this);
        Intent i = getIntent();
        String storyTitle = i.getStringExtra("story_title");
        initializeClass = new InitializeClass(dataBaseHelper,
                creatureNameTextView, storyTitleTextView,
                storyTextTextView, storyTitle, this);
        initializeClass.setUpStoryDetail();*/

        //Comment out the setUpTextSize() and setUpSound() method - debuging -------
        LinearLayout scrollLayout = findViewById(R.id.storyContentLayout);
        dataBaseHelper = new DataBaseHelper(StoryDetailActivity.this);
        Intent i = getIntent();
        String storyTitle = i.getStringExtra("story_title");
        setUpTwoChoices = new SetUpTwoChoices(dataBaseHelper,
                creatureNameTextView, storyTitleTextView,
                storyTextTextView, storyTitle, scrollLayout, this);
        setUpTwoChoices.setUpStoryDetail();


        //setup the sound feature
        //setUpTextSize(savedInstanceState);
        setUpSound(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Toolbar toolbar1 =
                findViewById(R.id.toolbar);
        registerForContextMenu(toolbar1);
        setUpTheme(savedInstanceState);
        setUpBGMusic();


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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Swiped Left", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), QuizTabActivity.class));
                }
                return true;
            }
        });

        // Attach GestureDetector to the main layout
        mainLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        //*************************************
    }

    /*public void setUpTextSize(Bundle savedInstanceState){
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int textSize = sharedPreferences.getInt(PREF_TEXT_SIZE, 12);
        //storyTextView.setText(String.valueOf(textSize));
        storyTextTextView.setTextSize(textSize);
        //Toast.makeText(this, Integer.toString(textSize), Toast.LENGTH_SHORT).show();
        if(savedInstanceState != null){
            mTextSize = savedInstanceState.getInt(STATE_TEXT_SIZE, textSize);
            //storyTextView.setText(String.valueOf(mTextSize));
            storyTextTextView.setTextSize(textSize);
        }
    }*/

    public void setUpSound(Bundle savedInstanceState){
        listenButton = findViewById(R.id.volume);
        //storyTextView = findViewById(R.id.storyText);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.ENGLISH);  // Change language if needed
                }
            }
        });

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSound = sharedPreferences.getBoolean(PREF_SOUND_EFFECTS, false);
        Toast.makeText(this, "Sound " + isSound, Toast.LENGTH_SHORT).show();
        isSoundEffects = isSound;

        //String text = storyTextTextView.getText().toString();
        String text = setUpTwoChoices.getAllDisplayedText();
        if (isSound) {
            listenButton.setImageResource(R.drawable.volume_on);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            listenButton.setImageResource(R.drawable.volume_off);
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                textToSpeech.stop();  // Stop reading immediately
            }
        }

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String text = storyTextTextView.getText().toString();
                String text = setUpTwoChoices.getAllDisplayedText();
                if (isMuted) {
                    listenButton.setImageResource(R.drawable.volume_on);
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    Toast.makeText(StoryDetailActivity.this, "Volume On", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoryDetailActivity.this, "Volume Off", Toast.LENGTH_SHORT).show();
                    listenButton.setImageResource(R.drawable.volume_off);
                    if (textToSpeech != null && textToSpeech.isSpeaking()) {
                        textToSpeech.stop();  // Stop reading immediately
                    }
                }
                isMuted = !isMuted; // Toggle state
            }
        });
    }

    /*public void setUpStoryDetail(){
        creatureNameTextView = findViewById(R.id.creatureName);
        storyTitleTextView = findViewById(R.id.storyTitle);
        storyTextTextView = findViewById(R.id.storyText);

        Intent i = getIntent();
        String storyTitle = i.getStringExtra("story_title");

        dataBaseHelper = new DataBaseHelper(StoryDetailActivity.this);
        ArrayList<StoryClass> storyClasses = dataBaseHelper.getAllStory();

        for (StoryClass sC: storyClasses){
            if (sC.getStoryTitle().equals(storyTitle)){
                storyClass = sC;
            }
        }

        creatureNameTextView.setText(storyClass.getCreatureName());
        storyTitleTextView.setText(storyClass.getStoryTitle());
        storyTextTextView.setText(storyClass.getStoryText());
    }*/


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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putBoolean(STATE_DARK_MODE, isDarkMode);
        outState.putInt(STATE_TEXT_SIZE, mTextSize);
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


    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop(); // Stop TTS playback
            textToSpeech.shutdown(); // Release TTS resources
            textToSpeech = null;
        }

        Intent stopIntent = new Intent(this, MusicService.class);
        stopIntent.setAction("STOP");
        startService(stopIntent);

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null && textToSpeech.isSpeaking()) {
            textToSpeech.stop(); // Pause TTS when the app is minimized
        }

        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction("PAUSE");
        startService(pauseIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    }
                }
            });
        }

        Intent resumeIntent = new Intent(this, MusicService.class);
        resumeIntent.setAction("RESUME");
        startService(resumeIntent);
    }


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


}