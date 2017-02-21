package com.shanbay.reader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.shanbay.reader.R;

/**
 * Created by windfall on 16-11-22.
 */
//自定义view为unit序号
public class UnitView extends View {
	private Paint mPaint, mTextPaint;
	private int unit;
	private int color;
	private int textSize;
	private int textColor;

	public UnitView(Context context) {
		super(context);
	}

	public UnitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.unit_view);
		color = typedArray.getColor(R.styleable.unit_view_unit_color, ContextCompat.getColor(context, R.color.colorPrimary));
		textSize = typedArray.getDimensionPixelSize(R.styleable.unit_view_unit_textSize, 100);
		textColor = typedArray.getColor(R.styleable.unit_view_unit_textColor, Color.WHITE);
		typedArray.recycle();
		initPaint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(color);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(textColor);

		mTextPaint.setTextSize(textSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int x = getWidth();
		int y = getHeight();

		canvas.drawRect(0, 0, x, y, mPaint);
		canvas.drawText("Unit " + unit, x / 2 - textSize, y / 2 + textSize / 2, mTextPaint);

	}

	public void setUnit(int unit) {
		this.unit = unit;
		this.invalidate();
	}
}
