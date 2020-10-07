package com.doozycod.megamind.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.doozycod.megamind.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int noOfDigits = 15, flickeringSpeed = 500, subtraction = 0;
    int digitSize = 1, levelNumber = 1;
    public static final String PREFS_NAME = "MyPrefsFile";
    Button level1, level2, level3, feedback, custom, start;
    TextView digitSizeValue, noOfDigitsValue, flickeringSpeedValue, subtractionValue, levelNoTextView;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_navi);

        SharedPreferences settings =
                getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("result", -22);
        editor.putInt("questions", -22);
        editor.putInt("digitSize", -22);
        editor.putInt("noOfDigits", -22);
        editor.putInt("flickeringSpeed", -22);
        editor.putInt("subtraction", -22);
        editor.putInt("wrongAnswers", -22);
        editor.putInt("levelNumber", -22);
        editor.apply();

        digitSizeValue = findViewById(R.id.digitSizeValue);
        noOfDigitsValue = findViewById(R.id.noOfDigitsValue);
        flickeringSpeedValue = findViewById(R.id.flickeringSpeedValue);
        subtractionValue = findViewById(R.id.subtractionValue);
        levelNoTextView = findViewById(R.id.levelNoTextView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAction);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        level1 = findViewById(R.id.level1btn);
        level2 = findViewById(R.id.level2btn);
        level3 = findViewById(R.id.level3btn);

        level1.setSelected(true);
        level1.setTextColor(getResources().getColor(R.color.spalsh_background));
        level2.setSelected(false);
        level2.setTextColor(getResources().getColor(R.color.grayishbrown));
        level3.setSelected(false);
        level3.setTextColor(getResources().getColor(R.color.grayishbrown));

        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digitSizeValue.setText("Single Digit");
                noOfDigitsValue.setText("15");
                flickeringSpeedValue.setText("500ms");
                subtractionValue.setText("Yes");

//                resetOtherButtonLayout();
//                level1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cobalt)));
//                level1.setBackgroundResource(R.drawable.blue_btn);
                level1.setSelected(true);
                level2.setSelected(false);
                level3.setSelected(false);


                level1.setTextColor(getResources().getColor(R.color.spalsh_background));
                level2.setTextColor(getResources().getColor(R.color.grayishbrown));
                level3.setTextColor(getResources().getColor(R.color.grayishbrown));
                levelNoTextView.setText("Single Digit");

                digitSize = 1;
                noOfDigits = 15;
                flickeringSpeed = 500;
                subtraction = 1;
                levelNumber = 1;
            }
        });


        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                resetOtherButtonLayout();

//               level2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cobalt)));

                //  level2.setBackgroundResource(R.drawable.blue_btn);
                level1.setSelected(false);
                level2.setSelected(true);
                level3.setSelected(false);

                level1.setTextColor(getResources().getColor(R.color.grayishbrown));
                level2.setTextColor(getResources().getColor(R.color.spalsh_background));
                level3.setTextColor(getResources().getColor(R.color.grayishbrown));

                digitSizeValue.setText("Double Digit");
                noOfDigitsValue.setText("10");
                flickeringSpeedValue.setText("500ms");
                subtractionValue.setText("Yes");
                levelNoTextView.setText("Double Digit");

                digitSize = 2;
                noOfDigits = 10;
                flickeringSpeed = 500;
                subtraction = 1;
                levelNumber = 2;
            }
        });


        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digitSizeValue.setText("Triple Digit");
                noOfDigitsValue.setText("5");
                flickeringSpeedValue.setText("500ms");
                subtractionValue.setText("Yes");
                levelNoTextView.setText("Triple Digit");
//                resetOtherButtonLayout();

//                level3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cobalt)));
                level1.setSelected(false);
                level2.setSelected(false);
                level3.setSelected(true);
//
                level1.setTextColor(getResources().getColor(R.color.grayishbrown));
                level2.setTextColor(getResources().getColor(R.color.grayishbrown));
                level3.setTextColor(getResources().getColor(R.color.spalsh_background));

                digitSize = 3;
                noOfDigits = 5;
                flickeringSpeed = 500;
                subtraction = 1;
                levelNumber = 3;
            }
        });


        feedback = findViewById(R.id.feedbackButton);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (MainActivity.this,
                                FeedbackActivity.class);
                startActivity(intent);
            }
        });


        custom = findViewById(R.id.customButton);

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (MainActivity.this,
                                CustomActivity.class);
                startActivity(intent);
            }
        });

        start = findViewById(R.id.startGamebtn);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (MainActivity.this,
                                GameActivity.class);
                intent.putExtra("digitSize", digitSize);
                intent.putExtra("noOfDigits", noOfDigits);
                intent.putExtra("flickeringSpeed", flickeringSpeed);
                intent.putExtra("subtraction", subtraction);
                intent.putExtra("levelNumber", levelNumber);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home: {
                startActivity(new Intent(MainActivity.this, MainNavigation.class));
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
            break;
            case R.id.feedback1:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.assignment:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.results:
                startActivity(new Intent(MainActivity.this, ResultActivity.class));
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.settings:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;

        }
        return true;
    }

    private void resetOtherButtonLayout() {
        final Button level1 = findViewById(R.id.level1btn);
        final Button level2 = findViewById(R.id.level2btn);
        final Button level3 = findViewById(R.id.level3btn);
//        final Button level4 = findViewById(R.id.level4btn);
//        final Button level5 = findViewById(R.id.level5btn);
//        final Button level6 = findViewById(R.id.level6btn);
//        final Button level7 = findViewById(R.id.level7btn);
//        final Button level8 = findViewById(R.id.level8btn);

        level1.setBackgroundTintList(null);
        level1.setTextColor
                (getResources().getColor(R.color.grayishbrown));

        level2.setBackgroundTintList(null);
        level2.setTextColor
                (getResources().getColor(R.color.grayishbrown));

        level3.setBackgroundTintList(null);
        level3.setTextColor
                (getResources().getColor(R.color.grayishbrown));

//        level4.setBackgroundResource
//                (R.drawable.levelnumbercircle_button);
//        level4.setTextColor
//                (getResources().getColor(R.color.levelButtonFontColor));
//
//        level5.setBackgroundResource
//                (R.drawable.levelnumbercircle_button);
//        level5.setTextColor
//                (getResources().getColor(R.color.levelButtonFontColor));
//
//        level6.setBackgroundResource
//                (R.drawable.levelnumbercircle_button);
//        level6.setTextColor
//                (getResources().getColor(R.color.levelButtonFontColor));
//
//        level7.setBackgroundResource
//                (R.drawable.levelnumbercircle_button);
//        level7.setTextColor
//                (getResources().getColor(R.color.levelButtonFontColor));
//
//        level8.setBackgroundResource
//                (R.drawable.levelnumbercircle_button);
//        level8.setTextColor
//                (getResources().getColor(R.color.levelButtonFontColor));

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


}
