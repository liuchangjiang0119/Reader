package com.shanbay.reader.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by windfall on 16-11-21.
 */
@Entity
public class Word {
    @Id
    public Long id;

    public String word;
    public String level;
    public String getLevel() {
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getWord() {
        return this.word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 800986185)
    public Word(Long id, String word, String level) {
        this.id = id;
        this.word = word;
        this.level = level;
    }
    @Generated(hash = 3342184)
    public Word() {
    }
    
}
