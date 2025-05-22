package com.example.fitostory;

public class QuizClass {
    private int id;
    private int image;

    private String[] questions;
    private String[][] options;
    private String[] answers;

    private String storyName; //new attributes

    public QuizClass(int id, int image, String storyName, String[] questions, String[][] options, String[] answers) {
        this.id = id;
        this.image = image;
        this.storyName = storyName;
        this.questions = questions;
        this.options = options;
        this.answers = answers;
    }

    /*public QuizClass(int id, int image){
        this.id = id;
        this.image = image;
    }*/

    public int getId() {
        return id;
    }
    public int getQuizImage() {
        return image;
    }
    public String getQuestion(int index) {
        return questions[index];
    }
    public String[] getAllQuestion(){
        return questions;
    }
    public String[] getOptions(int index) {
        return options[index];
    }
    public String[][] getAllOptions(){
        return options;
    }
    public String getAnswer(int index) {
        return answers[index];
    }
    public String[] getAllAnswers() {
        return answers;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public void setQuestions(String[] questions) {
        this.questions = questions;
    }
    public void setOptions(String[][] options) {
        this.options = options;
    }
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String getStoryName() { //new method
        return storyName;
    }

    public void setStoryName(String storyName) { //new method
        this.storyName = storyName;
    }
}
