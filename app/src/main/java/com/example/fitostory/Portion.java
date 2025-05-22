package com.example.fitostory;

public class Portion {
    String question;
    String[] options;
    String answer;

    public Portion(String question, String[] options, String answer){
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
}
