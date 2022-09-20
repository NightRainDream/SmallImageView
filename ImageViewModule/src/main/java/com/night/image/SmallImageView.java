package com.night.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * -----------------------------------------------------
 * 任意圆角ImageView
 * <p>
 * 项目地址:https://github.com/NightRainDream/SmallImageView
 * -----------------------------------------------------
 */
public class SmallImageView extends AppCompatImageView {
    private static final String TAG = "SmallImageView";
    private final Paint mBitmapPaint = new Paint();
    private final Path mPath = new Path();
    private final RectF mDisplayRect = new RectF();
    private final Matrix mMatrix = new Matrix();
    private final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    //展示宽高
    private int displayWidth = 0;
    private int displayHeight = 0;
    //顶部左侧圆角角度
    private float mTopLeftRadius = 0F;
    private final RectF mTopLeftRectF = new RectF();
    //顶部右侧圆角角度
    private float mTopRightRadius = 0F;
    private final RectF mTopRightRectF = new RectF();
    //底部左侧圆角角度
    private float mBottomLeftRadius = 0F;
    private final RectF mBottomLeftRectF = new RectF();
    //底部右侧圆角角度
    private float mBottomRightRadius = 0F;
    private final RectF mBottomRightRectF = new RectF();
    //全局圆角角度
    private float mRadius = 0F;

    {
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
    }

    public SmallImageView(@NonNull Context context) {
        this(context, null);
    }

    public SmallImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: ");
        displayWidth = w - getPaddingLeft() - getPaddingRight();
        displayHeight = h - getPaddingTop() - getPaddingBottom();
        mDisplayRect.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + displayWidth, getPaddingTop() + displayHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //过滤角度值
        formatRadius();
        //不设置圆角的情况下直接走原生
        if (mTopLeftRadius == 0F && mTopRightRadius == 0F && mBottomLeftRadius == 0F && mBottomRightRadius == 0F) {
            super.onDraw(canvas);
            return;
        }
        Bitmap mSourceBitmap = getBitmap();
        //未正确获取到Bitmap情况下，走原生
        if (mSourceBitmap == null) {
            super.onDraw(canvas);
            return;
        }
        //对Bitmap进行缩放处理
        Bitmap bitmap = scaleBitmap(mSourceBitmap, displayWidth, displayHeight);
        int saved = canvas.saveLayer(mDisplayRect, mBitmapPaint);
        if (isRound()) {
            //普通圆角
            Log.i(TAG, "onDraw: 通过drawRoundRect()绘制");
            canvas.drawRoundRect(mDisplayRect, mRadius, mRadius, mBitmapPaint);
        } else {
            //部分圆角
            Log.i(TAG, "onDraw: 通过drawPath()绘制");
            //初始化绘制Path圆角矩形
            initRadiusRectF();
            canvas.drawPath(getPath(), mBitmapPaint);
        }
        mBitmapPaint.setXfermode(xfermode);
        // 绘制bitmap
        canvas.drawBitmap(bitmap, getPaddingLeft(), getPaddingTop(), mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        canvas.restoreToCount(saved);
    }


    /**
     * 设置圆角直径
     *
     * @param radius 圆角直径
     */
    public void setRadius(final float radius) {
        Log.i(TAG, "setRadius: " + radius);
        this.mRadius = radius;
    }

    /**
     * 设置圆角直径
     *
     * @param topLeft     左上角圆角直径
     * @param topRight    右上角圆角直径
     * @param bottomLeft  左下角圆角直径
     * @param bottomRight 右下角圆角直径
     */
    public void setRadius(final float topLeft, final float topRight, final float bottomLeft, final float bottomRight) {
        Log.i(TAG, "setRadius==>topLeft" + topLeft + "||topRight" + topRight + "||bottomLeft" + bottomLeft + "||bottomRight" + bottomRight);
        this.mTopLeftRadius = topLeft;
        this.mTopRightRadius = topRight;
        this.mBottomLeftRadius = bottomLeft;
        this.mBottomRightRadius = bottomRight;
    }


