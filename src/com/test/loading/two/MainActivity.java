package com.test.loading.two;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ImageView;

public class MainActivity extends Activity {
	// 8个位置
	private int[] imgIDs = { R.id.widget31, R.id.widget32, R.id.widget33,
			R.id.widget34, R.id.widget35, R.id.widget36, R.id.widget37, };
	// 1对应20%亮度，2对应40亮度，3对应60亮度，4对应80亮度，5对应100亮度
	int[] arr = { 1, 2, 3, 4, 5 };
	// 5个效果
	int[] circleArr = { R.drawable.login_loading_choose_twenty,
			R.drawable.login_loading_choose_forty,
			R.drawable.login_loading_choose_sixty,
			R.drawable.login_loading_choose_eighty,
			R.drawable.login_loading_choose_hundred };
	// 初始化状态数组用
	int[] b = { 1, 1, 1, 1, 1, 1, 1 };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 开启一个线程
		final IndexThread thread = new IndexThread();
		thread.start();
	}

	/**画loading圈的Handler*/
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int[] transferArray = new int[b.length];
			if (null != msg.obj) {
				transferArray = (int[]) msg.obj;
				// Log.v("--MainActivity myHandler--",
				// "transferArray===" + Arrays.toString(transferArray));
				for (int j = 0; j < b.length; j++) {
//					changeCircle(j, transferArray[j]);
					((ImageView) findViewById(imgIDs[j]))
							.setBackgroundResource(circleArr[transferArray[j] - 1]);
				}
			}
		}
	};

	/**计算画圈状态的线程*/
	class IndexThread extends Thread {
		boolean flag = true;

		@Override
		public void run() {
			Message msg;
			while (flag) {
				for (int i = -arr.length; i < b.length - 1; i++) {
					// 当前的状态结果数组集
					int[] d = calPoint(arr, b, i);
					// Log.v("--MainActivity IndexThread--", "d==="+Arrays.toString(d));
					msg = new Message();
					msg.obj = d;
					myHandler.sendMessage(msg);
					// 50毫秒间隔
					SystemClock.sleep(100);
				}
			}
		}
	}

	/**
	 * 生成结果集数组
	 * @param arr 需要写的数组
	 * @param b 需要被写的数组
	 * @param index 序号
	 * @return 结果数组
	 */
	private int[] calPoint(int[] arr, int[] b, int index) {
		int[] c = new int[b.length];
		// 复制数组
		System.arraycopy(b, 0, c, 0, b.length);
		for (int i = 1; i < b.length; i++) {
			if ((i + index) > -1 && i < arr.length && (i + index) < b.length) {
				c[i + index] = arr[i];
			}
		}
		return c;
	}

}