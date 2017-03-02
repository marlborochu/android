package com.ma.bus.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.ma.android.views.DefaultView;
import com.ma.bus.activities.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public abstract class BusInfoHandler extends Handler{
	
	private Context context;
	public void setContext(Context context){
		this.context = context;
	}
	private DefaultView view;
	public void setDefaultView(DefaultView view){
		this.view = view;
	}
	protected String busno;
	public void setBusNo(String busno){
		this.busno = busno;
	}
	private boolean isRun = false;
	private HashMap<String, ArrayList> businfo;
	public HashMap<String, ArrayList> getBusInfo(){
		return this.businfo;
	}
	private Timer schedule;
	public boolean getScheduleStatus(){
		return (schedule != null) ;		
	}
	private HashMap<String, View> businfoViews = new HashMap();
	
	public void stopQuery(){
		if(schedule != null){
			schedule.cancel();
			schedule = null;
		}
	}
	public void startQuery(){
		
		stopQuery();
		schedule = new Timer();
		schedule.schedule(new TimerTask(){
			public void run(){
				try{
					if(!isRun){
						try{
							isRun = true;
							String tbusno = busno;
							HashMap tbusinfo = query();
							if(tbusno.equals(busno)){
								businfo = tbusinfo;
								BusInfoHandler.this.sendEmptyMessageDelayed(1, 3);
							}
						}finally{
							isRun = false;
						}
					}
				}catch(Exception e){e.printStackTrace();}
			}
		}, new Date(), 1000*5);
		
	}
	@Override
	public void handleMessage(Message msg) {

		switch (msg.what) {
		case 0:
			view.showDefaultDialog();
			((ViewGroup) view.getContent().findViewById(R.id.bus_infos))
					.removeAllViews();
			((ViewGroup) view.getContent().findViewById(R.id.bus_infos)).invalidate();
			businfoViews.clear();
			
			startQuery();
			break;
		case 1:
			viewDataCtl();
			view.dismissDefaultDialog();
			break;
		case 2:
			break;
		}

		PowerManager powerManager = (PowerManager) context
				.getSystemService(context.POWER_SERVICE);
		if (powerManager.isScreenOn()) {
			if (!getScheduleStatus())
				sendEmptyMessageDelayed(0, 1000);
		} else {
			if (getScheduleStatus())
				stopQuery();
			sendEmptyMessageDelayed(2, 3000);
		}
		super.handleMessage(msg);

	}

	private void viewDataCtl() {

		if (getBusInfo() != null) {

			int viewcount = ((ViewGroup) view.getContent().findViewById(
					R.id.bus_info_title)).getChildCount();
			if (viewcount == 0) {
				View titleview = view.getInflater().inflate(R.layout.businfo_1, null);
//				((TextView) titleview.findViewById(R.id.station)).setTextSize(22);
//				((TextView) titleview.findViewById(R.id.gotime)).setTextSize(22);
//				((TextView) titleview.findViewById(R.id.backtime)).setTextSize(22);
				
				((TextView) titleview.findViewById(R.id.station)).setTextColor(context.getResources().getColor(R.color.body_background));
				((TextView) titleview.findViewById(R.id.gotime)).setTextColor(context.getResources().getColor(R.color.body_background));
				((TextView) titleview.findViewById(R.id.backtime)).setTextColor(context.getResources().getColor(R.color.body_background));
				
				
				((TextView) titleview.findViewById(R.id.station))
						.setText("站名");
				((TextView) titleview.findViewById(R.id.gotime))
						.setText("去程");
				((TextView) titleview.findViewById(R.id.backtime))
						.setText("返程");
				titleview.setBackgroundColor(context.getResources()
						.getColor(R.color.text_background));
				((ViewGroup) view.getContent().findViewById(R.id.bus_info_title))
						.addView(titleview);

			}
			HashMap<String, ArrayList> businfo = getBusInfo();
			ArrayList busstations = businfo.get("BUSSTATION_INDEX");
			for (int i = 0; i < busstations.size(); i++) {

				String station = (String) busstations.get(i);
				View busview = null;
				if (businfoViews.containsKey(station)) {
					busview = businfoViews.get(station);
				} else {
					busview = view.getInflater().inflate(R.layout.businfo_1, null);
					businfoViews.put(station, busview);
					if(i%2 == 1){
						busview.setBackgroundColor(context.getResources().getColor(R.color.subject_background));
					}
					((ViewGroup) view.getContent().findViewById(R.id.bus_infos))
							.addView(busview);
					;
				}
//				Log.d("TestAA", station+"==>"+businfo);
				ArrayList<String> info = businfo.get(station);
				((TextView) busview.findViewById(R.id.station))
						.setText(station);
				((TextView) busview.findViewById(R.id.gotime)).setText(info
						.get(0));
				((TextView) busview.findViewById(R.id.backtime))
						.setText(info.get(1));
				

			}
		}

	}

	public abstract HashMap<String, ArrayList> query() ;
	
}
