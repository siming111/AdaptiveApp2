package com.example.adaptivefault;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class More_Solutions_Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private Solution[] solutions;
    private String error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        viewPager = findViewById(R.id.viewPager);
        Intent intent = getIntent();
        Solution[] solutions = (Solution[]) intent.getSerializableExtra("solutions");
        this.error = intent.getStringExtra("error");
        List<View> list = new ArrayList<>();
        if(solutions != null) {
            for(int i = 0; i < solutions.length; i++) {
                View view = View.inflate(this, R.layout.solution_view, null);
                final TextView textView = view.findViewById(R.id.solution);
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                ImageButton imageButton = view.findViewById(R.id.imageButton);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Network.feelGood(More_Solutions_Activity.this.error, textView.getText().toString());
                            }
                        }).start();
                        Error error =new Error(More_Solutions_Activity.this.error, textView.getText().toString());
                        error.saveInLocal(More_Solutions_Activity.this);
                        Toast.makeText(More_Solutions_Activity.this,"收藏成功！",Toast.LENGTH_LONG).show();
                    }
                });
                textView.setText(solutions[i].getSolution());
                list.add(view);
            }
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(list);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(ShowSolution_Activity.this,"" + position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {

        List<View> list;

        public MyPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }
}
