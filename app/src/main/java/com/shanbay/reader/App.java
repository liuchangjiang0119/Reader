package com.shanbay.reader;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shanbay.reader.dao.DaoMaster;
import com.shanbay.reader.dao.DaoSession;
import com.shanbay.reader.dao.LessonDao;
import com.shanbay.reader.dao.WordDao;
import com.shanbay.reader.model.Lesson;
import com.shanbay.reader.model.Word;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        mPreferences = getSharedPreferences("opencount",MODE_PRIVATE);
        mEditor = mPreferences.edit();
        boolean isFirstOpen = mPreferences.getBoolean("FirstOpen",true);
        initLessonDateBase();
        initWordDateBase();

        if (isFirstOpen){
            paserJson();
            mEditor.putBoolean("FirstOpen",false);
            mEditor.commit();
        }

        QueryBuilder<Lesson> queryBuilder = getLessonSession().getLessonDao().queryBuilder();





    }

    void paserJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseLessonJson();
                parseWordJson();

            }

        }).start();
    }




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

    void parseLessonJson(){
        InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.nec4));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
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
        String line = "";
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

    public SQLiteDatabase getWordDatabase() {
        return mWordDatabase;
    }

    public SQLiteDatabase getLessonDataBase() {
        return mLessonDataBase;
    }
}
