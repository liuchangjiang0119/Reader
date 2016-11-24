package com.shanbay.reader.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shanbay.reader.R;
import com.shanbay.reader.model.bean.Lesson;
import com.shanbay.reader.presenter.LessonPresenter;
import com.shanbay.reader.presenter.contract.LessonContract;
import com.shanbay.reader.view.ContentActivity;
import com.shanbay.reader.view.UnitView;
import com.shanbay.reader.view.adapter.LessonRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import immortalz.me.library.TransitionsHeleper;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonFragment extends BaseFragment implements LessonContract.LessonView{

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.unitView)
    UnitView mUnitView;


    private static int positon;
    private LessonRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LessonPresenter mPresenter;
    private  List<String> mTitleList;
    private List<Lesson> mLessonList;
//    每个页面布局一样，所以只创建一个fragment
    public static LessonFragment newInstance(int position){
        LessonFragment lessonFragment = new LessonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        lessonFragment.setArguments(bundle);
        return lessonFragment;

    }

//viewpager的懒加载处理，当页面可见且view初始化完成后在加载数据，避免空指针
    @Override
    public void loadLessonList() {

        if (!hasPrepared||!isVisble) {
            return;
        }

        positon = getArguments().getInt("POSITION");
        mUnitView.setUnit(positon+1);

        mPresenter = new LessonPresenter(this);
        mPresenter.loadLesson(positon);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lesson_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        initRecyclerView();
        hasPrepared = true;
//        当页面可见且view初始化完成的时候再填充数据否则会空指针异常
        loadLessonList();
    }
//将数据填充到RecyclerView
    @Override
    public void showLesson(List<Lesson> list) {
        if (list!=null) {
            mLessonList = list;

            mTitleList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                mTitleList.add(list.get(i).getTitle());
            }
            mAdapter.setTitleList(mTitleList);
        }
        mAdapter.setUnit(positon);

    }
//  初始化RecyclerView
    private void initRecyclerView(){

        mAdapter = new LessonRecyclerViewAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        自定义的RecyclerView的点击监听事件
        mAdapter.setOnItemClickListener(new LessonRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position,View clickView) {

                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("content", mLessonList.get(position).getContent());
                intent.putExtra("chinese" , mLessonList.get(position).getChinese());
                intent.putExtra("question", mLessonList.get(position).getQuestion());
                intent.putExtra("answer", mLessonList.get(position).getAnswer());
                intent.putExtra("word",mLessonList.get(position).getWord());
                intent.putExtra("lesson",mLessonList.get(position).getLesson());
//                一个转场动画的开源框架

                TransitionsHeleper.startActivity(getActivity(),intent,clickView);

            }
        });


    }
}
