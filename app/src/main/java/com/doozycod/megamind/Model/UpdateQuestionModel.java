package com.doozycod.megamind.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateQuestionModel {

    /*public UpdateQuestionModel(String assignmentStatus, Integer percentage, String actualCompletionDate, List<Question> questions) {
        this.assignmentStatus = assignmentStatus;
        this.percentage = percentage;
        this.actualCompletionDate = actualCompletionDate;
        this.questions = questions;
    }*/

    @SerializedName("assignmentStatus")
    @Expose
    private String assignmentStatus;
    @SerializedName("percentage")
    @Expose
    private int percentage;
    @SerializedName("actualCompletionDate")
    @Expose
    private String actualCompletionDate;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    public String getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public String getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(String actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public static class Question {
        public Question(int questionNum, String question, long answer, long studentAnswer, boolean result) {
            this.questionNum = questionNum;
            this.question = question;
            this.answer = answer;
            this.studentAnswer = studentAnswer;
            this.result = result;
        }

        @SerializedName("questionNum")
        @Expose
        private int questionNum;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("answer")
        @Expose
        private long answer;
        @SerializedName("studentAnswer")
        @Expose
        private long studentAnswer;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        @SerializedName("result")
        @Expose
        private boolean result;

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

        public long getAnswer() {
            return answer;
        }

        public void setAnswer(Integer answer) {
            this.answer = answer;
        }

        public long getStudentAnswer() {
            return studentAnswer;
        }

        public void setStudentAnswer(Integer studentAnswer) {
            this.studentAnswer = studentAnswer;
        }
    }
}