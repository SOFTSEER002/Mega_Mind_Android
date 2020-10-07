package com.doozycod.megamind.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentAssignmentModel {


    @SerializedName("studentId")
    @Expose
    private int studentId;
    @SerializedName("assignmentArray")
    @Expose
    private List<AssignmentArray> assignmentArray = null;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public List<AssignmentArray> getAssignments() {
        return assignmentArray;
    }

    public void setAssignments(List<AssignmentArray> assignments) {
        this.assignmentArray = assignments;
    }
    public static class AssignmentArray {
        public AssignmentArray(String studentAssignmentId, String assignmentType, String assignmentDate, String expectedCompletionDate, String assignmentStatus, int percentage, int digitSize, int digitCount, int speed, int questionsQty, Boolean subtraction, int dividentSize, int divisorSize) {
            this.studentAssignmentId = studentAssignmentId;
            this.assignmentType = assignmentType;
            this.assignmentDate = assignmentDate;
            this.expectedCompletionDate = expectedCompletionDate;
            this.assignmentStatus = assignmentStatus;
            this.percentage = percentage;
            this.digitSize = digitSize;
            this.digitCount = digitCount;
            this.speed = speed;
            this.questionsQty = questionsQty;
            this.subtraction = subtraction;
            this.dividentSize = dividentSize;
            this.divisorSize = divisorSize;
            this.questions = questions;
        }

        @SerializedName("studentAssignmentId")
        @Expose
        private String studentAssignmentId;
        @SerializedName("assignmentType")
        @Expose
        private String assignmentType;
        @SerializedName("assignmentDate")
        @Expose
        private String assignmentDate;
        @SerializedName("expectedCompletionDate")
        @Expose
        private String expectedCompletionDate;
        @SerializedName("assignmentStatus")
        @Expose
        private String assignmentStatus;



        @SerializedName("actualCompletionDate")
        @Expose
        private String actualCompletionDate;
        @SerializedName("percentage")
        @Expose
        private int percentage;
        @SerializedName("digitSize")
        @Expose
        private int digitSize;
        @SerializedName("digitCount")
        @Expose
        private int digitCount;
        @SerializedName("speed")
        @Expose
        private int speed;
        @SerializedName("questionsQty")
        @Expose
        private int questionsQty;
        @SerializedName("subtraction")
        @Expose
        private Boolean subtraction;
        @SerializedName("dividentSize")
        @Expose
        private int dividentSize;
        @SerializedName("divisorSize")
        @Expose
        private int divisorSize;
        @SerializedName("questions")
        @Expose
        private List<Question> questions = null;
        public String getActualCompletionDate() {
            return actualCompletionDate;
        }

        public void setActualCompletionDate(String actualCompletionDate) {
            this.actualCompletionDate = actualCompletionDate;
        }
        public String getStudentAssignmentId() {
            return studentAssignmentId;
        }

        public void setStudentAssignmentId(String assignmentId) {
            this.studentAssignmentId = assignmentId;
        }

        public String getAssignmentType() {
            return assignmentType;
        }

        public void setAssignmentType(String assignmentType) {
            this.assignmentType = assignmentType;
        }

        public String getAssignmentDate() {
            return assignmentDate;
        }

        public void setAssignmentDate(String assignmentDate) {
            this.assignmentDate = assignmentDate;
        }

        public String getExpectedCompletionDate() {
            return expectedCompletionDate;
        }

        public void setExpectedCompletionDate(String expectedCompletionDate) {
            this.expectedCompletionDate = expectedCompletionDate;
        }

        public String getAssignmentStatus() {
            return assignmentStatus;
        }

        public void setAssignmentStatus(String assignmentStatus) {
            this.assignmentStatus = assignmentStatus;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public int getDigitSize() {
            return digitSize;
        }

        public void setDigitSize(int digitSize) {
            this.digitSize = digitSize;
        }

        public int getDigitCount() {
            return digitCount;
        }

        public void setDigitCount(int digitCount) {
            this.digitCount = digitCount;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int spped) {
            this.speed = spped;
        }

        public int getQuestionsQty() {
            return questionsQty;
        }

        public void setQuestionsQty(int questionsQty) {
            this.questionsQty = questionsQty;
        }

        public Boolean getSubtraction() {
            return subtraction;
        }

        public void setSubtraction(Boolean subtraction) {
            this.subtraction = subtraction;
        }

        public int getDividentSize() {
            return dividentSize;
        }

        public void setDividentSize(int dividentSize) {
            this.dividentSize = dividentSize;
        }

        public int getDivisorSize() {
            return divisorSize;
        }

        public void setDivisorSize(int divisorSize) {
            this.divisorSize = divisorSize;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }
        public static class Question {
            public Question(int questionNum, String question, int answer, int studentAnswer, Boolean correctAnswer) {
                this.questionNum = questionNum;
                this.question = question;
                this.answer = answer;
                this.studentAnswer = studentAnswer;
                this.correctAnswer = correctAnswer;
            }

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

    }
}
