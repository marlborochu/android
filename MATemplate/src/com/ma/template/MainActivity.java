package com.ma.template;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle info) {
		super.onCreate(info);
		
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		
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
