package com.shanbay.reader.view;


import android.content.Intent;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shanbay.reader.R;
import com.shanbay.reader.presenter.LessonPresenter;
import com.shanbay.reader.presenter.contract.LessonContract;
import com.shanbay.reader.utils.CustomClickMovementMethod;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import immortalz.me.library.TransitionsHeleper;



public class ContentActivity extends AppCompatActivity implements View.OnClickListener,LessonContract.WordView{

    @BindView(R.id.content_toolbar)
    Toolbar toolbar;

    @BindView(R.id.question_text)
    TextView question_text;
    @BindView(R.id.content_text)
    TextView content_text;
    @BindView(R.id.chinese_text)
    TextView chinese_text;
    @BindView(R.id.fab_word)
    FloatingActionButton fab_word;
    @BindView(R.id.fab_anwser)
    FloatingActionButton fab_answer;
    @BindView(R.id.fab_newwords)
    FloatingActionButton fab_newwords;
    @BindView(R.id.cancle_highlight)
    FloatingActionButton cancle;
    @BindView(R.id.fab_menu)
    FloatingActionsMenu mFloatingActionsMenu;
    @BindView(R.id.content_layout)
    NestedScrollView mNestedScrollView;



    private String content,question,answer,word,chinese,lesson;
    private SpannableString wordSpannable;
    private LessonPresenter mPresenter;
    private PopupWindow mWordLevelPopupWindow,mNewWordsPopupWindow;
    private SeekBar mSeekBar;
    private int progress;
    private Snackbar mSnackbar;
    private Spannable spannableCopy;
    private List<Integer> allWordsListStart;
    private List<Integer> wordListStart;
    private List<String> allWordsList;
    private List<String> wordList;
    private Thread allWordThread;
    private String wordInfo;
    private TextView wordInfoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        转场动画开源框架
        TransitionsHeleper.getInstance().show(this,null);

        initData();

        registerListener();
        setTitle("Lesson  "+lesson);
        allWordThread = new Thread(mRunnable);
        allWordThread.start();


        mPresenter = new LessonPresenter(this);


/*
点击屏幕判断FloatingActionsMenu是否打开，如果是则关闭，
配合android:descendantFocusability="blocksDescendants"
使ViewGroup覆盖控件获得焦点
 */
        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (mFloatingActionsMenu.isExpanded()){
                    content_text.setFocusable(false);
                    mFloatingActionsMenu.collapse();
                    return true;
                }
                return false;
            }
        });




    }
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            textfilter(content);
            wordHighLight();
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    content_text.setText(spannableCopy);
/*重写LinkedMovementMethod的onTouchEvent方法，在未设置clickablespan的文字点击时返回false
 * 即点击为设置sapn的文字时TextView不消耗事件，把事件传递给ViewGroup。
 *
* */
                    content_text.setMovementMethod(LinkMovementMethod.getInstance());
                    content_text.setHighlightColor(ContextCompat.getColor(ContentActivity.this,R.color.colorPrimary));
//                    不能高亮
//                    content_text.setMovementMethod(CustomClickMovementMethod.getInstance());

//                    content_text.setFocusable(false);
//                    content_text.setClickable(false);
//                    content_text.setLongClickable(false);

                    break;
                default:
                    break;
            }
        }
    };


    //  将过滤后的字符串中的每个单词放进list里，并为每个单词setSpan
    private void textfilter(String content){
        allWordsList = new ArrayList<>();
//      使用正则表达式过滤掉标点符号和多余空格
        Pattern p = Pattern.compile("[.,\"\\?!:'()*]");
        Matcher m = p.matcher(content.trim());
        String rm_pun = m.replaceAll(" ");
        p = Pattern.compile(" {2,}");
        m = p.matcher(rm_pun);
        String rm_space = m.replaceAll(" ");
        String rm_enter = rm_space.replace('\n',' ');
        List<String> list = Arrays.asList(rm_enter.split(" "));
        for (int i=0;i<list.size();i++){
            if (list.get(i).length()>2){
                allWordsList.add(list.get(i));
            }
        }


    }
//给每一个单词设置clickablespan，做到单词点击高亮
    private void wordHighLight(){
        SpannableString spannable = new SpannableString(content);
        Pattern p;
        Matcher m;
        allWordsListStart = new ArrayList<>();
        int start ,end;
        int a= 0;
//遍历每个单词，并设置clickspan
        for (int i=0;i<allWordsList.size();i++){
            p = Pattern.compile("\\b"+allWordsList.get(i)+"\\b");
            m = p.matcher(content);
            while (m.find()){
                start = m.start();
                end = m.end();
                if (allWordsListStart.contains(start)) continue;
                allWordsListStart.add(start);
                spannable.setSpan(getClickableSpan(),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

        }

        spannableCopy = spannable;

    }


//设置ClickSpan的onClick方法
    private ClickableSpan getClickableSpan(){

        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view;
                String s = textView.getText().subSequence(textView.getSelectionStart(),textView.getSelectionEnd()).toString();
                mPresenter.getWordInfo(s);
                showWordPopupInfo();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(ContentActivity.this,R.color.grey));
                ds.setUnderlineText(false);
            }
        };
    }


