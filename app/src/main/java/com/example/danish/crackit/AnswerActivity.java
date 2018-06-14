package com.example.danish.crackit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {

    private View mAddBanner;

    private TextView mQuestion;
    private TextView mAnswer;

    private String mQuestionString;
    private String mAnswerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        mQuestionString = getIntent().getStringExtra("question");
        mAnswerString = getIntent().getStringExtra("answer");



        mAddBanner = (View)findViewById(R.id.addUnit_answer_activity);
        mAddBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(getResources().getString(R.string.add_url)));
                startActivity(webIntent);
            }
        });


        mQuestion = (TextView)findViewById(R.id.question);
        mAnswer = (TextView)findViewById(R.id.answer);

        mQuestion.setText(mQuestionString);
        mAnswer.setText(mAnswerString);


    }


}
