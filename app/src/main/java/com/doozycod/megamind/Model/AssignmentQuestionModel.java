package com.doozycod.megamind.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentQuestionModel {
    @SerializedName("questionNum")
    @Expose
    private int questionNum;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private int answer;
    @SerializedName("studentAnswer")
    @Expose
    private int studentAnswer;
    @SerializedName("correctAnswer")
    @Expose
    private Boolean correctAnswer;

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(int studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public Boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
