package com.wuwl.mynote.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.wuwl.mynote.ui.RegisterActivity;

import com.wuwl.mynote.R;
import com.wuwl.mynote.db.NoteDbReader;

public class LoginActivity extends BaseActivity {
    private String username;
    private EditText usernameText;
    private EditText editText;
    private Button button;
    public static SharedPreferences setinfo;
    public static final String USERNAME="USERNAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        System.out.println("2222");
        SQLiteDatabase dbreader = NoteDbReader.getDbreader(this);
        //获取数据库中密码，用final修饰，不能再次修改
        final String passwd = NoteDbReader.getPasswd(dbreader);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else if(passwd.equals(editText.getText().toString())){
                    onStop();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        editText = findViewById(R.id.edit_password);
        button = findViewById(R.id.login);
        usernameText = findViewById(R.id.edit_username1);
        username = setinfo.getString(USERNAME,"");
        //将取出的信息放在对应的EditText中
        usernameText.setText(username);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
