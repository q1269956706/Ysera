package com.example.expandablelistview.Beans;

/**
 * 子条目 一对一
 */
public class ChapterItem {
     private int id;
     private String name;
     private int pid;
     public static final String TABLE_NAME = "tb_chapter_item";
     public static final String COL_ID     = "_id";
     public static final String COL_NAME   = "name";
     public static final String COL_PID     = "pid";
    public ChapterItem() {
    }

    public ChapterItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() { return pid; }

    @Override
    public String toString() {
        return "ChapterItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                '}';
    }
}
