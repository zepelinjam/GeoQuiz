package com.example.geoquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch mAnsrers;
    public Boolean isWhite = true;
    public Boolean answersOff;
    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        initializeViews();
    }

    public void initializeViews(){
        mAnsrers = (Switch) findViewById(R.id.switch_answers);
    }
}
