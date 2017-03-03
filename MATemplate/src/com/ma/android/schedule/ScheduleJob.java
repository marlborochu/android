package com.ma.android.schedule;

import java.util.TimerTask;

import android.content.Context;

public abstract class ScheduleJob extends TimerTask{
	
	public abstract void setContext(Context context);
	public abstract boolean isRun();
}
