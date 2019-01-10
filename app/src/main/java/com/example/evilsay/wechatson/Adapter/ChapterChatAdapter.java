package com.example.evilsay.wechatson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evilsay.wechatson.Beans.ChapterChat;
import com.example.evilsay.wechatson.Beans.ChapterChatItem;
import com.example.evilsay.wechatson.R;

import java.util.List;

public class ChapterChatAdapter extends BaseExpandableListAdapter {
    private List<ChapterChat> chapterChats;
    //传当前上下文的视图，不了解context的话可以去Google一下
    private Context context;
    //对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.from()来载入
    private LayoutInflater layoutInflater;

    public ChapterChatAdapter(List<ChapterChat> chapterChats, Context context) {
        this.chapterChats = chapterChats;
        this.context = context;
//        传入当前上下文的布局
        this.layoutInflater = LayoutInflater.from(context);
    }

    //获得父列表项的数目
    @Override
    public int getGroupCount() {
        return chapterChats.size();
    }

    //获得子列表项的数目
    @Override
    public int getChildrenCount(int groupPosition) {
        return chapterChats.get(groupPosition).getChapterChatItems().size();
    }

    //拿到集合中第N个数据<个人理解>
    @Override
    public Object getGroup(int groupPosition) {
        return chapterChats.get(groupPosition);
    }

    //获取子列表项对应的Item
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return chapterChats.get(childPosition).getChapterChatItems().size();
    }

    //获得父列表项的Id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获得子列表项的Id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //我不知道是干嘛这个大家可以试一试把他改成true，缘分报bug!
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 父类列表的视图
     *
     * @param groupPosition 父列表集合下标
     * @param isExpanded    获取父列表点击状态，展开为TURE,为展开为false
     * @param convertView   父列表视图加载
     * @param parent        <当前父列表视图>布局的容器
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ChapterViewHolder chapterViewHolder = null;
        if (convertView == null) {
//          加载视图参数解释--第一个传的父控件的视图<你需要自己创建一个>，第二个参 数<使用自己layout中的控件参数>，第三个参数不使用默认控件参数
            convertView = layoutInflater.inflate(R.layout.parent_food, parent, false);
            chapterViewHolder = new ChapterViewHolder();
            chapterViewHolder.textView = convertView.findViewById(R.id.parent_text);
            chapterViewHolder.imageView = convertView.findViewById(R.id.img);
            //对象重用
            convertView.setTag(chapterViewHolder);
        } else {
            chapterViewHolder = (ChapterViewHolder) convertView.getTag();
        }
        ChapterChat chapterChat = chapterChats.get(groupPosition);
        chapterViewHolder.textView.setText(chapterChat.getName());
        chapterViewHolder.imageView.setImageResource(R.drawable.img_exb);
        chapterViewHolder.imageView.setSelected(isExpanded);
        return convertView;
    }

    /**
     * 子列表视图
     * 参数详解同上
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
//          这个视图传的就是子控件了，也需要自己创建一个
            convertView = layoutInflater.inflate(R.layout.item_child_chapter, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.textView = convertView.findViewById(R.id.id_text_view);
//            childViewHolder.imageView = convertView.findViewById(R.id.iv_user_head);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        ChapterChatItem chapterChatItem = chapterChats.get(groupPosition).getChapterChatItems().get(childPosition);
        childViewHolder.textView.setText(chapterChatItem.getName());
//        childViewHolder.imageView.setImageResource(R.mipmap.addressbook);
        return convertView;
    }

    //列表项是否能否触发事件，返回true则为可以响应点击
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // 内部类
    class ChapterViewHolder {
        TextView textView;
        ImageView imageView;

    }

    class ChildViewHolder {
        TextView textView;
    }
}
