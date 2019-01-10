package com.example.evilsay.wechatson.Model;



public class ChatResult{
    private String chat;
    private String time;
    private String name;
    public static final String TABLE_NAME = "ChatBackUP";
    public static final String NAME       = "name";
    public static final String TIME       = "time";
    public static final String CHAT       = "chat";
    public static final String ID         = "_id";
    public static final String ID_NAME    = "Number";
    public void setChat(String chat) {
        this.chat = chat;
    }
    public String getChat() {
        return chat;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
