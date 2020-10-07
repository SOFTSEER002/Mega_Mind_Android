package com.doozycod.megamind.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.doozycod.megamind.Fragments.AssignmentsFragment;
import com.doozycod.megamind.Model.AssignmentResultModel;
import com.doozycod.megamind.Model.CalculationTypeModel;
import com.doozycod.megamind.Model.QuestionModel;
import com.doozycod.megamind.Model.ResultModel;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.Model.UpdateQuestionModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "MegaMind.db";
    public final static String ASSIGNMENT_TABLE = "ASSIGNMENT_TABLE";
    public final static String RESULT_TABLE = "RESULT_TABLE";
    public final static String CALCULATION = "CALCULATION";
    public final static String CALCULATION_TYPE = "CALCULATION_TYPE";
    public final static String USER_ANSWER = "USER_ANSWER";
    public final static String QUESTIONS = "QUESTIONS";
    public final static String FLICKERING_SPEED = "FLICKERING_SPEED";
    public final static String QUESTION_TO_ATTEMPT = "QUESTION_TO_ATTEMPT";
    public final static String CORRECT_ANSWER = "CORRECT_ANSWER";
    public final static String CHECK_ANSWER = "CHECK_ANSWER";
    public final static String CALCULATION_ITEMS_TABLE = "CALCULATION_ITEMS_TABLE";
    public final static String ASSIGNMENT_STATUS = "ASSIGNMENT_STATUS";
    public final static String STUDENT_ID = "STUDENT_ID";
    public final static String ASSIGNMENT_ID = "ASSIGNMENT_ID";
    public final static String DATE_OF_ASSIGNMENT_GIVEN = "DATE_OF_ASSIGNMENT_GIVEN";
    public final static String COMPLETION_DATE = "expectedCompletionDate";
    public final static String PERCENTAGE = "percentage";
    public final static String QUESTION = "QUESTION";
    public final static String QUESTION_NUM = "QUESTION_NUM";
    public final static String QUESTION_FOR_STUDENT = "QUESTION_FOR_STUDENT";
    public final static String DIGIT_SIZE = "DIGIT_SIZE";
    public final static String DIGIT_COUNT = "digitCount";
    public final static String SUBTRACTION = "subtraction";
    public final static String DIVIDEND_SIZE = "DIVIDEND_SIZE";
    public final static String DIVISOR_SIZE = "DIVISOR_SIZE";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //        Calculation Type
        sqLiteDatabase.execSQL("CREATE TABLE " + CALCULATION_ITEMS_TABLE
                + " (ID integer PRIMARY KEY," + CALCULATION_TYPE + " text )");

        //        Offline user questions answers

        sqLiteDatabase.execSQL("CREATE TABLE " + RESULT_TABLE
                + " (ID integer PRIMARY KEY," + QUESTIONS + " text," + QUESTION_TO_ATTEMPT + " text," + CALCULATION + " text," + CALCULATION_TYPE + " text,"
                + USER_ANSWER + " text," + CHECK_ANSWER + " text," + CORRECT_ANSWER + " text )");
