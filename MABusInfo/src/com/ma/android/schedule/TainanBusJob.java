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

public class TainanBusJob extends ScheduleJob {

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
			String url = "http://tourguide.tainan.gov.tw/NewTNBusAPI/API/GetPathTypeList.ashx";
			String result = HttpUtil.getInstance().request(url);
//			System.out.println(result);;
			HashMap parameter = new HashMap();
			JSONArray jarray = new JSONArray(result);
			
			JSONArray ijarray = new JSONArray();
			for(int i = 0;i<jarray.length();i++){
				JSONObject jobject =  jarray.getJSONObject(i);
				String type = jobject.getString("typeid");
				parameter.put("Type", type);
				String paths = HttpUtil.getInstance().requestPost("http://tourguide.tainan.gov.tw/NewTNBusAPI/API/PathList.ashx",null, parameter);
				JSONArray jarray2 = new JSONArray(paths);
				for(int j = 0;j<jarray2.length();j++){
					JSONObject jobject2 = jarray2.getJSONObject(j);
					String name = jobject2.getString("name");
					String value = jobject2.getString("id");
					JSONObject path = new JSONObject();
					path.put(name, value);
					ijarray.put(path);
				}
			}
			
			new MA_PARAMTERDAO().setParameter(context, Constant.KEY_BUS_NO_INFO_3, ijarray.toString());
		}catch(Exception e){e.printStackTrace();}
		
	}
}
