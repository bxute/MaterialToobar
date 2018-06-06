package xute.materialtoolbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

public class BottomToolbar extends View {


    public static final int GROW_CIRCLE_DURATION = 600;
    public static final int SHRINK_RECTANGLE_DURATION = 400;
    public static final int TOOLBAR_CURVE_DURATION = 400;
    public static final int BUTTON_ANIMATION_DURATION = 400;
    public static final int TOOLBAR_HEIGHT_IN_DP = 56;
    public static final int BUTTON_GAP_ABOVE_TOOLBAR = 8;
    public static final int TEXT_SIZE = 28;
    public static final int TOOLBAR_OFFSET_IN_DP = 12;
    public static final int BITMAP_PADDING_IN_DP = 16;

    private Path mToobarPath;
    private Path mButtonPath;

    private Paint mToolbarPaint;
    private Paint mCurvePaint;

    private int toolbarOffsetInPx;
    private int mScreenWidth;
    private int mScreenHeight;
    private float maxScreenRadius;

    //for growing circle
    private float mCircleRadius;
    private boolean shouldDrawCircle = true;

    //for shrinking rectangle
    private int mRectangleStartY;
    private boolean shouldDrawRectangle = false;
    private int mToolbarHeightInPx;

    //for bottom curve
    private int mCurveRadius;
    private boolean shouldDrawCurve;

    //for button
    private float mButtonGapAboveToolbar;
    private boolean shouldDrawButton;
    private Paint mTextPaint;
    private Paint mButtonPaint;
    private int textSize;
    private Context mContext;
    private int startGrowingCircleRadius;

    private int shrinkRectangleStartDelay;
    private int toolbarCurveStartDelay;
    private int buttonAnimationStartDelay;

    private int screenHorizontalMidPoint;
    private int screenVerticalMidPoint;
    private int toolbarHeightMidPoint;

    private int toolbarStartY;
    private int toolbarStartX;

    private int toolbarCurveStartX;
    private int toolbarCurveEndX;
    private int toolbarDippedPointX;
    private int toolbarDippedPointY;

    private int buttonLeftX;
    private int buttonLeftY;

    private int buttonBottomX;
    private int buttonBottomY;

    private int buttonRightX;
    private int buttonRightY;

    private int buttonTopX;
    private int buttonTopY;

    private int bitmapPadding;
    private Bitmap customer;
    private Bitmap cloud;

    private RectF customerRectF;
    private RectF cloudRectF;
    private boolean shouldDrawBitmap;


    public BottomToolbar(Context context) {
        super(context);
        init(context);
    }

    public BottomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        Resources resources = context.getResources();
        //prepare bitmaps
        cloud = BitmapFactory.decodeResource(resources, R.drawable.cloud);
        customer = BitmapFactory.decodeResource(resources, R.drawable.customer);

