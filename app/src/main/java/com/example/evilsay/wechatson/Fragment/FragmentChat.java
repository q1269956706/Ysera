package com.example.evilsay.wechatson.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evilsay.wechatson.Adapter.ChatAdapter;
import com.example.evilsay.wechatson.Model.ChatResult;
import com.example.evilsay.wechatson.MainActivity;
import com.example.evilsay.wechatson.PLZ.ChatBlz;
import com.example.evilsay.wechatson.R;
import java.util.ArrayList;
import java.util.List;


public class FragmentChat extends android.support.v4.app.Fragment implements View.OnClickListener {
    private String user;
    private String name;
    protected ImageButton  WeChat_Start;
    protected EditText     WeChat_SendMessage;
    private ChatAdapter    chatAdapter;
    private TextView     weChat_history;
    private ListView       listView;
    private List<ChatResult> chatResults = new ArrayList<>();
    private ChatBlz chatBlz = new ChatBlz();
    private boolean settingNetWork = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        InitView(view);
        return view;
    }
    @SuppressLint("CutPasteId")
    private void InitView(View view) {
        WeChat_Start =  view.findViewById(R.id.search).findViewById(R.id.WeChat_Start);
        WeChat_SendMessage =  view.findViewById(R.id.search).findViewById(R.id.WeChat_SendMessage);
        weChat_history = view.findViewById(R.id.history);
        WeChat_Start.setOnClickListener(this);
        weChat_history.setOnClickListener(this);
        listView = view.findViewById(R.id.list_view_demo);
        chatAdapter = new ChatAdapter(chatResults,getActivity(),FragmentChat.this,name);
        listView.setAdapter(chatAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.WeChat_Start:
                CheckNull();
                break;
        }
    }
    private void CheckNull() {
        if (!WeChat_SendMessage.getText().toString().equals("")) {
            ButtonMagic();
            Asynchronous();
            WeChat_SendMessage.setText("");
        }else{
            Toast.makeText(getActivity(), "不能输入空的聊天信息", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        user = ((MainActivity) context).Date_User();
        name = ((MainActivity) context).Date_Name();
    }
    //显示跟隐藏，功能的更迭
    private void ButtonMagic(){
        if (settingNetWork){
            weChat_history.setText("本地记录");
        }else {
            weChat_history.setText("群聊界面");
        }
    }
    private void Asynchronous(){
        chatBlz.loadFromDb(getContext(), settingNetWork,user,WeChat_SendMessage.getText().toString(),new ChatBlz.Callback() {
            @Override
            public void OnSuccess(List<ChatResult> results) {
                chatResults.addAll(results);
                chatAdapter.setData(results);
                chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void UnSuccess(Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}

