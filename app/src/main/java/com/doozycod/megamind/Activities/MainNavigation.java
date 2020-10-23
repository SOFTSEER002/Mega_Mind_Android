package com.doozycod.megamind.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.doozycod.megamind.Fragments.AllResultFragment;
import com.doozycod.megamind.Fragments.AssignmentsFragment;
import com.doozycod.megamind.Fragments.FeedbackFragment;
import com.doozycod.megamind.Fragments.LoginFragment;
import com.doozycod.megamind.Fragments.PracticeFragment;
import com.doozycod.megamind.Fragments.ResultsFragment;
import com.doozycod.megamind.Fragments.SelectedAssignmentResultFragment;
import com.doozycod.megamind.Fragments.SettingsFragment;
import com.doozycod.megamind.Interface.OnFragmentInteractionListener;
import com.doozycod.megamind.Interface.Onloggedin;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.JobScheduleUtils;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.Activities.WrongActivity.PREFS_NAME;

public class MainNavigation extends AppCompatActivity implements Onloggedin,
        NavigationView.OnNavigationItemSelectedListener, ResultsFragment.Callback,
        OnFragmentInteractionListener, PracticeFragment.OnNavigation, SelectedAssignmentResultFragment.Callback {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    DatabaseHelper databaseHelper;
    TextView headText, headText2;
    SharedPreferenceMethod sharedPreferenceMethod;
    TextView tv, txtFeedback, txtLogout, txtVersion;
    ImageView imgFeedback, imgLogout;
    int FLAG = 0;
    PracticeFragment practiceFragment;
    SelectedAssignmentResultFragment selectedAssignmentResultFragment;
    ResultsFragment resultsFragment;

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headText = toolbar.findViewById(R.id.headText);
        headText2 = toolbar.findViewById(R.id.headText2);
        txtFeedback = navigationView.findViewById(R.id.txtFeedback);
        txtLogout = navigationView.findViewById(R.id.txtLogout);
        txtVersion = navigationView.findViewById(R.id.txtVersion);
        imgFeedback = navigationView.findViewById(R.id.imgFeedback);
        imgLogout = navigationView.findViewById(R.id.imgLogout);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main_navigation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Log.e(TAG, "onCreate: density " + getResources().getDisplayMetrics().density);
        initUI();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        practiceFragment = new PracticeFragment();
        practiceFragment.setmListener(this);

        selectedAssignmentResultFragment = new SelectedAssignmentResultFragment();
        selectedAssignmentResultFragment.setListener(this);

        resultsFragment = new ResultsFragment();
        resultsFragment.setListener(this);
//        toolbar properties
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

//        drawer properties
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        if (databaseHelper.getCalculationTypes().size() == 0) {
            databaseHelper.insertCalculationType("Addition");
            databaseHelper.insertCalculationType("Subtraction");
            databaseHelper.insertCalculationType("Multiplication");
            databaseHelper.insertCalculationType("Division");
        }


//        navigation setup
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        Log.e(TAG, "onCreate: Logged in" + sharedPreferenceMethod.checkLogin());

        if (sharedPreferenceMethod.checkLogin()) {
            navigationView.setCheckedItem(R.id.practice);
            navigationView.getHeaderView(0).findViewById(R.id.levelCheck).setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
            navigationView.getMenu().findItem(R.id.feedback1).setVisible(false);
//            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.assignment).setVisible(true);
            navigationView.getMenu().findItem(R.id.results).setVisible(true);
            navigationView.getMenu().findItem(R.id.settings).setVisible(true);
//            navigationView.getMenu().findItem(R.id.feedback2).setVisible(true);
//            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
            View headerView = navigationView.getHeaderView(0);
            tv = headerView.findViewById(R.id.userLevel);
            tv.setText(sharedPreferenceMethod.getLevel());

            txtFeedback.setVisibility(View.VISIBLE);
            txtLogout.setVisibility(View.VISIBLE);
            imgLogout.setVisibility(View.VISIBLE);
            imgFeedback.setVisibility(View.VISIBLE);
        }
//        Interfaces
        new LoginFragment().setListener(this);
        new ResultsFragment().setListener(this);

//        changes for assignment screen

//        if (sharedPreferenceMethod.checkLogin()) {
        if (getIntent().hasExtra("practice")) {
            getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out,
                    android.R.animator.fade_in,
                    android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).addToBackStack("Practice").commit();
        }
        if (getIntent().hasExtra("answer")) {
            getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out,
                    android.R.animator.fade_in,
                    android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).addToBackStack("Practice").commit();
        }
        if (getIntent().hasExtra("assignmentReceived")) {
            getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out,
                    android.R.animator.fade_in,
                    android.R.animator.fade_out).replace(R.id.nav_host_fragment, new AssignmentsFragment()).addToBackStack("FragmentAssignment").commit();
        }
        if (getIntent().hasExtra("result")) {
            headText.setVisibility(View.GONE);
            headText2.setVisibility(View.VISIBLE);
            headText2.setText("Results");
            if (getIntent().hasExtra("assignment")) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new SelectedAssignmentResultFragment()).commit();
            } else {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new ResultsFragment()).commit();
            }

        }
        if (sharedPreferenceMethod.checkLogin()) {
            Log.e(TAG, "onCreate:  power Exercise  " + getIntent().getStringExtra("practice"));
//        Default Fragment check is user login
//            if (getIntent().getStringExtra("practice").equals("answer")) {
//                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
//                        android.R.animator.fade_out,
//                        android.R.animator.fade_in,
//                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).addToBackStack("Practice").commit();
//            }
            if (getIntent().hasExtra("power")) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).commit();
                Log.e(TAG, "onCreate:  power Exercise  " + getIntent().getStringExtra("practice"));
                return;
            }
            if (getIntent().hasExtra("practice")) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).commit();
                return;
            }
            if (getIntent().hasExtra("answer") && getIntent().hasExtra("assignment")) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new AssignmentsFragment()).addToBackStack("Practice").commit();
                return;
            }
            if (getIntent().hasExtra("answer")) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).addToBackStack("Practice").commit();
                return;

            }
            if (getIntent().hasExtra("assignmentReceived")) {
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new AssignmentsFragment()).addToBackStack("FragmentAssignment").commit();
            }
            if (getIntent().hasExtra("result")) {
                headText.setVisibility(View.GONE);
                headText2.setVisibility(View.VISIBLE);
                headText2.setText("Results");
                if (getIntent().hasExtra("assignment")) {
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out).replace(R.id.nav_host_fragment, new SelectedAssignmentResultFragment()).commit();
                } else {
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out).replace(R.id.nav_host_fragment, new ResultsFragment()).addToBackStack("practice").commit();
                }

            } else {
                defaultFragment();
            }
