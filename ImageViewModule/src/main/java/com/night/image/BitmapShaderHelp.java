package com.night.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

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
    //是否重置资源
    private boolean isResetRes = false;


    public BitmapShaderHelp() {
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
    }


    /**
     * 展示区域发生变化
     *
     * @param left   左侧
     * @param top    顶部
     * @param right  右侧
     * @param bottom 底部
     */
    public void onSizeChanged(int left, int top, int right, int bottom) {
        LogUtils.i("展示区域发生变化");
        mDisplayRect.setEmpty();
        mDisplayRect.set(left, top, right, bottom);
        mBitmapShader = null;
        mBitmapPaint.setShader(null);
    }

    /**
     * 设置透明度
     *
     * @param alpha 0-255
     */
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
    }

    /**
     * ImageBitmap
     *
     * @param drawable Drawable
     */
    public void setImageBitmap(@Nullable Drawable drawable) {
        if (drawable == null) {
            //重置资源
            isResetRes = true;
            return;
        }
        //处理资源
        isResetRes = false;
        mBitmapShader = null;
        mBitmapPaint.setShader(null);
        if (drawable instanceof BitmapDrawable) {
            this.mBitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            try {
                // 获取 drawable 长宽
                int drawableWidth = drawable.getIntrinsicWidth();
                int drawableHeight = drawable.getIntrinsicHeight();
                // 获取drawable的颜色格式
                Bitmap.Config mDrawableConfig = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                Bitmap otherBitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, mDrawableConfig);
                Canvas canvas = new Canvas(otherBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                this.mBitmap = otherBitmap;
            } catch (Exception e) {
                Bitmap errorBitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(errorBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                this.mBitmap = errorBitmap;
            }
        }
    }

    @Nullable
    public Paint getDrawPaint() {
        if (isResetRes) {
            return null;
        }
        if (mBitmap == null) {
            return null;
        }
        if (mBitmapShader == null) {
            initBitmapShader();
        }
        return mBitmapPaint;
    }

    /**
     * 初始化BitmapShader
     */
    private void initBitmapShader() {
        if (isResetRes) {
            return;
        }
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
        if (bitmapWidth * viewHeight > viewWidth * bitmapHeight) {
            scale = viewHeight / (float) bitmapHeight;
        } else {
            scale = viewWidth / (float) bitmapWidth;
        }
        float dx = (viewWidth - bitmapWidth * scale) / 2F;
        float dy = (viewHeight - bitmapHeight * scale) / 2F;
        mMatrix.set(null);
        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate(mDisplayRect.left + dx, mDisplayRect.top + dy);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }
}
