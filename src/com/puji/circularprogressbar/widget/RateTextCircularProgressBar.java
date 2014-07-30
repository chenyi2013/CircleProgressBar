package com.puji.circularprogressbar.widget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.puji.circularprogressbar.R;
import com.puji.circularprogressbar.widget.CircularProgressBar.OnProgressChangeListener;

/**
 * 中间带有进度百分比文字显示的环形进度值
 * 
 * <p>
 * 该类是对 CircularProgressBar（环形进度条）类的一个包装，添加进度文字显示部分
 * </p>
 * <p>
 * 实现对进度条进度改变的监听 {@link OnProgressChangeListener}，从而得到进度的百分比，然后显示在TextView上
 * </p>
 * 
 * 
 */
public class RateTextCircularProgressBar extends FrameLayout implements
		OnProgressChangeListener {

	/**
	 * 倒计时还未结束的标志
	 */
	private static final int RUNING = 1;
	/**
	 * 结束倒计时的标志
	 */
	private static final int COMPLEMENTED = 2;

	/**
	 * 圆形进度条的实例
	 */
	private CircularProgressBar mCircularProgressBar;

	/**
	 * 显示倒计时时间
	 */
	private TextView mRateText;

	/**
	 * 外圆环大小
	 */
	private float mOutCircleSize;
	/**
	 * 内圆环大小
	 */
	private float mInnerCircleSize;

	/**
	 * 用于显示时间的TextView的字体大小
	 */
	private int mRateTextSize;
	/**
	 * 用于显示时间的TextView的字体颜色
	 */
	private int mRateTextColor;

	private ScheduledExecutorService mSExecutorService;
	private ScheduledFuture<?> mScheduledFuture;

	/**
	 * 保证start方法里边计时代码只被执行一次
	 */
	private boolean mLock = false;

	/**
	 * 内圆环颜色
	 */
	private int mInnerCircleColor;
	/**
	 * 外圆环颜色
	 */
	private int mOutCircleColor;

	/**
	 * 总时间
	 */
	private int mTime;

	/***
	 * 更新UI的Handler
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RUNING:

				setProgress((int) ((msg.arg1 / (float) mTime) * mCircularProgressBar
						.getMax()));
				mRateText.setText(formatTime(msg.arg1));
				break;

			case COMPLEMENTED:
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 得到外圆环大小
	 * 
	 * @return
	 */
	public float getOutCircleSize() {
		return mOutCircleSize;
	}

	/**
	 * 设置外圆环大小
	 * 
	 * @param mOutCircleSize
	 */
	public void setOutCircleSize(float mOutCircleSize) {
		this.mOutCircleSize = mOutCircleSize;
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
	 * 设置倒计时的总时色
	 * 
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 */
	public void setTime(int minute, int second) {
		mTime = minute * 60 + second;
	}

	/**
	 * 设置倒计时的总时间
	 * 
	 * @param minute
	 *            分钟
	 */
	public void setMinute(int minute) {
		mTime = minute * 60;
	}

	/**
	 * 设置倒计时的总时间
	 * 
	 * @param second
	 *            秒
	 */
	public void setSecond(int second) {
		mTime = second;
	}

	/**
	 * 设置最大刻度值
	 */
	public void setMax(int max) {
		mCircularProgressBar.setMax(max);
	}

	/**
	 * 设置进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		mCircularProgressBar.setProgress(progress);
	}

	/**
	 * 得到 CircularProgressBar 对象，用来设置其他的其它属性
	 * 
	 * @return
	 */
	public CircularProgressBar getCircularProgressBar() {
		return mCircularProgressBar;
	}

	/**
	 * 设置中间进度百分比文字的尺寸
	 * 
	 * @param size
	 */
	public void setTextSize(float size) {
		mRateText.setTextSize(size);
	}

	/**
	 * 设置中间进度百分比文字的颜色
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		mRateText.setTextColor(color);
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @return
	 */
	private String formatTime(int time) {

		StringBuffer timeFormat = new StringBuffer();

		int minute = time / 60;
		int second = time - (time / 60) * 60;

		if (minute >= 0 && minute < 10) {
			timeFormat.append("0");
		}

		timeFormat.append(minute + ":");

		if (second >= 0 && second < 10) {
			timeFormat.append("0");
		}
		timeFormat.append(second);

		return timeFormat.toString();
	}

	public RateTextCircularProgressBar(Context context) {
		super(context);
		init();
	}

	public RateTextCircularProgressBar(Context context, AttributeSet attrs) {
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
		mRateTextSize = typedArray.getDimensionPixelSize(
				R.styleable.CircularProgressBar_rateTextSize, (int) (10));

		mRateTextColor = typedArray.getColor(
				R.styleable.CircularProgressBar_rateTextColor,
				Color.parseColor("#000000"));
		typedArray.recycle();
		init();
	}

	private void init() {

		mCircularProgressBar = new CircularProgressBar(getContext());

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;

		mCircularProgressBar.setLayoutParams(lp);
		mCircularProgressBar.setOutCircleSize(mOutCircleSize);
		mCircularProgressBar.setInnerCircleSize(mInnerCircleSize);
		mCircularProgressBar.setOutCircleColor(mOutCircleColor);
		mCircularProgressBar.setInnerCircleColor(mInnerCircleColor);

		mRateText = new TextView(getContext());
		mRateText.setLayoutParams(lp);
		mRateText.setGravity(Gravity.CENTER);
		mRateText.setTextColor(Color.BLACK);
		mRateText.setTextSize(mRateTextSize);
		mRateText.setTextColor(mRateTextColor);

		this.addView(mCircularProgressBar);
		this.addView(mRateText);

		mCircularProgressBar.setOnProgressChangeListener(this);
	}

	/**
	 * 启动计时器
	 */
	public void start() {

		if (!mLock) {
			mSExecutorService = Executors.newScheduledThreadPool(1);
			mScheduledFuture = mSExecutorService.scheduleAtFixedRate(
					new Runnable() {
						int time = mTime;

						@Override
						public void run() {
							Message msg = mHandler.obtainMessage();
							msg.what = RUNING;
							msg.arg1 = time--;
							msg.sendToTarget();

						}
					}, 0, 1, TimeUnit.SECONDS);

			mSExecutorService.schedule(new Runnable() {

				@Override
				public void run() {

					Message msg = mHandler.obtainMessage();
					msg.what = COMPLEMENTED;
					msg.arg1 = 0;

					msg.sendToTarget();

					mScheduledFuture.cancel(true);
					mSExecutorService.shutdown();
					mLock = true;

				}
			}, mTime, TimeUnit.SECONDS);
		}

	}

	@Override
	public void onChange(int duration, int progress, float rate) {
		// mRateText.setText(String.valueOf((int) (rate * 100) + "%"));
	}

}
