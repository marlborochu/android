package com.ma.bus.handler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.*;

import com.ma.android.utils.HttpUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TainanBusHandler extends BusInfoHandler {

	HashMap parameter = new HashMap();
	
	public HashMap<String, ArrayList> query() {
		
		try {
			
			HashMap<String, ArrayList> bustimes = new HashMap();
			ArrayList busstations = new ArrayList();
			bustimes.put("BUSSTATION_INDEX", busstations);
			
			queryBusInfo(bustimes,busstations,"0");
			queryBusInfo(bustimes,busstations,"1");
			
			return bustimes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void queryBusInfo(HashMap<String,ArrayList> bustimes,ArrayList busstations,String goback){
		
		try {
			
			String url = "http://tourguide.tainan.gov.tw/NewTNBusAPI/API/GoAndBackWithTime.ashx";
			
			parameter.put("id", busno);
			parameter.put("goback", goback);
			String sjarray = HttpUtil.getInstance().requestPost(url,null,parameter);
			
			JSONArray jarray = new JSONArray(sjarray);
			int lastStationIndex = 0;
			for(int i = 0;i<jarray.length();i++){
				
				JSONObject jobject = jarray.getJSONObject(i);
				String station = jobject.getString("StopName");
				String carNo = "";
				if(!jobject.isNull("CarId"))
					carNo = jobject.getString("CarId");
				String time = jobject.getString("Time");
				time = time.replaceAll("即", "");
				if(time.matches("[0-9]*")){
					time = time +"分";
				}
				if(!carNo.trim().equals("")){
					time = "";
				}
				if(!bustimes.containsKey(station)){
					bustimes.put(station, new ArrayList());
					bustimes.get(station).add("");
					bustimes.get(station).add("");
				}
				
				if(goback.equals("1")){
					if(!busstations.contains(station))
						busstations.add(lastStationIndex,station);
					bustimes.get(station).remove(1);
					bustimes.get(station).add(1,time+carNo);
				}else{
					if(!busstations.contains(station))
						busstations.add(station);
					bustimes.get(station).remove(0);
					bustimes.get(station).add(0,time+carNo);
				}
				lastStationIndex = busstations.indexOf(station);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
