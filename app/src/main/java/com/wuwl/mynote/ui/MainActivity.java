package com.wuwl.mynote.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.wuwl.mynote.R;
import com.wuwl.mynote.adapter.MainAdapter;
import com.wuwl.mynote.bean.Note;
import com.wuwl.mynote.db.NoteDB;
import com.wuwl.mynote.db.NoteDbReader;

public class MainActivity extends BaseActivity{

    private Cursor cursor;
    private Note mNote;
    private NoteDB mNoteDb;
    private RecyclerView mRvList;
    private MainAdapter mRvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDb();
    }

    private void selectDb() {
        SQLiteDatabase dbreader = NoteDbReader.getDbreader(this);
        //显示所有的日记信息
        cursor = NoteDbReader.selectAll(dbreader);

        mRvAdapter = new MainAdapter(this,cursor);

        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(linearLayoutManager);

        //调用setOnItemClickListener，然后重写两个方法
        mRvAdapter.setOnItemClickLitener(new MainAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                mNote = new Note(cursor.getInt(cursor.getColumnIndex(mNoteDb.ID)),
                        cursor.getString(cursor.getColumnIndex(mNoteDb.CONTENT)),
                        cursor.getString(cursor.getColumnIndex(mNoteDb.TIME)));
                intent.putExtra("nontInfo",mNote);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                cursor.moveToPosition(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("您确认删除此项目吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase dbwriter = NoteDbReader.getDbwriter(MainActivity.this);
                                NoteDbReader.delete(cursor.getInt(cursor.getColumnIndex(mNoteDb.ID)),dbwriter);
                                selectDb();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
            }
        });
        mRvList.setAdapter(mRvAdapter);
    }

    private void init() {
        mRvList = findViewById(R.id.rv_list);
        mNoteDb = new NoteDB(this);
    }
    public void add(View v) {
        Intent intent = new Intent(MainActivity.this, NotesActivity.class);

        startActivity(intent);
    }
    public void search(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void setting(View view) {
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        //通过intent传递source，注册页面根据source设置布局
        intent.putExtra("source","MainActivity");
        startActivity(intent);
    }
}
