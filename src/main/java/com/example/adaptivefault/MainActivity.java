package com.example.adaptivefault;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener,Chat_Fragment.OnFragmentInteractionListener,Introduce_Fragment.OnFragmentInteractionListener {

    private ImageView icon;
    private Button go;
    private TextView error;
    private SpinKitView wait;
    private EditText editText = null;
    private ConstraintLayout root;
    private ListView listView;
    private Error[] errors;
    private Map<String,Error> errorMap = new HashMap<>();
    //private HistoryFragment fragment;
    private FragmentManager manager;
    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private HistoryFragment historyFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //manager.beginTransaction().hide(fragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    //manager.beginTransaction().show(fragment).commit();
                    return true;
            }
            return false;
        }
    };
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightMode();
        //requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        setContentView(R.layout.activity_main5);
        checkReadPermission();
        manager = getSupportFragmentManager();
        viewPager = findViewById(R.id.fragmentviewpager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Chat_Fragment());
        historyFragment = new HistoryFragment();
        fragmentList.add(historyFragment);
        //fragmentList.add(new HistoryFragment());
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(manager, fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //navigation = findViewById(R.id.navigation);
        error = findViewById(R.id.error);
        go = findViewById(R.id.go);
        wait = findViewById(R.id.wait);
        icon = findViewById(R.id.icon);
        root = findViewById(R.id.root);
        //root.setVisibility(View.INVISIBLE);
        listView = findViewById(R.id.listview);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //设置拉出布局的宽度
                // linearLayout.setX(slideOffset * drawerView.getWidth());
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //  Log.e(TAG, "onDrawerSlide: " + "完全展开时执行");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Log.e(TAG, "onDrawerSlide: " + "完全关闭时执行");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //  Log.e(TAG, "onDrawerSlide: " + "改变状态时时执行");
            }
        });
        this.setRoot();
        this.setGoButton();
        this.setErrorTextView();
        //this.updateListView();
        this.setListView();
    }

    public boolean checkReadPermission() {
        String[] PERMISSION_AUDIO = {Manifest.permission.RECORD_AUDIO};
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, PERMISSION_AUDIO, 1);
        }
        return flag;
    }

    private void setListView(){
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (String)listView.getItemAtPosition(position);
                errorMap.get(text);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, localError_Activity.class);
                intent.putExtra("error",errorMap.get(text));
                startActivity(intent);
            }
        });
    }

    private void setErrorTextView(){
        error.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            final float height = icon.getLayoutParams().height;
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ValueAnimator anim = ValueAnimator.ofFloat(height, 0);
                    anim.setDuration(500);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float currentValue = (float) animation.getAnimatedValue();
                            //System.out.println(currentValue);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(icon.getLayoutParams());
                            layoutParams.height = (int) currentValue;
                            icon.setLayoutParams(layoutParams);
                        }
                    });
                    anim.start();
                } else {
                    ValueAnimator anim = ValueAnimator.ofFloat(0, height);
                    anim.setDuration(500);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float currentValue = (float) animation.getAnimatedValue();
                            //System.out.println(currentValue);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(icon.getLayoutParams());
                            layoutParams.height = (int) currentValue;
                            icon.setLayoutParams(layoutParams);
                        }
                    });
                    anim.start();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //icon.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setRoot(){
        root.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                root.setFocusable(true);
                root.setFocusableInTouchMode(true);
                root.requestFocus();
                return false;
            }
        });//焦点
    }

    private void setGoButton(){
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘
                if(!MainActivity.this.error.getText().toString().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, ShowSolution_Activity.class);
                            MainActivity.this.load();
                            Solution[] solutions = Network.ask(new Error(error.getText().toString(), null));
                            MainActivity.this.finish();
                            intent.putExtra("solutions", solutions);
                            intent.putExtra("error", error.getText().toString());
                            startActivity(intent);
                        }
                    }).start();
                }else{
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateListView(){
        //errors = Error.getFromLocal(this);
        for(Error error:errors){
            if(error != null)
            errorMap.put(error.getError(),error);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, (String[])errorMap.keySet().toArray(new String[0]));
        listView.setAdapter(adapter);
    }

    @Override
    public void onRestart(){
        //this.updateListView();
        super.onRestart();
    }

    public void load() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.wait.setVisibility(View.VISIBLE);
            }
        });
    }

    public void finish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.wait.setVisibility(View.INVISIBLE);
            }
        });
    }

    public String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    hideInput();
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
    */

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println("hello");
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        MyFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
    private void setLightMode() {//设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.VChatWhite));

            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    public void addError(Error error){
        historyFragment.addNewError(error);
    }
}
