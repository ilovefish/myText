package com.example.alertdialogtext;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;


/**用反射机制来赋值AlertDialog的private变量实现AlertDialog按键后不会取消的功能*/
public class MainActivity extends Activity {
	AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertDialog = new AlertDialog.Builder(this)
		.setTitle("abc")
		.setMessage("content")
		.setIcon(R.drawable.ic_launcher)
		.setPositiveButton("确定", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		
		
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//javaReflectI();
		javaReflectII();
		return super.onOptionsItemSelected(item);
	}

	private void javaReflectII(){
		alertDialog.show();
		try {
			//mShowing属性控制dialog是否已经被关闭，如果是便不再执行关闭动作
			java.lang.reflect.Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(alertDialog, false);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void javaReflectI(){
		alertDialog.show();
		try {
			//在alertDialog对象中的mAlert对象中的mhandler控制着按键关闭dialog的操作，通过替换对象可以取消操作
			java.lang.reflect.Field field = alertDialog.getClass().getDeclaredField("mAlert");//使field对应mAlert属性
			field.setAccessible(true);//使该私有属性可访问
			Object obj = field.get(alertDialog);//从alertDialog对象中获得mAlert属性
			field = obj.getClass().getDeclaredField("mHandler");//使field对应mHandler属性
			field.setAccessible(true);//使该私有属性可访问
			field.set(obj,new ButtonHandler(alertDialog));//把obj中的mHandler属性的值替换为ButtonHandler
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class ButtonHandler extends Handler{
		private WeakReference<DialogInterface> mDialog;//虚引用,handler对象可以一直存在但是所指向的对话框内存会随时被释放
		public ButtonHandler(DialogInterface dialog){
			mDialog = new WeakReference<DialogInterface>(dialog);
		}
		
		public void handleMessage(Message msg){
			switch(msg.what){
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
			((DialogInterface.OnClickListener)msg.obj).onClick(mDialog.get(), msg.what);
			break;
			}
		}
	}

}
