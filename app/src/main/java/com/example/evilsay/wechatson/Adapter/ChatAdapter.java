package com.example.evilsay.wechatson.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.evilsay.wechatson.Fragment.FragmentChat;
import com.example.evilsay.wechatson.Model.ChatResult;
import com.example.evilsay.wechatson.R;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private String name;
    private FragmentChat fragmentChat;
    private List<ChatResult> chatResultArrayList;
    private Context context;
    public ChatAdapter(List<ChatResult> chatResultArrayList, Context context,FragmentChat fragmentChat,String name) {
        this.chatResultArrayList = chatResultArrayList;
        this.context = context;
        this.fragmentChat = fragmentChat;
        this.name   = name;
    }
    public void setData(List<ChatResult> chatResultArrayList){
        this.chatResultArrayList = chatResultArrayList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return chatResultArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatResultArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (name.equals(chatResultArrayList.get(position).getName())){
            convertView = View.inflate(context,R.layout.chatting_item_msg_text_left,null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.user_text = convertView.findViewById(R.id.tv_username);
            viewHolder.time_text = convertView.findViewById(R.id.tv_send_time);
            viewHolder.chat      = convertView.findViewById(R.id.tv_chat_content);
            viewHolder.user_text.setText(chatResultArrayList.get(position).getName());
            viewHolder.time_text.setText(chatResultArrayList.get(position).getTime());
            viewHolder.chat.setText(chatResultArrayList.get(position).getChat());
        }else {
            convertView = View.inflate(context,R.layout.chatting_item_msg_text_right,null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.user_text = convertView.findViewById(R.id.tv_username);
            viewHolder.time_text = convertView.findViewById(R.id.tv_send_time);
            viewHolder.chat      = convertView.findViewById(R.id.tv_chat_content);
            viewHolder.user_text.setText(chatResultArrayList.get(position).getName());
            viewHolder.time_text.setText(chatResultArrayList.get(position).getTime());
            viewHolder.chat.setText(chatResultArrayList.get(position).getChat());
        }
        return convertView;
    }


    class ViewHolder{
        TextView  user_text,time_text,chat;
    }
}
