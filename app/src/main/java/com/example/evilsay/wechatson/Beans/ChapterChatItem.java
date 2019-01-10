package com.example.evilsay.wechatson.Beans;

public class ChapterChatItem {
    private Integer id;
    private String name;
    private Integer pid;
    public static final String TABLE_NAME = "tb_item";
    public static final String COD_NAME = "name";
    public static final String COD_PID = "pid";
    public static final String COD_ID = "_id";

    public ChapterChatItem() {

    }

    public ChapterChatItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "ChapterChatItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                '}';
    }
}
