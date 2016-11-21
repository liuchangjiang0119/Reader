package com.shanbay.reader.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shanbay.reader.R;
import com.shanbay.reader.presenter.LessonPresenter;
import com.shanbay.reader.presenter.contract.LessonContract;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener,LessonContract.WordView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.question_text)
    TextView question_text;
    @BindView(R.id.content_text)
    TextView content_text;
    @BindView(R.id.chinese_text)
    TextView chinese_text;

    private String content,question,answer,word,chinese,lesson;
    private SpannableString mSpannable;
    private LessonPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);
        initData();
        setTitle("Lesson  "+lesson);
        question_text.setText(question);
        content_text.setText(mSpannable);
        chinese_text.setText(chinese);
        mPresenter = new LessonPresenter(this);
        mPresenter.loadWord(5);


    }

    void initData(){
        Intent intent = getIntent();
        content = "  " +intent.getStringExtra("content");
        mSpannable = new SpannableString(content);
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        word = intent.getStringExtra("word");
        chinese = intent.getStringExtra("chinese");
        lesson = intent.getStringExtra("lesson");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab:
                if (content.indexOf("We")>-1) {

                    int start = content.indexOf("We");
                    int end = start + "We".length();
                    mSpannable.setSpan(new StyleSpan(Typeface.BOLD), start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    content_text.setText(mSpannable);
                }
                break;
        }
    }

    @Override
    public void showWord() {

    }
}
