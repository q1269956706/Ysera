package com.example.evilsay.wechatson.Beans;


import java.util.ArrayList;
import java.util.List;


public class ChatLab {
    public static List<ChapterChat> markData(String name){
        List<ChapterChat> chapterChats = new ArrayList<>();
        ChapterChat chapterChat = new ChapterChat(1,"群聊成员");
        chapterChat.addChild(1,name);
        chapterChats.add(chapterChat);
        return chapterChats;
    }
}
