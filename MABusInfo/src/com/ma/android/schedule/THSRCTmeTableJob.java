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
import com.ma.android.utils.DateUtil;
import com.ma.android.utils.FileUtil;
import com.ma.android.utils.HttpUtil;
import com.ma.bus.Constant;
import com.ma.template.dao.MA_PARAMTERDAO;

import android.content.Context;

public class THSRCTmeTableJob extends ScheduleJob {

	private Context context;
	private String queryDate;
	public void setQueryDate(String d){
		queryDate = d;
	}
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
					if(queryDate != null){
						queryTimeTable(queryDate);
					}else{
						for(int i = 1;i<=15;i++){
							Date d = DateUtil.getInstance().addDays(new Date(), i);
							queryTimeTable(DateUtil.getInstance().formatDate(d,"yyyyMMdd"));
						}
					}
				} finally {
					isRun = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void queryTimeTable(String yyyyMMdd) {
		
		try {
			
			String url = "http://www.thsrc.com.tw/tw/TimeTable/DailyTimeTable/"
					+ yyyyMMdd;
			System.out.println(url);
			;
			String html = HttpUtil.getInstance().request(url);

			Document doc = Jsoup.parse(html);
			List tables = doc.select("table div table");
			// System.out.println(tables.size());;
			JSONObject alltimes = new JSONObject();
			for (int i = 0; i < tables.size(); i++) {

				Element table = (Element) tables.get(i);

				if (i == 1 || i == 3) {

					JSONArray infoarray = new JSONArray();
					List trs = table.select("tr");
					for (int j = 1; j < trs.size(); j++) {
						Element tr = (Element) trs.get(j);
						List tds = tr.select("td");
						JSONObject jobject = new JSONObject();
						JSONArray timearray = new JSONArray();
						jobject.put("train_time", timearray);
						for (int x = 0; x < tds.size(); x++) {
							Element td = (Element) tds.get(x);
							if (x == 0) {
								jobject.put("train_no", td.text());
							} else {
								JSONObject time = new JSONObject();
								time.put(td.attr("title"), td.text());
								timearray.put(time);
							}
						}
						infoarray.put(jobject);
					}
					alltimes.put(i + "", infoarray);
				}
				if(!alltimes.isNull("1") && !alltimes.isNull("3"))
					new MA_PARAMTERDAO().setParameter(context, Constant.KEY_BUS_NO_INFO_6+"-"+yyyyMMdd, alltimes.toString());
				
				// System.out.println(table.select("tr").get(0));;
			}
			for(int i = 1;i<=15;i++){
				Date d = DateUtil.getInstance().addDays(new Date(), -i);
				new MA_PARAMTERDAO().removeParameter(context, Constant.KEY_BUS_NO_INFO_6+"-"+DateUtil.getInstance().formatDate(d,"yyyyMMdd"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
