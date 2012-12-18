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


/**�÷����������ֵAlertDialog��private����ʵ��AlertDialog�����󲻻�ȡ���Ĺ���*/
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
		.setPositiveButton("ȷ��", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).setNegativeButton("ȡ��", new OnClickListener() {
			
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
			//mShowing���Կ���dialog�Ƿ��Ѿ����رգ�����Ǳ㲻��ִ�йرն���
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
			//��alertDialog�����е�mAlert�����е�mhandler�����Ű����ر�dialog�Ĳ�����ͨ���滻�������ȡ������
			java.lang.reflect.Field field = alertDialog.getClass().getDeclaredField("mAlert");//ʹfield��ӦmAlert����
			field.setAccessible(true);//ʹ��˽�����Կɷ���
			Object obj = field.get(alertDialog);//��alertDialog�����л��mAlert����
			field = obj.getClass().getDeclaredField("mHandler");//ʹfield��ӦmHandler����
			field.setAccessible(true);//ʹ��˽�����Կɷ���
			field.set(obj,new ButtonHandler(alertDialog));//��obj�е�mHandler���Ե�ֵ�滻ΪButtonHandler
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
		private WeakReference<DialogInterface> mDialog;//������,handler�������һֱ���ڵ�����ָ��ĶԻ����ڴ����ʱ���ͷ�
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
