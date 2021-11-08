package com.wuwl.mynote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.wuwl.mynote.R;
import com.wuwl.mynote.adapter.MainAdapter;
import com.wuwl.mynote.bean.Note;
import com.wuwl.mynote.db.NoteDB;
import com.wuwl.mynote.db.NoteDbReader;

public class SearchActivity extends BaseActivity {
    private EditText mEtSearch;
    private Cursor cursor;
    private MainAdapter myAdapter;
    private RecyclerView mRvList;
    private NoteDB mNoteDb;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
    }

    private void init() {
        mEtSearch = findViewById(R.id.et_search);
        mRvList = findViewById(R.id.rv_list);
        mNoteDb = new NoteDB(this);
    }

    private void search(){
        //获取输入文本（转为String-->去空格）
        String findStr = mEtSearch.getText().toString().trim();
        if (!findStr.isEmpty()){
            SQLiteDatabase dbreader = NoteDbReader.getDbreader(this);
            //根据输入文本搜索，返回cursor
            cursor = NoteDbReader.select(dbreader, findStr);
            myAdapter = new MainAdapter(this,cursor);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRvList.setLayoutManager(linearLayoutManager);
            myAdapter.setOnItemClickLitener(new MainAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    cursor.moveToPosition(position);
                    Intent intent = new Intent(SearchActivity.this, NotesActivity.class);
                    mNote = new Note(cursor.getInt(cursor.getColumnIndex(mNoteDb.ID)),
                            cursor.getString(cursor.getColumnIndex(mNoteDb.CONTENT)),
                            cursor.getString(cursor.getColumnIndex(mNoteDb.TIME)));
                    intent.putExtra("nontInfo",mNote);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    cursor.moveToPosition(position);
                    new AlertDialog.Builder(SearchActivity.this)
                            .setMessage("您确认删除此项目吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase dbwriter = NoteDbReader.getDbwriter(SearchActivity.this);
                                    NoteDbReader.delete(cursor.getInt(cursor.getColumnIndex(mNoteDb.ID)),dbwriter);
                                    search();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create()
                            .show();
                }
            });
            mRvList.setAdapter(myAdapter);
        }
    }

    public void goBack(View view) {
        finish();
    }

    public void clean(View view) {
        mEtSearch.setText("");
    }
}
