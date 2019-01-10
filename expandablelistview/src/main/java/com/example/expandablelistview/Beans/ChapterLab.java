package com.example.expandablelistview.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟添加数据
 */
public class ChapterLab {
    public static List<Chapter> MockData(){
        List<Chapter> datas = new ArrayList<>();
        Chapter chapter = new Chapter(1,"android");
        Chapter chapter1 = new Chapter(2,"android");
        Chapter chapter2 = new Chapter(3,"android");

        chapter.addChild(1,"Android Child1");
        chapter.addChild(2,"androidText");
        chapter.addChild(3,"androidText");

        chapter1.addChild(4,"PythonTest1");
        chapter1.addChild(5,"PythonTest2");
        chapter1.addChild(6,"PythonTest3");

        chapter2.addChild(7,"C++Test1");
        chapter2.addChild(8,"C++Test2");
        chapter2.addChild(9,"C++Test3");

        datas.add(chapter);
        datas.add(chapter1);
        datas.add(chapter2);
        return datas;
    }
}
