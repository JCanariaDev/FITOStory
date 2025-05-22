package com.example.fitostory;

import java.util.ArrayList;
import java.util.List;

public class LibraryClass {
    private int id;
    private int image;
    private String creatureName;
    private List<StoryClass> stories; // List of stories for the creature

    public LibraryClass(int id, String creatureName, int image){
        this.id = id;
        this.image = image;
        this.creatureName = creatureName;
        this.stories = new ArrayList<>();
    }

    public void setLibraryId(int id) {
        this.id = id;
    }
    public void setLibraryImage(int image) {
        this.image = image;
    }
    public void setCreatureName(String creatureName) {
        this.creatureName = creatureName;
    }
    public void setStories(List<StoryClass> stories) {
        this.stories = stories;
    }

    // Add a new story to the category
    public void addStory(StoryClass story) {
        stories.add(story);
    }


    // Getters
    public int getLibraryId() {
        return id;
    }
    public int getLibraryImage() {
        return image;
    }
    public String getLibraryCreatureName() { return creatureName; }

    public List<StoryClass> getStories() { return stories; }
}
