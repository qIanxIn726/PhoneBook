package com.example.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    EditText etUser;
    EditText etSex;
    EditText etPwd;
    EditText etPhone;
    EditText etYear;
    EditText etMonth;
    EditText etDay;
    Button btn_add;
    Button btn_back;
    SQLiteDatabase db;

    private String nameString;
    private String sexString;
    private String pwdString;
    private String phoneString;
    private String yearString;
    private String monthString;
    private String dayString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "android1.db3", null);
        etUser = (EditText) findViewById(R.id.editTextUser);
        etSex = (EditText) findViewById(R.id.edit_sex);
        etPwd = (EditText) findViewById(R.id.editUserPwd);
        etPhone = (EditText) findViewById(R.id.edit_phone);
        etYear = (EditText) findViewById(R.id.edit_birth_year);
        etMonth = (EditText) findViewById(R.id.edit_birth_month);
        etDay = (EditText) findViewById(R.id.edit_birth_day);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameString = etUser.getText().toString();
                sexString = etSex.getText().toString();
                pwdString = etPwd.getText().toString();
                phoneString = etPhone.getText().toString();
                yearString = etYear.getText().toString();
                monthString = etMonth.getText().toString();
                dayString = etDay.getText().toString();
                //db.execSQL("drop table if exists 通讯录");

                if(nameString.isEmpty()||sexString.isEmpty()
                        ||pwdString.isEmpty()||phoneString.isEmpty()
                                ||yearString.isEmpty()||monthString.isEmpty()
                                ||dayString.isEmpty()){
                    Toast.makeText(AddActivity.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Cursor cursor = db.rawQuery("select * from 通讯录", null);
                        if (cursor.moveToFirst()) {
                            String phone = cursor.getString(cursor.getColumnIndex("手机"));
                            //检查是否有重复注册
                            if (phone.equals(phoneString)) {
                                Toast.makeText(AddActivity.this, "该账号已被注册", Toast.LENGTH_LONG).show();
                            } else {
                                db.execSQL("insert into 通讯录 values(null,?,?,?,?,?)",
                                        new String[]{nameString, sexString, phoneString, (yearString + "/" + monthString + "/" + dayString), pwdString});
                                Toast.makeText(AddActivity.this, "个人信息已写入通讯录", Toast.LENGTH_SHORT).show();
                            }
                        }
                        cursor.close();
                    }catch(Exception e){//表不存在
                        db.execSQL("create table 通讯录(id integer primary key autoincrement,"
                                + "姓名 char(4)," + "性别 char (1)," + "手机 char(11)," + "生日 char(11)," + "密码 char(6))");
                        db.execSQL("insert into 通讯录 values(null,?,?,?,?,?)",
                                new String[]{nameString, sexString, phoneString, (yearString+"/"+monthString+"/"+dayString),pwdString});
                        Toast.makeText(AddActivity.this,"个人信息已写入通讯录",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
    });
    }
}


