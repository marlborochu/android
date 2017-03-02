package com.ma.android.schedule;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ma.android.dao.SQLLiteDAO;
import com.ma.android.utils.FileUtil;
import com.ma.android.utils.HttpUtil;
import com.ma.bus.Constant;
import com.ma.template.dao.MA_PARAMTERDAO;

import android.content.Context;
import android.util.Log;

public class KaohsiungBusJob extends ScheduleJob {

	private Context context;

	@Override
	public void setContext(Context arg) {
		// TODO Auto-generated method stub
		context = arg;
	}

	@Override
	public boolean isRun() {
		// TODO Auto-generated method stub
		return false;
	}

	boolean isRun;
	

	@Override
	public void run() {
		try {

			if (!isRun) {
				try {
					isRun = true;
					queryBusInfo();
				} finally {
					isRun = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void queryBusInfo(){
		
		try{
			
			String url = "http://ibus.tbkc.gov.tw/bus/NewAPI/RealRoute.ashx";
			HashMap parameter = new HashMap();
			parameter.put("type", "GetRoute");
			parameter.put("Lang", "Cht");
			String sjsonarray = HttpUtil.getInstance().requestPost(url,null,parameter);
			JSONArray oldJSONArray = new JSONArray(sjsonarray);			
			JSONArray jarray = new JSONArray();
			
			for(int i = 0;i<oldJSONArray.length();i++){
				String name = oldJSONArray.getJSONObject(i).getString("nameZh");
				String value = oldJSONArray.getJSONObject(i).getString("ID");
				JSONObject jobject = new JSONObject();
				jobject.put(name, value);
				jarray.put(jobject);
			}
			new MA_PARAMTERDAO().setParameter(context, Constant.KEY_BUS_NO_INFO_4, jarray.toString());
		}catch(Exception e){e.printStackTrace();}
		
	}
}
