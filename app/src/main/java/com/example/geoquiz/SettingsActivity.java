package com.example.geoquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public Switch mAnswersSwitch;
    private SharedPreferences mSettings;
    private Button mAboutButton;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_ANSWERS = "show_answers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        initializeViews();

        mAboutButton.setOnClickListener(new View.OnClickListener() { //обработчик нажатия кнопки true
            @Override
            public void onClick(View v) {
                dialogAbout();
            }
        });
    }

    // диалоговое окно, которое сообщает о информации о приложении
    public void dialogAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("О приложении") // заголовок
                .setMessage("География: правда или ложь (GeoQuiz)"
                        +"\n"+ "Версия: 1.0"
                        +"\n"+ "Автор: Юрий Пилипенко"
                        +"\n"+ "Контактный e-mail: zepelinjam@gmail.com")
                .setCancelable(true)
                .setNeutralButton("OK",
                        new DialogInterface.OnClickListener() { // по нажатии ...
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // закрыть диалог
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
        // отрисовка кнопки по центру
        final Button neutralbutton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralbutton.getLayoutParams();
        neutralButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        neutralbutton.setLayoutParams(neutralButtonLL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_ANSWERS, mAnswersSwitch.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_ANSWERS)) {
            mAnswersSwitch.setChecked(mSettings.getBoolean(APP_PREFERENCES_ANSWERS, false));
        }
    }

    public void initializeViews(){
        mAnswersSwitch = (Switch) findViewById(R.id.switch_answers);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mAboutButton = (Button) findViewById(R.id.button_about);
    }
}
