package com.ma.android.schedule;

import java.io.File;
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

public class TWTrainJob extends ScheduleJob {

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
					queryStation();
				} finally {
					isRun = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void queryStation(){
		try{
			
			String httpResult = HttpUtil.getInstance().request(
					"http://twtraffic.tra.gov.tw/twrail/");
			Document doc = Jsoup.parse(httpResult);
			List scripts = doc.select("script");
			
			if(scripts.size() > 5){
				
				String stations = scripts.get(5).toString();
				stations=stations.replaceAll("<script>", "");
				stations=stations.replaceAll("</script>", "");
				
				String[] infos = stations.split(";");
				String replaceStr = "TRStation.push|\\(|\\)|'";
				JSONArray jarray = new JSONArray();
				for (int i = 0; i < infos.length; i = i + 4) {

					String seq = infos[i].replaceAll(replaceStr, "");
					String stationCode = infos[i + 1].replaceAll(replaceStr, "");
					String stationName = infos[i + 2].replaceAll(replaceStr, "");
					String stationEName = infos[i + 3].replaceAll(replaceStr, "");

					JSONObject hm = new JSONObject();
					hm.put("seq", seq);
					hm.put("code", stationCode);
					hm.put("name", stationName);
					hm.put("ename", stationEName);

					jarray.put(hm);
				}
				
				new MA_PARAMTERDAO().setParameter(context, Constant.KEY_BUS_NO_INFO_5, jarray.toString());
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
