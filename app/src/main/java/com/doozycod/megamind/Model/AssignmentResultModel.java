package com.doozycod.megamind.Model;

public class AssignmentResultModel {

    private String numberArray;
    private String userAnswer;
    private String correctAnswer;
    private String QuestionNo;
    private String checkAnswer;
    private String noOfQuestions;
    private String calculationType;
    private String assignmentId;
    private String flickeringSpeed;


    private String dateOfAssignment;



    public AssignmentResultModel(String numberArray, String userAnswer, String correctAnswer, String questionNo,
                                 String checkAnswer, String noOfQuestions, String calculationType, String assignmentId, String flickeringSpeed, String dateOfAssignment) {
        this.numberArray = numberArray;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        QuestionNo = questionNo;
        this.checkAnswer = checkAnswer;
        this.noOfQuestions = noOfQuestions;
        this.calculationType = calculationType;
        this.assignmentId = assignmentId;
        this.flickeringSpeed = flickeringSpeed;
        this.dateOfAssignment = dateOfAssignment;
    }


    public String getDateOfAssignment() {
        return dateOfAssignment;
    }

    public void setDateOfAssignment(String dateOfAssignment) {
        this.dateOfAssignment = dateOfAssignment;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getFlickeringSpeed() {
        return flickeringSpeed;
    }

    public void setFlickeringSpeed(String flickeringSpeed) {
        this.flickeringSpeed = flickeringSpeed;
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
