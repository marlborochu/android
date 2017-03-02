package com.ma.bus.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;

public class BUS0001 extends DefaultView{

	public BUS0001(Context arg) {
		super(arg);
	}

	@Override
	public void init() {
		
		this.setHeader(R.layout.header_view_layout);
		this.setContent(inflater.inflate(R.layout.bus0001, null));
		
		this.getHeader().findViewById(R.id.exit_img)
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().finish();
			}

		});
		getContent().findViewById(R.id.but_favorite).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID3001);
			}
			
		});
		getContent().findViewById(R.id.but_taipei).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID1001);
			}
			
		});
		getContent().findViewById(R.id.but_taichung).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID1002);
			}
			
		});
		getContent().findViewById(R.id.but_tainan).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID1003);
			}
			
		});
		getContent().findViewById(R.id.but_kaohsiung).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID1004);
			}
			
		});
		getContent().findViewById(R.id.but_twtrain).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID2001);
			}
			
		});
		getContent().findViewById(R.id.but_thsrc).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Constant.getInstance().getActivity().changeView(Constant.KEY_VIEW_ID2002);
			}
			
		});
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
