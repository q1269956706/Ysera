package com.example.evilsay.wechatson.Fragment;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.example.evilsay.wechatson.Adapter.ChapterChatAdapter;
import com.example.evilsay.wechatson.Beans.ChapterChat;

import com.example.evilsay.wechatson.Beans.ChapterChatItem;;
import com.example.evilsay.wechatson.PLZ.AddressPlz;
import com.example.evilsay.wechatson.R;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentAddress extends android.support.v4.app.Fragment{
    private static final String TAG = "FragmentAddress" ;
    private AddressPlz chatplz = new AddressPlz();
    private ExpandableListView listView;
    private ChapterChatAdapter adapter;
    private ContentResolver resolver;
    private List<ChapterChat> chapterChats = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_address,container,false);
        InitDate(view);
        ListenerItem();
        InitData();
        return view;
    }
    private void InitData() {
        chatplz.loadFromDb(getActivity(), new AddressPlz.Callback() {
            @Override
            public void onSuccess(List<ChapterChat> chapterChatList) {
                chapterChats.addAll(chapterChatList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception ex) {
                ex.printStackTrace();
            }
        },true);
    }

    private void InitDate(View view) {
        listView = view.findViewById(R.id.expandable_listView);
        chapterChats.clear();
        adapter  = new ChapterChatAdapter(chapterChats, getActivity());
        listView.setAdapter(adapter);
        resolver = Objects.requireNonNull(getActivity()).getContentResolver();
    }


    private void ShowUserUI(Integer user,String ShowName){
            new SweetAlertDialog(getActivity())
                    .setTitleText("用户详细信息")
                    .setContentText(ShowName+"的ID为"+user)
                    .show();
    }

    private void ContentProvider(int childPosition,int groupPosition){
        ChapterChatItem item = chapterChats.get(groupPosition).getChapterChatItems().get(childPosition);
        ContentValues values = new ContentValues();
        Uri insert = resolver.insert(Uri.parse("content://com.example.expandablelistview/helloworld?name=你爸爸"), new ContentValues());
        long parseId = ContentUris.parseId(insert);
        Toast.makeText(getActivity(), "添加item成功,ItemId为"+parseId,Toast.LENGTH_SHORT).show();
    }

    private void ListenerItem(){
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ChapterChatItem chapterChatItem = chapterChats.get(groupPosition).getChapterChatItems().get(childPosition);
                ShowUserUI(chapterChatItem.getId(),chapterChatItem.getName());
//                ContentProvider(childPosition,groupPosition);
//                if (childPosition == 2){
//                    resolver.delete(Uri.parse("content://com.example.expandablelistview"),null,null);
//                    Log.e(TAG, "onChildClick: "+"删除数据库成功" );
//                }
                return false;
            }
        });
    }
}
