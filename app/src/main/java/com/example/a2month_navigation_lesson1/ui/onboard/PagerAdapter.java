package com.example.a2month_navigation_lesson1.ui.onboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.a2month_navigation_lesson1.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    public  interface OnStartClickListener{
        void onClick();
    }
    private  OnStartClickListener onStartClickListener;

    public  void setOnStartClickListener(OnStartClickListener onStartClickListener){
        this.onStartClickListener=onStartClickListener;
    }

    private String[] titles=new String[] {"Hello","How are you","I'm fine thanks"};

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
View view= LayoutInflater.from(container.getContext()).inflate(R.layout.papge_onboard,container,false);

        TextView textTitle=view.findViewById(R.id.textTitle);
        textTitle.setText(titles[position]);
        Button btnStart=view.findViewById(R.id.btnStart);
        if(position==2){
            btnStart.setVisibility(View.VISIBLE);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartClickListener.onClick();
                }
            });
        }
        else{
            btnStart.setVisibility(View.GONE);
        }
        container.addView(view);

        return view;// kak onViewCreatedHolder
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
        // super.destroyItem(container, position, object);
    }
}
