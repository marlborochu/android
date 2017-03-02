package com.ma.bus.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.ma.android.schedule.ScheduleJob;
import com.ma.android.schedule.TainanBusJob;
import com.ma.android.schedule.TaipeiBusJob;
import com.ma.android.utils.AndroidUtil;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.bus.handler.BusInfoHandler;
import com.ma.bus.handler.TainanBusHandler;
import com.ma.bus.handler.TaipeiBusHandler;
import com.ma.template.dao.MA_PARAMTERDAO;

// 台南公車查詢

public class BUS1003 extends BUS1000 {

	
	public BUS1003(Context arg) {
		super(arg);
		
	}
	@Override
	public void init() {
		busnoinfo = Constant.KEY_BUS_NO_INFO_3;
		keepbusinfo = Constant.KEY_KEEP_BUS_NO_3;
		busnos = new MA_PARAMTERDAO().getParameter(context,busnoinfo);		
		handler = new TainanBusHandler();
		handler.setContext(context);
		handler.setDefaultView(this);
		super.init();

		((TextView)getHeader().findViewById(R.id.app_name)).setText("台南公車資訊");
	}
	public void queryBusNo(){
		new Thread(){
			public void run(){
				try{
					
					job = new TainanBusJob();
					job.run();
					busnos = new MA_PARAMTERDAO().getParameter(context,busnoinfo);
					initHandler.sendEmptyMessage(0);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}
