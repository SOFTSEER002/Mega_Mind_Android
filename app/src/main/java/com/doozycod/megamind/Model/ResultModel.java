package com.doozycod.megamind.Model;

public class ResultModel {
    private String numberArray;
    private String userAnswer;
    private String correctAnswer;
    private String QuestionNo;
    private String checkAnswer;


    private String noOfQuestions;


    private String calculationType;

    public ResultModel(String numberArray, String userAnswer, String correctAnswer, String QuestionNo, String checkAnswer, String noOfQuestions, String calculationType) {
        this.numberArray = numberArray;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.QuestionNo = QuestionNo;
        this.checkAnswer = checkAnswer;
        this.noOfQuestions = noOfQuestions;
        this.calculationType = calculationType;
    }

    public String getQuestionNo() {
        return QuestionNo;
    }

    public void setQuestionNo(String questionNo) {
        QuestionNo = questionNo;
    }

    public String getNumberArray() {
        return numberArray;
    }

    public void setNumberArray(String numberArray) {
        this.numberArray = numberArray;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCheckAnswer(String checkAnswer) {
        this.checkAnswer = checkAnswer;
    }

    public String getNoOfQuestions() {
        return noOfQuestions;
    }

    public String getCheckAnswer() {
        return checkAnswer;
    }

    public void setNoOfQuestions(String noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }
}
