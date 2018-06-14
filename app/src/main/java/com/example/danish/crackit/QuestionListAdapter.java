package com.example.danish.crackit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Question> mQuestions;

    public QuestionListAdapter(Context context, ArrayList<Question> questions) {
        mContext = context;
        mQuestions = questions;
    }

    public void addAll(List<Question> questions){
        mQuestions.addAll(questions);
    }

    public void clear(){
        mQuestions.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.question_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Question question = mQuestions.get(position);

        holder.mQuestion.setText(question.getmQuestion());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent answerIntent = new Intent(mContext, AnswerActivity.class);
                answerIntent.putExtra("question", question.getmQuestion());
                answerIntent.putExtra("answer", question.getmAnswer());
                mContext.startActivity(answerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView mQuestion;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mQuestion = (TextView)itemView.findViewById(R.id.question_textView);
        }
    }
}
