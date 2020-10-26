package com.doozycod.megamind.Interface;

import com.doozycod.megamind.Model.AssignmentQuestionModel;
import com.doozycod.megamind.Model.NewAssignmentResponse;
import com.doozycod.megamind.Model.QuestionModel;
import com.doozycod.megamind.Model.ResponseModel;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.Model.StudentModel;
import com.doozycod.megamind.Model.UpdateQuestionModel;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("student")
    Call<StudentModel> getStudent(@Query("mCode") String mCode);

    @GET("student/{id}")
    Call<StudentModel> getStudentById(@Path("id") int id);

    @PATCH("student/{id}/")
    Call<StudentModel> updateStudentDetails(@Path("id") int id, @Query("mCode") String mCode, @Body StudentModel studentDetails);

    @GET("student/{id}/assignments")
    Call<StudentAssignmentModel> getAssignmentById(@Path("id") int id, @Query("status") String status);

    @FormUrlEncoded
    @POST("student/{id}/assignment")
    Call<NewAssignmentResponse> createAssignmentById(@Path("id") int id, @Field("status") String status);

    @PATCH("student/{id}/assignment/{assignmentId}/")
    Call<ResponseModel> updateAssignment(@Path("id") int id, @Path("assignmentId") int assignmentId, @Body JSONObject questionModel);

    @GET("student/{id}/assignment/{assignmentId}/questions")
    Call<QuestionModel> getDetailOfQuestion(@Path("id") int id, @Path("assignmentId") String assignmentId);
}
