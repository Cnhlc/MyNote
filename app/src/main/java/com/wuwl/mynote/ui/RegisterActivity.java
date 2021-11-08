package com.wuwl.mynote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wuwl.mynote.R;
import com.wuwl.mynote.db.NoteDbReader;

import javax.xml.transform.Source;

public class RegisterActivity extends BaseActivity {
    private String username;
    private EditText password;
    private EditText rePassword;
    private Button register;
    private String mainSource;
    private EditText usernameText;
    public static final String USERNAME="USERNAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SQLiteDatabase dbreader = NoteDbReader.getDbreader(this);



        boolean isFirst = NoteDbReader.selectPaswd(dbreader);
//        boolean isFirst = NoteDbReader.getPasswd(dbreader) == null ? false : true;
        mainSource = getIntent().getStringExtra("source");
        //当程序不是第一次登录，并且不是从MainActivity跳转而来就进入LoginActivity
//        if (!isFirst && mainSource == null) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }

        init();

        //当程序从MainActivity跳转过来，那么就改布局，变成修改密码界面
        if (mainSource != null) {
            if (mainSource.equals("MainActivity")) {
                LinearLayout ll = findViewById(R.id.ll_goback);
                //显示返回按钮
                ll.setVisibility(View.VISIBLE);
                password.setHint("输入四位数字新密码");
            }
        }
    }

    private void init() {
        password = findViewById(R.id.edit_password);
        rePassword = findViewById(R.id.edit_re_password2);
        register = findViewById(R.id.register);
        usernameText = findViewById(R.id.edit_username);
        //获取Shared Preferences对象
        SharedPreferences setinfo = getPreferences(Activity.MODE_PRIVATE);
        //取出保存的名字
        username = setinfo.getString(USERNAME,"");
        //将取出的信息放在对应的EditText中
        usernameText.setText(username);

    }

    protected void onResume() {
        super.onResume();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请输入四位数数字密码", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(rePassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "输入两次密码不同！", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().length() != 4) {
                    Toast.makeText(RegisterActivity.this, "密码长度必须为4位数字", Toast.LENGTH_SHORT).show();
                } else {
                    //判断是否是应是注册界面
                    if (mainSource == null) {
                        registerPwd();
                    } else if (mainSource != null && mainSource.equals("MainActivity")) {
                        registerPwd2();
                    }
                }

            }
        });
    }


    private void registerPwd() {
        SQLiteDatabase dbwriter = NoteDbReader.getDbwriter(this);
        boolean success = NoteDbReader.registerPaswd(dbwriter, password.getText().toString());
        if (success) {
            if (mainSource != null && mainSource.equals("MainActivity")) {
                Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
            } else {
                onStop();
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (mainSource != null && mainSource.equals("MainActivity")) {
                Toast.makeText(this, "修改密码失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerPwd2() {
        SQLiteDatabase dbwriter = NoteDbReader.getDbwriter(this);
        //先删除密码
        NoteDbReader.deletePaswd(dbwriter);
        registerPwd();
    }

    protected void onStop() {
        SharedPreferences setinfo = getPreferences(Activity.MODE_PRIVATE);
        setinfo.edit().putString(USERNAME, usernameText.getText().toString()).commit();
        super.onStop();
    }
    public void goBack(View view) {
        finish();
    }
}
