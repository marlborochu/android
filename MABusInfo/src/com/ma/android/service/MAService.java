package com.ma.android.service;

import com.ma.android.schedule.ScheduleManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MAService extends Service{
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d(MAService.class.getSimpleName(), "onStartCommand");
		ScheduleManager.getInstance(this).startSchedule();
		return super.onStartCommand(intent, flags, startId);
		
	}

	@Override
	public void onDestroy() {
		Log.d(MAService.class.getSimpleName(), "onDestroy");
		ScheduleManager.getInstance(this).cancleSchedule();
		super.onDestroy();
	}
	
	
}
