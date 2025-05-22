package com.example.fitostory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fitostory.QuizClass;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quiz53.db";
    public static final String QUIZ_TABLE = "quiz_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image_res";
    public static final String COLUMN_STORY_NAME = "story_name";
    public static final String COLUMN_QUESTIONS = "questions";
    public static final String COLUMN_OPTIONS = "options";
    public static final String COLUMN_ANSWERS = "answers";

    // Library Table
    public static final String LIBRARY_TABLE = "library_table";
    public static final String LIBRARY_ID = "library_id";
    public static final String LIBRARY_IMAGE = "library_image";
    public static final String LIBRARY_NAME = "library_name";

    // Story Table
    public static final String STORY_TABLE = "story_table";
    public static final String STORY_ID = "story_id";
    public static final String STORY_IMAGE = "story_image";
    public static final String STORY_CREATURE = "story_creature";
    public static final String STORY_TITLE = "story_title";
    public static final String STORY_TEXT = "story_text";
    public static final String STORY_NAME = "story_name";
    public static final String STORY_QUIZ_ID = "story_quiz_id";
    public static final String STORY_GOOD = "good_outcome";
    public static final String STORY_BAD = "bad_outcome";

    public static final String FAVORITES_TABLE = "favorites_table";
    public static final String FAVORITES_STORY_NAME = "favorite_story_name";

    public static final String USER_TABLE = "USER_TABLE";
    //public static final String USER_ID = "id";
    public static final String USER_NAME = "username";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_AVATAR_ICON = "avatarIcon";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + QUIZ_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_IMAGE + " INTEGER, " +
                COLUMN_STORY_NAME + " TEXT, " +
                COLUMN_QUESTIONS + " TEXT, " +
                COLUMN_OPTIONS + " TEXT, " +
                COLUMN_ANSWERS + " TEXT)";
        db.execSQL(createTableStatement);

        String createLibraryTable = "CREATE TABLE " + LIBRARY_TABLE + " (" +
                LIBRARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LIBRARY_IMAGE + " INTEGER, " +
                LIBRARY_NAME + " TEXT)";
        db.execSQL(createLibraryTable);

        String createStoryTable = "CREATE TABLE " + STORY_TABLE + " (" +
                STORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STORY_IMAGE + " INTEGER, " +
                STORY_CREATURE + " TEXT, " +
                STORY_TITLE + " TEXT, " +
                STORY_TEXT + " TEXT, " +
                STORY_NAME + " TEXT, " +
                STORY_QUIZ_ID + " INTEGER, " +
                STORY_GOOD + " TEXT, " +
                STORY_BAD + " TEXT)";

        db.execSQL(createStoryTable);

        String createFavoritesTable = "CREATE TABLE " + FAVORITES_TABLE + " (" +
                FAVORITES_STORY_NAME + " TEXT)";
        db.execSQL(createFavoritesTable);

        String createUserInfoTableStatement = "CREATE TABLE " + USER_TABLE + " ("
                + USER_NAME + " TEXT, " + USER_EMAIL + " TEXT, "
                + USER_PASSWORD + " TEXT, " + USER_AVATAR_ICON + " INTEGER)";
        db.execSQL(createUserInfoTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUIZ_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LIBRARY_TABLE);
        onCreate(db);
    }


    public boolean updateUserData(String email, String category, String newData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Choose the correct column to update
        switch (category.toLowerCase()) {
            case "username":
                cv.put(USER_NAME, newData);
                break;
            case "email":
                cv.put(USER_EMAIL, newData);
                break;
            case "password":
                cv.put(USER_PASSWORD, newData);
                break;
            default:
                db.close();
                return false; // Invalid category
        }

        int rowsAffected = db.update(USER_TABLE, cv, USER_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsAffected > 0; // true if update was successful
    }


    public String getUserData(String category, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String column = null;

        // Determine which column to get
        switch (category.toLowerCase()) {
            case "username":
                column = USER_NAME;
                break;
            case "email":
                column = USER_EMAIL;
                break;
            case "psw":
            case "password":
                column = USER_PASSWORD;
                break;
            default:
                db.close();
                return null; // Invalid category
        }

        Cursor cursor = db.query(USER_TABLE, new String[]{column}, USER_EMAIL + " = ?", new String[]{email}, null, null, null);

        String result = null;
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndexOrThrow(column));
        }

        cursor.close();
        db.close();
        return result; // null if not found
    }


    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.moveToFirst(); // true if at least one match found
        cursor.close();
        db.close();
        return exists;
    }

    public boolean addUser(UserInfo userInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        // No summary row exists, so insert a new one
        ContentValues cv = new ContentValues();
        cv.put(USER_EMAIL, userInfo.getEmail());
        cv.put(USER_NAME, userInfo.getUsername());
        cv.put(USER_PASSWORD, userInfo.getPassword());
        cv.put(USER_AVATAR_ICON, userInfo.getAvatarIcon());

        long insert = db.insert(USER_TABLE, null, cv);
        return insert != -1;
    }


    public UserInfo getUser(String userEmail, String userPassword) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if email exists
        Cursor emailCursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ?", new String[]{userEmail});
        if (!emailCursor.moveToFirst()) {
            emailCursor.close();
            db.close();
            return null; // Email not found
        }

        // Email found, now check for correct password
        Cursor userCursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ? AND " + USER_PASSWORD + " = ?", new String[]{userEmail, userPassword});
        if (userCursor.moveToFirst()) {
            String email = userCursor.getString(userCursor.getColumnIndexOrThrow(USER_EMAIL));
            String username = userCursor.getString(userCursor.getColumnIndexOrThrow(USER_NAME));
            String password = userCursor.getString(userCursor.getColumnIndexOrThrow(USER_PASSWORD));
            int avatarIcon = userCursor.getInt(userCursor.getColumnIndexOrThrow(USER_AVATAR_ICON));

            UserInfo userInfo = new UserInfo(email, username, password, avatarIcon);
            userCursor.close();
            emailCursor.close();
            db.close();
            return userInfo;
        }

        // Email exists but password is wrong
        userCursor.close();
        emailCursor.close();
        db.close();
        return new UserInfo(userEmail, null, null, -1); // special case to indicate email exists but password is wrong
    }

    public UserInfo getUserWithEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if email exists
        Cursor emailCursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ?", new String[]{userEmail});
        if (!emailCursor.moveToFirst()) {
            emailCursor.close();
            db.close();
            return null; // Email not found
        }

        // Email found, now check for correct password
        //Cursor userCursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ? AND " + USER_PASSWORD + " = ?", new String[]{userEmail, userPassword});
        if (emailCursor.moveToFirst()) {
            String email = emailCursor.getString(emailCursor.getColumnIndexOrThrow(USER_EMAIL));
            String username = emailCursor.getString(emailCursor.getColumnIndexOrThrow(USER_NAME));
            String password = emailCursor.getString(emailCursor.getColumnIndexOrThrow(USER_PASSWORD));
            int avatarIcon = emailCursor.getInt(emailCursor.getColumnIndexOrThrow(USER_AVATAR_ICON));

            UserInfo userInfo = new UserInfo(email, username, password, avatarIcon);
            emailCursor.close();
            emailCursor.close();
            db.close();
            return userInfo;
        }

        // Email exists but password is wrong
        emailCursor.close();
        emailCursor.close();
        db.close();
        return new UserInfo(userEmail, null, null, -1); // special case to indicate email exists but password is wrong
    }


    public boolean addFavorites(String storyName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FAVORITES_STORY_NAME, storyName);

        long insert =  db.insert(FAVORITES_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getAllFavoritesName(){
        ArrayList<String> names = new ArrayList<>();

        String queryString = "SELECT * FROM " + FAVORITES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                String storyName = cursor.getString(0);
                names.add(storyName);

            } while (cursor.moveToNext());
        } else {
            //Do nothing
        }

        cursor.close();;
        db.close();

        return names;
    }

    public boolean addQuiz(QuizClass quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, quiz.getId());
        cv.put(COLUMN_IMAGE, quiz.getQuizImage());
        cv.put(COLUMN_STORY_NAME, quiz.getStoryName());

        // Convert questions, options, and answers arrays to JSON strings
        JSONArray questionsJson = new JSONArray();
        JSONArray optionsJson = new JSONArray();
        JSONArray answersJson = new JSONArray();

        try {
            for (String question : quiz.getAllQuestion()) {
                questionsJson.put(question);
            }
            for (String[] optionSet : quiz.getAllOptions()) {
                JSONArray optionArray = new JSONArray();
                for (String option : optionSet) {
                    optionArray.put(option);
                }
                optionsJson.put(optionArray);
            }
            for (String answer : quiz.getAllAnswers()) {
                answersJson.put(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        cv.put(COLUMN_QUESTIONS, questionsJson.toString());
        cv.put(COLUMN_OPTIONS, optionsJson.toString());
        cv.put(COLUMN_ANSWERS, answersJson.toString());

        long insert = db.insert(QUIZ_TABLE, null, cv);
        db.close();
        return insert != -1;
    }


    public ArrayList<QuizClass> getAllQuizzes() {
        ArrayList<QuizClass> quizList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + QUIZ_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);

        //can you create also the method to get the specific QuizClass using the id
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int imageResId = cursor.getInt(1);
                String storyName = cursor.getString(2);
                String questionsJson = cursor.getString(3);
                String optionsJson = cursor.getString(4);
                String answersJson = cursor.getString(5);

                try {
                    JSONArray questionsArray = new JSONArray(questionsJson);
                    JSONArray optionsArray = new JSONArray(optionsJson);
                    JSONArray answersArray = new JSONArray(answersJson);

                    String[] questions = new String[questionsArray.length()];
                    String[][] options = new String[optionsArray.length()][4];
                    String[] answers = new String[answersArray.length()];

                    for (int i = 0; i < questionsArray.length(); i++) {
                        questions[i] = questionsArray.getString(i);
                    }

                    for (int i = 0; i < optionsArray.length(); i++) {
                        JSONArray optionSet = optionsArray.getJSONArray(i);
                        for (int j = 0; j < optionSet.length(); j++) {
                            options[i][j] = optionSet.getString(j);
                        }
                    }

                    for (int i = 0; i < answersArray.length(); i++) {
                        answers[i] = answersArray.getString(i);
                    }

                    quizList.add(new QuizClass(id, imageResId, storyName, questions, options, answers));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quizList;
    }

    // New method to retrieve quiz by ID
    public QuizClass getQuizById(int quizId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QUIZ_TABLE + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(quizId)});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            String storyName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORY_NAME));
            String questionsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTIONS));
            String optionsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTIONS));
            String answersJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWERS));

            try {
                JSONArray questionsArray = new JSONArray(questionsJson);
                JSONArray optionsArray = new JSONArray(optionsJson);
                JSONArray answersArray = new JSONArray(answersJson);

                String[] questions = new String[questionsArray.length()];
                String[][] options = new String[optionsArray.length()][4];
                String[] answers = new String[answersArray.length()];

                for (int i = 0; i < questionsArray.length(); i++) {
                    questions[i] = questionsArray.getString(i);
                }

                for (int i = 0; i < optionsArray.length(); i++) {
                    JSONArray optionSet = optionsArray.getJSONArray(i);
                    for (int j = 0; j < optionSet.length(); j++) {
                        options[i][j] = optionSet.getString(j);
                    }
                }

                for (int i = 0; i < answersArray.length(); i++) {
                    answers[i] = answersArray.getString(i);
                }

                cursor.close();
                db.close();
                return new QuizClass(id, imageRes, storyName, questions, options, answers);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public QuizClass getQuizByStoryName(String storyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QUIZ_TABLE + " WHERE " + COLUMN_STORY_NAME + " = ?", new String[]{storyName});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            String questionsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTIONS));
            String optionsJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTIONS));
            String answersJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWERS));

            try {
                JSONArray questionsArray = new JSONArray(questionsJson);
                JSONArray optionsArray = new JSONArray(optionsJson);
                JSONArray answersArray = new JSONArray(answersJson);

                String[] questions = new String[questionsArray.length()];
                String[][] options = new String[optionsArray.length()][4];
                String[] answers = new String[answersArray.length()];

                for (int i = 0; i < questionsArray.length(); i++) {
                    questions[i] = questionsArray.getString(i);
                }

                for (int i = 0; i < optionsArray.length(); i++) {
                    JSONArray optionSet = optionsArray.getJSONArray(i);
                    for (int j = 0; j < optionSet.length(); j++) {
                        options[i][j] = optionSet.getString(j);
                    }
                }

                for (int i = 0; i < answersArray.length(); i++) {
                    answers[i] = answersArray.getString(i);
                }

                cursor.close();
                db.close();
                return new QuizClass(id, imageRes, storyName, questions, options, answers);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }


    public boolean addLibrary(LibraryClass library) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LIBRARY_IMAGE, library.getLibraryImage());
        cv.put(LIBRARY_NAME, library.getLibraryCreatureName());
        long insert = db.insert(LIBRARY_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public ArrayList<LibraryClass> getAllLibraries() {
        ArrayList<LibraryClass> libraries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LIBRARY_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int image = cursor.getInt(1);
                String creatureName = cursor.getString(2);
                libraries.add(new LibraryClass(id, creatureName, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return libraries;
    }

    public boolean addStory(StoryClass story) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STORY_IMAGE, story.getStoryImage());
        cv.put(STORY_CREATURE, story.getCreatureName());
        cv.put(STORY_TITLE, story.getStoryTitle());
        cv.put(STORY_TEXT, story.getStoryText());
        cv.put(STORY_NAME, story.getStoryTitle());
        cv.put(STORY_QUIZ_ID, story.getQuizClass().getId());
        cv.put(STORY_GOOD, story.getGoodOutcome());
        cv.put(STORY_BAD, story.getBadOutcome());
        long insert = db.insert(STORY_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public ArrayList<StoryClass> getStoriesByLibrary(String creatureName) {
        ArrayList<StoryClass> stories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + STORY_TABLE + " WHERE " + STORY_CREATURE + " = ?", new String[]{creatureName});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(STORY_ID));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(STORY_IMAGE));
                String cursorCreatureName = cursor.getString(cursor.getColumnIndexOrThrow(STORY_CREATURE));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(STORY_TITLE));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(STORY_TEXT));
                int quizId = cursor.getInt(cursor.getColumnIndexOrThrow(STORY_QUIZ_ID));
                String good = cursor.getString(cursor.getColumnIndexOrThrow(STORY_GOOD));
                String bad = cursor.getString(cursor.getColumnIndexOrThrow(STORY_BAD));

                QuizClass quiz = getQuizById(quizId); // Ensure we fetch the quiz by its unique ID
                stories.add(new StoryClass(id, image, cursorCreatureName, title, text, quiz, good, bad));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stories;
    }

    public ArrayList<StoryClass> getAllStory() {
        ArrayList<StoryClass> stories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + STORY_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(STORY_ID));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(STORY_IMAGE));
                String creatureName = cursor.getString(cursor.getColumnIndexOrThrow(STORY_CREATURE));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(STORY_TITLE));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(STORY_TEXT));
                int quizId = cursor.getInt(cursor.getColumnIndexOrThrow(STORY_QUIZ_ID));
                String good = cursor.getString(cursor.getColumnIndexOrThrow(STORY_GOOD));
                String bad = cursor.getString(cursor.getColumnIndexOrThrow(STORY_BAD));

                QuizClass quiz = getQuizById(quizId); // Retrieve quiz using quiz ID
                stories.add(new StoryClass(id, image, creatureName, title, text, quiz, good, bad));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return stories;
    }
}