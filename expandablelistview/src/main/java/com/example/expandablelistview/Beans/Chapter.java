package com.example.expandablelistview.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 组条目 一对多
 */
public class Chapter {
    private int id;
    private String name;
    //子条目数据源
    private List<ChapterItem> chapterItems = new ArrayList<>();
    public static final String TABLE_NAME = "tb_chapter";
    public static final String COL_ID     = "_id";
    public static final String COL_NAME   = "name";

    public Chapter() {
    }

    public Chapter(int id, String name) {
        this.id = id;
        this.name = name;
    }
//    数据源
    public void addChild (ChapterItem chapterItem){
        chapterItem.setPid(getId());
        chapterItems.add(chapterItem);
    }
//    数据源
    public void addChild(int cid, String name){
        ChapterItem chapterItem = new ChapterItem(cid,name);
        chapterItem.setPid(getId());
        chapterItems.add(chapterItem);

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChapterItem> getChapterItems() {
        return chapterItems;
    }

    public void setChapterItems(List<ChapterItem> chapterItems) {
        this.chapterItems = chapterItems;
    }
    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chapterItems=" + chapterItems +
                '}';
    }

}