    /**
     * 获取不规则弧度Path
     *
     * @return Path
     */
    private Path getPath() {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingTop = getPaddingTop();
        mPath.reset();
        //顶部路径
        mPath.moveTo(mPaddingLeft, mPaddingTop + mTopLeftRadius);
        if (!mTopLeftRectF.isEmpty()) {
            //绘制圆弧
            mPath.arcTo(mTopLeftRectF, 180F, 90F);
        } else {
            //绘制直角
            mPath.lineTo(mPaddingLeft, mPaddingTop);
        }
        mPath.lineTo(mPaddingLeft + displayWidth - mTopRightRadius, mPaddingTop);
        //右侧路径
        if (!mTopRightRectF.isEmpty()) {
            //绘制圆弧
            mPath.arcTo(mTopRightRectF, 270F, 90F);
        } else {
            //绘制直角
            mPath.lineTo(mPaddingLeft + displayWidth, mPaddingTop);
        }
        mPath.lineTo(mPaddingLeft + displayWidth, mPaddingTop + displayHeight - mBottomRightRadius);
        //绘制底部
        if (!mBottomRightRectF.isEmpty()) {
            //绘制圆弧
            mPath.arcTo(mBottomRightRectF, 0F, 90F);
        } else {
            mPath.lineTo(mPaddingLeft + displayWidth, mPaddingTop + displayHeight);
        }
        mPath.lineTo(mPaddingLeft + mBottomLeftRadius, mPaddingTop + displayHeight);
        //绘制右侧
        if (!mBottomLeftRectF.isEmpty()) {
            //绘制圆弧
            mPath.arcTo(mBottomLeftRectF, 90F, 90F);
        } else {
            mPath.lineTo(mPaddingLeft, mPaddingTop + displayHeight);
        }
        mPath.close();
        return mPath;
    }

    /**
     * 是否是正常圆角
     *
     * @return 是否是正常圆角
     */
    private boolean isRound() {
        return (mRadius != 0F);
    }

