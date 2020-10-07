package com.doozycod.megamind.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceMethod {
    Context context;
    SharedPreferences sp;

    public SharedPreferenceMethod(Context context) {
        this.context = context;

    }

    public void insertProperties(String type, String flickerSpeed, String digitSize, String noOfDigits , String noOfQuestions) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("type", type);
        sp_editior.putString("flickerSpeed", flickerSpeed);
        sp_editior.putString("digitSize", digitSize);
        sp_editior.putString("noOfDigits", noOfDigits);
        sp_editior.putString("noOfQuestions", noOfQuestions);
        sp_editior.commit();

    }
    public void saveUserDetails(String id, String mCode, String name, String screenName , String level,String assignmentDay) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.putString("id", id);
        sp_editior.putString("mCode", mCode);
        sp_editior.putString("name", name);
        sp_editior.putString("screenName", screenName);
        sp_editior.putString("level", level);
        sp_editior.putString("assignmentDay", assignmentDay);
        sp_editior.commit();

    }
    public void savePowerAttributes(String random,String time, String startFrom, String exerciseType, String answer , String count) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.putString("random", random);
        sp_editior.putString("startFrom", startFrom);
        sp_editior.putString("time", time);
        sp_editior.putString("exerciseType", exerciseType);
        sp_editior.putString("answer", answer);
        sp_editior.putString("count", count);
        sp_editior.commit();

    }

    public String getSeconds() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("time", "");
    }

    public String getCount() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("count", "");
    }
    public String getPowerAnswer() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("answer", "");
    }
    public String getExerciseType() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("exerciseType", "");
    }
    public String getStartFrom() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("startFrom", "");
    }
    public String getPowerRandom() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("random", "");
    }

        public void updateScreenName(String studentName){
            sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor sp_editior = sp.edit();
            sp_editior.putString("screenName", studentName);
            sp_editior.commit();

        }
    public String getID() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("id", "");
    }
    public String getmCode() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("mCode", "");
    }
    public String getName() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("name", "");
    }
    public String getScreenName() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("screenName", "");
    }
    public String getLevel() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("level", "");
    }
    public String getAssignmentDay() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("assignmentDay", "");
    }
    public int getDate() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getInt("date", 10);
    }
    public void saveAssignmentTime(int time) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putInt("date", time);
        sp_editior.commit();
    }
    public void insertDivision(String divisor) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("divisor", divisor);
        sp_editior.commit();
    }
    public void saveLogin(boolean checkUser) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putBoolean("checkUser", checkUser);
        sp_editior.commit();
    }

    public void saveSubtraction(boolean checkUser) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putBoolean("Subtraction", checkUser);
        sp_editior.commit();
    }
    public void setAssignmentId(String assignmentId) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("assignmentId", assignmentId);
        sp_editior.commit();
    }
    public void setPercentage(String assignmentId) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("setPercentage", assignmentId);
        sp_editior.commit();
    }
    public void setPosition(String position) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("position", position);
        sp_editior.commit();
    }
    public void saveOnResultClick(String position) {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("saveOnResultClick", position);
        sp_editior.commit();
    }

    public String getOnResultClick() {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("saveOnResultClick", "");
    }

    public String getPosition() {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("position", "");
    }

    public String getPercentage() {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("setPercentage", "");
    }

    public String getAssignmentId() {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("assignmentId", "");
    }
    public boolean getSubtraction() {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getBoolean("Subtraction", false);
    }
    public boolean checkLogin() {
        sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getBoolean("checkUser", false);
    }
    public String getType() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("type", "");
    }
    public String getDivisorSize() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("divisor", "");
    }
    public String getFlickerSpeed() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("flickerSpeed", "");
    }

    public String getDigitSize() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("digitSize", "");
    }

    public String getNoOfDigits() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("noOfDigits", "");
    }
    public String getQuestions() {
        SharedPreferences sp = context.getSharedPreferences("Megamind_PREF", Context.MODE_PRIVATE);
        return sp.getString("noOfQuestions", "");
    }


}
