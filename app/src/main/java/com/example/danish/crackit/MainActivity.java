package com.example.danish.crackit;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Question>>{

    private static final String url = "https://learncodeonline.in/api/android/datastructure.json";


    private static final int QUESTION_LOADER_ID = 101;

    private ProgressDialog mProgressDialog;


    private RecyclerView mRecyclerView;

    private TextView mEmptyTextView;

    private QuestionListAdapter mAdapter;

    private View mAddBanner;

    private SwipeRefreshLayout mSwipeRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoaderManager loaderManager = getLoaderManager();

        mAddBanner = (View)findViewById(R.id.addUnit);
        mAddBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(getResources().getString(R.string.add_url)));
                startActivity(webIntent);
            }
        });

        mEmptyTextView = (TextView)findViewById(R.id.empty_view);
        mEmptyTextView.setText("No Internet Connection");


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Loading Questions");
        mProgressDialog.setCanceledOnTouchOutside(false);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_list_item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new QuestionListAdapter(this, new ArrayList<Question>());

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mEmptyTextView.getVisibility() == View.GONE && !hasInternet()){
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyTextView.setVisibility(View.VISIBLE);
                }
                if(mEmptyTextView.getVisibility() == View.VISIBLE && hasInternet()){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyTextView.setVisibility(View.GONE);
                }
                mProgressDialog.show();
                loaderManager.restartLoader(QUESTION_LOADER_ID, null, MainActivity.this);
                mSwipeRefresh.setRefreshing(false);
            }
        });



        if(hasInternet()){
            mEmptyTextView.setVisibility(View.GONE);
            if(loaderManager.getLoader(QUESTION_LOADER_ID) == null){
                mProgressDialog.show();
                loaderManager.initLoader(QUESTION_LOADER_ID, null, this);
            }else {
                loaderManager.restartLoader(QUESTION_LOADER_ID, null, this);
            }

        }else{
            mEmptyTextView.setVisibility(View.VISIBLE);
            Toast("No internet connection");
        }

    }




    @Override
    public android.content.Loader<List<Question>> onCreateLoader(int id, Bundle args) {
        return new QuestionsLoader(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Question>> loader, List<Question> data) {
        mAdapter.clear();
        mProgressDialog.dismiss();
        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Question>> loader) {
        mAdapter.clear();
    }

    private boolean hasInternet(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }

    private void Toast(String s){
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }


}