        mScreenWidth = resources.getDisplayMetrics().widthPixels;
        mScreenHeight = resources.getDisplayMetrics().heightPixels;
        mToolbarHeightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TOOLBAR_HEIGHT_IN_DP, resources.getDisplayMetrics());
        mButtonGapAboveToolbar = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BUTTON_GAP_ABOVE_TOOLBAR, resources.getDisplayMetrics());
        toolbarOffsetInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TOOLBAR_OFFSET_IN_DP, resources.getDisplayMetrics());
        bitmapPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BITMAP_PADDING_IN_DP, resources.getDisplayMetrics());
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE, resources.getDisplayMetrics());

        startGrowingCircleRadius = mToolbarHeightInPx / 2;

        //prepare delay
        shrinkRectangleStartDelay = GROW_CIRCLE_DURATION + 400;
        toolbarCurveStartDelay = shrinkRectangleStartDelay + SHRINK_RECTANGLE_DURATION + 200;
        buttonAnimationStartDelay = toolbarCurveStartDelay + TOOLBAR_CURVE_DURATION;

        maxScreenRadius = (float) Math.sqrt(Math.pow(mScreenHeight, 2) + Math.pow(mScreenWidth, 2));
        mCircleRadius = 0;
        mRectangleStartY = 0;

        mToobarPath = new Path();
        mButtonPath = new Path();

        mToolbarPaint = new Paint();
        mToolbarPaint.setStyle(Paint.Style.FILL);
        mToolbarPaint.setAntiAlias(true);
        mToolbarPaint.setColor(Color.parseColor("#34495e"));

        mCurvePaint = new Paint();
        mCurvePaint.setStyle(Paint.Style.FILL);
        mCurvePaint.setAntiAlias(true);
        mCurvePaint.setPathEffect(new CornerPathEffect(0));
        mCurvePaint.setColor(Color.parseColor("#34495e"));
        mCurvePaint.setShadowLayer(2, 0, 0, Color.RED);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        mButtonPaint = new Paint();
        mButtonPaint.setColor(Color.parseColor("#ffffff"));
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setAntiAlias(true);
        mButtonPaint.setPathEffect(new CornerPathEffect(0));
        mButtonPaint.setShadowLayer(2, 0, 4, Color.RED);

        //configure cordinates
        screenHorizontalMidPoint = mScreenWidth / 2;
        screenVerticalMidPoint = mScreenHeight / 2;
        toolbarHeightMidPoint = mToolbarHeightInPx / 2;

        toolbarStartX = 0;
        toolbarStartY = mScreenHeight - mToolbarHeightInPx;

        toolbarCurveStartX = screenHorizontalMidPoint - toolbarHeightMidPoint;
        toolbarCurveEndX = screenHorizontalMidPoint + toolbarHeightMidPoint;
        toolbarDippedPointX = screenHorizontalMidPoint;
        toolbarDippedPointY = mScreenHeight - toolbarHeightMidPoint;

        buttonLeftX = toolbarCurveStartX;
        buttonLeftY = (int) (toolbarStartY - mButtonGapAboveToolbar);

        buttonBottomX = toolbarDippedPointX;
        buttonBottomY = (int) (toolbarDippedPointY - mButtonGapAboveToolbar);

        buttonRightX = toolbarCurveEndX;
        buttonRightY = (int) (toolbarStartY - mButtonGapAboveToolbar);

        buttonTopX = toolbarDippedPointX;
        buttonTopY = (int) (mScreenHeight - (mToolbarHeightInPx * (1.5)) - mButtonGapAboveToolbar);

        //bitmap width
        customerRectF = new RectF(mScreenWidth - 5 * bitmapPadding, toolbarStartY + bitmapPadding, mScreenWidth - 3 * bitmapPadding, mScreenHeight - bitmapPadding);
        cloudRectF = new RectF(3 * bitmapPadding, toolbarStartY + bitmapPadding, 5 * bitmapPadding, mScreenHeight - bitmapPadding);
        //prepare button path
        mButtonPath = getButtonPath();

        startAnimating();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (shouldDrawCircle) {
            canvas.drawCircle(screenHorizontalMidPoint, screenVerticalMidPoint, mCircleRadius, mToolbarPaint);
        }

        if (shouldDrawRectangle) {
            canvas.drawRect(0, mRectangleStartY, mScreenWidth, mScreenHeight, mToolbarPaint);
        }

        if (shouldDrawCurve) {
            canvas.drawPath(mToobarPath, mCurvePaint);
        }

        if (shouldDrawButton) {
            canvas.drawPath(mButtonPath, mButtonPaint);
            canvas.drawText("+", screenHorizontalMidPoint, toolbarStartY, mTextPaint);
        }

        if (shouldDrawBitmap) {
            canvas.drawBitmap(cloud, null, customerRectF, null);
            canvas.drawBitmap(customer, null, cloudRectF, null);
        }
    }

    private void startAnimating() {
        ValueAnimator circleAnimation = getGrowingCircleAnimation();
        ValueAnimator rectangleAnimation = getShrinkRectangleAnimator();
        ValueAnimator curveAnimation = getCurvingValueAnimator();
        ValueAnimator buttonAnimator = getButtonValueAnimator();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(circleAnimation, rectangleAnimation, curveAnimation, buttonAnimator);
        animatorSet.start();
    }

    private ValueAnimator getGrowingCircleAnimation() {
        ValueAnimator growingCircleRadiusAnimator = ValueAnimator.ofFloat(startGrowingCircleRadius, maxScreenRadius);
        growingCircleRadiusAnimator.setDuration(GROW_CIRCLE_DURATION);
        growingCircleRadiusAnimator.setInterpolator(new AccelerateInterpolator(2f));
        growingCircleRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCircleRadius = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        growingCircleRadiusAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                shouldDrawRectangle = true;
                shouldDrawCircle = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return growingCircleRadiusAnimator;
    }

    private ValueAnimator getShrinkRectangleAnimator() {
        ValueAnimator shrinkRectangleAnimator = ValueAnimator.ofInt(0, toolbarStartY);
        shrinkRectangleAnimator.setDuration(SHRINK_RECTANGLE_DURATION);
        shrinkRectangleAnimator.setStartDelay(shrinkRectangleStartDelay);
        shrinkRectangleAnimator.setInterpolator(new AccelerateInterpolator(1.5f));
        shrinkRectangleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRectangleStartY = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        shrinkRectangleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //   shouldDrawRectangle = false;
                shouldDrawCurve = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return shrinkRectangleAnimator;

    }

    private ValueAnimator getCurvingValueAnimator() {
        ValueAnimator bottomCurAnimator = ValueAnimator.ofInt(0, mToolbarHeightInPx / 2);
        bottomCurAnimator.setDuration(TOOLBAR_CURVE_DURATION);
        bottomCurAnimator.setStartDelay(toolbarCurveStartDelay);
        bottomCurAnimator.setInterpolator(new BounceInterpolator());
        bottomCurAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurveRadius = (int) valueAnimator.getAnimatedValue();
                shouldDrawRectangle = false;
                setToolbarPath(mCurveRadius);
                invalidate();
            }
        });

        bottomCurAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //shouldDrawButton = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return bottomCurAnimator;
    }

    private ValueAnimator getButtonValueAnimator() {
        ValueAnimator buttonValueAnimator = ValueAnimator.ofArgb(Color.parseColor("#ffffff"), Color.parseColor("#34495e"));
        buttonValueAnimator.setStartDelay(buttonAnimationStartDelay);
        buttonValueAnimator.setDuration(BUTTON_ANIMATION_DURATION);
        buttonValueAnimator.setInterpolator(new DecelerateInterpolator(2f));
        buttonValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                shouldDrawButton = true;
                shouldDrawBitmap = true;
                mButtonPaint.setColor((Integer) valueAnimator.getAnimatedValue());
                invalidate();
            }
        });
        return buttonValueAnimator;
    }

    private Path getButtonPath() {
        mButtonPath = new Path();
        mButtonPath.moveTo(buttonLeftX, buttonLeftY);
        mButtonPath.lineTo(buttonBottomX, buttonBottomY);
        mButtonPath.lineTo(buttonRightX, buttonRightY);
        mButtonPath.lineTo(buttonTopX, buttonTopY);
        mButtonPath.close();
        return mButtonPath;
    }

    private void setToolbarPath(int mCurveRadius) {
        mToobarPath.reset();
        mToobarPath.moveTo((-1) * toolbarOffsetInPx, toolbarStartY);
        mToobarPath.lineTo(screenHorizontalMidPoint - mCurveRadius, toolbarStartY);
        mToobarPath.lineTo(screenHorizontalMidPoint, toolbarDippedPointY);
        mToobarPath.lineTo(screenHorizontalMidPoint + mCurveRadius, toolbarStartY);
        mToobarPath.lineTo(mScreenWidth + toolbarOffsetInPx, toolbarStartY);
        mToobarPath.lineTo(mScreenWidth + toolbarOffsetInPx, mScreenHeight);
        mToobarPath.lineTo((-1) * toolbarOffsetInPx, mScreenHeight + toolbarOffsetInPx);
        mToobarPath.lineTo((-1) * toolbarOffsetInPx, toolbarStartY);
        mToobarPath.close();
        mButtonPaint.setPathEffect(new CornerPathEffect(mCurveRadius / 3));
        mCurvePaint.setPathEffect(new CornerPathEffect(mCurveRadius / 3));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (cordinateInsideButton(event.getX(), event.getY())) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (buttonClickListener != null) {
                    buttonClickListener.onPlusButtonClicked();
                }
            }
        }
        return true;
    }

    private boolean cordinateInsideButton(float x, float y) {
        if (x > buttonLeftX && x < buttonRightX && y > buttonTopY && y < buttonBottomY) {
            return true;
        }
        return false;
    }

    private ButtonClickListener buttonClickListener;

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public interface ButtonClickListener {
        void onPlusButtonClicked();
    }
}
