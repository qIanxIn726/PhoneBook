package com.example.sqlite;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/5/20.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QueryActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editSex;
    private EditText editPhone;
    private EditText editBYear;
    private EditText editBMonth;
    private EditText editBDay;

    private String nameString;
    private String sexString;
    private String phoneString;
    private String yearString;
    private String monthString;
    private String dayString;
    //设置空格占位符
    private String space = "\u3000";//全角空格
    private String half_space = "\u0020";//半角空格
    //额外捡到的知识点：一个汉字相当于一个全角空格的长度，一个阿拉伯数字相当于两个半角空格的长度

    private Button btn_search1;
    private Button btn_search2;
    private Button btn_back;

    private TextView textView;

    SQLiteDatabase db;

    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        editName = (EditText) findViewById(R.id.editTextUser);
        editSex = (EditText) findViewById(R.id.edit_sex);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editBYear = (EditText) findViewById(R.id.edit_birth_year);
        editBMonth = (EditText) findViewById(R.id.edit_birth_month);
        editBDay = (EditText) findViewById(R.id.edit_birth_day);

        btn_search1 = (Button) findViewById(R.id.btn_search1);
        btn_search2 = (Button) findViewById(R.id.btn_search2) ;
        btn_back = (Button) findViewById(R.id.btn_back);

        textView = (TextView) findViewById(R.id.textViewQuery);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "android1.db3", null);

        //条件搜索
        btn_search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = editName.getText().toString();
                sexString = editSex.getText().toString();
                phoneString = editPhone.getText().toString();
                yearString = editBYear.getText().toString();
                monthString = editBMonth.getText().toString();
                dayString = editBDay.getText().toString();

                try {
                    //如果无任何条件，则为全部显示
                    if (nameString.isEmpty() && sexString.isEmpty() && phoneString.isEmpty()
                            && yearString.isEmpty() && monthString.isEmpty() && dayString.isEmpty()) {
                        textView.setText("");
                        Cursor cursor = db.rawQuery("select * from 通讯录", null);

                        //判断是否有数据
                        if(cursor.getCount() == 0){
                            Toast.makeText(QueryActivity.this,"无此数据,试试换个条件？",Toast.LENGTH_SHORT).show();
                        }else{


                            //信息显示
                            if (cursor.moveToFirst()) {
                                int column = cursor.getColumnCount();
                                //标题显示
                                for (int i = 0; i < column - 1; i++) {
                                    String string = cursor.getColumnName(i);
                                    if(string.equals("性别")){
                                        textView.append(cursor.getColumnName(i) + space + space+ space + space + space);
                                    }else if (string.equals("手机")){
                                        textView.append(cursor.getColumnName(i) + space + space + space + space + space + space);
                                    }else{
                                        textView.append(cursor.getColumnName(i) + space + space + space);
                                    }
                                }
                                textView.append("\n");

                                //数据内容显示
                                while (!cursor.isAfterLast()) {
                                    for (int j = 0; j < column - 1; j++) {
                                        String str = cursor.getColumnName(j);
                                        if(str.equals("id")){//判断id的位数
                                            String id = cursor.getString(j);
                                            if(id.length() < 2){//是个位数
                                                textView.append(id + space + space + space);
                                            }else if(id.length() < 3){//是十位数
                                                textView.append(id + space + half_space + half_space + space);
                                            }else{
                                                textView.append(id + space);
                                            }
                                        }else if(str.equals("姓名")){//判断姓名长度
                                            String name = cursor.getString(j);
                                            if(name.length() < 3){//姓名是两个字
                                                textView.append(name + space + space + space + space);
                                            }else if(name.length() < 4){//姓名是三个字
                                                textView.append(name + space + space + space);
                                            }else if(name.length() < 5){//姓名是四个字
                                                textView.append(name + space + space);
                                            }else{//姓名是五个字
                                                textView.append(name + space);
                                            }
                                        }else{
                                            textView.append(cursor.getString(j) + space + space + space);
                                        }
                                    }
                                    textView.append("\n");
                                    cursor.moveToNext();
                                }
                            }


                        }
                    } else {
                        //有部分条件，当生日年、月、日都没有输入
                        if (yearString.isEmpty() && monthString.isEmpty() && dayString.isEmpty()) {

                            //根据条件输入情况构建搜索语句
                            StringBuilder sql = new StringBuilder();
                            sql.append("select * from 通讯录 where ");
                            if (!nameString.isEmpty()) {
                                sql.append("姓名 = " + "'" + nameString + "'");

                                if (!sexString.isEmpty()) {
                                    sql.append("and 性别 = " + "'" + sexString + "'");
                                }
                                if (!phoneString.isEmpty()) {
                                    sql.append("and 手机 = " + phoneString);
                                }
                                if (!yearString.isEmpty()) {
                                    sql.append("and 生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                }
                            } else {
                                if (!sexString.isEmpty()) {
                                    sql.append("性别 = " + "'" + sexString + "'");
                                    if (!phoneString.isEmpty()) {
                                        sql.append("and 手机 = " + phoneString);
                                    }
                                    if (!yearString.isEmpty()) {
                                        sql.append("and 生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                    }
                                } else {
                                    if (!phoneString.isEmpty()) {
                                        sql.append("手机 = " + phoneString);
                                        if (!yearString.isEmpty()) {
                                            sql.append("and 生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                        }
                                    } else {
                                        if (!yearString.isEmpty()) {
                                            sql.append("生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                        }
                                    }
                                }
                            }

                            String Sql = sql.toString();

                            textView.setText("");
                            Cursor cursor = db.rawQuery(Sql, null);

                            //判断是否有数据
                            if(cursor.getCount() == 0){
                                Toast.makeText(QueryActivity.this,"无此数据,试试换个条件？",Toast.LENGTH_SHORT).show();
                            }else{


                                //信息显示
                                if (cursor.moveToFirst()) {
                                    int column = cursor.getColumnCount();
                                    //标题显示
                                    for (int i = 0; i < column - 1; i++) {
                                        String string = cursor.getColumnName(i);
                                        if(string.equals("性别")){
                                            textView.append(cursor.getColumnName(i) + space + space+ space + space + space);
                                        }else if (string.equals("手机")){
                                            textView.append(cursor.getColumnName(i) + space + space + space + space + space + space);
                                        }else{
                                            textView.append(cursor.getColumnName(i) + space + space + space);
                                        }
                                    }
                                    textView.append("\n");

                                    //数据内容显示
                                    while (!cursor.isAfterLast()) {
                                        for (int j = 0; j < column - 1; j++) {
                                            String str = cursor.getColumnName(j);
                                            if(str.equals("id")){//判断id的位数
                                                String id = cursor.getString(j);
                                                if(id.length() < 2){//是个位数
                                                    textView.append(id + space + space + space);
                                                }else if(id.length() < 3){//是十位数
                                                    textView.append(id + space + half_space + half_space + space);
                                                }else{
                                                    textView.append(id + space);
                                                }
                                            }else if(str.equals("姓名")){//判断姓名长度
                                                String name = cursor.getString(j);
                                                if(name.length() < 3){//姓名是两个字
                                                    textView.append(name + space + space + space + space);
                                                }else if(name.length() < 4){//姓名是三个字
                                                    textView.append(name + space + space + space);
                                                }else if(name.length() < 5){//姓名是四个字
                                                    textView.append(name + space + space);
                                                }else{//姓名是五个字
                                                    textView.append(name + space);
                                                }
                                            }else{
                                                textView.append(cursor.getString(j) + space + space + space);
                                            }
                                        }
                                        textView.append("\n");
                                        cursor.moveToNext();
                                    }
                                }


                            }

                            //当年、月、日填写不完整
                        } else if (yearString.isEmpty() || monthString.isEmpty() || dayString.isEmpty()) {
                            Toast.makeText(QueryActivity.this, "请输入完整生日信息", Toast.LENGTH_SHORT).show();
                        } else {
                            //根据条件输入情况构建搜索语句
                            StringBuilder sql = new StringBuilder();
                            sql.append("select * from 通讯录 where ");
                            if (!nameString.isEmpty()) {
                                sql.append("姓名 = " + "'" + nameString + "'");

                                if (!sexString.isEmpty()) {
                                    sql.append("and 性别 = " + "'" + sexString + "'");
                                }
                                if (!phoneString.isEmpty()) {
                                    sql.append("and 手机 = " + phoneString);
                                }
                                if (!yearString.isEmpty()) {
                                    sql.append("and 生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                }
                            } else {
                                if (!sexString.isEmpty()) {
                                    sql.append("性别 = " + "'" + sexString + "'");
                                    if (!phoneString.isEmpty()) {
                                        sql.append("and 手机 = " + phoneString);
                                    }
                                    if (!yearString.isEmpty()) {
                                        sql.append("and 生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                    }
                                } else {
                                    if (!phoneString.isEmpty()) {
                                        sql.append("手机 = " + phoneString);
                                        if (!yearString.isEmpty()) {
                                            sql.append("and 生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                        }
                                    } else {
                                        if (!yearString.isEmpty()) {
                                            sql.append("生日 = " + "'" + yearString + "/" + monthString + "/" + dayString + "'");
                                        }
                                    }
                                }
                            }

                            String Sql = sql.toString();
                            textView.setText("");
                            Cursor cursor = db.rawQuery(Sql, null);

                            //判断是否有数据
                            if(cursor.getCount() == 0){
                                Toast.makeText(QueryActivity.this,"无此数据,试试换个条件？",Toast.LENGTH_SHORT).show();
                            }else{


                                //信息显示
                                if (cursor.moveToFirst()) {
                                    int column = cursor.getColumnCount();
                                    //标题显示
                                    for (int i = 0; i < column - 1; i++) {
                                        String string = cursor.getColumnName(i);
                                        if(string.equals("性别")){
                                            textView.append(cursor.getColumnName(i) + space + space+ space + space + space);
                                        }else if (string.equals("手机")){
                                            textView.append(cursor.getColumnName(i) + space + space + space + space + space + space);
                                        }else{
                                            textView.append(cursor.getColumnName(i) + space + space + space);
                                        }
                                    }
                                    textView.append("\n");

                                    //数据内容显示
                                    while (!cursor.isAfterLast()) {
                                        for (int j = 0; j < column - 1; j++) {
                                            String str = cursor.getColumnName(j);
                                            if(str.equals("id")){//判断id的位数
                                                String id = cursor.getString(j);
                                                if(id.length() < 2){//是个位数
                                                    textView.append(id + space + space + space);
                                                }else if(id.length() < 3){//是十位数
                                                    textView.append(id + space + half_space + half_space + space);
                                                }else{
                                                    textView.append(id + space);
                                                }
                                            }else if(str.equals("姓名")){//判断姓名长度
                                                String name = cursor.getString(j);
                                                if(name.length() < 3){//姓名是两个字
                                                    textView.append(name + space + space + space + space);
                                                }else if(name.length() < 4){//姓名是三个字
                                                    textView.append(name + space + space + space);
                                                }else if(name.length() < 5){//姓名是四个字
                                                    textView.append(name + space + space);
                                                }else{//姓名是五个字
                                                    textView.append(name + space);
                                                }
                                            }else{
                                                textView.append(cursor.getString(j) + space + space + space);
                                            }
                                        }
                                        textView.append("\n");
                                        cursor.moveToNext();
                                    }
                                }


                            }
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(QueryActivity.this,"当前无数据",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //全部显示
        btn_search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    textView.setText("");
                    Cursor cursor = db.rawQuery("select * from 通讯录",null);

                    //判断是否有数据
                    if(cursor.getCount() == 0){
                        Toast.makeText(QueryActivity.this,"无此数据,试试换个条件？",Toast.LENGTH_SHORT).show();
                    }else {


                        //信息显示
                        if (cursor.moveToFirst()) {
                            int column = cursor.getColumnCount();
                            //标题显示
                            for (int i = 0; i < column - 1; i++) {
                                String string = cursor.getColumnName(i);
                                if(string.equals("性别")){
                                    textView.append(cursor.getColumnName(i) + space + space+ space + space + space);
                                }else if (string.equals("手机")){
                                    textView.append(cursor.getColumnName(i) + space + space + space + space + space + space);
                                }else{
                                    textView.append(cursor.getColumnName(i) + space + space + space);
                                }
                            }
                            textView.append("\n");

                            //数据内容显示
                            while (!cursor.isAfterLast()) {
                                for (int j = 0; j < column - 1; j++) {
                                    String str = cursor.getColumnName(j);
                                    if(str.equals("id")){//判断id的位数
                                        String id = cursor.getString(j);
                                        if(id.length() < 2){//是个位数
                                            textView.append(id + space + space + space);
                                        }else if(id.length() < 3){//是十位数
                                            textView.append(id + space + half_space + half_space + space);
                                        }else{
                                            textView.append(id + space);
                                        }
                                    }else if(str.equals("姓名")){//判断姓名长度
                                        String name = cursor.getString(j);
                                        if(name.length() < 3){//姓名是两个字
                                            textView.append(name + space + space + space + space);
                                        }else if(name.length() < 4){//姓名是三个字
                                            textView.append(name + space + space + space);
                                        }else if(name.length() < 5){//姓名是四个字
                                            textView.append(name + space + space);
                                        }else{//姓名是五个字
                                            textView.append(name + space);
                                        }
                                    }else{
                                        textView.append(cursor.getString(j) + space + space + space);
                                    }
                                }
                                textView.append("\n");
                                cursor.moveToNext();
                            }
                        }


                    }

                }catch (Exception e){
                    Toast.makeText(QueryActivity.this,"当前无数据",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueryActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

