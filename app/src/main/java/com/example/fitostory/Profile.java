package com.example.fitostory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Profile extends AppCompatActivity {
    Toolbar toolbar;
    private GestureDetector gestureDetector;
    static final String STATE_DARK_MODE = "Dark Mode";
    private boolean isDarkMode;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "themePrefs";
    private static final String PREF_THEME = "isDarkTheme";
    private static final String PREF_BG_MUSIC = "bgMusic";

    TextView username;
    TextView email;

    ImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    String PREF_KEY = "profile_image_path";

    DataBaseHelper dataBaseHelper;


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
            //Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent i = new Intent(this, FavoritesActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            //Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
    //******************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataBaseHelper = new DataBaseHelper(Profile.this);

        profileImageView = findViewById(R.id.profilePic);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved image from internal storage
        String imagePath = sharedPreferences.getString(PREF_KEY, null);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                //profileImageView.setImageURI(Uri.fromFile(imgFile));
                Glide.with(this)
                        .load(Uri.fromFile(imgFile))
                        .apply(RequestOptions.circleCropTransform())  // Apply circular transformation
                        .into(profileImageView);
            }
        }

        profileImageView.setOnClickListener(v -> openImageChooser());

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Toolbar toolbar1 =
                findViewById(R.id.toolbar);
        registerForContextMenu(toolbar1);

        setUpTheme(savedInstanceState);
        setUpBGMusic();
        setUpProfile();

        ImageButton editUsernameBtn = findViewById(R.id.editUsername);
        ImageButton editEmailBtn = findViewById(R.id.editEmail);
        ImageButton editPasswordBtn = findViewById(R.id.editPassword);

        editUsernameBtn.setOnClickListener(v -> showEditUsernameDialog());
        editEmailBtn.setOnClickListener(v -> showEditEmailDialog());
        editPasswordBtn.setOnClickListener(v -> showEditPasswordDialog());


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
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Swiped Left", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // Attach GestureDetector to the main layout
        mainLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        //*************************************-
    }

    public void setUpProfile(){
        String email = SessionManager.getInstance().getUserEmail();
        TextView emailTextView = findViewById(R.id.email);
        emailTextView.setText(email);
        TextView psw = findViewById(R.id.password);
        psw.setText(dataBaseHelper.getUserWithEmail(email).getPassword());
        TextView username = findViewById(R.id.username);
        username.setText(dataBaseHelper.getUserWithEmail(email).getUsername());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try {
                // Save to internal storage
                String filename = "profile_" + System.currentTimeMillis() + ".jpg";
                File dir = new File(getFilesDir(), "SIMPLE_APP");
                if (!dir.exists()) dir.mkdirs();

                File destFile = new File(dir, filename);
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                OutputStream outputStream = new FileOutputStream(destFile);

                byte[] buffer = new byte[4096];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();

                // Save path in SharedPreferences
                sharedPreferences.edit().putString(PREF_KEY, destFile.getAbsolutePath()).apply();

                // Set the image in ImageView
                //profileImageView.setImageURI(Uri.fromFile(destFile));
                Glide.with(this)
                        .load(Uri.fromFile(destFile))  // Load image from resources or URL
                        .apply(RequestOptions.circleCropTransform())  // Apply circular transformation
                        .into(profileImageView);

            } catch (Exception e) {
                e.printStackTrace();
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
            //Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent i = new Intent(this, FavoritesActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            //Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
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

    // In your ProfileActivity.java

    private void showEditPasswordDialog() {
        // Inflate custom view
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_password, null);

        EditText editPassword = view.findViewById(R.id.editPassword);

        // Build dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    String newPassword = editPassword.getText().toString();
                    // Handle password update logic here
                    Toast.makeText(this, "Password changed to: " + newPassword, Toast.LENGTH_SHORT).show();
                    String email = SessionManager.getInstance().getUserEmail();
                    dataBaseHelper.updateUserData(email, "password", newPassword);
                    setUpProfile();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        dialog.show();
    }

    private void showEditUsernameDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_username, null);

        EditText editUsername = view.findViewById(R.id.editUsername);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newUsername = editUsername.getText().toString();
                    // Save updated username
                    Toast.makeText(this, "Username updated to: " + newUsername, Toast.LENGTH_SHORT).show();
                    String email = SessionManager.getInstance().getUserEmail();
                    dataBaseHelper.updateUserData(email, "username", newUsername);
                    setUpProfile();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditEmailDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_email, null);

        EditText editEmail = view.findViewById(R.id.editEmail);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newEmail = editEmail.getText().toString();
                    // Save updated email
                    Toast.makeText(this, "Email updated to: " + newEmail, Toast.LENGTH_SHORT).show();
                    String email = SessionManager.getInstance().getUserEmail();
                    dataBaseHelper.updateUserData(email, "email", newEmail);
                    SessionManager.getInstance().setUserEmail(newEmail);
                    setUpProfile();
                })
                .setNegativeButton("Cancel", null)
                .show();
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

    public void logout(View view) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void editPic(View view) {
        //In Android Studio how if i have an onclick in that put in the imageview in the xml, so if that method is execute, how cabn you
        //pop up the image chooser so after tha user pick the image, that image is set into the imageview, and how can you also
        //put the int or id of that image or binary or whatever, or set up in the sharedpreferences whatever you want, so that the profile image is
        //permanent unless the user click the imageview again and pick another image
    }
}