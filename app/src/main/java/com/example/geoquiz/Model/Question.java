package com.example.geoquiz.Model;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private int mAnswerResId;

    public int getTextResId() {
        return mTextResId;
    }

    public int getAnswerResId() {
        return mAnswerResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Question(int textResId, boolean answerTrue, int AnswerResId) {
        mTextResId = textResId; // ссылка на строку вопроса
        mAnswerTrue = answerTrue; // правильный ответ правда/ложь
        mAnswerResId = AnswerResId;
    }
}
