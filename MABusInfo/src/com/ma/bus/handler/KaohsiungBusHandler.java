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

public class KaohsiungBusHandler extends BusInfoHandler {

	private ArrayList busstations = new ArrayList();
	private HashMap<String,String> stationsMap = new HashMap();
	
	private int goStations = 0;
	private int backStations = 0;
	
	public HashMap<String, ArrayList> query() {
		
		try {
			
			HashMap<String, ArrayList> bustimes = new HashMap();
			bustimes.put("BUSSTATION_INDEX", busstations);
			queryBusTime(bustimes,0,1);
			
			return bustimes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void queryBusTime(HashMap<String,ArrayList> bustimes,int timeIndex,int type){
		
		try{
			
			String url = "http://ibus.tbkc.gov.tw/bus/newAPI/GetEstimateTime.ashx";
			HashMap parameter = new HashMap();
			parameter.put("type", "web");
			parameter.put("routeid", busno);
			parameter.put("Lang", "Cht");
			
			String sjarray = HttpUtil.getInstance().requestPost(url, null, parameter);
			JSONArray jarray = new JSONArray(sjarray);
			
			int lastStationIndex = 0;
			for(int i = 0;i<jarray.length();i++){
				
				JSONObject jobject = jarray.getJSONObject(i);
				String goback = jobject.getString("goback");
				JSONArray times = jobject.getJSONArray("cometime");
				String lastCarId = "";
				for(int j = 0;j<times.length();j++){
					
					JSONObject time = times.getJSONObject(j);
					String station = time.getString("stopname");
					String cometime = time.getString("cometime");
					String carid = time.getString("carid");
					String seq = time.getString("seq");
					
					if(carid.equals(lastCarId)) carid = "";
					else lastCarId = carid;
					
					if(cometime.matches("[0-9]*")) cometime = cometime+"分";
					
					if(!bustimes.containsKey(station)){
						bustimes.put(station, new ArrayList());
						bustimes.get(station).add("");
						bustimes.get(station).add("");
					}
					
					if(goback.equals("2")){
						if(!busstations.contains(station))
							busstations.add(lastStationIndex,station);
						bustimes.get(station).remove(1);
						bustimes.get(station).add(1,cometime+carid);
					}else{
						if(!busstations.contains(station))
							busstations.add(station);
						bustimes.get(station).remove(0);
						bustimes.get(station).add(0,cometime+carid);
					}
					lastStationIndex = busstations.indexOf(station);
				}
			}
			
		}catch(Exception e){e.printStackTrace();}
		
	}
	
}
