package com.example.danish.crackit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ConcurrentModificationException;

public class SliderAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mInflater;

    public String[] text = {"Welcome \n\n To the world of \n\n Learning", "Make yourself \n\n comfortable with \n\n Data Structure"};

    public SliderAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mInflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(R.layout.intro_slide_layout, container, false);

        TextView msg = (TextView)v.findViewById(R.id.intro_msg_textView);
        msg.setText(text[position]);

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
