package com.shanbay.reader;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.shanbay.reader.dao.DaoMaster;
import com.shanbay.reader.dao.DaoSession;
import com.shanbay.reader.dao.LessonDao;
import com.shanbay.reader.dao.WordDao;
import com.shanbay.reader.model.bean.Lesson;
import com.shanbay.reader.model.bean.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by windfall on 16-11-18.
 */

public class App extends Application {

    private static App instance;


    private Lesson mLesson;
    private Word mWord;
    private DaoMaster.DevOpenHelper mLessonHelper,mWordHelper;
    private SQLiteDatabase mLessonDataBase,mWordDatabase;
    private DaoSession mLessonSession,mWordSession;
    private DaoMaster mLessonMaster,mWordMaster;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    public static App getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        通过SharePreferences判断是否是第一次打开
        mPreferences = getSharedPreferences("opencount",MODE_PRIVATE);
        mEditor = mPreferences.edit();
        boolean isFirstOpen = mPreferences.getBoolean("FirstOpen",true);
        initLessonDateBase();
        initWordDateBase();

        if (isFirstOpen){
//            如果是第一次打开，进行数据库的插入工作
            paserJson();
            mEditor.putBoolean("FirstOpen",false);
            mEditor.commit();
        }
        mEditor.commit();

    }
//需要分别把lesson内容和单词表内容分别导入数据库，应为比较耗时所以新建一个线程
    void paserJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseLessonJson();
                parseWordJson();

            }

        }).start();
    }



//数据库（GreenDao）初始化
    void initLessonDateBase(){
        mLessonHelper = new DaoMaster.DevOpenHelper(this,"lesson",null);
        mLessonDataBase = mLessonHelper.getWritableDatabase();
        mLessonMaster = new DaoMaster(mLessonDataBase);
        mLessonSession = mLessonMaster.newSession();

    }
    void initWordDateBase(){
        mWordHelper = new DaoMaster.DevOpenHelper(this,"word",null);
        mWordDatabase = mWordHelper.getWritableDatabase();
        mWordMaster = new DaoMaster(mWordDatabase);
        mWordSession = mWordMaster.newSession();
    }
//从json解析数据
    void parseLessonJson(){
        InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.nce4));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line ;
        StringBuilder builder = new StringBuilder();
        try {

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();

            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            JSONObject itemJson;
            LessonDao lessonDao = getLessonSession().getLessonDao();

            for (int i = 0; i < jsonArray.length(); i++) {
                itemJson = (JSONObject) jsonArray.get(i);
                mLesson = new Lesson(null,itemJson.getString("lesson"),itemJson.getString("title"),
                        itemJson.getString("question"),itemJson.getString("content"),itemJson.getString("answer"),
                        itemJson.getString("word"),itemJson.getString("chinese"));

                lessonDao.insert(mLesson);
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void parseWordJson(){
        InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.nce4_words));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line ;
        try {
            while ((line = bufferedReader.readLine())!=null){
                builder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("word");
            WordDao wordDao = getWordSession().getWordDao();
            JSONObject itemJson;

            String key;
            String value;

            for (int i=0;i<jsonArray.length();i++){
                itemJson = jsonArray.getJSONObject(i);
                key = itemJson.keys().next();
                value = itemJson.getString(key);
                mWord = new Word(null,key,value);
                wordDao.insert(mWord);
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public DaoSession getLessonSession() {
        return mLessonSession;
    }

    public DaoSession getWordSession() {
        return mWordSession;
    }

}
