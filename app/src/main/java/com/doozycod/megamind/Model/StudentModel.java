package com.doozycod.megamind.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("mCode")
    @Expose
    private String mCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("screenName")
    @Expose
    private String screenName;
    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("assignmentDay")
    @Expose
    private String assignmentDay;

    public String getAssignmentDay() {
        return assignmentDay;
    }

    public void setAssignmentDay(String assignmentDay) {
        this.assignmentDay = assignmentDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
