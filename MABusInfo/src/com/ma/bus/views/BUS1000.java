package com.ma.bus.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.ma.android.schedule.ScheduleJob;
import com.ma.android.utils.AndroidUtil;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.bus.handler.BusInfoHandler;
import com.ma.bus.handler.TaipeiBusHandler;
import com.ma.template.dao.MA_PARAMTERDAO;

public abstract class BUS1000 extends DefaultView {

	ArrayList busnoas = new ArrayList();
	HashMap<String, String> busnoHM = new HashMap();

	String busnos;
	BusInfoHandler handler;
	ScheduleJob job;
	String busnoinfo;
	String keepbusinfo;

	public BUS1000(Context arg) {
		super(arg);
	}

	public abstract void queryBusNo();

	@Override
	public void init() {

		this.setHeader(R.layout.header_view_layout);
		this.setContent(inflater.inflate(R.layout.bus1001, null));

		this.getHeader().findViewById(R.id.exit_img).setVisibility(View.GONE);
		this.getHeader().findViewById(R.id.before_img)
				.setVisibility(View.VISIBLE);

		this.getHeader().findViewById(R.id.before_img)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Constant.getInstance().getActivity()
								.changeView(Constant.KEY_VIEW_ID0001);
					}

				});

		boolean networkstatus = AndroidUtil.getInstance().getNetWorkStatus(context);
		if(!networkstatus){
			AndroidUtil.getInstance().showToast(context, "目前無網路服務，請稍後再試！");
		}else{
		
			if (busnos != null) {
				initHandler.sendEmptyMessage(0);
			} else {
				initHandler.sendEmptyMessage(1);
			}
		}
	}

	Handler initHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				dismissDefaultDialog();
				initBusInfo();
				break;
			case 1:
				showDefaultDialog();
				queryBusNo();
				break;
			}
			super.handleMessage(msg);

		}

	};

	private void initBusInfo() {
		try {

			JSONArray jarray = new JSONArray(busnos);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jobject = jarray.getJSONObject(i);
				String name = (String) jobject.keys().next();
				String value = jobject.getString(name);
				if (!busnoas.contains(name)) {
					busnoas.add(name);
					busnoHM.put(name, value);
					busnoas.add("*" + name);
					busnoHM.put("*" + name, value);
				}
			}
			Collections.sort(busnoas);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_dropdown_item_1line, busnoas);
			AutoCompleteTextView actv = (AutoCompleteTextView) getContent()
					.findViewById(R.id.auto_complete_text);
			actv.setAdapter(adapter);

			getContent().findViewById(R.id.button1).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							String busno = ((AutoCompleteTextView) getContent()
									.findViewById(R.id.auto_complete_text))
									.getText().toString();
							if (busnoas.contains(busno)) {

								handler.setBusNo(busnoHM.get(busno));
								InputMethodManager inputManager = (InputMethodManager) context
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								inputManager.hideSoftInputFromWindow(Constant
										.getInstance().getActivity()
										.getCurrentFocus().getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
								// keep busno
								new MA_PARAMTERDAO().setParameter(context,
										keepbusinfo, busno);
								handler.sendEmptyMessage(0);

							} else {
								AndroidUtil.getInstance().showToast(context,
										"無法查詢  [" + busno + "] 資料");
							}
						}

					});

			getContent().findViewById(R.id.button2).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							String busno = ((AutoCompleteTextView) getContent()
									.findViewById(R.id.auto_complete_text))
									.getText().toString();
							if(!busnoas.contains(busno)){
								AndroidUtil.getInstance().showToast(context, "無法新增["+busno+"]至我的最愛");
								return;
							}else{
								String queryKey = Constant.KEY_FAVORITE+keepbusinfo;
								String favoritestring = new MA_PARAMTERDAO().getParameter(context,
										queryKey, null);
								JSONArray favorites = null;
								try{
									if(favoritestring == null){
										favorites = new JSONArray();
									}else{
										favorites = new JSONArray(favoritestring);
									}
									favorites.put(busno);
									new MA_PARAMTERDAO().setParameter(context,queryKey,favorites.toString());
									AndroidUtil.getInstance().showToast(context, "已加入["+busno+"]至我的最愛");
								}catch(Exception e){e.printStackTrace();}
							}
						}

					});

		} catch (Exception e) {
			e.printStackTrace();
		}

		String lastQueryNo = new MA_PARAMTERDAO().getParameter(context,
				keepbusinfo, null);
		if (lastQueryNo != null) {
			((AutoCompleteTextView) getContent().findViewById(
					R.id.auto_complete_text)).setText(lastQueryNo);
			handler.setBusNo(busnoHM.get(lastQueryNo));
			handler.sendEmptyMessage(0);
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		handler.stopQuery();
		handler.removeMessages(0);

	}
}
