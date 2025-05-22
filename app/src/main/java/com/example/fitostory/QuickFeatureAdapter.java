package com.example.fitostory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class QuickFeatureAdapter extends RecyclerView.Adapter<QuickFeatureAdapter.QuickFeatureViewHolder> {
    public LayoutInflater mInflater;
    private Context context;

    private ArrayList<LibraryClass> mLibraryList; // List to hold Library items
    private ArrayList<StoryClass> mStoryList; // List to hold Story items
    private ArrayList<QuizClass> mQuizList; // List to hold Library items
    private ArrayList<StoryClass> mFavoritesList;
    private int view;

    DataBaseHelper dataBaseHelper;
    ArrayList<String> storyNames;


    public QuickFeatureAdapter(Context context, ArrayList<?> mList, int view) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        if (view == R.layout.library_item){
            this.mLibraryList = (ArrayList<LibraryClass>) mList;
        } else if (view == R.layout.story_item){
            this.mStoryList = (ArrayList<StoryClass>) mList;
        } else if (view == R.layout.quick_features_cardview){
            this.mStoryList = (ArrayList<StoryClass>) mList;
        } else if (view == R.layout.quiz_item){
            this.mQuizList = (ArrayList<QuizClass>) mList;
        } else if (view == R.layout.favorites_item){
            this.mFavoritesList = (ArrayList<StoryClass>) mList;
        }
        this.view = view;
        dataBaseHelper = new DataBaseHelper(context);
        storyNames = dataBaseHelper.getAllFavoritesName();
        /*if(!dataBaseHelper.getAllFavoritesName().isEmpty()){
            storyNames = dataBaseHelper.getAllFavoritesName();
        } else {
            storyNames = new ArrayList<>();
        }*/

    }

    class QuickFeatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        QuickFeatureAdapter mAdapter;
        public ConstraintLayout storyBox;
        public  ImageView imageView;
        public  Button button;

        public TextView creatureName;
        public TextView storyTitle;

        public ImageButton heartBtn;

        public QuickFeatureViewHolder(@NonNull View itemView, QuickFeatureAdapter adapter) {
            super(itemView);
            if (view == R.layout.library_item) {
                storyBox = itemView.findViewById(R.id.storyBox);
                imageView = itemView.findViewById(R.id.imageView);
                button = itemView.findViewById(R.id.button);
                creatureName = itemView.findViewById(R.id.creatureName);

                this.mAdapter = adapter;
                itemView.setOnClickListener(this);
            } else if (view == R.layout.story_item || view == R.layout.quick_features_cardview){
                storyBox = itemView.findViewById(R.id.storyBox);
                imageView = itemView.findViewById(R.id.imageView);
                button = itemView.findViewById(R.id.button);
                storyTitle = itemView.findViewById(R.id.storyTitle);
                heartBtn = itemView.findViewById(R.id.heartBtn);

                this.mAdapter = adapter;
                itemView.setOnClickListener(this);
            } else if (view == R.layout.quiz_item){
                storyBox = itemView.findViewById(R.id.storyBox);
                imageView = itemView.findViewById(R.id.imageView);
                button = itemView.findViewById(R.id.button);
                storyTitle = itemView.findViewById(R.id.storyTitle);

                this.mAdapter = adapter;
                itemView.setOnClickListener(this);
            } else if (view == R.layout.favorites_item){
                storyBox = itemView.findViewById(R.id.storyBox);
                imageView = itemView.findViewById(R.id.imageView);
                button = itemView.findViewById(R.id.button);
                storyTitle = itemView.findViewById(R.id.storyTitle);

                this.mAdapter = adapter;
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(mAdapter.mInflater.getContext(), "Clicked: " + subjectName.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public QuickFeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View mItemView = mInflater.inflate(R.layout.quick_features_cardview, parent, false);
        View mItemView = mInflater.inflate(view, parent, false);
        return new QuickFeatureViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickFeatureViewHolder holder, int position) {
        if (view == R.layout.library_item){
            LibraryClass currentLibrary = mLibraryList.get(position);
            //holder.creatureName.setText(currentLibrary.getLibraryCreatureName());
            Glide.with(context)
                    .load(currentLibrary.getLibraryImage())
                    .into(holder.imageView);
            holder.button.setOnClickListener(v -> {
                String creatureName = currentLibrary.getLibraryCreatureName();
                Intent i = new Intent(context.getApplicationContext(), StoryListActivity.class);
                i.putExtra("creature_name", creatureName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            });

        } else if (view == R.layout.story_item){
            StoryClass currentStory = mStoryList.get(position);
            holder.storyTitle.setText(currentStory.getStoryTitle());
            Glide.with(context)
                    .load(currentStory.getStoryImage())
                    .into(holder.imageView);
            holder.button.setOnClickListener(v -> {
                String storyTitle = currentStory.getStoryTitle();
                Intent i = new Intent(context.getApplicationContext(), StoryDetailActivity.class);
                i.putExtra("story_title", storyTitle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            });

            //Debug---------------------------------------------
            holder.heartBtn.setOnClickListener(view1 -> {
                if (!storyNames.contains(currentStory.getStoryTitle())) {
                    Toast.makeText(context, "Add to favorites", Toast.LENGTH_SHORT).show();
                    dataBaseHelper.addFavorites(currentStory.getStoryTitle());
                    storyNames = dataBaseHelper.getAllFavoritesName(); // update the list
                } else {
                    Toast.makeText(context, "Already in favorites", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (view == R.layout.quick_features_cardview){
            StoryClass currentStory = mStoryList.get(position);
            holder.storyTitle.setText(currentStory.getStoryTitle());
            Glide.with(context)
                    .load(currentStory.getStoryImage())
                    .into(holder.imageView);
            holder.button.setOnClickListener(v -> {
                String storyTitle = currentStory.getStoryTitle();
                Intent i = new Intent(context.getApplicationContext(), StoryDetailActivity.class);
                i.putExtra("story_title", storyTitle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            });
        } else if (view == R.layout.quiz_item){
            QuizClass currentQuiz = mQuizList.get(position);
            holder.storyTitle.setText(currentQuiz.getStoryName() + "\nQuiz");
            holder.button.setOnClickListener(v -> {
                String quizStoryTitle = currentQuiz.getStoryName();
                Intent i = new Intent(context.getApplicationContext(), QuizActivity.class);
                i.putExtra("quiz_storyTitle", quizStoryTitle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            });
            Glide.with(context)
                    .load(currentQuiz.getQuizImage())
                    .into(holder.imageView);  // Load image from resources or URL
            //.apply(RequestOptions.circleCropTransform())  // Apply circular transformation
        } else if (view == R.layout.favorites_item){
            StoryClass currentStory = mFavoritesList.get(position);
            holder.storyTitle.setText(currentStory.getStoryTitle());
            Glide.with(context)
                    .load(currentStory.getStoryImage())
                    .into(holder.imageView);
            holder.button.setOnClickListener(v -> {
                String storyTitle = currentStory.getStoryTitle();
                Intent i = new Intent(context.getApplicationContext(), StoryDetailActivity.class);
                i.putExtra("story_title", storyTitle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            });
        }



    }

    @Override
    public int getItemCount() {
        if (view == R.layout.story_item){
            return mStoryList.size();
        } else if (view == R.layout.quick_features_cardview){
            return mStoryList.size();
        } else if (view == R.layout.quiz_item){
            return mQuizList.size();
        } else if (view == R.layout.favorites_item){
            return mFavoritesList.size();
        }else { //Means library
            return mLibraryList.size();
        }
    }
}
