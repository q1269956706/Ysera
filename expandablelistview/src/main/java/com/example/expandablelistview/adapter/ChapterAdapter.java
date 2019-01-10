package com.example.expandablelistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.expandablelistview.Beans.Chapter;
import com.example.expandablelistview.Beans.ChapterItem;
import com.example.expandablelistview.R;

import java.util.List;

public class ChapterAdapter extends BaseExpandableListAdapter {
    private List<Chapter> mDatas;
    //对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflater()来载入；
    private LayoutInflater inflater;
    private Context mcontext;
    //    给它一个构造方法
    public ChapterAdapter(List<Chapter> datas, Context context) {
        this.mcontext = context;
        this.mDatas   = datas;
        //传当前上下文的视图，不了解context的话可以去Google一下
        this.inflater = LayoutInflater.from(context);
    }

    @Override

    //获得父列表项的数目
    public int getGroupCount() {
        return mDatas.size();
    }
    //获得子列表项的数目
    @Override
    public int getChildrenCount(int i) {
        return mDatas.get(i).getChapterItems().size();
    }
    //拿到集合中第N个数据<个人理解>
    @Override
    public Object getGroup(int i) {
        return mDatas.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public Object getChild(int i, int i1) {
        return mDatas.get(i).getChapterItems().get(i1);
    }

    //获得父列表项的Id
    @Override
    public long getGroupId(int i) {
        return i;
    }
    //获得子列表项的Id
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    //我不知道是干嘛这个大家可以试一试把他改成true，缘分报bug!
    @Override
    public boolean hasStableIds() {
        return false;
    }
    // 父类列表的视图
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ParentViewHolder parentViewHolder = null;
        if (view == null){
            //加载视图参数解释--第一个传的父控件的视图<你需要自己创建一个>，第二个参数<使用自己layout中的控件参数>，第三个参数不使用默认控件参数
            view = inflater.inflate(R.layout.item_paremt_chapter,viewGroup,false);

            parentViewHolder = new ParentViewHolder();

            parentViewHolder.textView = view.findViewById(R.id.text_view);
            //            对象重用
            view.setTag(parentViewHolder);
        }else {
            parentViewHolder = (ParentViewHolder) view.getTag();
        }
//        获取父类集合下标中指定数据
        Chapter chapter = mDatas.get(i);
        parentViewHolder.textView.setText(chapter.getName());
        return view;
    }
    // 子类列表的视图
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder = null;
        if (view == null){
            view = inflater.inflate(R.layout.item_child_chapter,viewGroup,false);

            childViewHolder = new ChildViewHolder();

            childViewHolder.textView = view.findViewById(R.id.id_text_view);
            view.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        // 获取子类集合下标中指定数据
        ChapterItem chapterItem = mDatas.get(i).getChapterItems().get(i1);
        childViewHolder.textView.setText(chapterItem.getName());
        return view;
    }
    //子列表项是否能触发事件，返回true则为可以响应点击
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    class ParentViewHolder{
        TextView textView;
    }
    class ChildViewHolder{
        TextView textView;
    }
}
