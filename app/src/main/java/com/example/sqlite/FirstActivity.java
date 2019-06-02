package com.example.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class FirstActivity extends AppCompatActivity {

    private Button btn_Add;
    private Button btn_Edit;
    private Button btn_Search;
    private Button btn_Delete;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btn_Add = (Button) findViewById(R.id.btn_add);
        btn_Edit = (Button) findViewById(R.id.btn_edit);
        btn_Search = (Button) findViewById(R.id.btn_search);
        btn_Delete = (Button) findViewById(R.id.btn_delete);

        btn_Add.setOnClickListener(button_click);
        btn_Edit.setOnClickListener(button_click);
        btn_Search.setOnClickListener(button_click);
        btn_Delete.setOnClickListener(button_click);

    }

    private View.OnClickListener button_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_add:
                {
                    Intent intent1 = new Intent(FirstActivity.this,AddActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                }
                case R.id.btn_edit:
                {
                    Intent intent2 = new Intent(FirstActivity.this,EditActivityFirst.class);
                    startActivity(intent2);
                    finish();
                    break;
                }
                case R.id.btn_search:
                {
                    Intent intent3 = new Intent(FirstActivity.this,QueryActivity.class);
                    startActivity(intent3);
                    finish();
                    break;
                }
                case R.id.btn_delete:
                {
                    Intent intent4 = new Intent(FirstActivity.this,DeleteActivity.class);
                    startActivity(intent4);
                    finish();
                    break;
                }
            }
        }
    };
}
