package com.ma.bus.views;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.ma.android.schedule.TaichungBusJob;
import com.ma.android.schedule.TaipeiBusJob;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.bus.handler.BusInfoHandler;
import com.ma.bus.handler.TaichungBusHandler;
import com.ma.bus.handler.TaipeiBusHandler;
import com.ma.template.dao.MA_PARAMTERDAO;

// 台中公車查詢

public class BUS1002 extends BUS1000 {
	
	public BUS1002(Context arg) {
		super(arg);
	}
	@Override
	public void init() {
		busnoinfo = Constant.KEY_BUS_NO_INFO_2;
		keepbusinfo = Constant.KEY_KEEP_BUS_NO_2;
		busnos = new MA_PARAMTERDAO().getParameter(context,busnoinfo);	
		handler = new TaichungBusHandler();
		handler.setContext(context);
		handler.setDefaultView(this);
		super.init();
		((TextView)getHeader().findViewById(R.id.app_name)).setText("台中公車資訊");
	}
	public void queryBusNo(){
		new Thread(){
			public void run(){
				try{
					
					job = new TaichungBusJob();
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
