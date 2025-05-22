package com.example.fitostory;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.ArrayList;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    String storyTitle;
    int numberOfQuiz;
    TextView q1, q2, q3, q4, q5, q6, q7, q8, q9, q10;

    RadioButton a1, b1, c1, d1;
    RadioButton a2, b2, c2, d2;
    RadioButton a3, b3, c3, d3;
    RadioButton a4, b4, c4, d4;
    RadioButton a5, b5, c5, d5;
    RadioButton a6, b6, c6, d6;
    RadioButton a7, b7, c7, d7;
    RadioButton a8, b8, c8, d8;
    RadioButton a9, b9, c9, d9;
    RadioButton a10, b10, c10, d10;


    ArrayList<TextView> qTextViews;

    ArrayList<ArrayList<RadioButton>> oRadioBtn;

    ArrayList<RadioButton> q1o;
    ArrayList<RadioButton> q2o;
    ArrayList<RadioButton> q3o;
    ArrayList<RadioButton> q4o;
    ArrayList<RadioButton> q5o;
    ArrayList<RadioButton> q6o;
    ArrayList<RadioButton> q7o;
    ArrayList<RadioButton> q8o;
    ArrayList<RadioButton> q9o;
    ArrayList<RadioButton> q10o;

    DataBaseHelper dataBaseHelper;
    StoryClass storyClass;

    TextView creatureNameTextView;
    TextView storyTitleTextView;

    Toolbar toolbar;
    private GestureDetector gestureDetector;
    static final String STATE_DARK_MODE = "Dark Mode";
    private boolean isDarkMode;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_THEME = "isDarkTheme";
    private static final String PREF_BG_MUSIC = "bgMusic";
    private static final String PREF_TIMER = "timer";

    private int score;
    String[] answers;
    String[] userAnswers;
    boolean[] correctAnswerStatus; // Track whether each question is answered correctly

    private boolean isMuted = true; // Initial state

    CountDownTimer countDownTimer;
    long timeLeftInMillis;
    TextView timer;

    Button prev;
    Button next;
    Button submit;
    int index = 0;

    private SoundPool soundPool;
    private int goodSound, badSound;
    private boolean soundsLoaded = false;

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
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        submit = findViewById(R.id.submit);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);

        toolbar = findViewById(R.id.toolbar);
        timer = findViewById(R.id.timer);
        setSupportActionBar(toolbar);
        final Toolbar toolbar1 =
                findViewById(R.id.toolbar);
        registerForContextMenu(toolbar1);

        setUpTheme(savedInstanceState);
        setUpBGMusic();
        startTimer();
        load();
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

    public void prevClick(View view) {
        if (index > 0) {
            index--;
            load();
        }
    }

    public void nextClick(View view) {
        if (index < numberOfQuiz - 1) {
            index++;
            load();
        } else {
            // We're on the last question and Submit button is clicked
            viewScoreClick(view);
        }
    }

    public void load() {
        Intent intent = getIntent();
        storyTitle = intent.getStringExtra("quiz_storyTitle");
        dataBaseHelper = new DataBaseHelper(QuizActivity.this);

        // Fetch quiz by story title
        QuizClass quiz = dataBaseHelper.getQuizByStoryName(storyTitle);
        numberOfQuiz = quiz.getAllQuestion().length;

        // Update pagination text
        TextView paginationText = findViewById(R.id.textView2);
        paginationText.setText((index + 1) + "/" + numberOfQuiz);

        // Handle button visibility based on current index
        Button prevButton = findViewById(R.id.prev);
        Button nextButton = findViewById(R.id.next);
        Button submitButton = findViewById(R.id.submit);

        if (index == 0) {
            prevButton.setTextColor(Color.GRAY);
        } else {
            prevButton.setTextColor(Color.WHITE);
        }

        if (index == numberOfQuiz - 1) {
            nextButton.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }

        // Initialize or ensure userAnswers array is created
        if (userAnswers == null) {
            userAnswers = new String[numberOfQuiz];
        }

        if (answers == null) {
            answers = quiz.getAllAnswers();
        }

        // Initialize correctAnswerStatus array if it's null
        if (correctAnswerStatus == null) {
            correctAnswerStatus = new boolean[numberOfQuiz];
            // Initialize all to false
            for (int i = 0; i < numberOfQuiz; i++) {
                correctAnswerStatus[i] = false;
            }
        }

        // Get the quiz container
        LinearLayout quizContainer = findViewById(R.id.quizContainer);
        quizContainer.removeAllViews();

        // Display only the question at the current index
        if (index < numberOfQuiz) {
            // Create TextView for the question
            TextView questionText = new TextView(this);
            questionText.setText((index + 1) + ". " + quiz.getQuestion(index));
            questionText.setTextSize(18);
            questionText.setTextColor(Color.WHITE);
            questionText.setPadding(0, 20, 0, 10);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 50, 0, 10);
            questionText.setLayoutParams(params);
            quizContainer.addView(questionText);

            // Create a RadioGroup for the options
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);

            // Get options for this specific question
            String[] options = quiz.getOptions(index);

            for (int j = 0; j < options.length; j++) {
                RadioButton rb = new RadioButton(this);
                char label = (char) ('a' + j); // a, b, c, d

                // Set the ID of each RadioButton
                String radioButtonId = label + Integer.toString(index + 1);
                rb.setId(View.generateViewId());
                rb.setTag(radioButtonId);
                rb.setTextColor(Color.WHITE);
                rb.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

                rb.setText(label + ". " + options[j]);

                // Check if this option was previously selected
                if (userAnswers[index] != null && userAnswers[index].equals(options[j])) {
                    rb.setChecked(true);
                }

                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionClick(v);
                    }
                });
                radioGroup.addView(rb);
            }

            quizContainer.addView(radioGroup);
        }

        // Set creature and story information
        creatureNameTextView = findViewById(R.id.creatureName);
        storyTitleTextView = findViewById(R.id.storyTitle);
        ArrayList<StoryClass> storyClasses = dataBaseHelper.getAllStory();
        for (StoryClass sC: storyClasses) {
            if (sC.getStoryTitle().equals(storyTitle)) {
                storyClass = sC;
                break;
            }
        }
        creatureNameTextView.setText(storyClass.getCreatureName());
        storyTitleTextView.setText(storyClass.getStoryTitle());
    }

    public void optionClick(View view) {
        RadioButton selectedRadioButton = (RadioButton) view;
        String selectedText = selectedRadioButton.getText().toString();

        // Retrieve the custom ID tag (e.g., "a6" for the 6th question, 1st option)
        String radioButtonId = (String) view.getTag();
        String letter = radioButtonId.substring(0, 1);  // "a", "b", "c", etc.
        String numberStr = radioButtonId.substring(1);  // "6" for the 6th question
        int questionIndex = Integer.parseInt(numberStr) - 1;  // Convert to zero-based index

        if (questionIndex >= 0 && questionIndex < answers.length) {
            String correctAnswer = answers[questionIndex];

            String[] parts = selectedText.split("\\. ", 2);
            if (parts.length == 2) {
                String selectedAnswerText = parts[1];

                // Store the previous state of this question (correct or not)
                boolean wasCorrectBefore = correctAnswerStatus[questionIndex];

                // Store the user's current answer
                userAnswers[questionIndex] = selectedAnswerText;

                // Check if the answer is correct
                boolean isCorrectNow = selectedAnswerText.equals(correctAnswer);

                // Update score based on the change in correctness
                if (wasCorrectBefore && !isCorrectNow) {
                    // Was correct before, now wrong - decrease score
                    score--;
                    correctAnswerStatus[questionIndex] = false;
                } else if (!wasCorrectBefore && isCorrectNow) {
                    // Was wrong before, now correct - increase score
                    score++;
                    correctAnswerStatus[questionIndex] = true;
                }
                // If no change in correctness (correct→correct or wrong→wrong),
                // don't change the score

                // Update the correctness status for this question
                correctAnswerStatus[questionIndex] = isCorrectNow;

                // Display the score
                //Toast.makeText(this, "Score: " + score, Toast.LENGTH_SHORT).show();
            }
        }
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

    private void startTimer() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int timerMinutes = sharedPreferences.getInt(PREF_TIMER, 1); // default 1 minute
        // Convert minutes to milliseconds
        timeLeftInMillis = timerMinutes * 60 * 1000;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsTotal = (int) (millisUntilFinished / 1000);
                int minutes = secondsTotal / 60;
                int seconds = secondsTotal % 60;

                String timeFormatted;
                if (minutes > 0) {
                    timeFormatted = String.format("%d:%02d s", minutes, seconds);
                } else {
                    timeFormatted = String.format("%d s", seconds);
                }

                timer.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                showTimeUpDialog();
            }
        }.start();
    }

    private void showTimeUpDialog() {
        showBlobShapedAlert("Time's up!", "", "😵", 0xFFE57373, badSound);

        /*new AlertDialog.Builder(QuizActivity.this)
                .setTitle("Time's up!")
                .setMessage("Your time is over.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent i = new Intent(QuizActivity.this, ScoreActivity.class);
                    i.putExtra("quiz_storyTitle", storyTitle);
                    i.putExtra("score", score);
                    i.putExtra("numOfQues", numberOfQuiz);
                    startActivity(i);
                    finish();
                })
                .show();*/
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
            Intent i = new Intent(this, ScoreActivity.class);
            i.putExtra("quiz_storyTitle", storyTitle);
            i.putExtra("score", score);
            i.putExtra("numOfQues", numberOfQuiz);
            startActivity(i);
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

    public void viewScoreClick(View view) {
        showBlobShapedAlert("View Score", "", "😊", 0xFF81C784, goodSound);
        /*Intent i = new Intent(this, ScoreActivity.class);
        i.putExtra("quiz_storyTitle", storyTitle);
        i.putExtra("score", score);
        i.putExtra("numOfQues", numberOfQuiz);
        startActivity(i);*/
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