package com.example.adaptivefault;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adaptivefault.Adapter.ChatMsgArrayAdapter;
import com.example.adaptivefault.com.afei.bottomtabbar.At_ChatRoom;
import com.example.adaptivefault.sidebar.ISideBarSelectCallBack;
import com.example.adaptivefault.sidebar.SideBar;
import com.example.adaptivefault.util.ChatMsg;
import com.example.adaptivefault.view.TitleBar;

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

    private Button unsatisfied;
    private Button voice;
    private Button clear;
    private String[] letters = new String[]{"A","B","C","D","E","1","2","3","4","5"};
    private String[] ans = new String[10];
    private int[] position ;
    private SideBar sideBar;
    private ListView listView;
    private EditText myMsg;
    private Button btnSend;
    private List<ChatMsg> chatMsgList;
    private ChatMsgArrayAdapter chatMsgListAdapter;

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
        ((AppCompatActivity)getContext()).getSupportActionBar().hide();
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
            ChatMsg msg = new ChatMsg();
            msg.setContent(content);
            msg.setUsrname("hello");
            msg.setIconID(R.drawable.avastertony);
            msg.setMyInfo(false);
            chatMsgList.add(msg);
            chatMsgListAdapter.notifyDataSetChanged();
        }
    }
    private void initViews(View view) {
        unsatisfied = view.findViewById(R.id.At_satisfied);
        clear = view.findViewById(R.id.At_clear);
        voice = view.findViewById(R.id.At_voice);
        sideBar =  view.findViewById(R.id.side_bar);
        listView =  view.findViewById(R.id.lv_chat_room);
        myMsg =  view.findViewById(R.id.myMsg);
        btnSend =  view.findViewById(R.id.btnSend);
        chatMsgList = new ArrayList<>();
        chatMsgListAdapter = new ChatMsgArrayAdapter(getContext(), R.layout.chat_other, chatMsgList);
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
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),"点赞成功",Toast.LENGTH_SHORT).show();
                TextView topic = view.findViewById(R.id.content);
                String ans_good = topic.getText().toString();
                //send(ans);
                return true;
            }
        });
    }
}