//            Log.e(TAG, "onCreate: login");
        } else {
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
        }

        sharedPreferenceMethod.saveAssignmentTime(17);
        checkAssignmentDay();


        JobScheduleUtils.scheduleJob(this);


        txtFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                headText.setText("Megamind\nAbacus Practice");
                headText.setVisibility(View.VISIBLE);
                headText2.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new FeedbackFragment()).addToBackStack("Feedback").commit();

            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                sharedPreferenceMethod.saveLogin(false);
                if (databaseHelper != null) {
                    databaseHelper.deleteAll();
                }

                //CLEAR THE STACK AFTER LOGGING OUT
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if (fragmentManager.getBackStackEntryCount() != -1) {

                }
//
//                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,
                                android.R.animator.fade_out,
                                android.R.animator.fade_in,
                                android.R.animator.fade_out)
                        .replace(R.id.nav_host_fragment, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
                navigationView.setCheckedItem(R.id.nav_home);
                navigationView.getHeaderView(0).findViewById(R.id.levelCheck).setVisibility(View.GONE);
                navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                navigationView.getMenu().findItem(R.id.feedback1).setVisible(true);
                navigationView.getMenu().findItem(R.id.assignment).setVisible(true);
                navigationView.getMenu().findItem(R.id.results).setVisible(false);
                navigationView.getMenu().findItem(R.id.settings).setVisible(false);
                txtLogout.setVisibility(View.GONE);
                txtFeedback.setVisibility(View.GONE);
                imgLogout.setVisibility(View.GONE);
                imgFeedback.setVisibility(View.GONE);


//                int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
//                if (backStackEntry > 0) {
//                    for (int i = 0; i < backStackEntry; i++) {
//                        getSupportFragmentManager().popBackStackImmediate();
//                    }
//                }
            }
        });

