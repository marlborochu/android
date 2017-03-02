package com.ma.bus.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ma.android.utils.AndroidUtil;
import com.ma.android.utils.DateUtil;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.bus.handler.TWTrainHandler;
import com.ma.template.dao.MA_PARAMTERDAO;

public class BUS2001 extends DefaultView{

	public BUS2001(Context arg) {
		super(arg);
	}
	TWTrainHandler handler = new TWTrainHandler();
	@Override
	public void init() {
		
		this.setHeader(R.layout.header_view_layout);
		this.setContent(inflater.inflate(R.layout.bus2001, null));
		handler.setContext(context);
		handler.setDefaultView(this);
		
		((TextView)getHeader().findViewById(R.id.app_name)).setText("台鐵時刻表");
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
		
		getContent().findViewById(R.id.btn_train_time).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				InputMethodManager inputManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(
						Constant.getInstance()
								.getActivity()
								.getCurrentFocus()
								.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				handler.sendEmptyMessage(2);
				
			}
			
		});
		
		ArrayList dates = new ArrayList();
		for(int i = 0;i<15;i++){
			Date d = DateUtil.getInstance().addDays(new Date(), i);
			String sd = DateUtil.getInstance().formatDate(d,"yyyy/MM/dd");
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int dayofweek = c.get(Calendar.DAY_OF_WEEK);
			switch(dayofweek){
				case Calendar.SUNDAY:
					sd = sd +"-"+"星期日";
					break;
				case Calendar.MONDAY:
					sd = sd +"-"+"星期一";
					break;
				case Calendar.TUESDAY:
					sd = sd +"-"+"星期二";
					break;
				case Calendar.WEDNESDAY:
					sd = sd +"-"+"星期三";
					break;
				case Calendar.THURSDAY:
					sd = sd +"-"+"星期四";
					break;
				case Calendar.FRIDAY:
					sd = sd +"-"+"星期五";
					break;
				case Calendar.SATURDAY:
					sd = sd +"-"+"星期六";
					break;	
			}
			dates.add(sd);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_dropdown_item_1line,
				dates);
		((Spinner)getContent().findViewById(R.id.date_spinner)).setAdapter(adapter);
		
		
		boolean networkstatus = AndroidUtil.getInstance().getNetWorkStatus(context);
		if(!networkstatus){
			AndroidUtil.getInstance().showToast(context, "目前無網路服務，請稍後再試！");
		}else{
			String keepString = new MA_PARAMTERDAO().getParameter(context, Constant.KEY_KEEP_BUS_NO_5);
			if(keepString != null){
				try{
					
					JSONObject keepData = new JSONObject(keepString);
					
					String fromname = keepData.getString("fromname");
					String toname = keepData.getString("toname");
					if(!keepData.isNull("date")){
						String date = keepData.getString("date");
						int position = -1;
						for(int i = 0;i<dates.size();i++){
							if(((String)dates.get(i)).startsWith(date)){
								position = i;
							}
						}
						if(position != -1){
							((Spinner)getContent().findViewById(R.id.date_spinner)).setSelection(position);
						}
					}
					
					((AutoCompleteTextView) this.getContent()
							.findViewById(R.id.auto_complete_text_1)).setText(fromname);
					((AutoCompleteTextView) this.getContent()
							.findViewById(R.id.auto_complete_text_2)).setText(toname);
					
					handler.sendEmptyMessageDelayed(2, 500);
					
				}catch(Exception e){e.printStackTrace();}
			}
			handler.sendEmptyMessage(0);
		}
		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
