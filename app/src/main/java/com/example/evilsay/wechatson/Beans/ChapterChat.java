package com.example.evilsay.wechatson.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 父类 一对多
 */
public class ChapterChat {
    private Integer id;
    private String name;
    public static final String TABLE_NAME = "tb_chapter";
    public static final String COD_NAME = "name";
    public static final String COD_ID = "_id";
    public List<ChapterChatItem> chapterChatItems = new ArrayList<>();

    public ChapterChat(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    //    添加数据源对象
    public void addChild(ChapterChatItem chapterChatItem) {
        chapterChatItem.setPid(getId());
        chapterChatItems.add(chapterChatItem);
    }

    //    指定数据值
    public void addChild(Integer id, String name) {
        ChapterChatItem chapterChatItem = new ChapterChatItem(id, name);
        chapterChatItem.setPid(getId());
        chapterChatItems.add(chapterChatItem);
    }

    public ChapterChat() {

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

    public List<ChapterChatItem> getChapterChatItems() {
        return chapterChatItems;
    }

    public void setChapterChatItems(List<ChapterChatItem> chapterChatItems) {
        this.chapterChatItems = chapterChatItems;
    }
}
