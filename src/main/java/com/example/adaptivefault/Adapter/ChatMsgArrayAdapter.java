package com.example.adaptivefault.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adaptivefault.R;
import com.example.adaptivefault.util.ChatMsg;
import java.util.List;


public class ChatMsgArrayAdapter extends ArrayAdapter<ChatMsg> {
    static int number = 0;
    private LayoutInflater inflater;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private List<ChatMsg> chatMsgs;
    public ChatMsgArrayAdapter(@NonNull Context context, @LayoutRes int resource, List<ChatMsg> chatMsgs) {
        super(context, resource);
        this.inflater = LayoutInflater.from(context);
        this.chatMsgs = chatMsgs;
    }
    @Override
    public int getCount() {
        return chatMsgs.size();
    }
    @Nullable
    @Override
    public ChatMsg getItem(int position) {
        return chatMsgs.get(position);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMsg msg = getItem(position);
        final View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            assert msg != null;
            if (msg.isMyInfo()) {
                view = inflater.inflate(R.layout.chat_me2, parent, false);
            } else {
                view = inflater.inflate(R.layout.chat_other, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.icon =  view.findViewById(R.id.icon);
            viewHolder.username =  view.findViewById(R.id.username);
            viewHolder.content = view.findViewById(R.id.content);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageResource(chatMsgs.get(position).getIconID());
        viewHolder.username.setText(chatMsgs.get(position).getUsrname());
        viewHolder.content.setText(chatMsgs.get(position).getContent());
        number++;
        Log.d("number",""+number);
        return view;
    }
    private class ViewHolder {
        ImageView icon;
        TextView username;
        TextView content;
    }
}
