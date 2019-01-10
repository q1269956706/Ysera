package com.example.expandablelistview;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.expandablelistview.Utils.PowerManger;

public class ReadSMS extends AppCompatActivity implements View.OnClickListener{
    protected Button Read_SMS,ReadContacts,whiteContacts;
    private static final String TAG = "READ_SMS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_layout);
        PowerManger.Apply(this);
        Read_SMS = findViewById(R.id.ReadMsm);
        Read_SMS.setOnClickListener(this);
        ReadContacts = findViewById(R.id.ReadContacts);
        ReadContacts.setOnClickListener(this);
        whiteContacts = findViewById(R.id.Read);
        whiteContacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ReadMsm:
//                读短信
                ContentResolver resolver = getContentResolver();
                Uri uri = Uri.parse("content://sms/inbox");
                Cursor query = resolver.query(uri, null, null, null, null);
                while (query.moveToNext()){
                    String msg = "";
                    String address = query.getString(query.getColumnIndex("address"));
                    String body = query.getString(query.getColumnIndex("body"));
                    msg = address +":"+ body;
                    Log.e(TAG, "onClick: "+msg);
                }
                break;
//                读联系人
            case R.id.ReadContacts:
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext()){
                    // ContactsContract.Contacts.DISPLAY_NAME 为姓名
                    // ContactsContract.Contacts._ID 主键
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String _id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Log.e(TAG, "onClick: "+name+_id);
//                    需要查询的列明
                    String selections = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
                    Cursor query1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selections, new String[]{_id}, null);
                    while (query1.moveToNext()){
                        String phone = query1.getString(query1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        name += " " + phone;
                    }
                    Log.e(TAG, "ReadContacts: "+name);
                }
                break;
//                写联系人
            case R.id.Read:
                //添加空记录
                ContentResolver whiteResolver = getContentResolver();
                ContentValues   values        = new ContentValues();
                Uri phone                     = whiteResolver.insert(ContactsContract.RawContacts.CONTENT_URI,values);
                long parseId = ContentUris.parseId(phone);
                //插入姓名
//                指定姓名列
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,"FuckMe");
//                指定和姓名关联的编号列
                values.put(ContactsContract.Data.RAW_CONTACT_ID,parseId);
//                指定该行数据的类型
                values.put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                whiteResolver.insert(ContactsContract.Data.CONTENT_URI,values);
                //插入电话号码
                values.clear();
//                以上同理
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,"17953257886");
                values.put(ContactsContract.Data.RAW_CONTACT_ID,parseId);
                values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                whiteResolver.insert(ContactsContract.Data.CONTENT_URI,values);
                Toast.makeText(this,"添加联系人成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