//        Student Assignment Table
        sqLiteDatabase.execSQL("CREATE TABLE " + ASSIGNMENT_TABLE
                + " (ID integer PRIMARY KEY," + STUDENT_ID + " text," + ASSIGNMENT_ID + " text," + QUESTION_TO_ATTEMPT + " integer," + FLICKERING_SPEED + " integer,"
                + CALCULATION_TYPE + " text," + COMPLETION_DATE + " text," + DIGIT_SIZE + " integer," + DIGIT_COUNT + " integer," + DIVIDEND_SIZE + " integer," + DIVISOR_SIZE + " integer,"
                + PERCENTAGE + " integer," + SUBTRACTION + " text," + ASSIGNMENT_STATUS + " text,"
                + DATE_OF_ASSIGNMENT_GIVEN + " text )");

        sqLiteDatabase.execSQL("CREATE TABLE " + QUESTION_FOR_STUDENT
                + " (ID integer PRIMARY KEY," + STUDENT_ID + " integer," + ASSIGNMENT_ID + " text," + CALCULATION_TYPE + " text," + QUESTION_NUM + " integer,"
                + CALCULATION + " text," + QUESTION_TO_ATTEMPT + " text," + USER_ANSWER + " text," + CHECK_ANSWER + " text," + CORRECT_ANSWER + " text)");
    }

    public void insertCalculationType(String calculation_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CALCULATION_TYPE, calculation_type);
        db.insert(CALCULATION_ITEMS_TABLE, null, cv);

    }

    public boolean columnExists(String studentId, String assignmentId) {
        String sql = "SELECT EXISTS (SELECT * FROM ASSIGNMENT_TABLE WHERE STUDENT_ID='" + studentId + "'" + " AND " + "ASSIGNMENT_ID= '" + assignmentId + "'  LIMIT 1)";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void insertAssignment(int studentId, String assignmentId, int noOfQuestions, int flickeringSpeed, String calculation_type,
                                 int digitSize, int digitCount, int percentage, String subtraction, String completionDate,
                                 String assignmentStatus, String dateOfAssignment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STUDENT_ID, studentId);
        cv.put(ASSIGNMENT_ID, assignmentId);
        cv.put(QUESTION_TO_ATTEMPT, noOfQuestions);
        cv.put(FLICKERING_SPEED, flickeringSpeed);
        cv.put(CALCULATION_TYPE, calculation_type);
        cv.put(COMPLETION_DATE, completionDate);
        cv.put(DIGIT_SIZE, digitSize);
        cv.put(DIGIT_COUNT, digitCount);
        cv.put(PERCENTAGE, percentage);
        cv.put(SUBTRACTION, subtraction);
        cv.put(ASSIGNMENT_STATUS, assignmentStatus);
        cv.put(DATE_OF_ASSIGNMENT_GIVEN, dateOfAssignment);
        db.insert(ASSIGNMENT_TABLE, null, cv);
    }

    public ArrayList<StudentAssignmentModel.AssignmentArray> getAssignment(int student) {
        ArrayList<StudentAssignmentModel.AssignmentArray> list = new ArrayList<StudentAssignmentModel.AssignmentArray>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + ASSIGNMENT_TABLE, null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String studentId = res.getString(1);
            String assignmentId = res.getString(2);
            int noOfQuestions = res.getInt(3);
            int flickeringSpeed = res.getInt(4);
            String calculationType = res.getString(5);
            String completionDate = res.getString(6);
            int digitSize = res.getInt(7);
            int digitCount = res.getInt(8);
            int dividentSize = res.getInt(9);
            int divisorSize = res.getInt(10);
            int percentage = res.getInt(11);
            String subtraction = res.getString(12);
            String assignmentStatus = res.getString(13);
            String dateOfAssignment = res.getString(14);

            StudentAssignmentModel.AssignmentArray resultModel = new StudentAssignmentModel.AssignmentArray(assignmentId, calculationType, dateOfAssignment, completionDate, assignmentStatus, percentage,
                    digitSize, digitCount, flickeringSpeed, noOfQuestions, Boolean.valueOf(subtraction), dividentSize, divisorSize);
            list.add(resultModel);
        }
        return list;
    }

    public void insertAssignmentQuestionAnswer(String studentId, String assignmentId, String questionNo, String noOfQuestions, String calculation,
                                               String calculation_type, String user_answer, String correct_answer, String checkAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STUDENT_ID, studentId);
        cv.put(ASSIGNMENT_ID, assignmentId);
        cv.put(QUESTION_NUM, questionNo);
        cv.put(QUESTION_TO_ATTEMPT, noOfQuestions);
        cv.put(CALCULATION, calculation);
        cv.put(CALCULATION_TYPE, calculation_type);
        cv.put(USER_ANSWER, user_answer);
        cv.put(CORRECT_ANSWER, correct_answer);
        cv.put(CHECK_ANSWER, checkAnswer);
        db.insert(QUESTION_FOR_STUDENT, null, cv);

    }

    public void insertQuestionAnswer(String questions, String noOfQuestions, String calculation, String calculation_type, String user_answer, String correct_answer, String checkAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(QUESTIONS, questions);
        cv.put(QUESTION_TO_ATTEMPT, noOfQuestions);
        cv.put(CALCULATION, calculation);
        cv.put(CALCULATION_TYPE, calculation_type);
        cv.put(USER_ANSWER, user_answer);
        cv.put(CORRECT_ANSWER, correct_answer);
        cv.put(CHECK_ANSWER, checkAnswer);
        db.insert(RESULT_TABLE, null, cv);
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RESULT_TABLE);
        db.execSQL("DELETE FROM " + CALCULATION_ITEMS_TABLE);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RESULT_TABLE);
        db.execSQL("DELETE FROM " + CALCULATION_ITEMS_TABLE);
        db.execSQL("DELETE FROM " + QUESTION_FOR_STUDENT);
        db.execSQL("DELETE FROM " + ASSIGNMENT_TABLE);
    }

    public void updateQuestionAttempts(String question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(QUESTIONS, question);
        db.update(CALCULATION_ITEMS_TABLE, cv, QUESTIONS + "=" + question, null);
    }

    public ArrayList<UpdateQuestionModel.Question> getQuestionResults(int assignmentID) {
        ArrayList<UpdateQuestionModel.Question> list = new ArrayList<UpdateQuestionModel.Question>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + QUESTION_FOR_STUDENT + " WHERE " + ASSIGNMENT_ID + " ='" + assignmentID + "'", null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String studentId = res.getString(1);
            String assignmentId = res.getString(2);
            String calculationType = res.getString(3);
            String questionNo = res.getString(4);
            String calculations = res.getString(5);
            String noOfQuestions = res.getString(6);
            String userAnswer = res.getString(7);
            String checkAnswer = res.getString(8);
            long i = 0;
            try {
                i = Long.parseLong(res.getString(9));
            } catch (NumberFormatException ex) { // handle your exception
                ex.printStackTrace();
            }
            UpdateQuestionModel.Question resultModel = new UpdateQuestionModel.Question(Integer.parseInt(questionNo), calculations,
                    i, Long.parseLong(userAnswer), Boolean.valueOf(checkAnswer));
            list.add(resultModel);
        }
        return list;
    }

    public ArrayList<ResultModel> getResults() {
        ArrayList<ResultModel> list = new ArrayList<ResultModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + RESULT_TABLE, null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String questions = res.getString(1);
            String noOfQuestions = res.getString(2);
            String calculations = res.getString(3);
            String calculationType = res.getString(4);
            String userAnswer = res.getString(5);
            String checkAnswer = res.getString(6);
            String correctAnswer = res.getString(7);
            ResultModel resultModel = new ResultModel(calculations, userAnswer, correctAnswer, questions, checkAnswer, noOfQuestions, calculationType);
            list.add(resultModel);
        }
        return list;
    }

    public ArrayList<AssignmentResultModel> getAssignmentResults() {
        ArrayList<AssignmentResultModel> list = new ArrayList<AssignmentResultModel>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + ASSIGNMENT_TABLE, null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String questions = res.getString(1);
            String assignmentId = res.getString(2);
            String noOfQuestions = res.getString(3);
            String flickeringSpeed = res.getString(4);

            String calculations = res.getString(5);
            String calculationType = res.getString(6);
            String userAnswer = res.getString(7);
            String checkAnswer = res.getString(8);
            String correctAnswer = res.getString(9);
            String dateOfAssignment = res.getString(10);

            AssignmentResultModel resultModel = new AssignmentResultModel(calculations, userAnswer, correctAnswer, questions, checkAnswer, noOfQuestions, calculationType, assignmentId, flickeringSpeed, dateOfAssignment);
            list.add(resultModel);
        }
        return list;
    }

    public ArrayList<StudentAssignmentModel.AssignmentArray.Question> uncompletedAssignmentss() {
        ArrayList<StudentAssignmentModel.AssignmentArray.Question> list = new ArrayList<StudentAssignmentModel.AssignmentArray.Question>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + QUESTION_FOR_STUDENT, null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String studentId = res.getString(1);
            String assignmentId = res.getString(2);
            String calculationType = res.getString(3);
            String questionNo = res.getString(4);
            String calculations = res.getString(5);
            String noOfQuestions = res.getString(6);
            String userAnswer = res.getString(7);
            String checkAnswer = res.getString(8);
            String correctAnswer = res.getString(9);
            StudentAssignmentModel.AssignmentArray.Question resultModel = new StudentAssignmentModel.AssignmentArray.Question(Integer.parseInt(questionNo), calculations,
                    Integer.parseInt(correctAnswer), Integer.parseInt(userAnswer), Boolean.valueOf(checkAnswer));
            list.add(resultModel);
        }
        return list;
    }

    public int uncompletedAssignments(int assignmentId) {
        String countQuery = "SELECT  * FROM " + QUESTION_FOR_STUDENT + " WHERE " + ASSIGNMENT_ID + " ='" + assignmentId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<String> getResultsByTypeDISTINCT() {

        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT CALCULATION_TYPE from " + RESULT_TABLE, null);
        while (res.moveToNext()) {
            String id = res.getString(0);
            list.add(id);
        }
        return list;
    }

    public ArrayList<ResultModel> getResultsByCalculationType(String type) {
        ArrayList<ResultModel> list = new ArrayList<ResultModel>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + RESULT_TABLE + " WHERE " + CALCULATION_TYPE + "= '" + type + "'", null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String questions = res.getString(1);
            String noOfQuestions = res.getString(2);
            String calculations = res.getString(3);
            String calculationType = res.getString(4);
            String userAnswer = res.getString(5);
            String checkAnswer = res.getString(6);
            String correctAnswer = res.getString(7);
            ResultModel resultModel = new ResultModel(calculations, userAnswer, correctAnswer, questions, checkAnswer, noOfQuestions, calculationType);
            list.add(resultModel);
        }
        return list;
    }

    public ArrayList<CalculationTypeModel> getCalculationTypes() {
        ArrayList<CalculationTypeModel> list = new ArrayList<CalculationTypeModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + CALCULATION_ITEMS_TABLE, null);
        while (res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String calculationType = res.getString(1);
            CalculationTypeModel resultModel = new CalculationTypeModel(calculationType);
            list.add(resultModel);
        }
        return list;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
