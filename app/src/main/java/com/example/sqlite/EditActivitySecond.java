package com.example.sqlite;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivitySecond extends AppCompatActivity {

    EditText etUser;
    EditText etSex;
    EditText etPwd;
    EditText etPhone;
    EditText etYear;
    EditText etMonth;
    EditText etDay;
    Button btn_edit;
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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_second);

        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "android1.db3", null);
        etUser = (EditText) findViewById(R.id.editTextUser);
        etSex = (EditText) findViewById(R.id.edit_sex);
        etPwd = (EditText) findViewById(R.id.editUserPwd);
        etPhone = (EditText) findViewById(R.id.edit_phone);
        etYear = (EditText) findViewById(R.id.edit_birth_year);
        etMonth = (EditText) findViewById(R.id.edit_birth_month);
        etDay = (EditText) findViewById(R.id.edit_birth_day);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameString = etUser.getText().toString();
                sexString = etSex.getText().toString();
                pwdString = etPwd.getText().toString();
                phoneString = etPhone.getText().toString();
                yearString = etYear.getText().toString();
                monthString = etMonth.getText().toString();
                dayString = etDay.getText().toString();

                if((nameString.isEmpty())||(sexString.isEmpty())||(pwdString.isEmpty())
                        ||(phoneString.isEmpty())||(yearString.isEmpty())
                        ||(monthString.isEmpty())||(dayString.isEmpty())){
                    Toast.makeText(EditActivitySecond.this,"请填写完整信息",Toast.LENGTH_LONG).show();
                }else{
                    try{
                        db.execSQL("update 通讯录 set 姓名 = ?,性别 = ?,密码 = ?,手机 = ?,生日 = ? " +
                                "where 姓名 = ? and 性别 = ? and 生日 = ?",new String[]{nameString,sexString,pwdString,phoneString,
                                (yearString+"/"+monthString+"/"+dayString),nameString,sexString,(yearString+"/"+monthString+"/"+dayString)});
                        Toast.makeText(EditActivitySecond.this,"您的信息已经修改成功",Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }});

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivitySecond.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
