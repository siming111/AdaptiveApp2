package com.example.adaptivefault;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

    private ImageView icon;
    private Button go;
    private TextView error;
    private SpinKitView wait;
    private FloatingActionButton floatingActionButton;
    private EditText editText = null;
    private ConstraintLayout root;
    private ListView listView;
    private Error[] errors;
    private Map<String,Error> errorMap = new HashMap<>();
    //private BlankFragment fragment;
    private FragmentManager manager;
    private BottomNavigationView navigation;
    private ViewPager viewPager;
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
                case R.id.navigation_notifications:
                    //manager.beginTransaction().hide(fragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        viewPager = findViewById(R.id.fragmentviewpager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BlankFragment());
        fragmentList.add(new BlankFragment());
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
        navigation = findViewById(R.id.navigation);
        error = findViewById(R.id.error);
        go = findViewById(R.id.go);
        wait = findViewById(R.id.wait);
        icon = findViewById(R.id.icon);
        root = findViewById(R.id.root);
        listView = findViewById(R.id.listview);
        this.setRoot();
        this.setFloatingActionButton();
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

    private void setFloatingActionButton(){
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.checkReadPermission();
                SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID + "=5ce8d0be");
                final RecognizerDialog rd = new RecognizerDialog(MainActivity.this, null);
                //设置参数accent,language等参数
                rd.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
                rd.setParameter(SpeechConstant.ACCENT, "mandarin");//普通话
                //设置回调接口
                rd.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        //获取返回结果
                        rd.dismiss();
                        String result = parseIatResult(recognizerResult.getResultString());
                        if(!result.equals("。"))
                            error.setText(result);
                        //Log.e("result",result);
                        //Log.e("b",b+"");
                    }

                    @Override
                    public void onError(SpeechError speechError) {

                    }
                });
                //显示对话框
                rd.show();
            }
        });
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

    public void setIcon() {
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
}
