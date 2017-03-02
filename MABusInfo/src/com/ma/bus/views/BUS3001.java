package com.ma.bus.views;

import org.json.JSONArray;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.android.utils.AndroidUtil;
import com.ma.android.views.DefaultView;
import com.ma.bus.Constant;
import com.ma.bus.activities.R;
import com.ma.template.dao.MA_PARAMTERDAO;

public class BUS3001 extends DefaultView {

	public BUS3001(Context arg) {
		super(arg);
	}

	@Override
	public void init() {

		this.setHeader(R.layout.header_view_layout);
		this.setContent(inflater.inflate(R.layout.bus3001, null));

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
		
		queryAllFavorite();

	}

	private void queryAllFavorite(final String keykeepbus,final String actionview, String busname ) {
		try {

			final String key1 = Constant.KEY_FAVORITE + keykeepbus;
			String favoritestring = new MA_PARAMTERDAO().getParameter(context,
					key1, null);
			if (favoritestring != null && !favoritestring.trim().equals("")) {
				
				ViewGroup vg = (ViewGroup) this.getInflater().inflate(
						R.layout.favorite_1, null);
				JSONArray jarray = new JSONArray(favoritestring);
				if(jarray.length() > 0)
					((TextView) vg.findViewById(R.id.bus_name)).setText(busname);
				else
					return;
				for (int i = 0; i < jarray.length(); i++) {

					final String busno = jarray.getString(i);
					final View infoView = this.getInflater().inflate(
							R.layout.favorite_2, null);
					((TextView) infoView.findViewById(R.id.busno))
							.setText(busno);
					((ViewGroup) vg.findViewById(R.id.bus_favorite_list))
							.addView(infoView);

					infoView.findViewById(R.id.button2).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									new MA_PARAMTERDAO().setParameter(context,
											keykeepbus, busno);
									Constant.getInstance()
											.getActivity()
											.changeView(actionview);
								}

							});
					infoView.findViewById(R.id.button1).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									String favoritestring = new MA_PARAMTERDAO().getParameter(context,
											key1, null);
									try{
										JSONArray jarray = new JSONArray(favoritestring);
										JSONArray ijarray = new JSONArray();
										for (int i = 0; i < jarray.length(); i++) {
											String dbusno = jarray.getString(i);
											if(!busno.equals(dbusno)){
												ijarray.put(dbusno);
											}
										}
										new MA_PARAMTERDAO().setParameter(context,
												key1, ijarray.toString());
										new Handler(){
											@Override
											public void handleMessage(Message msg) {
												
												infoView.setVisibility(View.GONE);
												infoView.startAnimation(AndroidUtil.getInstance().getDismissBottom());
												
											}
										}.sendEmptyMessageDelayed(0, 3);
										
									}catch(Exception e){e.printStackTrace();}
								}

							});

				}
				((ViewGroup) this.getContent().findViewById(R.id.bus_favorite))
						.addView(vg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void queryAllFavorite() {
		queryAllFavorite(Constant.KEY_KEEP_BUS_NO_1,Constant.KEY_VIEW_ID1001,"台北公車");
		queryAllFavorite(Constant.KEY_KEEP_BUS_NO_2,Constant.KEY_VIEW_ID1002,"台中公車");
		queryAllFavorite(Constant.KEY_KEEP_BUS_NO_3,Constant.KEY_VIEW_ID1003,"台南公車");
		queryAllFavorite(Constant.KEY_KEEP_BUS_NO_4,Constant.KEY_VIEW_ID1004,"高雄公車");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
