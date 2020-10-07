package com.doozycod.megamind.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionModel {

    @SerializedName("assignmentId")
    @Expose
    private String assignmentId;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public class Question {

        @SerializedName("questionNum")
        @Expose
        private Integer questionNum;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("answer")
        @Expose
        private Integer answer;
        @SerializedName("studentAnswer")
        @Expose
        private Integer studentAnswer;
        @SerializedName("result")
        @Expose
        private Boolean result;

        public Integer getQuestionNum() {
            return questionNum;
        }

        public void setQuestionNum(Integer questionNum) {
            this.questionNum = questionNum;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public Integer getAnswer() {
            return answer;
        }

        public void setAnswer(Integer answer) {
            this.answer = answer;
        }

        public Integer getStudentAnswer() {
            return studentAnswer;
        }

        public void setStudentAnswer(Integer studentAnswer) {
            this.studentAnswer = studentAnswer;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }
    }
}

