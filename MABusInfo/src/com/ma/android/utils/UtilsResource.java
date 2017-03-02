package com.ma.android.utils;
/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 ������T�������q
 * 
 * */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

public class UtilsResource {
	
	public final static String ResourceName = "android-utils";
	
	public final static String KEY_DATABASE_NAME = "DATABASE-NAME";
	public final static String KEY_DATABASE_VERSION = "DATABASE-VERSION";
	public final static String KEY_SQL_FILE_INIT_TABLE = "INIT-TABLE-FILE-NAME";
	public final static String KEY_SQL_FILE_INIT_DATA = "INIT-DATA-FILE-NAME";
	public final static String KEY_SQL_FILE_UPGRADE_TABLE = "UPGRADE-TABLE-FILE_NAME";
	public final static String KEY_SQL_FILE_UPGRADE_DATA = "UPGRADE-DATA-FILE-NAME";
	
	public final static String KEY_VIEW_ID = "VIEW-ID.";
	public final static String KEY_SCHEDULE_JOB_ID = "SCHEDULE-JOB.";
	
	public final static String KEY_TTS_SUBJECT = "SUBJECT";
	public final static String KEY_TTS_CONTEXT = "CONTEXT";
	
	public final static BigDecimal KEY_PAGE_DEFAULT_SIZE = new BigDecimal(0.065);
	
	private static UtilsResource instance;
	public synchronized static UtilsResource getInstance(){		
		if(instance == null){ instance = new UtilsResource();}
		return instance;		
	} 
	
	private ResourceBundle resource;
	private UtilsResource(){
		resource = ResourceBundle.getBundle(ResourceName);
	}
	public String getData(String key,String defaultValue){
		if(resource.containsKey(key)) return resource.getString(key);
		return defaultValue;
	}
	
	private ArrayList<String> viewIds;
	public ArrayList<String> getViewIds(){ 
		
		if(viewIds == null){
			viewIds = new ArrayList();
			Iterator ite = resource.keySet().iterator();
			ArrayList<String> keys = new ArrayList();
			while(ite.hasNext()){
				String key = (String)ite.next();
				if(key.startsWith(this.KEY_VIEW_ID)){
					keys.add(key);
				}
			}
			Collections.sort(keys);
			for(int i = 0;i<keys.size();i++){
				viewIds.add(resource.getString(keys.get(i)));
			}
		}
		return viewIds;
	}
	private ArrayList<String> scheduleJobs;
	public ArrayList<String> getScheduleJobs(){ 
		
		if(scheduleJobs == null){
			scheduleJobs = new ArrayList();
			Iterator ite = resource.keySet().iterator();
			ArrayList<String> keys = new ArrayList();
			while(ite.hasNext()){
				String key = (String)ite.next();
				if(key.startsWith(this.KEY_SCHEDULE_JOB_ID)){
					keys.add(key);
				}
			}
			Collections.sort(keys);
			for(int i = 0;i<keys.size();i++){
				scheduleJobs.add(resource.getString(keys.get(i)));
			}
		}
		return scheduleJobs;
	}
	
	public final static String KEY_A="A";
	public final static String KEY_B="B";
	public final static String KEY_C="C";
	public final static String KEY_D="D";
	public final static String KEY_E="E";
	public final static String KEY_F="F";
	public final static String KEY_G="G";
	public final static String KEY_H="H";
	public final static String KEY_I="I";
	public final static String KEY_J="J";
	public final static String KEY_K="K";
	public final static String KEY_L="L";
	public final static String KEY_M="M";
	public final static String KEY_N="N";
	public final static String KEY_O="O";
	public final static String KEY_P="P";
	public final static String KEY_Q="Q";
	public final static String KEY_R="R";
	public final static String KEY_S="S";
	public final static String KEY_T="T";
	public final static String KEY_U="U";
	public final static String KEY_V="V";
	public final static String KEY_W="W";
	public final static String KEY_X="X";
	public final static String KEY_Y="Y";
	public final static String KEY_Z="Z";
	
	public final static String KEY_DATA="data";
	public final static String KEY_LAYOUT_ID="layout_id";
}
