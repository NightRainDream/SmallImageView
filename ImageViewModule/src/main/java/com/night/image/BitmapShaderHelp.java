package com.night.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

class BitmapShaderHelp {
    //绘制Bitmap画笔
    private final Paint mBitmapPaint = new Paint();
    //缩放矩形
    private final Matrix mMatrix = new Matrix();
    //实际展示区域
    private final RectF mDisplayRect = new RectF();
    //原始Bitmap
    private Bitmap mBitmap;
    //BitmapShader
    private BitmapShader mBitmapShader = null;


    public BitmapShaderHelp() {
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
    }


    public void onSizeChanged(int left, int top, int right, int bottom) {
        LogUtils.i("展示区域发生变化");
        mDisplayRect.set(left, top, right, bottom);
        mBitmapShader = null;
    }

    /**
     * ImageBitmap
     *
     * @param drawable Drawable
     */
    public void setImageBitmap(@Nullable Drawable drawable) {
        if (drawable == null) {
            return;
        }
        mBitmapShader = null;
        if (drawable instanceof BitmapDrawable) {
            this.mBitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            try {
                Bitmap otherBitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(otherBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                this.mBitmap = otherBitmap;
            } catch (Exception e) {
                LogUtils.e("获取Bitmap错误==>" + e.getMessage());
            }
        }
    }

    @Nullable
    public Paint getDrawPaint() {
        if (mBitmap == null) {
            return null;
        }
        if (mBitmapShader == null) {
            initBitmapShader();
        }
        return mBitmapPaint;
    }

    private void initBitmapShader() {
        if (mBitmap == null) {
            return;
        }
        int bitmapHeight = mBitmap.getHeight();
        int bitmapWidth = mBitmap.getWidth();
        float viewWidth = mDisplayRect.width();
        float viewHeight = mDisplayRect.height();
        LogUtils.i("View尺寸==>宽:"+viewWidth+"||高:"+viewHeight);
        LogUtils.i("Bitmap尺寸==>宽:"+bitmapWidth+"||高:"+bitmapHeight);
        float mScaleX = viewWidth / bitmapWidth;
        float mScaleF = viewHeight / bitmapHeight;
        float mScale = Math.max(mScaleX, mScaleF);
        LogUtils.i("最大缩放比==>"+mScale);
        mMatrix.set(null);
        mMatrix.setScale(mScale, mScale);
        mMatrix.postTranslate((viewWidth - bitmapWidth * mScale) / 2F,(viewHeight - bitmapHeight * mScale) / 2F);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }
}
