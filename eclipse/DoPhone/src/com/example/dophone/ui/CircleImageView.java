package com.example.dophone.ui;


import com.example.dophone.R;

import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Bitmap;  
import android.graphics.BitmapShader;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Matrix;  
import android.graphics.Paint;  
import android.graphics.RectF;  
import android.graphics.Shader;  
import android.graphics.drawable.BitmapDrawable;  
import android.graphics.drawable.ColorDrawable;  
import android.graphics.drawable.Drawable;  
import android.net.Uri;  
import android.util.AttributeSet;  
import android.widget.ImageView;  
  
public class CircleImageView extends ImageView {  
  
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;  
  
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;  
    private static final int COLORDRAWABLE_DIMENSION = 2;  
  
    private static final int DEFAULT_BORDER_WIDTH = 0;  
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;  
  
    private final RectF mDrawableRect = new RectF();  
    private final RectF mBorderRect = new RectF();  
  
    private final Matrix mShaderMatrix = new Matrix();  
    private final Paint mBitmapPaint = new Paint();  
    private final Paint mBorderPaint = new Paint();  
  
    private int mBorderColor = DEFAULT_BORDER_COLOR;  
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;  
  
    private Bitmap mBitmap;  
    private BitmapShader mBitmapShader;  
    private int mBitmapWidth;  
    private int mBitmapHeight;  
  
    private float mDrawableRadius;  
    private float mBorderRadius;  
  
    private boolean mReady;  
    private boolean mSetupPending;  
  
    public CircleImageView(Context context) {  
        super(context);  
  
        init();  
    }  
  
    public CircleImageView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
  
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
  
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);  
  
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);  
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);  
  
        a.recycle();  
  
        init();  
    }  
  
    private void init() {  
        super.setScaleType(SCALE_TYPE);  
        mReady = true;  
  
        if (mSetupPending) {  
            setup();  
            mSetupPending = false;  
        }  
    }  
  
    @Override  
    public ScaleType getScaleType() {  
        return SCALE_TYPE;  
    }  
  
    @Override  
    public void setScaleType(ScaleType scaleType) {  
        if (scaleType != SCALE_TYPE) {  
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));  
        }  
    }  
  
    @Override  
    public void setAdjustViewBounds(boolean adjustViewBounds) {  
        if (adjustViewBounds) {  
            throw new IllegalArgumentException("adjustViewBounds not supported.");  
        }  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        if (getDrawable() == null) {  
            return;  
        }  
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);  
        if (mBorderWidth != 0) {  
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);  
        }  
          
        /**********图片延迟加载时间的各个方法的生命周期(模仿网络图片的加载情�??)*****************/  
        //onSizeChanged --> onDraw --> setImageResource --> onDraw  
    }  
  
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
        setup();  
    }  
  
    public int getBorderColor() {  
        return mBorderColor;  
    }  
  
    public void setBorderColor(int borderColor) {  
        if (borderColor == mBorderColor) {  
            return;  
        }  
  
        mBorderColor = borderColor;  
        mBorderPaint.setColor(mBorderColor);  
        invalidate();  
    }  
  
    public int getBorderWidth() {  
        return mBorderWidth;  
    }  
  
    public void setBorderWidth(int borderWidth) {  
        if (borderWidth == mBorderWidth) {  
            return;  
        }  
  
        mBorderWidth = borderWidth;  
        System.out.println("setBorderWidth -- setup");  
        setup();  
    }  
  
    @Override  
    public void setImageBitmap(Bitmap bm) {  
        super.setImageBitmap(bm);  
        mBitmap = bm;  
        System.out.println("setImageBitmap -- setup");  
        setup();  
    }  
  
    @Override  
    public void setImageDrawable(Drawable drawable) {  
        super.setImageDrawable(drawable);  
        mBitmap = getBitmapFromDrawable(drawable);  
        setup();  
    }  
  
    @Override  
    public void setImageResource(int resId) {  
        super.setImageResource(resId);  
        mBitmap = getBitmapFromDrawable(getDrawable());  
        setup();  
    }  
  
    @Override  
    public void setImageURI(Uri uri) {  
        super.setImageURI(uri);  
        mBitmap = getBitmapFromDrawable(getDrawable());  
        setup();  
    }  
  
    private Bitmap getBitmapFromDrawable(Drawable drawable) {  
        if (drawable == null) {  
            return null;  
        }  
  
        if (drawable instanceof BitmapDrawable) {  
            return ((BitmapDrawable) drawable).getBitmap();  
        }  
  
        try {  
            Bitmap bitmap;  
  
            if (drawable instanceof ColorDrawable) {  
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);  
            } else {  
                bitmap = Bitmap.createBitmap(20, 20, BITMAP_CONFIG);  
            }  
  
            Canvas canvas = new Canvas(bitmap);  
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());  
            drawable.draw(canvas);  
            return bitmap;  
        } catch (OutOfMemoryError e) {  
            return null;  
        }  
    }  
  
    private void setup() {  
        if (!mReady) {  
            mSetupPending = true;  
            return;  
        }  
        //这个mBitmap比较妙，只有当有图片被设置以后才会开始执行下面的方法�??  
        if (mBitmap == null) {  
            return;  
        }  
  
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);  
  
        mBitmapPaint.setAntiAlias(true);  
        mBitmapPaint.setShader(mBitmapShader);  
  
        mBorderPaint.setStyle(Paint.Style.STROKE);  
        mBorderPaint.setAntiAlias(true);  
        mBorderPaint.setColor(mBorderColor);  
        mBorderPaint.setStrokeWidth(mBorderWidth);  
  
        mBitmapHeight = mBitmap.getHeight();  
        mBitmapWidth = mBitmap.getWidth();  
  
        //整个图像的显示区域：即全部的View大小区域�??  
        mBorderRect.set(0, 0, getWidth(), getHeight());  
        //Border的半径为长宽中取小的那一边，android中drawCircle的半径是内圆的半径，不是外圆的半径�??  
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);  
  
        //图片显示的区域：即View的大小区域减去边界的大小�??  
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);  
        //图片的半径大小取图片小边�??  
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);  
  
        updateShaderMatrix();  
        invalidate();  
    }  
  
    private void updateShaderMatrix() {  
        float scale;  
        float dx = 0;  
        float dy = 0;  
  
        mShaderMatrix.set(null);  
  
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {  
            scale = mDrawableRect.height() / (float) mBitmapHeight;  
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;  
        } else {  
            scale = mDrawableRect.width() / (float) mBitmapWidth;  
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;  
        }  
          
        mShaderMatrix.setScale(scale, scale);  
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);  
  
        mBitmapShader.setLocalMatrix(mShaderMatrix);  
    }  
  
}  