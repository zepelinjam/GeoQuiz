package com.example.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private Button mStartButton;
    private Button mSettingsButton;
    private Button mHighScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initializeViews();

        // обработчик на кнопке "Старт"

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // запускаем игру - активити "QuizActivity"
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, QuizActivity.class);
                startActivity(intent);
            }
        });


        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // запуск настроек
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        mHighScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, HighscoreActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initializeViews(){
        mStartButton = (Button) findViewById(R.id.start_button);
        mSettingsButton = (Button) findViewById(R.id.settings_button);
        mHighScoresButton = (Button) findViewById(R.id.highscores_button);
    }

}
