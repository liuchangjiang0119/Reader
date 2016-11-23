package com.shanbay.reader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Printer;
import android.view.View;

import com.shanbay.reader.R;

/**
 * Created by windfall on 16-11-22.
 */

public class LessonNumView extends View {
    private Paint mPaint,mTextPaint;
    private int lesson;
    private int color;
    private int textSize;
    private int width;
    private int height;
    private int textColor;
    public LessonNumView(Context context) {
        super(context);
    }

    public LessonNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lesson_view);
        color = typedArray.getColor(R.styleable.lesson_view_lesson_color, ContextCompat.getColor(context,R.color.colorPrimary));
        textSize = typedArray.getDimensionPixelSize(R.styleable.lesson_view_lesson_textSize,80);
        width = typedArray.getDimensionPixelSize(R.styleable.lesson_view_lesson_width,180);
        height = typedArray.getDimensionPixelSize(R.styleable.lesson_view_lesson_height,180);
        textColor = typedArray.getColor(R.styleable.lesson_view_lesson_textColor, Color.WHITE);
        typedArray.recycle();
        initPaint();
    }
    void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getSize(width,widthMeasureSpec),getSize(height,heightMeasureSpec));
    }

    private int getSize(int defaultSize,int measureSpec){
        int size = defaultSize;
        int Specmode = MeasureSpec.getMode(measureSpec);
        int SpecSize = MeasureSpec.getSize(measureSpec);
        switch (Specmode){
            case MeasureSpec.AT_MOST:
                size = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                size = SpecSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                size = defaultSize;
                break;
        }
        return size;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth()/2;
        int y = getHeight()/2;
        int r = x - 5;


        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float dy = -metrics.descent+(metrics.bottom-metrics.top)/2;
        float startY = y +dy;

        canvas.drawCircle(x,y,r,mPaint);
        canvas.drawText(String.valueOf(lesson),x,startY,mTextPaint);
    }

    public void setLesson(int lesson){
        this.lesson = lesson;
        this.invalidate();
    }
}
