package com.shanbay.reader.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by windfall on 16-11-20.
 */
@Entity
public class Lesson {
    @Id
    public Long id;
    public String lesson;
    public String title;
    public String question;
    public String content;
    public String answer;
    public String word;
    public String chinese;
    public String getChinese() {
        return this.chinese;
    }
    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
    public String getWord() {
        return this.word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getAnswer() {
        return this.answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLesson() {
        return this.lesson;
    }
    public void setLesson(String lesson) {
        this.lesson = lesson;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1093428238)
    public Lesson(Long id, String lesson, String title, String question,
            String content, String answer, String word, String chinese) {
        this.id = id;
        this.lesson = lesson;
        this.title = title;
        this.question = question;
        this.content = content;
        this.answer = answer;
        this.word = word;
        this.chinese = chinese;
    }
    @Generated(hash = 1669664117)
    public Lesson() {
    }
}
