package com.example.fitostory;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetUpTwoChoices {
    DataBaseHelper dataBaseHelper;
    StoryClass storyClass;
    Context context;
    private TextView creatureNameTextView;
    private TextView storyTitleTextView;
    private TextView storyTextTextView;
    private String storyTitle;
    private SoundPool soundPool;
    private int goodSound, badSound, neutralSound;
    private boolean soundsLoaded = false;

    LinearLayout scrollLayout;

    String option1;
    String option2;

    public SetUpTwoChoices(DataBaseHelper dataBaseHelper, TextView creatureNameTextView,
                           TextView storyTitleTextView, TextView storyTextTextView,
                           String storyTitle, LinearLayout scrollLayout, Context context){
        this.dataBaseHelper = dataBaseHelper;
        this.creatureNameTextView = creatureNameTextView;
        this.storyTitleTextView = storyTitleTextView;
        this.storyTextTextView = storyTextTextView;
        this.storyTitle = storyTitle;
        this.scrollLayout = scrollLayout;
        this.context = context;
        initSounds();
    }

    public SetUpTwoChoices(DataBaseHelper dataBaseHelper){
        this.dataBaseHelper = dataBaseHelper;
    }

    /**
     * Initialize SoundPool and load sound effects
     */
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
        goodSound = soundPool.load(context, R.raw.clap, 1);
        badSound = soundPool.load(context, R.raw.bad, 1);
        neutralSound = soundPool.load(context, R.raw.party, 1);

        // Set listener to know when sounds are loaded
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            soundsLoaded = true;
        });
    }

    public void setUpStory(){
        Stories stories = new Stories(dataBaseHelper);
        stories.setUp();
    }

    public void setUpStoryDetail(){
        ArrayList<StoryClass> storyClasses = dataBaseHelper.getAllStory();
        for (StoryClass sC: storyClasses){
            if (sC.getStoryTitle().equals(storyTitle)){
                storyClass = sC;
            }
        }
        creatureNameTextView.setText(storyClass.getCreatureName());
        storyTitleTextView.setText(storyClass.getStoryTitle());

        String fullStory = storyClass.getStoryText();

        // Regex pattern to find choices like [[CHOICE:Option1|Option2]]
        Pattern pattern = Pattern.compile("\\[\\[CHOICE:(.*?)\\|(.*?)\\]\\]");
        Matcher matcher = pattern.matcher(fullStory);

        int lastIndex = 0;
        while (matcher.find()) {
            // Add the text before the choice
            String textBeforeChoice = fullStory.substring(lastIndex, matcher.start()).trim();
            if (!textBeforeChoice.isEmpty()) {
                TextView textView = new TextView(context);
                textView.setText(textBeforeChoice);
                textView.setTextSize(16);
                textView.setTextColor(Color.WHITE);
                textView.setPadding(0, 16, 0, 16);
                scrollLayout.addView(textView);
            }

            // Create a container for the direction button (for styling purposes)
            CardView directionCardView = new CardView(context);
            directionCardView.setRadius(dpToPx(context, 20));
            directionCardView.setCardElevation(dpToPx(context, 4));

            // Add margin to the CardView
            int marginInDp = 16;
            float scale = context.getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * scale + 0.5f);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(marginInPx, marginInPx, marginInPx, marginInPx/2);
            directionCardView.setLayoutParams(cardParams);

            // Create the direction button with new styling
            TextView directionText = new TextView(context);
            directionText.setText("What is your next direction?");
            directionText.setTextSize(14);
            directionText.setTextColor(Color.WHITE);
            directionText.setGravity(Gravity.CENTER);
            directionText.setPadding(0, dpToPx(context, 12), 0, dpToPx(context, 12));

            // Create gradient background
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.BR_TL,
                    new int[] {0xFF9C27B0, 0xFF673AB7}
            );
            gradientDrawable.setCornerRadius(dpToPx(context, 20));

            directionCardView.setBackground(gradientDrawable);

            // Add the text to the card
            directionCardView.addView(directionText);

            // Add subtle animation to draw attention
            animatePulse(directionCardView);

            // Add the direction card to the layout
            scrollLayout.addView(directionCardView);

            // Add the two choice buttons
            option1 = matcher.group(1).trim();
            option2 = matcher.group(2).trim();

            // Create container for option buttons
            LinearLayout optionsContainer = new LinearLayout(context);
            optionsContainer.setOrientation(LinearLayout.HORIZONTAL);
            optionsContainer.setWeightSum(2);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            containerParams.setMargins(marginInPx, marginInPx/2, marginInPx, marginInPx);
            optionsContainer.setLayoutParams(containerParams);

            // Create option button 1 with enhanced styling
            CardView option1CardView = createOptionButton(option1, 0xFFA52A2A, 0);
            LinearLayout.LayoutParams option1Params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            option1Params.setMargins(0, 0, marginInPx/2, 0);
            option1CardView.setLayoutParams(option1Params);

            // Create option button 2 with enhanced styling
            CardView option2CardView = createOptionButton(option2, 0xFF1B9A99, 1);
            LinearLayout.LayoutParams option2Params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            option2Params.setMargins(marginInPx/2, 0, 0, 0);
            option2CardView.setLayoutParams(option2Params);

            // Add buttons to container
            optionsContainer.addView(option1CardView);
            optionsContainer.addView(option2CardView);

            // Add the container to the main layout
            scrollLayout.addView(optionsContainer);

            // Set up listeners for both option buttons
            option1CardView.setOnClickListener(v -> {
                animateButtonPress(option1CardView);
                new Handler().postDelayed(() -> showOutcome(option1, option1CardView, option2CardView), 300);
            });

            option2CardView.setOnClickListener(v -> {
                animateButtonPress(option2CardView);
                new Handler().postDelayed(() -> showOutcome(option2, option2CardView, option1CardView), 300);
            });

            lastIndex = matcher.end();
        }

        // Add remaining text after the last choice
        if (lastIndex < fullStory.length()) {
            String remainingText = fullStory.substring(lastIndex).trim();
            if (!remainingText.isEmpty()) {
                TextView textView = new TextView(context);
                textView.setText(remainingText);
                textView.setTextSize(16);
                textView.setPadding(0, 16, 0, 16);
                scrollLayout.addView(textView);
            }
        }
    }

    /**
     * Creates a styled option button
     */
    private CardView createOptionButton(String text, int baseColor, int index) {
        CardView cardView = new CardView(context);
        cardView.setRadius(dpToPx(context, 15));
        cardView.setCardElevation(dpToPx(context, 4));

        // Create gradient background with base color
        int darkerColor = manipulateColor(baseColor, 0.8f);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {baseColor, darkerColor}
        );
        gradientDrawable.setCornerRadius(dpToPx(context, 15));

        // Create TextView for button text
        TextView buttonText = new TextView(context);
        buttonText.setText(text);
        buttonText.setTextSize(14);
        buttonText.setTextColor(Color.WHITE);
        buttonText.setGravity(Gravity.CENTER);
        buttonText.setPadding(dpToPx(context, 8), dpToPx(context, 12), dpToPx(context, 8), dpToPx(context, 12));

        // Set background
        cardView.setBackground(gradientDrawable);

        // Add text to card
        cardView.addView(buttonText);

        // Add entrance animation with delay based on index (staggered entrance)
        animateEntrance(cardView, 200 + (index * 150));

        // Store the option text as a tag for later reference
        cardView.setTag(text);

        return cardView;
    }

    /**
     * Animate button entrance with a subtle bounce effect
     */
    private void animateEntrance(View view, long delay) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setStartDelay(delay)
                .setDuration(500)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();
    }

    /**
     * Animate button press effect
     */
    private void animateButtonPress(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.start();
    }

    /**
     * Animate a subtle pulse to draw attention
     */
    private void animatePulse(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.03f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.03f, 1.0f);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setDuration(1500);
        scaleY.setDuration(1500);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }

    /**
     * Manipulate color brightness
     * @param color initial color
     * @param factor <1 = darker, >1 = lighter
     * @return manipulated color
     */
    private int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    private void showOutcome(String choice, CardView clickedCard, CardView otherCard) {
        String outcomeText;

        // Disable both buttons by changing their appearance and clickability
        clickedCard.setClickable(false);
        otherCard.setClickable(false);

        // Visual feedback for disabled state
        GradientDrawable disabledGradient = new GradientDrawable(
                GradientDrawable.Orientation.BR_TL,
                new int[] {0xFFAAAAAA, 0xFF888888}
        );
        disabledGradient.setCornerRadius(dpToPx(context, 15));

        clickedCard.setBackground(disabledGradient);
        otherCard.setBackground(disabledGradient);

        // Find TextViews inside CardViews and change text color
        if (clickedCard.getChildAt(0) instanceof TextView) {
            ((TextView) clickedCard.getChildAt(0)).setTextColor(0xFFDDDDDD);
        }
        if (otherCard.getChildAt(0) instanceof TextView) {
            ((TextView) otherCard.getChildAt(0)).setTextColor(0xFFDDDDDD);
        }

        //if (storyClass.getStoryTitle().equals("The Monkey and the Turtle")) {
            if (choice.equals(option1)) {
                showBlobShapedAlert("Bad Outcome", "", "😵", 0xFFE57373, badSound); // Red-ish color for bad outcome
                outcomeText = storyClass.getBadOutcome();
                // Add vibration for bad outcome for tactile feedback
                vibrate(500);
            } else if (choice.equals(option2)) {
                showBlobShapedAlert("Good Outcome","", "😊", 0xFF81C784, goodSound); // Green-ish color for good outcome
                outcomeText = storyClass.getGoodOutcome();
            } else {
                outcomeText = "Your choice had no clear effect...";
                showBlobShapedAlert("Uncertain Outcome", outcomeText, "🤔", 0xFFFFB74D, neutralSound); // Orange-ish for uncertain
            }

            // Show outcome below the buttons with animation
            TextView outcomeTextView = new TextView(context);
            outcomeTextView.setText(outcomeText);
            outcomeTextView.setTextSize(16);
            outcomeTextView.setTextColor(Color.BLACK);
            outcomeTextView.setPadding(dpToPx(context, 16), dpToPx(context, 16), dpToPx(context, 16), dpToPx(context, 16));
            outcomeTextView.setAlpha(0f);

            // Add styled background to outcome text
            GradientDrawable outcomeBg = new GradientDrawable();
            outcomeBg.setCornerRadius(dpToPx(context, 10));
            outcomeBg.setColor(0xFFF5F5F5);
            outcomeBg.setStroke(dpToPx(context, 1), 0xFFE0E0E0);
            outcomeTextView.setBackground(outcomeBg);

            // Add margin to outcome text
            LinearLayout.LayoutParams outcomeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            outcomeParams.setMargins(dpToPx(context, 16), dpToPx(context, 8), dpToPx(context, 16), dpToPx(context, 16));
            outcomeTextView.setLayoutParams(outcomeParams);

            scrollLayout.addView(outcomeTextView);

            // Animate outcome text appearing
            outcomeTextView.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        /*} else {
            Toast.makeText(context, "There is wrong in the storyClass title", Toast.LENGTH_SHORT).show();
        }*/
    }

    /**
     * Provides haptic feedback for dramatic effect
     * @param milliseconds duration of vibration
     */
    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26
                vibrator.vibrate(milliseconds);
            }
        }
    }

    /**
     * Shows a blob-shaped custom alert dialog with engaging UI elements and sound effects
     *
     * @param title The title of the alert
     * @param message The message to display
     * @param emoji The emoji to show at the top
     * @param accentColor The accent color for elements in the dialog
     * @param soundId The sound to play when dialog appears
     */
    private void showBlobShapedAlert(String title, String message, String emoji, int accentColor, int soundId) {
        // Create a dialog without a title
        Dialog customDialog = new Dialog(context);
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
                dpToPx(context, 2)  // Stroke width
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

    /**
     * Plays the specified sound with proper error handling
     *
     * @param soundId ID of the sound to play
     */
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

    /**
     * Convert dp to pixels
     */
    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public String getAllDisplayedText() {
        StringBuilder allText = new StringBuilder();

        for (int i = 0; i < scrollLayout.getChildCount(); i++) {
            View child = scrollLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                allText.append(textView.getText().toString().trim()).append("\n");
            } else if (child instanceof CardView && ((CardView) child).getChildAt(0) instanceof TextView) {
                TextView buttonText = (TextView) ((CardView) child).getChildAt(0);
                allText.append("Choice: ").append(buttonText.getText().toString().trim()).append("\n");
            } else if (child instanceof LinearLayout) {
                LinearLayout container = (LinearLayout) child;
                for (int j = 0; j < container.getChildCount(); j++) {
                    View innerChild = container.getChildAt(j);
                    if (innerChild instanceof CardView && ((CardView) innerChild).getChildAt(0) instanceof TextView) {
                        TextView buttonText = (TextView) ((CardView) innerChild).getChildAt(0);
                        allText.append("Option: ").append(buttonText.getText().toString().trim()).append("\n");
                    }
                }
            }
        }

        return allText.toString().trim(); // Return the full story content
    }

    /**
     * Clean up resources when the activity is destroyed
     */
    public void cleanup() {
        if (soundPool != null) {
            soundPool.release();
            soundsLoaded = false;
        }
    }
}