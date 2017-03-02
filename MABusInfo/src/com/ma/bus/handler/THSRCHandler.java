package com.ma.bus.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ma.android.schedule.THSRCTmeTableJob;
import com.ma.android.schedule.TWTrainJob;
import com.ma.android.utils.AndroidUtil;
import com.ma.android.utils.DateUtil;
import com.ma.android.utils.HttpUtil;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.template.dao.MA_PARAMTERDAO;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class THSRCHandler extends Handler{
	
	private Context context;
	public void setContext(Context context){
		this.context = context;
	}
	private DefaultView view;
	public void setDefaultView(DefaultView view){
		this.view = view;
	}
	private JSONArray stations;
	private HashMap stationMap;
	int queryTimes = 0;
	private JSONArray stationTimes;
	
	@Override
	public void handleMessage(Message msg) {
		try{
			switch (msg.what) {
			
				case 0:
					
					String d = DateUtil.getInstance().formatDate(new Date(),"yyyyMMdd");
					String timetable = new MA_PARAMTERDAO().getParameter(context,Constant.KEY_BUS_NO_INFO_6+"-"+d);
					if(timetable == null && queryTimes < 5){
						view.showDefaultDialog();
						queryTimeTable();
						queryTimes++;
					}else if(queryTimes >= 5){
						view.dismissDefaultDialog();
						AndroidUtil.getInstance().showToast(context, "無法取得相關資料");
					}else{
						view.dismissDefaultDialog();
						initView();
					}	
					break;
				case 1:
					initTimeTableView();
					break;
				case 2:
					
					break;
				case 3:
					break;	
			}
		}catch(Exception e){e.printStackTrace();}
		super.handleMessage(msg);
	}
	
	private void initTimeTableView(){
		
		try{
			
			String date = ((Spinner)view.getContent().findViewById(R.id.date_spinner)).getSelectedItem().toString();
			date = date.split("-")[0];
			final String yyyyMMdd = date.replaceAll("/", "");
			String timetable = new MA_PARAMTERDAO().getParameter(context,Constant.KEY_BUS_NO_INFO_6+"-"+yyyyMMdd);
		
			if(timetable == null){
				view.showDefaultDialog();
				new Timer().schedule(new TimerTask(){
					public void run(){
						try{
							THSRCTmeTableJob tjob = new THSRCTmeTableJob();
							tjob.setQueryDate(yyyyMMdd);
							tjob.run();
							THSRCHandler.this.sendEmptyMessage(1);
						}catch(Exception e){e.printStackTrace();}
					}
				}, new Date());
			}else{
				
				view.dismissDefaultDialog();
				((ViewGroup) view.getContent().findViewById(R.id.train_infos)).removeAllViews();
				int viewcount = ((ViewGroup) view.getContent().findViewById(
						R.id.trian_info_title)).getChildCount();
				if (viewcount == 0) {
					
					View timeTitleView = view.getInflater().inflate(R.layout.train_stationtime_2, null);
					((TextView)timeTitleView.findViewById(R.id.typecode)).setText("車次");
					((TextView)timeTitleView.findViewById(R.id.fromtime)).setText("開車時間");
					((TextView)timeTitleView.findViewById(R.id.totime)).setText("到達時間");
					
					timeTitleView.setBackgroundColor(context.getResources()
							.getColor(R.color.text_background));
					
					((ViewGroup) view.getContent().findViewById(R.id.trian_info_title)).addView(timeTitleView);
				}
				
				JSONObject jobject = new JSONObject(timetable);
				String fromname = ((Spinner)view.getContent().findViewById(R.id.auto_complete_text_1)).getSelectedItem().toString();
				String toname = ((Spinner)view.getContent().findViewById(R.id.auto_complete_text_2)).getSelectedItem().toString();
				
				JSONArray tarray = jobject.getJSONArray("1").getJSONObject(0).getJSONArray("train_time");
				ArrayList test = new ArrayList();
				for(int i = 0;i<tarray.length();i++){
					JSONObject tjobject = tarray.getJSONObject(i);
					Iterator ite = tjobject.keys();
					if(ite.hasNext()) test.add(ite.next());
				}
				int fromIndex = test.indexOf(fromname);
				int toIndex = test.indexOf(toname);
				
				JSONArray qarray = null;
				if(fromIndex < toIndex){
					qarray = jobject.getJSONArray("1");
				}else if(toIndex < fromIndex){
					qarray = jobject.getJSONArray("3");
				}else{
					AndroidUtil.getInstance().showToast(context, "起迄站相同，無法查詢");
					return;
				}
				JSONArray showArray = new JSONArray();
				for(int i = 0;i<qarray.length();i++){
					JSONObject qjobject = qarray.getJSONObject(i);
					JSONArray stationTimes = qjobject.getJSONArray("train_time");
					for(int j = 0;j<stationTimes.length();j++){
						JSONObject jstation = stationTimes.getJSONObject(j);
						if(!jstation.isNull(fromname) && !jstation.getString(fromname).equals("")){
							showArray.put(qjobject);
						}
					}
				}
				JSONArray showArray1 = new JSONArray();
				for(int i = showArray.length()-1;i>=0;i--){
					JSONObject qjobject = showArray.getJSONObject(i);
					JSONArray stationTimes = qjobject.getJSONArray("train_time");
					for(int j = 0;j<stationTimes.length();j++){
						JSONObject jstation = stationTimes.getJSONObject(j);
						if(!jstation.isNull(toname) && !jstation.getString(toname).trim().equals("")){
							showArray1.put(qjobject);
						}
					}
				}
				
				for(int i = showArray1.length()-1;i>=0;i--){
					
					JSONObject qjobject = showArray1.getJSONObject(i);
					JSONArray stationTimes = qjobject.getJSONArray("train_time");
					String traincode = qjobject.getString("train_no");
					String fromtime = null;
					String totime = null;
					for(int j = 0;j<stationTimes.length();j++){
						JSONObject jstation = stationTimes.getJSONObject(j);
						if(!jstation.isNull(toname) && !jstation.getString(toname).equals("")){
							totime = jstation.getString(toname);
						}
						if(!jstation.isNull(fromname) && !jstation.getString(fromname).equals("")){
							fromtime = jstation.getString(fromname);
						}
					}
					
					View timeView = view.getInflater().inflate(R.layout.train_stationtime_2, null);
					((TextView)timeView.findViewById(R.id.typecode)).setText(traincode);
					((TextView)timeView.findViewById(R.id.fromtime)).setText(fromtime);
					((TextView)timeView.findViewById(R.id.totime)).setText(totime);
					
					if(i%2 == 1){
						timeView.setBackgroundColor(context.getResources().getColor(R.color.subject_background));
					}
					
					((ViewGroup) view.getContent().findViewById(R.id.train_infos)).addView(timeView);
					
				}
				
			}
			
		}catch(Exception e){e.printStackTrace();}
	}
	private void initView(){
		try{
			
			ArrayList dates = new ArrayList();
			for(int i = 0;i<15;i++){
				Date d = DateUtil.getInstance().addDays(new Date(), i);
				String sd = DateUtil.getInstance().formatDate(d,"yyyy/MM/dd");
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				int dayofweek = c.get(Calendar.DAY_OF_WEEK);
				switch(dayofweek){
					case Calendar.SUNDAY:
						sd = sd +"-"+"星期日";
						break;
					case Calendar.MONDAY:
						sd = sd +"-"+"星期一";
						break;
					case Calendar.TUESDAY:
						sd = sd +"-"+"星期二";
						break;
					case Calendar.WEDNESDAY:
						sd = sd +"-"+"星期三";
						break;
					case Calendar.THURSDAY:
						sd = sd +"-"+"星期四";
						break;
					case Calendar.FRIDAY:
						sd = sd +"-"+"星期五";
						break;
					case Calendar.SATURDAY:
						sd = sd +"-"+"星期六";
						break;	
				}
				dates.add(sd);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					context, android.R.layout.simple_dropdown_item_1line,
					dates);
			((Spinner)view.getContent().findViewById(R.id.date_spinner)).setAdapter(adapter);
			
			String d = DateUtil.getInstance().formatDate(new Date(),"yyyyMMdd");
			String timetable = new MA_PARAMTERDAO().getParameter(context,Constant.KEY_BUS_NO_INFO_6+"-"+d);
			JSONObject jtimetable = new JSONObject(timetable);
			{
				ArrayList stations1 = new ArrayList();
				ArrayList stations2 = new ArrayList();
				JSONArray jarray = jtimetable.getJSONArray("1").getJSONObject(0).getJSONArray("train_time");
				for(int i = 0;i<jarray.length();i++){
					JSONObject jobject = jarray.getJSONObject(i);
					Iterator ite = jobject.keys();
					if(ite.hasNext()){
						stations1.add(ite.next());
					}
				}
				for(int i = stations1.size()-1;i>=0;i--){
					stations2.add(stations1.get(i));
				}
				{
					ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
							context, android.R.layout.simple_dropdown_item_1line,
							stations1);
					ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
							context, android.R.layout.simple_dropdown_item_1line,
							stations2);
					((Spinner) view.getContent()
							.findViewById(R.id.auto_complete_text_1)).setAdapter(adapter1);
					((Spinner) view.getContent()
							.findViewById(R.id.auto_complete_text_2)).setAdapter(adapter2);
				}
			}
			
		}catch(Exception e){e.printStackTrace();}
	}
	public void queryTimeTable(){
		
		new Timer().schedule(new TimerTask(){
			public void run(){
				try{
					THSRCTmeTableJob tjob = new THSRCTmeTableJob();
					tjob.setQueryDate(DateUtil.getInstance().formatDate(new Date(),"yyyyMMdd"));
					tjob.run();
					THSRCHandler.this.sendEmptyMessage(0);
				}catch(Exception e){e.printStackTrace();}
			}
		}, new Date());
		
	}
}
