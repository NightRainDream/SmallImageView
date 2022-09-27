package com.night.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

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
    private final Path mPath = new Path();
    private final RectF mDisplayRect = new RectF();
    private BitmapShaderHelp mBitmapShaderHelp;
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
    //是否强制正方形View
    private boolean isSquare = false;


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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isSquare){
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        displayWidth = w - getPaddingLeft() - getPaddingRight();
        displayHeight = h - getPaddingTop() - getPaddingBottom();
        mDisplayRect.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
        HELP().onSizeChanged(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        displayWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        displayHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        mDisplayRect.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + displayWidth, getPaddingTop() + displayHeight);
        HELP().onSizeChanged(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + displayWidth, getPaddingTop() + displayHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //过滤角度值
        formatRadius();
        if (mTopLeftRadius == 0F && mTopRightRadius == 0F && mBottomLeftRadius == 0F && mBottomRightRadius == 0F) {
            super.onDraw(canvas);
            return;
        }
        Paint mImagePaint = HELP().getDrawPaint();
        if (mImagePaint == null) {
            super.onDraw(canvas);
            return;
        }
        if (isRound()) {
            canvas.drawRoundRect(mDisplayRect, mRadius, mRadius, mImagePaint);
        } else {
            initRadiusRectF();
            canvas.drawPath(getPath(), mImagePaint);
        }
    }


    /**
     * 设置圆角直径
     *
     * @param radius 圆角直径
     */
    public void setRadius(final float radius) {
        LogUtils.i("设置圆角==>" + radius);
        if (radius == mRadius) {
            return;
        }
        this.mRadius = radius;
        invalidate();
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
        LogUtils.i("设置圆角==>topLeft" + topLeft + "||topRight" + topRight + "||bottomLeft" + bottomLeft + "||bottomRight" + bottomRight);
        if (topLeft == mTopLeftRadius && topRight == mTopRightRadius && bottomLeft == mBottomLeftRadius && bottomRight == mBottomRightRadius) {
            return;
        }
        this.mRadius = 0;
        this.mTopLeftRadius = topLeft;
        this.mTopRightRadius = topRight;
        this.mBottomLeftRadius = bottomLeft;
        this.mBottomRightRadius = bottomRight;
        invalidate();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        HELP().setImageBitmap(getDrawable());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        HELP().setImageBitmap(getDrawable());
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        HELP().setImageBitmap(getDrawable());
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        HELP().setAlpha((int) alpha);
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
        //是否强制正方形View
        isSquare = array.getBoolean(R.styleable.SmallImageView_customSquare,isSquare);
        array.recycle();
        LogUtils.i("过滤前圆角直径==>mTopLeftRadius:" + mTopLeftRadius + "||mTopRightRadius:" + mTopRightRadius + "||mBottomRightRadius:" + mBottomRightRadius + "||mBottomLeftRadius:" + mBottomLeftRadius + "||mRadius:" + mRadius);
    }


    /**
     * 过滤圆弧直接
     */
    private void formatRadius() {
        int minDisplaySize = Math.min(displayWidth, displayHeight) / 2;
        if (mRadius > 0) {
            if (mRadius > minDisplaySize) {
                mRadius = minDisplaySize;
            }
            mTopLeftRadius = mRadius;
            mTopRightRadius = mRadius;
            mBottomRightRadius = mRadius;
            mBottomLeftRadius = mRadius;
        } else {
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
            if (mTopLeftRadius == mTopRightRadius && mTopRightRadius == mBottomRightRadius && mBottomRightRadius == mBottomLeftRadius) {
                mRadius = mTopLeftRadius;
            }
        }
        LogUtils.i("过滤后圆角直径==>mTopLeftRadius:" + mTopLeftRadius + "||mTopRightRadius:" + mTopRightRadius + "||mBottomRightRadius:" + mBottomRightRadius + "||mBottomLeftRadius:" + mBottomLeftRadius + "||mRadius:" + mRadius);
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
            } else {
                mTopLeftRectF.setEmpty();
            }
            if (mTopRightRadius != 0) {
                float rectTopRight = mTopRightRadius * 2;
                mTopRightRectF.set(getPaddingLeft() + displayWidth - rectTopRight, getPaddingTop(), getPaddingLeft() + displayWidth, rectTopRight + getPaddingTop());
            } else {
                mTopRightRectF.setEmpty();
            }
            if (mBottomRightRadius != 0) {
                float rectBottomRight = mBottomRightRadius * 2;
                mBottomRightRectF.set(getPaddingLeft() + displayWidth - rectBottomRight, getPaddingTop() + displayHeight - rectBottomRight, getPaddingLeft() + displayWidth, getPaddingTop() + displayHeight);
            } else {
                mBottomRightRectF.setEmpty();
            }
            if (mBottomLeftRadius != 0) {
                float rectBottomLeft = mBottomLeftRadius * 2;
                mBottomLeftRectF.set(getPaddingLeft(), getPaddingTop() + displayHeight - rectBottomLeft, getPaddingLeft() + rectBottomLeft, getPaddingTop() + displayHeight);
            } else {
                mBottomLeftRectF.setEmpty();
            }
        }
    }


    /**
     * Help对象
     *
     * @return BitmapShaderHelp
     */
    private BitmapShaderHelp HELP() {
        if (mBitmapShaderHelp == null) {
            mBitmapShaderHelp = new BitmapShaderHelp();
        }
        return mBitmapShaderHelp;
    }
}
