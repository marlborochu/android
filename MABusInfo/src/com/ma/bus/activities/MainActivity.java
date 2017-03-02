package com.ma.bus.activities;

import java.lang.reflect.Constructor;

import com.ma.android.service.MAService;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.views.BUS0001;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
public class MainActivity extends Activity {
	
	private DefaultView dv;
	@Override
	protected void onCreate(Bundle info) {
		
		super.onCreate(info);
		
		Intent inte = new Intent(this,MAService.class);		
		this.startService(inte);
		
		Constant.getInstance().setActivity(this);
		
		Bundle data = getIntent().getExtras();
		if(data != null && data.containsKey(Constant.KEY_VIEW_ID)){
			try{
				Constructor c = Class.forName(data.getString(Constant.KEY_VIEW_ID)).getConstructor(Context.class);
				dv = (DefaultView)c.newInstance(this);
			}catch(Exception e){e.printStackTrace();}
		}else{
			dv = new BUS0001(this);
		}

		dv.init();
		setContentView(dv);
		
	}
	public void changeView(String viewID){
		
		try{	
			Constructor c = Class.forName(viewID).getConstructor(Context.class);
			DefaultView dv = (DefaultView)c.newInstance(this);
			
			ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
		            .findViewById(android.R.id.content)).getChildAt(0);
			viewGroup.removeAllViews();
//			viewGroup = null;
			
			dv.init();
			setContentView(dv);
			this.dv.destroy();
			this.dv = dv;
			
		}catch(Exception e){e.printStackTrace();}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(dv.getClass().getSimpleName().equals("BUS0001")){
				this.finish();
			}else{
				this.changeView(Constant.KEY_VIEW_ID0001);
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		Intent inte = new Intent(this,MAService.class);	
		this.stopService(inte);
		dv.destroy();
		super.onDestroy();
	}
	private ProgressDialog dialog;
	
	private Handler defaultDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (dialog == null || !dialog.isShowing()) {
				dialog = getProgressDialog();
				dialog.show();
			}
		}
	};
	private Handler dismissDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
		}
	};

	public void showDefaultDialog() {
		defaultDialogHandler.sendMessage(new Message());
	}

	public void dismissDefaultDialog() {
		dismissDialogHandler.sendMessage(new Message());
	}

	private ProgressDialog getProgressDialog() {

		dialog = new ProgressDialog(this, R.style.StyledDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;

	}

}
