package com.example.evilsay.wechatson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.evilsay.wechatson.Fragment.FragmentAddress;
import com.example.evilsay.wechatson.Fragment.FragmentChat;
import com.example.evilsay.wechatson.Fragment.FragmentMe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String user,name;
    protected LinearLayout main_Address,main_Chat,main_Me;
    protected FragmentAddress fragmentAddress = new FragmentAddress();
    protected FragmentChat fragmentChat       = new FragmentChat();
    protected FragmentMe fragmentMe           = new FragmentMe();
    protected ImageView img_Me,img_address,img_Chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        FragmentView();
        GetLoginDate();

    }
//    初始化fragment视图
    private void FragmentView() {
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.menu_main,fragmentMe)
                .add(R.id.menu_main,fragmentAddress)
                .add(R.id.menu_main,fragmentChat)
                .hide(fragmentChat)
                .hide(fragmentMe)
                .commit();
        img_address.setImageResource(R.mipmap.new_address);
        img_Me.setImageResource(R.drawable.me);
        img_Chat.setImageResource(R.drawable.wechat);
    }
//初始化view视图
    private void InitView() {
        main_Address=findViewById(R.id.main_Address);
        main_Chat   =findViewById(R.id.main_Chat);
        main_Me     =findViewById(R.id.main_Me);
        img_Me      =findViewById(R.id.img_Me);
        img_address =findViewById(R.id.img_address);
        img_Chat    =findViewById(R.id.img_chat);
        main_Address.setOnClickListener(this);
        main_Chat   .setOnClickListener(this);
        main_Me     .setOnClickListener(this);
    }
//监听器
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_Address:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragmentAddress)
                        .hide(fragmentMe)
                        .hide(fragmentChat)
                        .commit();
                img_address.setImageResource(R.mipmap.new_address);
                img_Me.setImageResource(R.drawable.me);
                img_Chat.setImageResource(R.drawable.wechat);
                break;
            case R.id.main_Chat:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragmentChat)
                        .hide(fragmentMe)
                        .hide(fragmentAddress)
                        .commit();
                img_Chat.setImageResource(R.mipmap.wechat_new);
                img_Me.setImageResource(R.drawable.me);
                img_address.setImageResource(R.drawable.addressbook);
                break;
            case R.id.main_Me:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragmentMe)
                        .hide(fragmentAddress)
                        .hide(fragmentChat)
                        .commit();
                img_Me.setImageResource(R.mipmap.me_new);
                img_address.setImageResource(R.drawable.addressbook);
                img_Chat.setImageResource(R.drawable.wechat);
                break;
        }
    }
    private void GetLoginDate(){
        if (getIntent() !=null){
            user = getIntent().getStringExtra(LoginActivity.LOGIN_USER);
            name = getIntent().getStringExtra(LoginActivity.LOGIN_NAME);
        }
    }

    public String Date_User(){
        return user;
    }
    public String Date_Name(){
        return name;
    }
}
