package com.ma.android.schedule;

/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 ������T�������q
 * 
 * */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.ma.android.utils.DateUtil;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class ScheduleManager {

	private static ScheduleManager instance;

	private ScheduleManager() {
	}

	public synchronized static ScheduleManager getInstance(Context context) {
		if (instance == null) {
			instance = new ScheduleManager();
			instance.initJob();
		}
		instance.context = context;
		return instance;
	}

	private Timer timer = null;
	private Context context;

	public void startSchedule() {
		
		if(timer != null){
			cancleSchedule();
		}
		Log.d(ScheduleManager.class.getSimpleName(), "Start Schedule Job....");
		timer = new Timer(true);
		timer.schedule(new Schedule(), new Date(), 1000);
	}

	public void cancleSchedule() {
		
		Log.d(ScheduleManager.class.getSimpleName(), "Cancle Schedule Job....");
		if(timer != null){
			try{
				timer.cancel();
			}catch(Exception e){e.printStackTrace();}
			timer = null;
		}
	}

	private Date executeDate = new Date();

	public Date getExecuteDate() {
		return executeDate;
	}
	private HashMap<ScheduleJob,String> jobs = new HashMap();
	public HashMap<ScheduleJob,String> getJobs(){
		return (HashMap)this.jobs;
	}
	/**
	 * ==> * * * * * *
	 * ==> 秒 分 時 日 月 年
	 * */
	private void initJob(){
//		jobs.put(new DownloadJob(),"*/5 * * * * *");
//		jobs.put(new CheckStorageSize(),"*/10 * * * * *");
	}
	public void startJob(){
		Iterator ite = jobs.keySet().iterator();
		while(ite.hasNext()){
			try{
				ScheduleJob tt = (ScheduleJob)ite.next();
				tt.setContext(context);
				new Thread(tt).start();
			}catch(Exception e){e.printStackTrace();}
		}
	}
	class Schedule extends TimerTask {
		
		public Schedule(){
			
//			startJob();
			
		}
		boolean isScreenOn = true;
		boolean firstTimeScreenOn = false;
		boolean firstTimeRun = false;
		public boolean execute(Date d,String s){
			
			boolean isRun = true;
			
			try{
				
				String[] sf = s.split(" ");
				for(int i = 0;i<sf.length && isRun;i++){
					
					String[] sfs = sf[i].split("/");
					int checkValue = -1;
					if(sfs.length == 2){
						checkValue = Integer.valueOf(sfs[1]);
					}else if(!sfs[0].equals("*")){
						checkValue = Integer.valueOf(sfs[0]);
					}
					if(checkValue == -1) continue;
					
					int timeValue = -1;
					switch(i){
						case 0 :
							timeValue = Integer.valueOf(DateUtil.getInstance().formatDate(d,"ss"));						
							break;
						case 1 :					
							timeValue = Integer.valueOf(DateUtil.getInstance().formatDate(d,"mm"));
							break;
						case 2 :
							timeValue = Integer.valueOf(DateUtil.getInstance().formatDate(d,"kk"));
							break;
						case 3 :
							timeValue = Integer.valueOf(DateUtil.getInstance().formatDate(d,"dd"));
							break;
						case 4 :
							timeValue = Integer.valueOf(DateUtil.getInstance().formatDate(d,"MM"));
							break;
						case 5 :
							timeValue = Integer.valueOf(DateUtil.getInstance().formatDate(d,"yyyy"));
							break;
						default :
							break;
					};
					if(sfs.length == 2){
						if(timeValue%checkValue == 0) isRun = true;
						else isRun = false;
					}else{
						if(checkValue == timeValue) isRun = true;
						else isRun = false;
					}
					
				}
				
			}catch(Exception e){e.printStackTrace();}
			
			return isRun;
		}
		@Override
		public void run() {
			
			try{
				
				executeDate = new Date();
				
				Iterator ite = jobs.keySet().iterator();
				while(ite.hasNext()){
					ScheduleJob tt = (ScheduleJob)ite.next();
					String ed = jobs.get(tt);
					boolean isRun = execute(executeDate,ed);
					if(isRun){
						tt.setContext(context);
						new Thread(tt).start();
					}
				}
				

				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
