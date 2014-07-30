package com.puji.circularprogressbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.puji.circularprogressbar.R;

/**
 * 环形的进度条
 * 
 * 
 */
public class CircularProgressBar extends View {

	/**
	 * 最大进度
	 */
	private int mDuration = 100;
	/**
	 * 当前进度
	 */
	private int mProgress = 30;

	/**
	 * 内圆环笔刷
	 */
	private Paint mOutCirclePaint = new Paint();
	/**
	 * 外圆环笔刷
	 */
	private Paint mInnerCirclePaint = new Paint();
	private RectF mRectF = new RectF();

	/**
	 * 外圆环大小
	 */
	private float mOutCircleSize;
	/**
	 * 内圆环大小
	 */
	private float mInnerCircleSize;

	/**
	 * 内圆环颜色
	 */
	private int mInnerCircleColor;
	/**
	 * 外圆环颜色
	 */
	private int mOutCircleColor;

	/**
	 * 进度条改变监色
	 * 
	 * {@link #onChange(int duration, int progress, float rate)}
	 */
	public interface OnProgressChangeListener {
		/**
		 * 进度改变事件，当进度条进度改变，就会调用该方法
		 * 
		 * @param duration
		 *            总进度
		 * @param progress
		 *            当前进度
		 * @param rate
		 *            当前进度与总进度的商 即：rate = (float)progress / duration
		 */
		public void onChange(int duration, int progress, float rate);
	}

	private OnProgressChangeListener mOnChangeListener;

	/**
	 * 设置进度条改变监听
	 * 
	 * @param l
	 */
	public void setOnProgressChangeListener(OnProgressChangeListener l) {
		mOnChangeListener = l;
	}

	/**
	 * 得到内圆环大小
	 * 
	 * @return
	 */
	public float getInnerCircleSize() {
		return mInnerCircleSize;
	}

	/**
	 * 设置内圆环大小
	 * 
	 * @param mInnerCircleSize
	 */
	public void setInnerCircleSize(float mInnerCircleSize) {
		this.mInnerCircleSize = mInnerCircleSize;
	}

	/**
	 * 设置进度条的最大值该值大于 0
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		if (max < 0) {
			max = 0;
		}
		mDuration = max;
	}

	/**
	 * 得到进度条的最大值
	 * 
	 * @return
	 */
	public int getMax() {
		return mDuration;
	}

	/**
	 * 设置进度条的当前的进度值
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		if (progress > mDuration) {
			progress = mDuration;
		}
		mProgress = progress;
		if (mOnChangeListener != null) {
			mOnChangeListener
					.onChange(mDuration, progress, getRateOfProgress());
		}
		invalidate();
	}

	/**
	 * 得到进度条当前的进度值
	 * 
	 * @return
	 */
	public int getProgress() {
		return mProgress;
	}

	/**
	 * 设置进度条背景的颜色
	 */
	public void setBackgroundColor(int color) {
	}

	/**
	 * 设置环形的宽度
	 * 
	 * @param width
	 */
	public void setOutCircleSize(float width) {
		mOutCircleSize = width;

	}

	/**
	 * 得到内圆环颜色
	 * 
	 * @return
	 */
	public int getInnerCircleColor() {
		return mInnerCircleColor;
	}

	/**
	 * 设置内圆环颜色
	 * 
	 * @param mInnerCircleColor
	 */
	public void setInnerCircleColor(int mInnerCircleColor) {
		this.mInnerCircleColor = mInnerCircleColor;
	}

	/**
	 * 得到外圆环颜色
	 * 
	 * @return
	 */
	public int getOutCircleColor() {
		return mOutCircleColor;
	}

	/**
	 * 设置外圆环颜色
	 * 
	 * @param mOutCircleColor
	 */
	public void setOutCircleColor(int mOutCircleColor) {
		this.mOutCircleColor = mOutCircleColor;
	}

	/**
	 * 得到当前的进度的比率
	 * <p>
	 * 用进度条当前的进度值与进度条的最大值值求商
	 * </p>
	 * 
	 * @return
	 */
	private float getRateOfProgress() {
		return (float) mProgress / mDuration;
	}

	public CircularProgressBar(Context context) {
		super(context);

	}

	public CircularProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		float density = getResources().getDisplayMetrics().density;

		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.CircularProgressBar, 0, 0);
		mOutCircleSize = typedArray.getDimension(
				R.styleable.CircularProgressBar_outCircleSize,
				(int) (10 * density));
		mInnerCircleSize = typedArray.getDimension(
				R.styleable.CircularProgressBar_innerCircleSize,
				(int) (10 * density));
		mInnerCircleColor = typedArray.getColor(
				R.styleable.CircularProgressBar_innerCircleColor,
				Color.parseColor("#00ff00"));
		mOutCircleColor = typedArray.getColor(
				R.styleable.CircularProgressBar_outCircleColor,
				Color.parseColor("#ff00ff"));
		typedArray.recycle();

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int halfWidth = getWidth() / 2;
		int halfHeight = getHeight() / 2;
		int radius = halfWidth < halfHeight ? halfWidth : halfHeight;
		float halfStrokeWidth = mOutCircleSize / 2;

		// 画内圆环
		mInnerCirclePaint.setDither(true);
		mInnerCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mInnerCirclePaint.setAntiAlias(true);
		mInnerCirclePaint.setStrokeWidth(mInnerCircleSize);
		mInnerCirclePaint.setStyle(Paint.Style.STROKE);
		mInnerCirclePaint.setColor(mInnerCircleColor);

		canvas.drawCircle(halfWidth, halfHeight, radius - mOutCircleSize
				- mInnerCircleSize / 2, mInnerCirclePaint);

		// 画当前进度的圆环
		mOutCirclePaint.setDither(true);
		mOutCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mOutCirclePaint.setAntiAlias(true);
		mOutCirclePaint.setStrokeWidth(mOutCircleSize);
		mOutCirclePaint.setStyle(Paint.Style.STROKE); // 设置图形为空心圆
		mOutCirclePaint.setColor(mOutCircleColor); // 改变画笔颜色
		mRectF.top = halfHeight - radius + halfStrokeWidth;
		mRectF.bottom = halfHeight + radius - halfStrokeWidth;
		mRectF.left = halfWidth - radius + halfStrokeWidth;
		mRectF.right = halfWidth + radius - halfStrokeWidth;
		canvas.drawArc(mRectF, -90, getRateOfProgress() * 360, false,
				mOutCirclePaint);
		canvas.save();
	}

}
