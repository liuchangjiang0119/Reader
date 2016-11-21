package com.shanbay.reader.view.fragment;

import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TintContextWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shanbay.reader.R;
import com.shanbay.reader.model.Lesson;
import com.shanbay.reader.presenter.LessonPresenter;
import com.shanbay.reader.presenter.contract.LessonContract;
import com.shanbay.reader.view.ContentActivity;
import com.shanbay.reader.view.adapter.LessonRecyclerViewAdapter;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonFragment extends BaseFragment implements LessonContract.LessonView{


    private RecyclerView mRecyclerView;
    private static int positon;
    private LessonRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private LessonPresenter mPresenter;
    private  List<String> mTitleList;
    private List<Lesson> mLessonList;
    public static LessonFragment newInstance(int position){
        LessonFragment lessonFragment = new LessonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        lessonFragment.setArguments(bundle);
        return lessonFragment;

    }



    @Override
    public void loadLessonList() {

        if (!hasPrepared||!isVisble) {
            return;
        }

            positon = getArguments().getInt("POSITION");
            mPresenter = new LessonPresenter(this);
            mPresenter.loadLesson(positon);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("---------","onCreate");

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
        initRecyclerView(view);
        hasPrepared = true;
        loadLessonList();
    }

    @Override
    public void showLesson(List<Lesson> list) {
//        Log.d("----------","showLesson");
        if (list!=null) {
            mLessonList = list;

            mTitleList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                mTitleList.add(list.get(i).getTitle());
            }
            mAdapter.setTitleList(mTitleList);
        }

    }

    void initRecyclerView(View v){
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mAdapter = new LessonRecyclerViewAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LessonRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("content", mLessonList.get(position).getContent());
                intent.putExtra("chinese" , mLessonList.get(position).getChinese());
                intent.putExtra("question", mLessonList.get(position).getQuestion());
                intent.putExtra("answer", mLessonList.get(position).getAnswer());
                intent.putExtra("word",mLessonList.get(position).getWord());
                intent.putExtra("lesson",mLessonList.get(position).getLesson());
                startActivity(intent);
            }
        });


    }
}