    /**
     * 对Bitmap进行缩放裁剪处理
     *
     * @param bitmap     Bitmap
     * @param viewWidth  展示的宽
     * @param viewHeight 展示的高
     * @return 处理后的Bitmap
     */
    private Bitmap scaleBitmap(@NonNull Bitmap bitmap, int viewWidth, int viewHeight) {
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
        if (mBitmapWidth == viewWidth && mBitmapHeight == viewHeight) {
            Log.i(TAG, "scaleBitmap: 使用原始Bitmap");
            return bitmap;
        }
        float mScaleX = (float) viewWidth / mBitmapWidth;
        float mScaleF = (float) viewHeight / mBitmapHeight;
        float mScale = Math.max(mScaleX, mScaleF);
        mMatrix.set(null);
        mMatrix.setScale(mScale, mScale);
        Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, mBitmapWidth, mBitmapHeight, mMatrix, true);
        int mScaleWidth = scaleBitmap.getWidth();
        int mScaleHeight = scaleBitmap.getHeight();
        if (mScaleWidth == viewWidth && mScaleHeight == viewHeight) {
            Log.i(TAG, "scaleBitmap: 使用缩放后Bitmap");
            return scaleBitmap;
        }
        int indexX = (mScaleWidth - viewWidth) / 2;
        int indexY = (mScaleHeight - viewHeight) / 2;
        Log.i(TAG, "scaleBitmap: 使用裁剪后Bitmap");
        Bitmap cutBitmap = Bitmap.createBitmap(scaleBitmap, indexX, indexY, viewWidth, viewHeight);
        scaleBitmap.recycle();
        return cutBitmap;
    }

    /**
     * 初始化自定义属性
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        //初始化自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SmallImageView);
        mRadius = array.getDimension(R.styleable.SmallImageView_customRadius, mRadius);
        //左上角
        mTopLeftRadius = array.getDimension(R.styleable.SmallImageView_customRadiusTopLeft, mTopLeftRadius);
        //右上角
        mTopRightRadius = array.getDimension(R.styleable.SmallImageView_customRadiusTopRight, mTopRightRadius);
        //左下角
        mBottomLeftRadius = array.getDimension(R.styleable.SmallImageView_customRadiusBottomLeft, mBottomLeftRadius);
        //右下角
        mBottomRightRadius = array.getDimension(R.styleable.SmallImageView_customRadiusBottomRight, mBottomRightRadius);
        array.recycle();
        Log.i(TAG, "过滤前圆角直径==>mTopLeftRadius:" + mTopLeftRadius + "||mTopRightRadius:" + mTopRightRadius + "||mBottomRightRadius:" + mBottomRightRadius + "||mBottomLeftRadius:" + mBottomLeftRadius + "||mRadius:" + mRadius);
    }


    /**
     * 过滤圆弧直接
     */
    private void formatRadius() {
        int minDisplaySize = Math.min(displayWidth, displayHeight);
        //全局直径优先
        if (mRadius > 0) {
            if (mRadius > minDisplaySize) {
                mRadius = minDisplaySize;
            }
            mTopLeftRadius = mRadius;
            mTopRightRadius = mRadius;
            mBottomLeftRadius = mRadius;
            mBottomRightRadius = mRadius;
        } else {
            //防止设置圆弧直径过大
            if (mTopLeftRadius > minDisplaySize) {
                mTopLeftRadius = minDisplaySize;
            }
            if (mTopRightRadius > minDisplaySize) {
                mTopRightRadius = minDisplaySize;
            }
            if (mBottomRightRadius > minDisplaySize) {
                mBottomRightRadius = minDisplaySize;
            }
            if (mBottomLeftRadius > minDisplaySize) {
                mBottomLeftRadius = minDisplaySize;
            }
            //如果四个角相等，相当于设置了全局的角度
            if (mTopLeftRadius == mTopRightRadius && mTopRightRadius == mBottomRightRadius && mBottomRightRadius == mBottomLeftRadius) {
                mRadius = mTopLeftRadius;
            }
        }
        Log.i(TAG, "过滤后圆角直径==>mTopLeftRadius:" + mTopLeftRadius + "||mTopRightRadius:" + mTopRightRadius + "||mBottomRightRadius:" + mBottomRightRadius + "||mBottomLeftRadius:" + mBottomLeftRadius + "||mRadius:" + mRadius);
    }

    /**
     * 初始化Path路径圆角矩形
     */
    private void initRadiusRectF() {
        if (!isRound()) {
            //这里乘以2是因为Path绘制弧形是以矩形的中心为中点，所以需要2倍大的矩形才能得到我们要的效果
            if (mTopLeftRadius != 0) {
                float rectTopLeft = mTopLeftRadius * 2;
                mTopLeftRectF.set(getPaddingLeft(), getPaddingTop(), rectTopLeft + getPaddingLeft(), rectTopLeft + getPaddingTop());
            }
            if (mTopRightRadius != 0) {
                float rectTopRight = mTopRightRadius * 2;
                mTopRightRectF.set(getPaddingLeft() + displayWidth - rectTopRight, getPaddingTop(), getPaddingLeft() + displayWidth, rectTopRight + getPaddingTop());
            }
            if (mBottomRightRadius != 0) {
                float rectBottomRight = mBottomRightRadius * 2;
                mBottomRightRectF.set(getPaddingLeft() + displayWidth - rectBottomRight, getPaddingTop() + displayHeight - rectBottomRight, getPaddingLeft() + displayWidth, getPaddingTop() + displayHeight);
            }
            if (mBottomLeftRadius != 0) {
                float rectBottomLeft = mBottomLeftRadius * 2;
                mBottomLeftRectF.set(getPaddingLeft(), getPaddingTop() + displayHeight - rectBottomLeft, getPaddingLeft() + rectBottomLeft, getPaddingTop() + displayHeight);
            }
        }
    }

    @Nullable
    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Log.i(TAG, "getBitmap: BitmapDrawable");
            return ((BitmapDrawable) getDrawable()).getBitmap();
        } else {
            Log.i(TAG, "getBitmap: 其他类型的Drawable");
            try {
                Bitmap otherBitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(otherBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return otherBitmap;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
