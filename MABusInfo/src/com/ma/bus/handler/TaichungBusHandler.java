package com.ma.bus.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.*;

import com.ma.android.utils.HttpUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TaichungBusHandler extends BusInfoHandler {

	private String tbusno = "";
	private ArrayList busstations = new ArrayList();
	private HashMap<String,String> stationsMap = new HashMap();
	
	private int goStations = 0;
	private int backStations = 0;
	
	public HashMap<String, ArrayList> query() {
		
		try {
			
			if(!tbusno.equals(busno)){
				busstations.clear();
				stationsMap.clear();
				queryStation();
			}
			HashMap<String, ArrayList> bustimes = new HashMap();
			bustimes.put("BUSSTATION_INDEX", busstations);
			queryBusTime(bustimes,0,1);
			queryBusTime(bustimes,1,2);
			return bustimes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void queryBusTime(HashMap<String,ArrayList> bustimes,int timeIndex,int type){
		
		String url_1 = "http://citybus.taichung.gov.tw/iTravel/RealRoute/aspx/RealRoute.ashx";
		HashMap parameter = new HashMap();
		parameter.put("Type", "GetFreshData");
		parameter.put("Lang", "Cht");
		parameter.put("Data", busno+","+type+"_,"+backStations);
		if(busno.length() > 4)
			parameter.put("BusType", "1");
		else
			parameter.put("BusType", "0");
		String result = HttpUtil.getInstance().requestPost(url_1,null, parameter);
		
		String[] datas = result.split("@")[0].split("\\|");
		for(int i = 0;i<datas.length;i++){
			String[] businfos = datas[i].split(",");
			if(businfos.length > 2){
				
				String time1 = businfos[0].replaceAll("_", "");
				String time2 = businfos[1].replaceAll("_", "");
				if(time2.trim().equals("")) time2 = "未發車";
				String stationNo = businfos[2];
				
				String stationName = stationsMap.get(stationNo);
				
				if(!bustimes.containsKey(stationName)){
					bustimes.put(stationName, new ArrayList());
					bustimes.get(stationName).add("");
					bustimes.get(stationName).add("");
				}
				if(time1.equals("null")){
					bustimes.get(stationName).remove(timeIndex);
					bustimes.get(stationName).add(timeIndex,time2);
				}else{
					bustimes.get(stationName).remove(timeIndex);
					if(Integer.valueOf(time1) < 3){
						bustimes.get(stationName).add(timeIndex,"將到站");
					}else{
						bustimes.get(stationName).add(timeIndex,time1+"分");
					}
				}
				
			}
		}
	}
	private void queryStation(){
		
		try{
			
			String url_1 = "http://citybus.taichung.gov.tw/iTravel/RealRoute/aspx/RealRoute.ashx";
			HashMap parameter = new HashMap();
			parameter.put("Type", "GetStop");
			parameter.put("Lang", "Cht");
			parameter.put("Data", busno+",1");
			if(busno.length() > 4)
				parameter.put("BusType", "1");
			else
				parameter.put("BusType", "0");
			
			String result = HttpUtil.getInstance().requestPost(url_1, null, parameter);
			Log.d("TestAA", ""+parameter+"=="+result);
			String[] datas = result.split("\\|");
			goStations = (datas.length);;
			for(int i = 0;i<datas.length;i++){
				String[] stations = datas[i].split(",");
				if(stations.length > 4){
					String stationNo = stations[4];
					String stationName = stations[1].replaceAll("_", "");;
					busstations.add(stationName);
					stationsMap.put(stationNo, stationName);
				}
			}
			
			tbusno = busno;
		}catch(Exception e){e.printStackTrace();}
		
		try{
			
			String url_1 = "http://citybus.taichung.gov.tw/iTravel/RealRoute/aspx/RealRoute.ashx";
			HashMap parameter = new HashMap();
			parameter.put("Type", "GetStop");
			parameter.put("Lang", "Cht");
			parameter.put("Data", busno+",2");
			if(busno.length() > 4)
				parameter.put("BusType", "1");
			else
				parameter.put("BusType", "0");
			String result = HttpUtil.getInstance().requestPost(url_1, null, parameter);
			String[] datas = result.split("\\|");
			goStations = (datas.length);;
			int laststationIndex = busstations.size();
			for(int i = 0;i<datas.length;i++){
				String[] stations = datas[i].split(",");
				if(stations.length > 4){
					String stationNo = stations[4];
					String stationName = stations[1].replaceAll("_", "");
					stationsMap.put(stationNo, stationName);
					
					if(!busstations.contains(stationName)){
						busstations.add(laststationIndex,stationName);
					}else{
						laststationIndex = busstations.indexOf(stationName);
					}
				}
			}
			
			tbusno = busno;
		}catch(Exception e){e.printStackTrace();}
	}
}
