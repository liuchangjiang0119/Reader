package com.shanbay.reader.presenter;

import android.util.Log;

import com.shanbay.reader.App;

import com.shanbay.reader.dao.LessonDao;
import com.shanbay.reader.dao.WordDao;
import com.shanbay.reader.model.bean.Lesson;
import com.shanbay.reader.model.bean.Word;
import com.shanbay.reader.model.bean.WordInfo;
import com.shanbay.reader.model.http.ShanBayApi;
import com.shanbay.reader.presenter.contract.LessonContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonPresenter implements LessonContract.Presenter {

    private LessonDao mLessonDao;
    private WordDao mWordDao;
    private QueryBuilder<Lesson> mBuilder;
    private List<Lesson> mLessonList;
    private List<String> words;
    private List<Word> mWordList;
    private LessonContract.LessonView mLessonView;
    private LessonContract.WordView mWordView;
    private Map<String,String> map;

    private final String BASE_URL = "https://api.shanbay.com/bdc/";
    public LessonPresenter(LessonContract.LessonView view) {
        mLessonView = view;
    }
    public LessonPresenter(LessonContract.WordView view){
        mWordView = view;

    }
//根据单元从数据库中查询课文列表
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
//根据单词等级从数据库中查询单词
    @Override
    public void loadWord(int level) {
        mWordDao = App.getInstance().getWordSession().getWordDao();
        QueryBuilder<Word> builder = mWordDao.queryBuilder();
        words = new ArrayList<>();

        switch (level){
            case 0:
                mWordList = builder.where(WordDao.Properties.Level.eq(""+level)).build().list();
                break;
            case 1:
                mWordList = builder.where(WordDao.Properties.Level.between("0","1")).build().list();
                break;
            case 2:
                mWordList = builder.where(WordDao.Properties.Level.between("0","2")).build().list();
                break;
            case 3:
                mWordList = builder.where(WordDao.Properties.Level.between("0","3")).build().list();
                break;
            case 4:
                mWordList = builder.where(WordDao.Properties.Level.between("0","4")).build().list();
                break;
            case 5:
                mWordList = builder.where(WordDao.Properties.Level.between("0","5")).build().list();
                break;
        }

        for (int i =0;i<mWordList.size();i++){
            words.add(mWordList.get(i).getWord());
        }

        mWordView.showWord(words);

    }

    @Override
    public void getWordInfo(String word) {
        map = new HashMap<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShanBayApi shanBayApi = retrofit.create(ShanBayApi.class);

        shanBayApi.getWordInfo(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WordInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mWordView.showError();

                    }

                    @Override
                    public void onNext(WordInfo info) {
                        map.put("chinese",info.getData().getCn_definition().getDefn());
                        mWordView.showWordInfo(map);
                    }
                });
    }

    @Override
    public void onDestory() {
        mLessonView = null;
        mWordView = null;
    }
}
