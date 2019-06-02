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

/**
 * Created by 华南理工大学物理与光电学院 on 2019/6/1.
 */

public class DeleteActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editPwd;
    private EditText editPhone;

    private String nameString;
    private String pwdString;
    private String phoneString;

    private Button btn_delete;
    private Button btn_back;

    SQLiteDatabase db;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "android1.db3", null);

        editName = (EditText) findViewById(R.id.editTextUser);
        editPwd = (EditText) findViewById(R.id.editUserPwd);
        editPhone = (EditText) findViewById(R.id.edit_phone);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameString = editName.getText().toString();
                pwdString = editPwd.getText().toString();
                phoneString = editPhone.getText().toString();
                //构建操作语句头
                StringBuilder sql = new StringBuilder();
                sql.append("delete from 通讯录 where ");

                //情形1，姓名或密码无输入
                if(nameString.isEmpty()||pwdString.isEmpty()){
                    //情形2，姓名或密码无输入，手机无输入
                    if(phoneString.isEmpty()){
                        Toast.makeText(DeleteActivity.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    }else if(phoneString.equals("123456789")){
                        //情形5，输入特殊电话号码，删除整张数据表
                        db.execSQL("drop table 通讯录");
                        Toast.makeText(DeleteActivity.this,"信息已全部删除",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(DeleteActivity.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //情形3，有姓名，密码，无手机号
                    if(phoneString.isEmpty()){
                        Toast.makeText(DeleteActivity.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    }else{
                        //情形4，都有输入
                        //先查询是否有这条数据
                        Cursor cursor = db.rawQuery("select * from 通讯录 where 姓名 = ? and 手机 = ? and 密码 = ?",new String[]{nameString,phoneString,pwdString});
                        //无此数据，提示
                        if(cursor.getCount() == 0){
                            Toast.makeText(DeleteActivity.this,"无此数据，是否输入错误？",Toast.LENGTH_SHORT).show();
                        }else{
                            //正确输入，删除并提示
                            sql.append("姓名 = " + "'"+ nameString + "'");
                            sql.append(" and 手机 = " + phoneString);
                            sql.append(" and 密码 = " + pwdString);

                            db.execSQL(sql.toString());
                            Toast.makeText(DeleteActivity.this,"信息删除成功",Toast.LENGTH_SHORT).show();
                        }




                    }
                }
            }
        });

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
