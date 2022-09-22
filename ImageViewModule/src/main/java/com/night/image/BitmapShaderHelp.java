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
        mDisplayRect.setEmpty();
        mDisplayRect.set(left, top, right, bottom);
        mBitmapShader = null;
        mBitmapPaint.setShader(null);
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
        mBitmapPaint.setShader(null);
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
        //资源尺寸
        int bitmapHeight = mBitmap.getHeight();
        int bitmapWidth = mBitmap.getWidth();
        //展示区域尺寸
        float viewWidth = mDisplayRect.width();
        float viewHeight = mDisplayRect.height();
        float scale;
        float x = bitmapWidth - viewWidth;
        float y = bitmapHeight - viewHeight;
        if (x < y) {
            //宽度
            scale = viewWidth / bitmapWidth;
        } else {
            //高度
            scale = viewHeight / bitmapHeight;
        }
        float dx = (viewWidth - bitmapWidth*scale ) / 2F;
        float dy = (viewHeight -bitmapHeight*scale) / 2F;
        mMatrix.set(null);
        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate(mDisplayRect.left+dx, mDisplayRect.top+dy);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }
}
