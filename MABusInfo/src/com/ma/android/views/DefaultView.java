package com.ma.android.views;

/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 
 * 
 * */
import java.math.BigDecimal;

import com.ma.android.utils.AndroidUtil;
import com.ma.android.utils.UtilsResource;
import com.ma.bus.activities.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class DefaultView extends RelativeLayout {
	
	protected Context context;
	protected View content;
	private ProgressDialog dialog;
	
	protected LayoutInflater inflater = (LayoutInflater) getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	public LayoutInflater getInflater(){
		return this.inflater;
	}
	private View header;
	protected BigDecimal displayWidth;
	protected BigDecimal displayHeight ;
	public DefaultView(Context arg) {

		super(arg);
		this.context = arg;
		this.setLayoutParams(new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT));
		this.setBackgroundColor(Color.WHITE);
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		displayWidth = new BigDecimal(size.x);
		displayHeight = new BigDecimal(size.y);
	}
	
	public void setHeader(int headerID){
		
		RelativeLayout.LayoutParams topParam = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		topParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		this.header = new Header(context,headerID);
		addView(header, topParam);
		
	}
	public View getHeader(){
		return this.header;
	}
	public void setContent(View v){
		
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
//		param.setMargins(15, 0, 15, 0); 
		if(header != null)
			param.addRule(RelativeLayout.BELOW, header.getId());
		param.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(v, param);
		this.content = v;
		
	}
	public View getContent(){
		return this.content;
	}
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

	public ProgressDialog getProgressDialog() {

		ProgressDialog dialog = new ProgressDialog(context,
				R.style.StyledDialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;

	}
	public LinearLayout.LayoutParams getLinearLayoutParams1(){
		return new LinearLayout.LayoutParams(
				displayWidth.intValue(), 
				displayHeight
				.multiply(UtilsResource.KEY_PAGE_DEFAULT_SIZE)
				.multiply(new BigDecimal(1.6)).intValue()
				);
	}
	public LinearLayout.LayoutParams getLinearLayoutParams(int multiple){
		return new LinearLayout.LayoutParams(
				displayWidth.intValue(), 
				displayHeight
				.multiply(UtilsResource.KEY_PAGE_DEFAULT_SIZE)
				.multiply(new BigDecimal(1.6*multiple)).intValue()
				);
	}
	public RelativeLayout.LayoutParams getRelativeLayoutLayoutParams(int multiple){
		return new RelativeLayout.LayoutParams(
				displayWidth.intValue(), 
				displayHeight
				.multiply(UtilsResource.KEY_PAGE_DEFAULT_SIZE)
				.multiply(new BigDecimal(1.6*multiple)).intValue()
				);
	}
	public abstract void init();
	public abstract void destroy();
}
