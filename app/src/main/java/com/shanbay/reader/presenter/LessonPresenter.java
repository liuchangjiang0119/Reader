package com.shanbay.reader.presenter;

import android.util.Log;

import com.shanbay.reader.App;

import com.shanbay.reader.dao.LessonDao;
import com.shanbay.reader.dao.WordDao;
import com.shanbay.reader.model.Lesson;
import com.shanbay.reader.model.Word;
import com.shanbay.reader.presenter.contract.LessonContract;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonPresenter implements LessonContract.Presenter {

    private LessonDao mLessonDao;
    private WordDao mWordDao;
    private QueryBuilder<Lesson> mBuilder;
    private List<Lesson> mLessonList;
    private List<String> words;
    private List<Word> mWordLsit;
    private LessonContract.LessonView mLessonView;
    private LessonContract.WordView mWordView;

    public LessonPresenter(LessonContract.LessonView view) {
        mLessonView = view;
    }
    public LessonPresenter(LessonContract.WordView view){
        mWordView = view;

    }

    @Override
    public void loadLesson(int unit) {
        mLessonDao = App.getInstance().getLessonSession().getLessonDao();
        mBuilder = mLessonDao.queryBuilder();
        switch (unit){
            case 0:
                mLessonList = mBuilder.where(LessonDao.Properties.Id.between(0,8)).build().list();

                break;
            case 1:
                mLessonList = mBuilder.where(LessonDao.Properties.Id.between(9,16)).build().list();

                break;
            case 2:
                mLessonList = mBuilder.where(LessonDao.Properties.Id.between(17,24)).build().list();

                break;
            case 3:
                mLessonList = mBuilder.where(LessonDao.Properties.Id.between(25,32)).build().list();

                break;
            case 4:
                mLessonList = mBuilder.where(LessonDao.Properties.Id.between(33,40)).build().list();
                break;
            case 5:
                mLessonList = mBuilder.where(LessonDao.Properties.Id.between(41,48)).build().list();
                break;


        }
        mLessonView.showLesson(mLessonList);

    }

    @Override
    public void loadWord(int level) {
        mWordDao = App.getInstance().getWordSession().getWordDao();
        QueryBuilder<Word> builder = mWordDao.queryBuilder();
        words = new ArrayList<>();
        switch (level){
            case 0:
                mWordLsit = builder.where(WordDao.Properties.Level.eq(""+level)).build().list();
                break;
            case 1:
                mWordLsit = builder.where(WordDao.Properties.Level.between("0","1")).build().list();
                break;
            case 2:
                mWordLsit = builder.where(WordDao.Properties.Level.between("0","2")).build().list();
                break;
            case 3:
                mWordLsit = builder.where(WordDao.Properties.Level.between("0","3")).build().list();
                break;
            case 4:
                mWordLsit = builder.where(WordDao.Properties.Level.between("0","4")).build().list();
                break;
            case 5:
                mWordLsit = builder.where(WordDao.Properties.Level.between("0","5")).build().list();
                break;
        }

        Log.d("-----------",mWordLsit.size()+"");
    }
}