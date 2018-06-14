package com.example.danish.crackit;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class QuestionsLoader extends AsyncTaskLoader<List<Question>> {

    private static final String LOG_TAG = QuestionsLoader.class.getName();

    private String mUrl;
    private Context mC;

    public QuestionsLoader(Context context, String url) {
        super(context);
        mUrl = url;
        mC = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Question> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<Question> questions = QueryUtils.fetchQuestions(mUrl);

        return questions;
    }
}
