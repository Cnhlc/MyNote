package com.wuwl.mynote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuwl.mynote.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private OnItemClickLitener mOnItemClickLitener;

    public MainAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        mCursor.moveToPosition(position);
        //获取游标当前的数据的内容
        String dbcontext1 = mCursor.getString(mCursor.getColumnIndex("content"));
        //赋值给dbcontext，通过字符串切割，在item中显示部分内容
        String dbcontext = dbcontext1;
        String s = null;
        s = ClearBracket(dbcontext);
        if (s.length() >= 18) {
            s = s.substring(0, 18) + "……";
        }
        String dbtime = mCursor.getString(mCursor.getColumnIndex("time"));
        holder.tv_list_content.setText(s);
        holder.tv_list_time.setText(dbtime);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView, position);
                    //避免长按点击同时触发
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_list_content;
        public TextView tv_list_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_list_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_list_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * 功能描述: 去掉括号里面的内容
     */
    private String ClearBracket(String context) {
        context = context.replace("&nbsp;", "");
        int head = context.indexOf('<'); // 标记第一个使用左括号的位置
        if (head == -1) {
            return context; // 如果context中不存在括号，什么也不做，直接跑到函数底端返回初值str
        } else {
            int next = head + 1; // 从head+1起检查每个字符
            int count = 1; // 记录括号情况
            do {
                if (context.charAt(next) == '<')
                    count++;
                else if (context.charAt(next) == '>')
                    count--;
                next++; // 更新即将读取的下一个字符的位置
                if (count == 0) {
                    String temp = context.substring(head, next);
                    context = context.replace(temp, ""); // 用空内容替换，复制给context
                    head = context.indexOf('<'); // 找寻下一个左括号
                    next = head + 1; // 标记下一个左括号后的字符位置
                    count = 1; // count的值还原成1
                }
            } while (head != -1); // 如果在该段落中找不到左括号了，就终止循环
        }
        return context; // 返回更新后的context
    }
}
