package com.ma.bus.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.*;

import com.ma.android.utils.HttpUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TaipeiBusHandler extends BusInfoHandler {

	
	public HashMap<String, ArrayList> query() {
		
		try {
			
			String url = "http://pda.5284.com.tw/MQS/businfo2.jsp?routename="
					+ busno;
			String html = HttpUtil.getInstance().request(url);
			Document doc = Jsoup.parse(html);
			List tables = doc.select("table table table");

			HashMap<String, ArrayList> bustimes = new HashMap();
			ArrayList busstations = new ArrayList();
			bustimes.put("BUSSTATION_INDEX", busstations);
			String station = "";
			
			int stationIndex = 0;
			for (int i = 0; i < tables.size(); i++) {
				Element table = (Element) tables.get(i);
				List trs = table.select("tr");
				for (int x = 0; x < trs.size(); x++) {

					List tds = ((Element) trs.get(x)).select("td");

					for (int j = 0; j < tds.size(); j++) {
						Element td = (Element) tds.get(j);
						switch (j) {
						case 0:
							if (i == 0 && !busstations.contains(td.text())) {
								busstations.add(td.text());
							} else if (!busstations.contains(td.text())) {
								busstations.add(stationIndex, td.text());
							}
							station = td.text();
							stationIndex = busstations.indexOf(station);
							break;
						case 1:
							if (!bustimes.containsKey(station)) {
								bustimes.put(station, new ArrayList());
								bustimes.get(station).add("");
								bustimes.get(station).add("");
							}
							bustimes.get(station).remove(i);
							bustimes.get(station).add(i, td.text().replaceAll(" ", "\r\n"));
							break;
						default:
							break;
						}
					}
				}

			}
			return bustimes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
