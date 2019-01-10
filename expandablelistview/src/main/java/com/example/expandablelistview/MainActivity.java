package com.example.expandablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.expandablelistview.Beans.Chapter;
import com.example.expandablelistview.adapter.ChapterAdapter;
import com.example.expandablelistview.blz.ChapterBiz;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Chapter> mDatas = new ArrayList<>();
    public static  String TAG = "initEvents";
    private ExpandableListView listView;
    private ChapterBiz chapterBiz = new ChapterBiz();
    private BaseExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
        chapterBiz.loadData(MainActivity.this, new ChapterBiz.CallBack() {
            @Override
            public void onSuccess(List<Chapter> chapterList) {
                mDatas.addAll(chapterList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception ex) {

            }
        },true);
    }

    private void initEvents() {
//        子类监听器
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.e(TAG, "父类ID"+i+"子类ID"+i1);
                return false;
            }
        });
//        父类监听器
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Log.e(TAG, "groupPosition"+i);
                return false;
            }
        });
//        收缩时回调
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                Log.e(TAG, "GroupCollapse"+i);
            }
        });
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                Log.e(TAG, "GroupExpand"+i);
            }
        });
    }

    private void initView() {
        listView = findViewById(R.id.list_view);
        mDatas.clear();
        adapter = new ChapterAdapter(mDatas, this);
        listView.setAdapter(adapter);
    }
}
