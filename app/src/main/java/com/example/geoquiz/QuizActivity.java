package com.example.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.geoquiz.Model.Question;
import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton; // создание переменной для кнопки True (Правда)
    private Button mFalseButton; // создание переменной для кнопки False (Ложь)
    private TextSwitcher mQuestionTextSwitcher;
    private TextSwitcher mScoreTextSwitcher; // переменная для счёта
    private TextView mTimer; // переменная таймера
    private int mScore = 0; // переменная для подсчёта очков
    private int mCorrectAnswers = 0; // переменная для подсчёта кол-ва верных ответов
    private int mCurrentIndex; // переменная для счётчика вопросов
    Random mRandom = new Random(); // создание объекта Random
    private static final String TAG = "QuizActivity"; // Добавление константы TAG
    private ArrayList<Question> questions;
    private CountDownTimer mCT; // переменная для таймера обратного отсчета
    private String mReason = "";
    private Resources myResources;
    private Drawable mImage;
    private int mQestionsQuantaty;

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_ANSWERS = "show_answers";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called"); // Включение команды регистрации сообщения
        setContentView(R.layout.activity_quiz);

        initializeQuestions();
        mQestionsQuantaty = questions.size();
        initializeViews();
        makeMeAnim();

        mScoreTextSwitcher.setText(String.valueOf(mScore)); // выводим нулевой счёт в начале игры
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        updateQuestion(); // запуск обновления вопроса на сулчай первого запуска приложения
        mTrueButton.setOnClickListener(new View.OnClickListener() { //обработчик нажатия кнопки true
            @Override
            public void onClick(View v) {
                checkAnswer(true); // вызываем метод checkAnswer, и передаем в него true
                updateQuestion(); // обновелния вопроса
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() { //обработчик нажатия кнопки false
            @Override
            public void onClick(View v) {
                checkAnswer(false); // вызываем метод checkAnswer, и передаем в него false
                updateQuestion(); // обновелния вопроса
            }
        });
    }

    // метод для проверки правильности ответа
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questions.get(mCurrentIndex).isAnswerTrue(); // создаем лог. переменную answerIsTrue, присваиваем ей занчение из массива
        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) { // если ответ верен, то пишем что правильно
            messageResId = R.string.correct_toast;
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
            // вывод уведомления
            mScore++;
            mCorrectAnswers ++; // увличиваем счётчик правильных ответов на 1 если ответ верен
            mScoreTextSwitcher.setText(String.valueOf(mScore)); // увеличиваем счётчик правильных ответов
        } else { // если ответ неверен, то пишем что неправильно
            if (mSettings.getBoolean(APP_PREFERENCES_ANSWERS, false)) {
                mCT.cancel();
                CorrectAnswerDialog();
            }
            else {
                messageResId = R.string.incorrect_toast;
                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show(); // вывод уведомления
            }
        }
        questions.remove(mCurrentIndex);
    }

    // метод для обновления вопросов
    private void updateQuestion(){
        // если вопросы в массиве еще остались
        if (questions.size() != 0){
            mCurrentIndex = mRandom.nextInt(questions.size()); // выбираем рандомный индекс вопроса
            int question = questions.get(mCurrentIndex).getTextResId(); // переменной присваиваем id вопроса
            mQuestionTextSwitcher.setText(getText(question));
            // если таймер работает, то включаем. Актуально только при запуске приложения
            if (mCT != null) {
                mCT.cancel();
            }
            setTimer(); // запуск таймера на вопрос
        }
        // если вопросов больше нет
        else {
            mCT.cancel(); // останавливаем таймер
            mReason = "Поздравляем, Вы дали ответы на все вопросы!"; // заголовок диалогового окна
            mImage = myResources.getDrawable(R.drawable.win); // картина диалогового окна
            gameOverDialog(); // вызов диалога
        }
    }

    // иницализация вьюшек
    private void initializeViews() {
        mTrueButton = (Button) findViewById(R.id.true_button); // связываем переменную с id кнопки
        mFalseButton = (Button) findViewById(R.id.false_button); // связываем переменную с id кнопки
        mScoreTextSwitcher = (TextSwitcher)findViewById(R.id.score_textwiew); // связываем переменную c текствью (счёт)
        mQuestionTextSwitcher = (TextSwitcher) findViewById(R.id.question_text_view);
        mTimer = (TextView) findViewById(R.id.timer);
        myResources = getResources();
    }

    // метод, отвечающий за анимацию вопросов и счётчика
    private void makeMeAnim(){
        Animation slideInLeftAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation slideOutRightAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        mQuestionTextSwitcher.setInAnimation(slideInLeftAnimation);
        mQuestionTextSwitcher.setOutAnimation(slideOutRightAnimation);
        mScoreTextSwitcher.setInAnimation(slideInLeftAnimation);
        mScoreTextSwitcher.setOutAnimation(slideOutRightAnimation);

        mQuestionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(QuizActivity.this);
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setShadowLayer(10, 10, 10, Color.BLACK);
                return textView;
            }
        });

        mScoreTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(QuizActivity.this);
                textView.setTextSize(50);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setShadowLayer(10, 10, 10, Color.BLACK);
                return textView;
            }
        });
    }

    // метод для таймера обратного отсчёта
    public void setTimer() {
        mCT = new CountDownTimer(11000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimer.setText("Время: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                mReason = "Вермя вышло!";
                mImage = myResources.getDrawable(R.drawable.game_over);
                gameOverDialog(); // вызываем диалоговое окно про конец игры
            }
        }.start();
    }

    // диалоговое окно, которое сообщает о конце игры
    public void gameOverDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle(mReason) // заголовок
                .setMessage("Вы дали " + mScore +" (" + ((mScore*100)/mQestionsQuantaty) +"%) верных ответов.") // сообщение
                .setIcon(mImage)  //каринка
                .setCancelable(false) // можно закрыть только по кнопке
                .setNeutralButton("ОК",
                        new DialogInterface.OnClickListener() { // по нажатии ...
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // закрыть диалог
                                finish();   // закрыть активити
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
        // отрисовка кнопки по центру
        final Button neutralbutton = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralbutton.getLayoutParams();
        neutralButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        neutralbutton.setLayoutParams(neutralButtonLL);
    }

    // диалоговое окно, которое сообщает о верном ответе
    public void CorrectAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle("Правильный ответ") // заголовок
                .setMessage(questions.get(mCurrentIndex).getAnswerResId())
                .setCancelable(false)
                .setNeutralButton("Теперь Я знаю!",
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
    public void onBackPressed() {
        // super.onBackPressed();
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle("Закрыть текущую игру?") // заголовок
                .setMessage("Прогресс в игре не сохранится!")
                .setCancelable(true)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() { // по нажатии ...
                            public void onClick(DialogInterface dialog, int id) {
                                mCT.cancel();
                                finish(); // закрыть текущуюю игру
                            }
                        })
                .setNegativeButton("Нет",
                        new DialogInterface.OnClickListener() { // по нажатии ...
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // закрыть диалог
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // создание методов для различных состояний жизненного цикла приложения
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    // иннициализация вопросов
    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question(R.string.question_Oceans, true, R.string.answer_Oceans));
        questions.add(new Question(R.string.question_Suez, false, R.string.answer_Suez));
        questions.add(new Question(R.string.question_Nil, false, R.string.answer_Nil));
        questions.add(new Question(R.string.question_Amazonas,true, R.string.answer_Amazonas));
        questions.add(new Question(R.string.question_Himalayas,true, R.string.answer_Himalayas));
        questions.add(new Question(R.string.question_NY, false, R.string.answer_NY));
        questions.add(new Question(R.string.question_Greenland, false, R.string.answer_Greenland));
        questions.add(new Question(R.string.question_WhiteSea, false, R.string.answer_WhiteSea));
        questions.add(new Question(R.string.question_Equator, false, R.string.answer_Equator));
        questions.add(new Question(R.string.question_JapanStreets, true, R.string.answer_JapanStreets));
        questions.add(new Question(R.string.question_Arabia, true, R.string.answer_Arabia));
        questions.add(new Question(R.string.question_Nivelir, false, R.string.answer_Nivelir));
        questions.add(new Question(R.string.question_Canada, true, R.string.answer_Canada));
        questions.add(new Question(R.string.question_Antarctica, false, R.string.answer_Antarctica));
        questions.add(new Question(R.string.question_Azimuth, false, R.string.answer_Azimuth));
        questions.add(new Question(R.string.question_Religion, false, R.string.answer_Religion));
        questions.add(new Question(R.string.question_Maory, true, R.string.answer_Maory));
        questions.add(new Question(R.string.question_Chinese, true, R.string.answer_Chinese));
        questions.add(new Question(R.string.question_Cities, false, R.string.answer_Cities));
        questions.add(new Question(R.string.question_Bangladesh, false, R.string.answer_Bangladesh));
        questions.add(new Question(R.string.question_AustraliaCapital, true, R.string.answer_AustraliaCapital));
        questions.add(new Question(R.string.question_Peninsula, false, R.string.answer_Peninsula));
        questions.add(new Question(R.string.question_BrazilCapital, false, R.string.answer_BrazilCapital));
        questions.add(new Question(R.string.question_HighestWaterfall, false, R.string.answer_HighestWaterfall));
        questions.add(new Question(R.string.question_Andorra, true, R.string.answer_Andorra));
        questions.add(new Question(R.string.question_Australia_Land, true, R.string.answer_Australia_Land));
        questions.add(new Question(R.string.question_Two_Nationality, true, R.string.answer_Two_Nationality));
        questions.add(new Question(R.string.question_Toronto, false, R.string.answer_Toronto));
    }
}
