package com.example.adaptivefault.com.afei.bottomtabbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adaptivefault.Adapter.ChatMsgArrayAdapter;
import com.example.adaptivefault.sidebar.ISideBarSelectCallBack;
import com.example.adaptivefault.sidebar.SideBar;
import com.example.adaptivefault.util.ChatMsg;
import com.example.adaptivefault.view.TitleBar;

import java.util.ArrayList;
import java.util.List;
import com.example.adaptivefault.R;

public class At_ChatRoom extends AppCompatActivity {
    private Button unsatisfied;
    private Button voice;
    private Button clear;
    private String[] letters = new String[]{"A","B","C","D","E","1","2","3","4","5"};
    private String[] ans = new String[10];
    private int[] position ;
    private SideBar sideBar;
    private TitleBar titleBar;
    private ListView listView;
    private EditText myMsg;
    private Button btnSend;
    private List<ChatMsg> chatMsgList;
    private ChatMsgArrayAdapter chatMsgListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.chat_room);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();
    }
    private void foundSolution(){
        for(int i = 0;i<5;i++){
            ans[i] = "this is the solution:\n solution     "+ i;
        }
    }
    private void foundSolution_python(){
        for(int i = 5;i<10;i++){
            ans[i] = "this is the python solution:\n      "+ i ;
        }
    }
    private void send(String content){
            if (!content.isEmpty()) {
                final ChatMsg msg = new ChatMsg();
                msg.setContent(content);
                msg.setUsrname("hello");
                msg.setIconID(R.drawable.avastertony);
                msg.setMyInfo(false);
                chatMsgList.add(msg);
                chatMsgListAdapter.notifyDataSetChanged();
            }
    }
    private void initViews() {
        unsatisfied = findViewById(R.id.At_satisfied);
        clear = findViewById(R.id.At_clear);
        voice = findViewById(R.id.At_voice);
        sideBar =  findViewById(R.id.side_bar);
        listView =  findViewById(R.id.lv_chat_room);
        myMsg =  findViewById(R.id.myMsg);
        btnSend =  findViewById(R.id.btnSend);
        chatMsgList = new ArrayList<>();
        chatMsgListAdapter = new ChatMsgArrayAdapter(At_ChatRoom.this, R.layout.chat_other, chatMsgList);
        listView.setAdapter(chatMsgListAdapter);
        String[] a = new String[]{"A","B","C","D","E","1","2","3","4","7"};
        sideBar.setDataResource(a);
        unsatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = "正在为你搜索各大论坛";
                send(context);
                foundSolution_python();
                for(int i = 5;i < 10;i++){
                    send(ans[i]);
                    position[i] = chatMsgListAdapter.getCount() - 1; }
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = "正在为您清除数据";
                send(context);
                position = null;
                chatMsgList.removeAll(chatMsgList);
                chatMsgListAdapter.notifyDataSetChanged();
                listView.setAdapter(chatMsgListAdapter);
            }
        });
        sideBar.setVisibility(View.INVISIBLE);
        sideBar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                if(position == null){
                    return;
                }
                for(int i = 0;i<10;i++){
                    if(selectStr.equals(letters[i])){
                        listView.setSelection(position[i]);
                    }
                }
                if(selectStr.equals("#")){
                    /*String content = "WE TRY TO KEEPINGWORKING";
                    if (!content.isEmpty()) {
                        ChatMsg msg = new ChatMsg();
                        msg.setContent(content);
                        msg.setUsrname("hello");
                        msg.setIconID(R.drawable.avasterwe);
                        msg.setMyInfo(false);
                        chatMsgList.add(msg);
                        chatMsgListAdapter.notifyDataSetChanged();
                        //myMsg.setText("");
                    }*/
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sideBar.setVisibility(View.VISIBLE);
                String content = myMsg.getText().toString();
                if (!content.isEmpty()) {
                    ChatMsg msg = new ChatMsg();
                    msg.setContent(content);
                    msg.setUsrname("hello");
                    msg.setIconID(R.drawable.avasterdandelion);
                    msg.setMyInfo(true);
                    chatMsgList.add(msg);
                    chatMsgListAdapter.notifyDataSetChanged();
                    myMsg.setText("");
                }
                String content2 = "we are finding the solution";
                send(content2);
                foundSolution();
                position = new int[10];
                for(int i = 0;i < 5;i++){
                    send(ans[i]);
                    position[i] = chatMsgListAdapter.getCount() - 1; }
                String content3 = "已在数据库为你列出5种解决方法,如满意请长按你满意的答案，按不满意" +
                        "我们将从各大论坛上面搜寻资料";
                send(content3);
            }
        });
        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }
            @Override
            public void rightButtonClick() { }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(At_ChatRoom.this,"点赞成功",Toast.LENGTH_SHORT).show();
                TextView topic = view.findViewById(R.id.content);
                String ans_good = topic.getText().toString();
                //send(ans);
                return true;
            }
        });
    }
}