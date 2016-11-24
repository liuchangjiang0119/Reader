package com.shanbay.reader.presenter.contract;


import com.shanbay.reader.model.Lesson;

import java.util.List;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonContract {
//    MVP中的契约类，为V和P提供接口
    public interface Presenter{
        void loadLesson(int unit);
        void loadWord(int level);

    }

    public interface LessonView {
        void showLesson(List<Lesson> list);
    }

    public interface WordView {
        void showWord(List<String> list);

    }


}
