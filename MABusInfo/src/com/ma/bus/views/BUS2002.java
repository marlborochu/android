package com.ma.bus.views;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.ma.android.utils.AndroidUtil;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.bus.handler.THSRCHandler;
import com.ma.bus.handler.TWTrainHandler;
import com.ma.template.dao.MA_PARAMTERDAO;

public class BUS2002 extends DefaultView{

	public BUS2002(Context arg) {
		super(arg);
	}
	THSRCHandler handler = new THSRCHandler();
	@Override
	public void init() {
		
		this.setHeader(R.layout.header_view_layout);
		this.setContent(inflater.inflate(R.layout.bus2002, null));
		handler.setContext(context);
		handler.setDefaultView(this);
		
		((TextView)getHeader().findViewById(R.id.app_name)).setText("高鐵時刻表");
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
				
				handler.sendEmptyMessage(1);
				
			}
			
		});
		
		boolean networkstatus = AndroidUtil.getInstance().getNetWorkStatus(context);
		if(!networkstatus){
			AndroidUtil.getInstance().showToast(context, "目前無網路服務，請稍後再試！");
		}else{
			handler.sendEmptyMessage(0);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