//        showAlertDialog();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    void defaultFragment() {
//        if (getIntent().hasExtra("result")) {
//            navigationView.setCheckedItem(R.id.feedback2);
//            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PracticeFragment()).commit();

//        } else {
        navigationView.setCheckedItem(R.id.assignment);
        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PracticeFragment(), "FragmentAssignment").commit();
//        }

    }

    @Override
    public void OnNavigationMenuChange(boolean send, JSONObject jsonObject) {
        if (send) {
            navigationView.setCheckedItem(R.id.assignment);
            navigationView.getHeaderView(0).findViewById(R.id.levelCheck).setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
            navigationView.getMenu().findItem(R.id.feedback1).setVisible(false);
            navigationView.getMenu().findItem(R.id.assignment).setVisible(true);
            navigationView.getMenu().findItem(R.id.results).setVisible(true);
            navigationView.getMenu().findItem(R.id.settings).setVisible(true);
            txtLogout.setVisibility(View.VISIBLE);
            txtFeedback.setVisibility(View.VISIBLE);
            imgLogout.setVisibility(View.VISIBLE);
            imgFeedback.setVisibility(View.VISIBLE);
            tv = findViewById(R.id.userLevel);
            tv.setText(sharedPreferenceMethod.getLevel());

        }
    }


    public String checkAssignmentDay() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();

        String dayOfTheWeek = sdf.format(d);

//        Log.e(TAG, "checkAssignmentDay: " + dayOfTheWeek + "  " + d.getHours());
        return dayOfTheWeek;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
                headText.setText("Megamind\nAbacus Practice");
                headText.setVisibility(View.VISIBLE);
                headText2.setVisibility(View.GONE);
//                headText2.setText("Results");

            }
            break;
            case R.id.assignment: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                headText2.setText("Megamind\nAbacus Practice");
                headText2.setVisibility(View.VISIBLE);
                headText.setVisibility(View.GONE);
                sharedPreferenceMethod.saveOnResultClick("");

                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new AssignmentsFragment(), "FragmentAssignment").commit();

            }
            break;
            case R.id.practice: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new PracticeFragment()).addToBackStack("Practice").commit();
                headText.setText("Megamind\nAbacus Practice");
                headText.setVisibility(View.VISIBLE);
                headText2.setVisibility(View.GONE);

                if (sharedPreferenceMethod != null) {
                    if (databaseHelper != null) {
                        databaseHelper.deleteTable();
                    }
                    Log.e(TAG, "onCreateView: NOT NULL");
                    SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    int questionNo = prefs.getInt("questions", -22);
//                if (questionNo >= Integer.parseInt(sharedPreferenceMethod.getQuestions())) {

//                    SharedPreference

//                }
                }
            }
            break;
            case R.id.settings: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new SettingsFragment()).addToBackStack("Settings").commit();
                headText2.setText("Settings");
                headText2.setVisibility(View.VISIBLE);
                headText.setVisibility(View.GONE);
            }
            break;
            case R.id.feedback1:
            case R.id.feedback: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new FeedbackFragment()).addToBackStack("Feedback").commit();
                headText.setText("Megamind\nAbacus Practice");
                headText.setVisibility(View.VISIBLE);
                headText2.setVisibility(View.GONE);
            }
            break;
            case R.id.results: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                headText2.setText("Results");
                headText2.setVisibility(View.VISIBLE);
                headText.setVisibility(View.GONE);
                sharedPreferenceMethod.saveOnResultClick("saved");
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new AllResultFragment()).addToBackStack("AllResult").commit();
            }
            break;
            case R.id.logout: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                sharedPreferenceMethod.saveLogin(false);
                if (databaseHelper != null) {
                    databaseHelper.deleteAll();
                }

                //CLEAR THE STACK AFTER LOGGING OUT
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if (fragmentManager.getBackStackEntryCount() != -1) {

                }
