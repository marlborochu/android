package com.ma.bus.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

public class TWTrainHandler extends Handler{
	
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
	int querystationTimes = 0;
	private JSONArray stationTimes;
	
	@Override
	public void handleMessage(Message msg) {
		try{
			switch (msg.what) {
			
				case 0:
					String sstations = new MA_PARAMTERDAO().getParameter(context,Constant.KEY_BUS_NO_INFO_5);
					if(sstations == null && querystationTimes < 5){
						view.showDefaultDialog();
						queryStation();
						querystationTimes++;
					}else if(querystationTimes >= 5){
						view.dismissDefaultDialog();
						AndroidUtil.getInstance().showToast(context, "無法取得相關資料");
					}else{
						stations = new JSONArray(sstations);
						TWTrainHandler.this.sendEmptyMessage(1);
					}
					break;
				case 1:
					view.dismissDefaultDialog();
					setStation();
					break;
				case 2:
					queryStationTime();
					break;
				case 3:
					view.dismissDefaultDialog();
					setStationTime();
					break;	
			}
		}catch(Exception e){e.printStackTrace();}
		super.handleMessage(msg);
	}
	
	private void setStationTime(){
		
		try{
			
			((ViewGroup) view.getContent().findViewById(R.id.train_infos)).removeAllViews();
			
			int viewcount = ((ViewGroup) view.getContent().findViewById(
					R.id.trian_info_title)).getChildCount();
			if (viewcount == 0) {
				
				View timeTitleView = view.getInflater().inflate(R.layout.train_stationtime_1, null);
				((TextView)timeTitleView.findViewById(R.id.typecode)).setText("車種");
				((TextView)timeTitleView.findViewById(R.id.fromto)).setText("發車站→終點站");
				((TextView)timeTitleView.findViewById(R.id.fromtime)).setText("開車時間");
				((TextView)timeTitleView.findViewById(R.id.totime)).setText("到達時間");
				((TextView)timeTitleView.findViewById(R.id.price)).setText("票價");
				
				timeTitleView.setBackgroundColor(context.getResources()
						.getColor(R.color.text_background));
				
				((ViewGroup) view.getContent().findViewById(R.id.trian_info_title)).addView(timeTitleView);
			}
			
			for(int i = 0;i<stationTimes.length();i++){
				
				JSONObject jobject = stationTimes.getJSONObject(i);
				View timeView = view.getInflater().inflate(R.layout.train_stationtime_1, null);
				
				if(i%2 == 1){
					timeView.setBackgroundColor(context.getResources().getColor(R.color.subject_background));
				}
				
				String type = jobject.getString("train_type");
				String code = jobject.getString("train_code");
				String fromto = jobject.getString("fromto");
				String fromtime = jobject.getString("fromtime");
				String totime = jobject.getString("totime");
				String price = jobject.getString("price");
				((TextView)timeView.findViewById(R.id.typecode)).setText(type+" "+code);
				((TextView)timeView.findViewById(R.id.fromto)).setText(fromto);
				((TextView)timeView.findViewById(R.id.fromtime)).setText(fromtime);
				((TextView)timeView.findViewById(R.id.totime)).setText(totime);
				((TextView)timeView.findViewById(R.id.price)).setText(price);
				
				((ViewGroup) view.getContent().findViewById(R.id.train_infos)).addView(timeView);
				
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	private void setStation(){
		
		try{
			
			ArrayList station = new ArrayList();
			stationMap = new HashMap();
			for(int i = 0;i<stations.length();i++){
				
				String code = stations.getJSONObject(i).getString("code");
				String name = stations.getJSONObject(i).getString("name");
				
				stationMap.put(name, code);
				station.add(name);
				
				stationMap.put("*"+name, code);
				station.add("*"+name);
				
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					context, android.R.layout.simple_dropdown_item_1line,
					station);
			((AutoCompleteTextView) view.getContent()
					.findViewById(R.id.auto_complete_text_1)).setAdapter(adapter);
			((AutoCompleteTextView) view.getContent()
					.findViewById(R.id.auto_complete_text_2)).setAdapter(adapter);
		}catch(Exception e){e.printStackTrace();}
	}
	public void queryStation(){
		
		new Timer().schedule(new TimerTask(){
			public void run(){
				try{
					new TWTrainJob().run();
					TWTrainHandler.this.sendEmptyMessage(0);
				}catch(Exception e){e.printStackTrace();}
			}
		}, new Date());
		
	}
	public void queryStationTime(){
		
		String fromname = ((AutoCompleteTextView) view.getContent()
				.findViewById(R.id.auto_complete_text_1)).getText().toString();
		String toname = ((AutoCompleteTextView) view.getContent()
				.findViewById(R.id.auto_complete_text_2)).getText().toString();
		String date = ((Spinner)view.getContent().findViewById(R.id.date_spinner)).getSelectedItem().toString();
		date = date.split("-")[0];
		
		if(!stationMap.containsKey(fromname)){
			AndroidUtil.getInstance().showToast(context, "輸入起站不存在");
		}else if (!stationMap.containsKey(toname)){
			AndroidUtil.getInstance().showToast(context, "輸入迄站不存在");
		}else{
						
			final String fromcode = (String)stationMap.get(fromname);
			final String tocode = (String)stationMap.get(toname);
			
			// keep station name
			try{
				JSONObject jobject = new JSONObject();
				jobject.put("fromname", fromname);
				jobject.put("toname", toname);
				jobject.put("date", date);
				new MA_PARAMTERDAO().setParameter(context, Constant.KEY_KEEP_BUS_NO_5, jobject.toString());
			}catch(Exception e){e.printStackTrace();}
			
			view.showDefaultDialog();
			new Timer().schedule(new TimerTask(){
				public void run(){
					try{
						
						String date = ((Spinner)view.getContent().findViewById(R.id.date_spinner)).getSelectedItem().toString();
						date = date.split("-")[0];
						
						String requestURL = "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=0"
								+ "&searchdate="
								+ date
								+ "&fromstation="
								+ fromcode
								+ "&tostation="
								+ tocode
								+ "&trainclass=2"
								+ "&fromtime=0000&totime=2359";
						Log.d("Test", requestURL);
						String html = HttpUtil.getInstance().request(requestURL);
						Document doc = Jsoup.parse(html);
						List trs = doc.select("tr.Grid_Row");
						stationTimes = new JSONArray();
						for(int i = 1;i<trs.size();i++){
							
//							System.out.println(trs.get(i));;
							String traintype = ((Element)trs.get(i)).select("td.SearchResult_TrainType").text();
							String traincode = ((Element)trs.get(i)).select("td.SearchResult_TrainCode").text();
							String fromtime = ((Element)trs.get(i)).select("td.SearchResult_Time").get(0).text();
							String totime = ((Element)trs.get(i)).select("td.SearchResult_Time").get(1).text();
							String price = ((Element)trs.get(i)).select("td").get(8).text();
							String fromto = ((Element)trs.get(i)).select("td").get(3).text();
//							System.out.println(traintype+":"+traincode+":"+fromtime+":"+totime+":"+price+":"+fromto);;
							JSONObject jobject = new JSONObject();
							jobject.put("train_type", traintype);
							jobject.put("train_code", traincode);
							jobject.put("fromtime", fromtime);
							jobject.put("totime", totime);
							jobject.put("price", price);
							jobject.put("fromto", fromto);
							
							stationTimes.put(jobject);
							
						}
						
						TWTrainHandler.this.sendEmptyMessage(3);
						
					}catch(Exception e){e.printStackTrace();}
				}
			}, new Date());
		}
		
	}
}
