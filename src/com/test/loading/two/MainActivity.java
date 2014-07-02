package com.test.loading.two;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
/**
 * @author fox
 * 2014-7-2
 */
public class MainActivity extends Activity {
	// 7个位置
	private int[] imgIDs = { R.id.widget31, R.id.widget32, R.id.widget33,
			R.id.widget34, R.id.widget35, R.id.widget36, R.id.widget37, };
	// 标志循环向右的状态数组
	int[] arr = { 1, 2, 3, 4, 5 };
	// 6个效果:0对应20%亮度，1对应20%亮度，2对应40亮度，3对应60亮度，4对应80亮度，5对应100亮度
	int[] circleArr = { R.drawable.login_loading_choose_twenty,
			R.drawable.login_loading_choose_twenty,
			R.drawable.login_loading_choose_forty,
			R.drawable.login_loading_choose_sixty,
			R.drawable.login_loading_choose_eighty,
			R.drawable.login_loading_choose_hundred };
	// 初始化状态数组用
	int[] b = { 0, 0, 0, 0, 0, 0, 0 };

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
				// [0, 0, 0, 0, 0, 0, 0]
				// [5, 0, 0, 0, 0, 0, 0]
				// [4, 5, 0, 0, 0, 0, 0]
				// [3, 4, 5, 0, 0, 0, 0]
				// [2, 3, 4, 5, 0, 0, 0]
				// [1, 2, 3, 4, 5, 0, 0]
				// [0, 1, 2, 3, 4, 5, 0]
				// [0, 0, 1, 2, 3, 4, 5]
				// [0, 0, 0, 1, 2, 3, 4]
				// [0, 0, 0, 0, 1, 2, 3]
				// [0, 0, 0, 0, 0, 1, 2]
				// [0, 0, 0, 0, 0, 0, 1]
				transferArray = (int[]) msg.obj;
				// Log.v("--MainActivity myHandler--",
				// "transferArray===" + Arrays.toString(transferArray));
				int whereOne=commonSearch(transferArray,1);
				int whereFive=commonSearch(transferArray,5);
				//如果没有找到1或者第0个位置为1，遍历从0开始
				int fromIndex=(whereOne==-1||whereOne==0)?0:whereOne-1;
				//如果没有找到5，遍历到最后一个圆圈为止
				int endIndex=(whereFive==-1)?b.length:whereFive+1;
				//如果1和5均未找到，只需更新最后一个圆圈的状态
				if(whereOne==-1&&whereFive==-1){
					fromIndex=b.length-1;
					endIndex=b.length;
				}
//				Log.v("--MainActivity myHandler--", "fromIndex=="+fromIndex+",endIndex=="+endIndex);
				for (; fromIndex < endIndex; fromIndex++) {
					int whichView=imgIDs[fromIndex];
					int whatState=transferArray[fromIndex];
					((ImageView) findViewById(whichView))
							.setBackgroundResource(circleArr[whatState]);
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
				for (int i = -arr.length; i < b.length ; i++) {
					// 当前的状态结果数组集
					int[] d = calPoint(arr, b, i);
					Log.v("--MainActivity IndexThread--",
							"d===" + Arrays.toString(d));
					msg = new Message();
					msg.obj = d;
					myHandler.sendMessage(msg);
					// 100毫秒间隔
					SystemClock.sleep(1000);
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
		for (int i = 0; i < b.length; i++) {
			if ((i + index) > -1 && i < arr.length && (i + index) < b.length) {
				c[i + index] = arr[i];
			}
		}
		return c;
	}
	/**
	 * 查找int[]中某个整数的位置
	 * @param array 需要查找的int[]
	 * @param value 目标整数
	 * @return 找到返回位置序号，未找到返回-1
	 */
	private int commonSearch(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (value == array[i]) {
				return i; // 返回该元素在数组中的下标
			}
		}
		return -1; // 不存在该元素则返回-1
	}
}