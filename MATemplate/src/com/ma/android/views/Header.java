package com.ma.android.views;
/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 
 * 
 * */
import java.math.BigDecimal;

import com.ma.android.utils.AndroidUtil;
import com.ma.android.utils.UtilsResource;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Header extends LinearLayout {
	
	protected Context context;
	protected LayoutInflater inflater = (LayoutInflater) getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	public Header(Context arg,int headerID) {
		
		super(arg);
		this.context = arg;
		this.setOrientation(LinearLayout.VERTICAL);
		
		setId(AndroidUtil.getInstance().generateViewId());
		
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		BigDecimal displayWidth = new BigDecimal(size.x);
		BigDecimal displayHeight = new BigDecimal(size.y);
		
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				displayHeight
				.multiply(UtilsResource.KEY_PAGE_DEFAULT_SIZE)
				.multiply(new BigDecimal(1.6)).intValue()
				);

		View headerView = inflater.inflate(headerID, null);
		headerView.setLayoutParams(param);
		this.addView(headerView);
		
	}
	
}
