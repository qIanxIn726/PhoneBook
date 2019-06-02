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
 * Created by 华南理工大学物理与光电学院 on 2019/5/31.
 */

public class EditActivityFirst extends AppCompatActivity {

    private EditText editName;
    private EditText editPwd;
    private String nameString;
    private String pwdString;
    private Button btn_Confirm;
    private Button btn_Back;
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_edit_first);

        editName = (EditText) findViewById(R.id.editUser);
        editPwd = (EditText) findViewById(R.id.editPwd);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "android1.db3", null);


        btn_Confirm = (Button) findViewById(R.id.btn_confirm);
        btn_Back = (Button) findViewById(R.id.btn_back);
        btn_Confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nameString = editName.getText().toString();
                pwdString = editPwd.getText().toString();
                if((nameString.isEmpty())||(pwdString.isEmpty())){
                    Toast.makeText(EditActivityFirst.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor = db.rawQuery("select 密码 from 通讯录 where 姓名 = ?",
                            new String[]{nameString});
                    //首先判断是否存在这条数据
                    if(cursor.getCount() == 0){//不存在
                        Toast.makeText(EditActivityFirst.this, "不存在当前数据，是否添加？", Toast.LENGTH_LONG).show();
                    }else{//存在
                        if (cursor.moveToFirst()) {//判断密码是否匹配
                            String pwd = cursor.getString(cursor.getColumnIndex("密码"));
                            if (pwd.equals(pwdString)) {//匹配
                                Intent intent = new Intent(EditActivityFirst.this, EditActivitySecond.class);
                                startActivity(intent);
                                finish();
                            } else {//不匹配
                                Toast.makeText(EditActivityFirst.this, "密码不正确", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
        btn_Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivityFirst.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
