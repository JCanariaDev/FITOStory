package com.example.fitostory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoryListActivity extends AppCompatActivity {
    private EditText searchEditText;
    private ImageView searchButton;
    private ArrayList<StoryClass> filteredList;

    DataBaseHelper dataBaseHelper;
    ArrayList<StoryClass> storyClasses;
    Toolbar toolbar;

    private GestureDetector gestureDetector;

    private RecyclerView storyRecyclerView;
    private QuickFeatureAdapter storyAdapter;

    static final String STATE_DARK_MODE = "Dark Mode";

    private boolean isDarkMode;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_THEME = "isDarkTheme";
    private static final String PREF_BG_MUSIC = "bgMusic";

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
        setContentView(R.layout.activity_story_list);
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

        setUpTheme(savedInstanceState);
        setUpBGMusic();
        setUpLibraryStories();

        dataBaseHelper = new DataBaseHelper(StoryListActivity.this);
        //storyClasses = dataBaseHelper.getAllStory();//debug----------------

        filteredList = new ArrayList<>(storyClasses);

        storyRecyclerView = findViewById(R.id.recyclerView);
        storyAdapter = new QuickFeatureAdapter(getApplicationContext(), filteredList, R.layout.story_item);
        storyRecyclerView.setAdapter(storyAdapter);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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

//*************************************
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        // Search button click listener
        searchButton.setOnClickListener(v -> filterList(searchEditText.getText().toString()));
        listenerForEditText();
    }

    private void listenerForEditText(){
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Handle the search action here
                    // For example, start a search function
                    filterList(searchEditText.getText().toString());
                    return true;  // Indicates that the action was handled
                }
                return false;
            }
        });

    }

    private void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(storyClasses);
        } else {
            for (StoryClass storyClass : storyClasses) {
                if (storyClass.getStoryTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(storyClass);
                }
            }
        }
        storyAdapter.notifyDataSetChanged(); // Refresh RecyclerView
    }

    public void setUpLibraryStories(){
        Intent i = getIntent();
        String creatureName = i.getStringExtra("creature_name");

        dataBaseHelper = new DataBaseHelper(StoryListActivity.this);
        storyClasses = dataBaseHelper.getStoriesByLibrary(creatureName);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putBoolean(STATE_DARK_MODE, isDarkMode);
        super.onSaveInstanceState(outState);
    }

    //App Bar Implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(this, SettingsActivity.class);
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