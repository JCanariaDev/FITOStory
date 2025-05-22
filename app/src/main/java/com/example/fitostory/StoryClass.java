package com.example.fitostory;

public class StoryClass {
    private int id;
    private int image;
    private String creatureName;
    private String title;
    private String storyText;
    private QuizClass quizClass;
    private String goodOutcome;
    private String badOutcome;


    public StoryClass(int id, int image, String creatureName, String title, String storyText, QuizClass quizClass,
                      String goodOutcome, String badOutcome){
        this.id = id;
        this.image = image;
        this.creatureName = creatureName; //in which library this StoryClass belong = Category = creature name = creatureName attributes of a specific LibraryClass
        this.title= title;
        this.storyText = storyText;
        this.quizClass = quizClass;
        this.goodOutcome = goodOutcome;
        this.badOutcome = badOutcome;
    }

    public StoryClass(int id, int image, String creatureName, String title, String storyText, QuizClass quizClass){
        this.id = id;
        this.image = image;
        this.creatureName = creatureName; //in which library this StoryClass belong = Category = creature name = creatureName attributes of a specific LibraryClass
        this.title= title;
        this.storyText = storyText;
        this.quizClass = quizClass;
    }

    public StoryClass(int id, int image){
        this.id = id;
        this.image = image;
    }

    public int getStoryId() {
        return id;
    }
    public int getStoryImage() {
        return image;
    }
    public String getCreatureName() {
        return creatureName;
    }
    public String getStoryTitle() {
        return title;
    }
    public String getStoryText() {
        return storyText;
    }
    public QuizClass getQuizClass(){
        return quizClass;
    }
    public void setGoodOutcome(String goodOutcome) {
        this.goodOutcome = goodOutcome;
    }
    public void setBadOutcome(String badOutcome) {
        this.badOutcome = badOutcome;
    }

    public void setStoryId(int id) {
        this.id = id;
    }
    public void setStoryImage(int image) {
        this.image = image;
    }
    public void setCreatureName(String creatureName) {
        this.creatureName = creatureName;
    }
    public void setStoryTitle(String title) {
        this.title = title;
    }
    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }
    public void setStoryQuiz(QuizClass quizClass){
        this.quizClass = quizClass;
    }
    public String getGoodOutcome() {
        return goodOutcome;
    }
    public String getBadOutcome() {
        return badOutcome;
    }


}
