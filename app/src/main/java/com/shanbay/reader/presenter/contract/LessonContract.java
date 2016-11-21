package com.shanbay.reader.presenter.contract;


import com.shanbay.reader.model.Lesson;

import java.util.List;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonContract {
    public interface Presenter{
        void loadLesson(int unit);
        void loadWord(int level);

    }

    public interface LessonView {
        void showLesson(List<Lesson> list);
    }

    public interface WordView {
        void showWord();
    }
}
