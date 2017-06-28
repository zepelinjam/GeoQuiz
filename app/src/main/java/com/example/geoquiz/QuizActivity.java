package com.example.geoquiz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.Model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton; // создание переменной для кнопки True (Правдка)
    private Button mFalseButton; // создание переменной для кнопки False (Ложь)
    private TextView mQuestionTextView; // создание переменной для текствью (Вопрос)
    private TextView mScoreTextView; // переменная для счёта
    private TextView mTimer; // переменная таймера
    private int mScore = 0;
    private int mCurrentIndex; // переменная для счётчика вопросов
    Random mRandom = new Random(); // создание объекта Random
    private static final String TAG = "QuizActivity"; // Добавление константы TAG
    private ArrayList<Question> questions;
    private CountDownTimer mCT; // переменная для таймера обратного отсчета
    private String mReason = "";
    private Resources myResources;
    private Drawable mImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called"); // Включение команды регистрации сообщения
        setContentView(R.layout.activity_quiz);

        initializeQuestions();
        initializeViews();

        mScoreTextView.setText(String.valueOf(mScore)); // выводим нулевой счёт в начале игры
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

    // метод для обновления вопросов
    private void updateQuestion(){
        // если вопросы в массиве еще остались
        if (questions.size() != 0){
            mCurrentIndex = mRandom.nextInt(questions.size()); // выбираем рандомный индекс вопроса
            int question = questions.get(mCurrentIndex).getTextResId(); // переменной присваиваем id вопроса
            mQuestionTextView.setText(question); // устанавливаем в переменную mQuestionTextView вопрос согласно id
            // если таймер работает, то включаем. Актуально только при запуске приложения
            if (mCT != null) {
                mCT.cancel();
            }
            setTimer(); // запуск таймера на вопрос
        }
        // если вопросов больше нет
        else {
            mCT.cancel(); // останавливаем таймер
            mReason = "Поздравляем, Вы прошли все вопросы!"; // заголовок диалогового окна
            mImage = myResources.getDrawable(R.drawable.win); // картина диалогового окна
            gameOverDialog(); // вызов диалога
        }
    }

    // иницализация вьюшек
    private void initializeViews() {
        mTrueButton = (Button) findViewById(R.id.true_button); // связываем переменную с id кнопки
        mFalseButton = (Button) findViewById(R.id.false_button); // связываем переменную с id кнопки
        mScoreTextView = (TextView)findViewById(R.id.score_textwiew); // связываем переменную c текствью (счёт)
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view); // связываем переменную с id вопроса
        mTimer = (TextView) findViewById(R.id.timer);
        myResources = getResources();
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
    }

    // метод для проверки правильности ответа
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questions.get(mCurrentIndex).isAnswerTrue(); // создаем лог. переменную answerIsTrue, присваиваем ей занчение из массива
        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) { // если ответ верен, то пишем что правильно
                messageResId = R.string.correct_toast;
                mScore ++; // увличиваем счётчик правильных ответов на 1 если ответ верен
                mScoreTextView.setText(String.valueOf(mScore)); // увеличиваем счётчик правильных ответов
            } else { // если ответ неверен, то пишем что неправильно
                messageResId = R.string.incorrect_toast;
            }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show(); // вывод уведомления
        questions.remove(mCurrentIndex);
    }

    // метод для таймера обратного отсчёта
    public void setTimer() {
        mCT = new CountDownTimer(11000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimer.setText("Время: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                mReason = "Вермя вышло :(";
                mImage = myResources.getDrawable(R.drawable.game_over);
                gameOverDialog(); // вызываем диалоговое окно про конец игры
            }
        }.start();
    }

    // диалоговое окно, которое сообщает о конце игры
    public void gameOverDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle(mReason) // заголовок
                .setMessage("Вы дали: " + mScore + " верных ответов") // сообщение
                .setIcon(mImage)  //каринка
                .setCancelable(false) // можно закрыть только по кнопке
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() { // по нажатии ...
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // закрыть диалог
                                finish();   // закрыть активити
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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
}
