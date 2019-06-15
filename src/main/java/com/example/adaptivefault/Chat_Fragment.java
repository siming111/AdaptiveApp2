package com.example.adaptivefault;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adaptivefault.Adapter.ChatMsgArrayAdapter;
import com.example.adaptivefault.com.afei.bottomtabbar.At_ChatRoom;
import com.example.adaptivefault.sidebar.ISideBarSelectCallBack;
import com.example.adaptivefault.sidebar.SideBar;
import com.example.adaptivefault.util.ChatMsg;
import com.example.adaptivefault.view.TitleBar;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Chat_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Chat_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //private Button unsatisfied;
    //private Button voice;
    //private Button clear;
    private String[] letters = new String[]{"A", "B", "C", "D", "E", "1", "2", "3", "4", "5"};
    private String[] ans = new String[10];
    private int[] position;
    private SideBar sideBar;
    private ListView listView;
    private EditText myMsg;
    private Button btnSend;
    private List<ChatMsg> chatMsgList;
    private ChatMsgArrayAdapter chatMsgListAdapter;
    private String currentError = null;
    private ImageView voiceImageView;
    public Chat_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chat_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Chat_Fragment newInstance(String param1, String param2) {
        Chat_Fragment fragment = new Chat_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getContext()).getSupportActionBar().hide();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_room, container, false);
        initViews(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void foundSolution(String error) {
        Solution[] solutions = Network.ask(new Error(error,null));
        for (int i = 0; i < 5; i++) {
            ans[i] = solutions[i].getSolution();
        }
    }

    private void foundSolution_python(String error) {
        Solution[] solutions = Network.ask2(new Error(error,null));
        for (int i = 5; i < 10; i++) {
            ans[i] = solutions[i-5].getSolution();
        }
    }

    private void send(String content) {
        if (!content.isEmpty()) {
            ChatMsg msg = new ChatMsg();
            msg.setContent(content);
            msg.setUsrname("hello");
            msg.setIconID(R.drawable.avastertony);
            msg.setMyInfo(false);
            chatMsgList.add(msg);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatMsgListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void initViews(View view) {
        //unsatisfied = view.findViewById(R.id.At_satisfied);
        //clear = view.findViewById(R.id.At_clear);
        //voice = view.findViewById(R.id.At_voice);
        sideBar = view.findViewById(R.id.side_bar);
        listView = view.findViewById(R.id.lv_chat_room);
        myMsg = view.findViewById(R.id.myMsg);
        btnSend = view.findViewById(R.id.btnSend);
        voiceImageView = view.findViewById(R.id.voiceImageView);
        chatMsgList = new ArrayList<>();
        chatMsgListAdapter = new ChatMsgArrayAdapter(getContext(), R.layout.chat_other, chatMsgList);
        listView.setAdapter(chatMsgListAdapter);
        String[] a = new String[]{"A", "B", "C", "D", "E", "1", "2", "3", "4", "7"};
        sideBar.setDataResource(a);
        voiceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Chat_Fragment.this.checkReadPermission();
                SpeechUtility.createUtility(getActivity(), SpeechConstant.APPID + "=5ce8d0be");
                final RecognizerDialog rd = new RecognizerDialog(getActivity(), null);
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
                            myMsg.setText(result);
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
        });/*
        unsatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findMoreSolution();
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
                clearListView();
            }
        });
        */
        sideBar.setVisibility(View.INVISIBLE);
        sideBar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                if (position == null) {
                    return;
                }
                for (int i = 0; i < 10; i++) {
                    if (selectStr.equals(letters[i])) {
                        listView.setSelection(position[i]);
                    }
                }
                if (selectStr.equals("#")) {
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
                clearListView();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘
                sideBar.setVisibility(View.VISIBLE);
                String content = myMsg.getText().toString();
                currentError = content;
                if (!content.isEmpty()) {
                    ChatMsg msg = new ChatMsg();
                    msg.setContent(content);
                    //msg.setUsrname("hello");
                    msg.setIconID(R.drawable.avasterdandelion);
                    msg.setMyInfo(true);
                    chatMsgList.add(msg);
                    chatMsgListAdapter.notifyDataSetChanged();
                    myMsg.setText("");
                }
                String content2 = "正在搜索合适的解决方案";
                send(content2);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        foundSolution(currentError);
                        position = new int[10];
                        for (int i = 0; i < 5; i++) {
                            send(ans[i]);
                            position[i] = chatMsgListAdapter.getCount() - 1;
                        }
                        String content3 = "已在数据库为你列出5种解决方法,如满意请长按你满意的答案，按不满意" +
                                "我们将从各大论坛上面搜寻资料";
                        ChatMsg msg = new ChatMsg();
                        msg.setContent(content3);
                        msg.setUsrname("hello");
                        msg.setIconID(R.drawable.avastertony);
                        msg.setMyInfo(false);
                        msg.chat_fragment = Chat_Fragment.this;
                        chatMsgList.add(msg);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatMsgListAdapter.notifyDataSetChanged();
                                //listView.setSelection(position[1]);
                                //listView.setSelection(position[2]);
                            }
                        });

                    }
                }).start();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView topic = view.findViewById(R.id.content);
                String ans_good = topic.getText().toString();
                Log.d("ans_good",ans_good);
                final Error localError = new Error(currentError,ans_good);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Network.feelGood(localError.getError(),localError.getSolution());
                    }
                }).start();
                localError.saveInLocal(getContext());
                Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        send("欢迎来到自适应故障APP");
    }

    public boolean checkReadPermission() {
        String[] PERMISSION_AUDIO = {Manifest.permission.RECORD_AUDIO};
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(getActivity(), PERMISSION_AUDIO, 1);
        }
        return flag;
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

    private void clearListView() {
        String context = "正在为您清除数据";
        send(context);
        position = null;
        chatMsgList.removeAll(chatMsgList);
        chatMsgListAdapter.notifyDataSetChanged();
        listView.setAdapter(chatMsgListAdapter);
    }

    public void findMoreSolution(){
        String context = "正在为你搜索各大论坛";
        send(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                foundSolution_python(currentError);
                for (int i = 5; i < 10; i++) {
                    send(ans[i]);
                    position[i] = chatMsgListAdapter.getCount() - 1;
                }
            }
        }).start();
    }
}
