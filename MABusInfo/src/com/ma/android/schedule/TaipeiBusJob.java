package com.ma.android.schedule;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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

public class TaipeiBusJob extends ScheduleJob {

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
			
			String url = "http://pda.5284.com.tw/MQS/businfo1.jsp";
			String html = HttpUtil.getInstance().request(url);
			Document doc = Jsoup.parse(html);
			List options = doc.select("option");
			
			JSONArray jarray = new JSONArray();
			for(int i = 0;i<options.size();i++){
				Element ele = (Element) options.get(i);
				String value = URLDecoder.decode(ele.attr("value"),"UTF-8");
				if(value.trim().equals("")) continue;
				String name = ele.text();
				JSONObject jobject = new JSONObject();
				jobject.put(name, value);
				jarray.put(jobject);
			}
			new MA_PARAMTERDAO().setParameter(context, Constant.KEY_BUS_NO_INFO_1, jarray.toString());
		}catch(Exception e){e.printStackTrace();}
		
	}
}
