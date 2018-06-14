package com.example.danish.crackit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SliderActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private int mCurrentPage;

    private SliderAdapter mSliderAdapter;

    private Button mNext;
    private Button mPrev;

    private View mAddBanner;

    private TextView[] mDots;

    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // checking if user is using app for 1st time or not
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        String s = preferences.getString("isFirstTimeLaunch", "true");

        if(s.equals("true")){
            thisActivity();
        }else if(s.equals("false")){
            mainActivityIntent();
            finish();
        }else{
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }

    }

    private void thisActivity(){

        mAddBanner = (View)findViewById(R.id.addUnit_slider_activity);
        mAddBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(getResources().getString(R.string.add_url)));
                startActivity(webIntent);
            }
        });

        mSlideViewPager = (ViewPager)findViewById(R.id.viewPager);
        mDotLayout = (LinearLayout)findViewById(R.id.dotsLayout);

        mNext = (Button)findViewById(R.id.nextBtn);
        mPrev = (Button)findViewById(R.id.prevBtn);

        mSliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(mSliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNext.getText().toString().trim().equals("Finish")){
                    (preferences.edit()).putString("isFirstTimeLaunch", "false").apply();
                    mainActivityIntent();
                    finish();
                }
                mSlideViewPager.setCurrentItem(mCurrentPage+1);

            }
        });

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
    }

    private void mainActivityIntent(){
        Intent mainActivity = new Intent(SliderActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    private void addDotsIndicator(int position){
        mDots = new TextView[2];
        mDotLayout.removeAllViews();
        for(int i=0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if(position == 0){
                mNext.setEnabled(true);
                mPrev.setEnabled(false);
                mPrev.setVisibility(View.INVISIBLE);
                mNext.setText("Next");
                mPrev.setText("");
            }else{
                mNext.setEnabled(true);
                mPrev.setEnabled(true);
                mPrev.setVisibility(View.VISIBLE);
                mNext.setText("Finish");
                mPrev.setText("Back");
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


}
