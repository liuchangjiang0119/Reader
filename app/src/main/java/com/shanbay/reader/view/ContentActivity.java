package com.shanbay.reader.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private SpannableString mSpannable;
    private LessonPresenter mPresenter;
    private PopupWindow mWordLevelPopupWindow,mNewWordsPopupWindow;
    private SeekBar mSeekBar;
    private int progress;
    private SpannableString allwords;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TransitionsHeleper.getInstance().show(this,null);

        initData();
        registerListener();
        setTitle("Lesson  "+lesson);
        mFloatingActionsMenu.setEnabled(true);
        mPresenter = new LessonPresenter(this);
        allwords  = new SpannableString(content);
        wordHighLight(content_text);
        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (mFloatingActionsMenu.isExpanded()){
                    mFloatingActionsMenu.collapse();
                }
                if (mSnackbar.isShown()){
                    mSnackbar.dismiss();
                }
                return false;
            }
        });



    }

    void wordHighLight(TextView textView){
        Spannable spanString = (Spannable)textView.getText();
        Pattern p = Pattern.compile("[.,\"\\?!:'()*]");
        Matcher m = p.matcher(content.trim());
        String rm_pun = m.replaceAll(" ");
        p = Pattern.compile(" {2,}");
        m = p.matcher(rm_pun);
        String rm_space = m.replaceAll(" ");
        String rm_enter = rm_space.replace('\n',' ');


        int start ,end;
        List<String> allWordsList ;
        allWordsList = Arrays.asList(rm_enter.split(" "));

        for (int i=0;i<allWordsList.size();i++){
            p = Pattern.compile("\\b"+allWordsList.get(i)+"\\b");
            m = p.matcher(content);
            while (m.find()){

                start = m.start();
                end = m.end();

                spanString.setSpan(getClickableSpan(),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        content_text.setText(spanString);
        content_text.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public ClickableSpan getClickableSpan(){

        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView)view;
                String s = textView.getText().subSequence(textView.getSelectionStart(),textView.getSelectionEnd()).toString();

                Pattern p = Pattern.compile("\\b"+s+"\\b");
                Matcher matcher = p.matcher(textView.getText().toString());
                int start,end;

                while (matcher.find()){
                    start = matcher.start();
                    end = matcher.end();
                    allwords.setSpan(new StyleSpan(Typeface.BOLD),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
                content_text.setText(allwords);
                wordHighLight(content_text);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.GRAY);
                ds.setUnderlineText(false);
            }
        };
    }



    void initData(){
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
    void registerListener(){
        fab_word.setOnClickListener(this);
        fab_answer.setOnClickListener(this);
        fab_newwords.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab_anwser:
                mFloatingActionsMenu.collapse();
                mSnackbar = Snackbar.make(mFloatingActionsMenu,answer,10000);
                mSnackbar.show();
                mSnackbar.setAction(R.string.cancle, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSnackbar.dismiss();
                    }
                });
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

    @Override
    public void showWord(List<String> list) {

        if (list!=null){
            mSpannable = new SpannableString(content);
            String str;
            Pattern pattern;
            Matcher matcher;
            int start,end;
            for (int i=0;i<list.size();i++){
                str = "\\b"+list.get(i)+"\\b";
                pattern = Pattern.compile(str);
                matcher = pattern.matcher(content);
                while (matcher.find()){

                    start = matcher.start();
                    end = matcher.end();

                    mSpannable.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                content_text.setText(mSpannable);



            }
        }

    }


    @Override
    public void getAllword(List<String> list) {

        }



    void showWordLevelPopupWindow(){
        View root_view = LayoutInflater.from(this).inflate(R.layout.activity_content,null,false);
        View popup_view = LayoutInflater.from(this).inflate(R.layout.popup_wordlevel,null,false);
        mWordLevelPopupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mWordLevelPopupWindow.setContentView(popup_view);
        mWordLevelPopupWindow.setFocusable(true);
        mWordLevelPopupWindow.setOutsideTouchable(true);
        mWordLevelPopupWindow.showAtLocation(root_view, Gravity.BOTTOM,0,0);
        mSeekBar = (SeekBar)popup_view.findViewById(R.id.seekBar);
        mSeekBar.setProgress(progress);

        final TextView level_text = (TextView) popup_view.findViewById(R.id.text_level);
        level_text.setText(progress+"");
        final TextView content_popup = (TextView) root_view.findViewById(R.id.content_text);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int i, boolean b) {
                progress = i;
                content_popup.setText(content);
                level_text.setText(i+"");
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
    void showNewWordsPopupwindow(){
        View root_view = LayoutInflater.from(this).inflate(R.layout.activity_content,null,false);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_newwords,null,false);
        mNewWordsPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mNewWordsPopupWindow.setContentView(view);
        mNewWordsPopupWindow.setOutsideTouchable(true);
        mNewWordsPopupWindow.showAtLocation(root_view,Gravity.BOTTOM,0,0);
        TextView new_words = (TextView)view.findViewById(R.id.new_words);
        new_words.setText(word);


    }
}
