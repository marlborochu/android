package com.ma.bus;

import com.ma.bus.activities.MainActivity;

public class Constant {
	
	public final static String KEY_VIEW_ID = "VIEW_ID";
	public final static String KEY_VIEW_ID0001 = "com.ma.bus.views.BUS0001";
	public final static String KEY_VIEW_ID1001 = "com.ma.bus.views.BUS1001"; // 台北
	public final static String KEY_VIEW_ID1002 = "com.ma.bus.views.BUS1002"; // 台中
	public final static String KEY_VIEW_ID1003 = "com.ma.bus.views.BUS1003"; // 台南
	public final static String KEY_VIEW_ID1004 = "com.ma.bus.views.BUS1004"; // 高雄
	public final static String KEY_VIEW_ID2001 = "com.ma.bus.views.BUS2001"; // 台鐵
	public final static String KEY_VIEW_ID2002 = "com.ma.bus.views.BUS2002"; // 高鐵
	public final static String KEY_VIEW_ID3001 = "com.ma.bus.views.BUS3001"; // 我的最愛
	
	
	
	public final static String KEY_BUS_NO_INFO_1 = "BUS_NO_INFO_1"; // 台北
	public final static String KEY_BUS_NO_INFO_2 = "BUS_NO_INFO_2"; // 台中
	public final static String KEY_BUS_NO_INFO_3 = "BUS_NO_INFO_3"; // 台南
	public final static String KEY_BUS_NO_INFO_4 = "BUS_NO_INFO_4"; // 高雄
	
	public final static String KEY_BUS_NO_INFO_5 = "BUS_NO_INFO_5"; // 台鐵
	public final static String KEY_BUS_NO_INFO_6 = "BUS_NO_INFO_6"; // 高鐵
	
	public final static String KEY_FAVORITE = "FAVORITE";// 我的最愛
	
	public final static String KEY_KEEP_BUS_NO_1 = "KEEP_BUS_NO_1";// 台北
	public final static String KEY_KEEP_BUS_NO_2 = "KEEP_BUS_NO_2";// 台中
	public final static String KEY_KEEP_BUS_NO_3 = "KEEP_BUS_NO_3";// 台南
	public final static String KEY_KEEP_BUS_NO_4 = "KEEP_BUS_NO_4";// 高雄
	
	public final static String KEY_KEEP_BUS_NO_5 = "KEEP_BUS_NO_5";// 台鐵
	
	public static Constant instance;
	private Constant(){}
	public synchronized static Constant getInstance(){
		if(instance == null){ instance = new Constant();}
		return instance;
	}
	private MainActivity activity;
	public void setActivity(MainActivity activity){
		this.activity = activity;
	}
	public MainActivity getActivity(){
		return this.activity;
	}
}
