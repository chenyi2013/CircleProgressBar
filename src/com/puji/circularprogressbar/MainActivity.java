package com.puji.circularprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.puji.circularprogressbar.widget.RateTextCircularProgressBar;

public class MainActivity extends Activity {

	private RateTextCircularProgressBar mRateTextCircularProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRateTextCircularProgressBar = (RateTextCircularProgressBar) findViewById(R.id.rate_progress_bar);
		mRateTextCircularProgressBar.setSecond(160);// ���õ���ʱʱ��
		mRateTextCircularProgressBar.start();// ��ʼ����ʱ
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
