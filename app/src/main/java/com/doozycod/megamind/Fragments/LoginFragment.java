package com.doozycod.megamind.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doozycod.megamind.Interface.ApiService;
import com.doozycod.megamind.Interface.Onloggedin;
import com.doozycod.megamind.Model.StudentModel;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.ApiUtils;
import com.doozycod.megamind.Utils.CustomProgressBar;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.R.drawable.hide_icon;
import static com.doozycod.megamind.R.drawable.visible_icon;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText etEmail, etPassword;
    Button loginButton;
    Onloggedin onLoggedIn;
    Button visible_password;
    boolean isVisible = false;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;

    boolean connected = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onLoggedIn = (Onloggedin) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public void setListener(Onloggedin listener) {
        onLoggedIn = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        apiService = ApiUtils.getAPIService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());
        etEmail = view.findViewById(R.id.userEmail);
        etPassword = view.findViewById(R.id.userPassword);
        loginButton = view.findViewById(R.id.loginButton);
        visible_password = view.findViewById(R.id.visible_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEmail.getText().toString().equals("")) {
                    customProgressBar.showProgress();
                    loginStudentUsingMCode(etEmail.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Please enter mCode of Student!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        visible_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    visible_password.setBackground(getResources().getDrawable(hide_icon));
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isVisible = false;
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    isVisible = true;
                    visible_password.setBackground(getResources().getDrawable(visible_icon));
                }
            }
        });
        return view;
    }

    private void loginStudentUsingMCode(String mCode) {
        apiService.getStudent(mCode).enqueue(new Callback<StudentModel>() {
            @Override
            public void onResponse(Call<StudentModel> call, Response<StudentModel> response) {
                customProgressBar.hideProgress();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", response.body().getId());
                            jsonObject.put("mCode", response.body().getmCode());
                            jsonObject.put("name", response.body().getName());
                            jsonObject.put("screenName", response.body().getScreenName());
                            jsonObject.put("level", response.body().getLevel());
                            jsonObject.put("assignmentDay", response.body().getAssignmentDay());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sharedPreferenceMethod.saveUserDetails(String.valueOf(response.body().getId()), response.body().getmCode(), response.body().getName(),
                                response.body().getScreenName(), String.valueOf(response.body().getLevel()), response.body().getAssignmentDay());

                        sharedPreferenceMethod.saveLogin(true);
                        Log.e(TAG, "onCreate: Logged in" + sharedPreferenceMethod.checkLogin());
                        onLoggedIn.OnNavigationMenuChange(true, jsonObject);

                    }

//                    getActivity().getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, ).commit();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in,
                                    android.R.animator.fade_out,
                                    android.R.animator.fade_in,
                                    android.R.animator.fade_out)
                            .remove(new LoginFragment()).replace(R.id.nav_host_fragment, new AssignmentsFragment())
                            .commit();
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Invalid student supplied", Toast.LENGTH_SHORT).show();
                    }
                    if (response.code() == 404) {
                        Toast.makeText(getActivity(), "Student not found", Toast.LENGTH_SHORT).show();
                    }
                    if (response.code() == 502) {
                        showLoginCodeDialog();
                        Toast.makeText(getActivity(), "mCode is incorrect, Please Check!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<StudentModel> call, Throwable t) {
                customProgressBar.hideProgress();
                  if(connected == false){
                  showAlertDialog();}
                  else
                  {
                   Toast.makeText(getActivity(), "Check your Internet connection!", Toast.LENGTH_SHORT).show();
                  }
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void showLoginCodeDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.mcode_incorrect_dialog);
        Button closeDialog = dialog.findViewById(R.id.closeDialog);
        dialog.show();
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public boolean isConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;

        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void showAlertDialog() {
        if(isConnected() == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            // Set title
            alertDialogBuilder.setTitle("Can't reach to the Server");

            // Set dialog message
            alertDialogBuilder
                    .setMessage("Please check your Wifi or Mobile Data")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // Create Alert Dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // Display it
            alertDialog.show();

        }
    }



}
