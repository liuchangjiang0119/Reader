package com.shanbay.reader.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import com.shanbay.reader.R;
import com.shanbay.reader.view.LessonNumView;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mTitleList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private int unit;

    public LessonRecyclerViewAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    public void setTitleList(List<String> list){
        mTitleList = list;
        this.notifyDataSetChanged();
    }

    public void setUnit(int unit){
        this.unit = unit;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mOnItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_lesson,parent,false);
        LessonViewHolder viewHolder = new LessonViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((LessonViewHolder)holder).item_text.setText(mTitleList.get(position));
        ((LessonViewHolder)holder).mLessonNumView.setLesson(unit*8+position+1);
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.itemClick(holder.itemView,position,((LessonViewHolder) holder).mLessonNumView);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return mTitleList.size();
    }

    class LessonViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_text)
        TextView item_text;
        @BindView(R.id.lessonView)
        LessonNumView mLessonNumView;
        public LessonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public interface OnItemClickListener{
        void itemClick(View view ,int position,View clickView);

    }

}
