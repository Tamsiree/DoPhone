package com.example.dophone.view;

import com.example.dophone.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class ButtonView extends View {
	Path path;
	Bitmap bm;
	Paint paint;
	public ButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 使用软件加速
		// TODO Auto-generated constructor stub
		bm = BitmapFactory.decodeResource(getResources(), R.drawable.button1);
		path = new Path();
		path.addCircle(bm.getWidth()/2, bm.getHeight()/2, bm.getHeight()/2, Direction.CCW);
		paint = new Paint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获取的测量模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 获取的测量的数值
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = 0;
		int hight = 0;

		if (widthMode == MeasureSpec.EXACTLY) {
			// Parent has told us how big to be. So be it.
			width = widthSize;
		} else {// wrap_content
			if (isInEditMode()) {
				width = 239;

			} else {
				width = bm.getWidth();

			}
		}

		if (heightMode == MeasureSpec.EXACTLY) {

			hight = heightSize;
		} else {// wrap_content
			if (isInEditMode()) {
				hight = 239;

			} else {
				hight = bm.getHeight();

			}
		}
		setMeasuredDimension(width, hight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isInEditMode()) {
			return;
		}
		canvas.save();
		canvas.clipPath(path);
		canvas.drawBitmap(bm, 0, 0,paint);
		canvas.restore();
	}
}