//初始化从intent得到的数据
    private void initData(){
        Intent intent = getIntent();
        content = "    " +intent.getStringExtra("content");

        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        word = intent.getStringExtra("word");
        chinese = intent.getStringExtra("chinese");
        lesson = intent.getStringExtra("lesson");
        question_text.setText(question);
        content_text.setText(content,TextView.BufferType.SPANNABLE);
        chinese_text.setText(chinese);
    }
//    注册监听器
    private void registerListener(){
        fab_word.setOnClickListener(this);
        fab_answer.setOnClickListener(this);
        fab_newwords.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }
//为每一个floatbutton设置按钮
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab_anwser:
                mFloatingActionsMenu.collapse();
                mSnackbar = Snackbar.make(mFloatingActionsMenu,answer,3000);
                mSnackbar.show();

                break;
            case R.id.fab_word:
                mFloatingActionsMenu.collapse();
                mPresenter.loadWord(progress);
                showWordLevelPopupWindow();
                break;
            case R.id.fab_newwords:
                mFloatingActionsMenu.collapse();
                showNewWordsPopupwindow();
                break;
            case R.id.cancle_highlight:
                mFloatingActionsMenu.collapse();
                content_text.setText(content);
                content_text.setText(spannableCopy);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//根据等级从单词表中查询单词，通过presenter传过来
    @Override
    public void showWord(List<String> list) {

        wordListStart = new ArrayList<>();

        if (list!=null){
            wordList = new ArrayList<>();
            wordList.addAll(list);

                    wordSpannable = new SpannableString(content);
                    String str;
                    Pattern pattern;
                    Matcher matcher;
                    int start,end;
//            查询文章中是否含有单词表中的单词，如果有为每个单词设置span
                    for (int i=0;i<wordList.size();i++) {
                        str = "\\b" + wordList.get(i) + "\\b";
                        pattern = Pattern.compile(str);
                        matcher = pattern.matcher(content);
                        while (matcher.find()) {
                            start = matcher.start();
                            end = matcher.end();
                            if (!wordListStart.contains(start)){
                                wordListStart.add(start);
                                wordSpannable.setSpan(new StyleSpan(Typeface.BOLD),start,end,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        content_text.setText(wordSpannable);

                        }
                    }


        }
     }


    @Override
    protected void onStop() {
        super.onStop();
        if (mNewWordsPopupWindow!=null&&mNewWordsPopupWindow.isShowing()){
            mNewWordsPopupWindow.dismiss();
        }
        if (mWordLevelPopupWindow!=null&&mWordLevelPopupWindow.isShowing()){
            mWordLevelPopupWindow.dismiss();
        }
    }

    @Override
    public void showWordInfo(Map<String, String> map) {
        if (map!=null){
            wordInfo = map.get("chinese");
            wordInfoTextView.setText(wordInfo);

        }else {
            Snackbar.make(mFloatingActionsMenu,R.string.net_error,Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError() {
        Snackbar.make(mFloatingActionsMenu,R.string.net_error,Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }

    //    显示每篇文章中的new word and expression
    private void showNewWordsPopupwindow(){
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_content,null,false);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_newwords,null,false);
        mNewWordsPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mNewWordsPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mNewWordsPopupWindow.setAnimationStyle(R.style.popup_anim);
        mNewWordsPopupWindow.setContentView(view);
        mNewWordsPopupWindow.setOutsideTouchable(true);
        mNewWordsPopupWindow.showAtLocation(rootView,Gravity.BOTTOM,0,0);
        TextView new_words = (TextView)view.findViewById(R.id.new_words);
        new_words.setText(word);


    }
    //弹出选择单词等级菜单
    private void showWordLevelPopupWindow(){
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_content,null,false);
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_wordlevel,null,false);
        mWordLevelPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mWordLevelPopupWindow.setContentView(popupView);
        mWordLevelPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mWordLevelPopupWindow.setAnimationStyle(R.style.popup_anim);
        mWordLevelPopupWindow.setFocusable(true);
        mWordLevelPopupWindow.setOutsideTouchable(true);
        mWordLevelPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        mSeekBar = (SeekBar)popupView.findViewById(R.id.seekBar);
        mSeekBar.setProgress(progress);

        final TextView level_text = (TextView) popupView.findViewById(R.id.text_level);
        level_text.setText(String.valueOf(progress));
        final TextView content_popup = (TextView) rootView.findViewById(R.id.content_text);
//        seekbar用来选择单词等级
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int i, boolean b) {
                progress = i;
                content_popup.setText(spannableCopy);
                level_text.setText(String.valueOf(i));
                mPresenter.loadWord(i);


            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {

            }
        });

    }


    //    显示单词翻译
    private void showWordPopupInfo(){
        View root_view = LayoutInflater.from(this).inflate(R.layout.activity_content,null);
        View word_view = LayoutInflater.from(this).inflate(R.layout.popup_word_info,null);
        PopupWindow popupWindow = new PopupWindow(word_view,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setContentView(word_view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//        popupWindow.setAnimationStyle(R.style.popup_anim);
        popupWindow.showAtLocation(root_view,Gravity.BOTTOM,0,0);
        wordInfoTextView = (TextView)word_view.findViewById(R.id.word_info);
        wordInfoTextView.setText(R.string.loading);
    }
}