//
//                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,
                                android.R.animator.fade_out,
                                android.R.animator.fade_in,
                                android.R.animator.fade_out)
                        .replace(R.id.nav_host_fragment, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
                navigationView.setCheckedItem(R.id.nav_home);
                navigationView.getHeaderView(0).findViewById(R.id.levelCheck).setVisibility(View.GONE);
                navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                navigationView.getMenu().findItem(R.id.feedback1).setVisible(true);
                navigationView.getMenu().findItem(R.id.assignment).setVisible(true);
                navigationView.getMenu().findItem(R.id.results).setVisible(false);
                navigationView.getMenu().findItem(R.id.settings).setVisible(false);
                txtLogout.setVisibility(View.GONE);
                txtFeedback.setVisibility(View.GONE);
                imgLogout.setVisibility(View.GONE);
                imgFeedback.setVisibility(View.GONE);
            }
            break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        if (getIntent().hasExtra("result")) {
        super.onBackPressed();

        if (FLAG == 0) {
            FLAG = 1;
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == -1) {
                finishAffinity();
            }
        }
//        PracticeFragment myFragment = (PracticeFragment) getFragmentManager().findFragmentByTag("Practice");
        Fragment f = getFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (f instanceof PracticeFragment) {
            navigationView.setCheckedItem(R.id.practice);
            Log.e(TAG, "onBackPressed: practice");
            sharedPreferenceMethod.saveOnResultClick("");

        }
        if (f instanceof AssignmentsFragment) {
            navigationView.setCheckedItem(R.id.assignment);
            sharedPreferenceMethod.saveOnResultClick("");
            Log.e(TAG, "onBackPressed: Assignment");
        }
        if (f instanceof AllResultFragment) {
            navigationView.setCheckedItem(R.id.results);
            Log.e(TAG, "onBackPressed: results");
            sharedPreferenceMethod.saveOnResultClick("saved");

        }
        if (f instanceof SettingsFragment) {
            navigationView.setCheckedItem(R.id.settings);
            Log.e(TAG, "onBackPressed: settings");
            sharedPreferenceMethod.saveOnResultClick("");

        }
        if (f instanceof FeedbackFragment) {
            navigationView.setCheckedItem(R.id.feedback1);
            navigationView.setCheckedItem(R.id.feedback);
            Log.e(TAG, "onBackPressed: feedback");
            sharedPreferenceMethod.saveOnResultClick("");

        }
        // do something with f
//            ((CustomFragmentClass) f).doSomething();
//        } else {
//            defaultFragment();
//        }
    }

    @Override
    public void mCallbackListener(boolean result) {
        headText.setText("Megamind\nAbacus Practice");
        headText.setVisibility(View.VISIBLE);
        headText2.setVisibility(View.GONE);
    }


    @Override
    public void onFragmentInteraction(String title) {
        if (title.equals("assignment")) {
            navigationView.setCheckedItem(R.id.assignment);

        } else {
            headText.setText(title);
        }
        headText.setVisibility(View.VISIBLE);
        headText2.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void OnNavigationChange(String navigation) {
        if (navigation.equals("assignment")) {
            navigationView.setCheckedItem(R.id.assignment);
        }
    }

//
//    public boolean isConnected() {
//        boolean connected = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public void showAlertDialog() {
//
//        if(isConnected() == false) {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                    MainNavigation.this);
//
//            // Set title
//            alertDialogBuilder.setTitle("No Internet Available");
//
//            // Set dialog message
//            alertDialogBuilder
//                    .setMessage("Please turn on your Wifi or Mobile Data")
//                    .setCancelable(false)
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            // Create Alert Dialog
//            AlertDialog alertDialog = alertDialogBuilder.create();
//
//            // Display it
//            alertDialog.show();
//        }
//    }

}
