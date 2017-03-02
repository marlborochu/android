package com.ma.android.service;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MAWidgetProvider extends AppWidgetProvider {

	private Context context;

	
	public void onUpdate(Context arg, AppWidgetManager gm, int[] appWidgetIds) {
		
		Log.d(this.getClass().getSimpleName(), "onUpdate");
		context = arg;
		{
			Intent intent = new Intent(arg, this.getClass());
			arg.startService(intent);
		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onReceive(context, intent);
	}
	
	private void updateWidget(Context context,AppWidgetManager gm){
		try{
			
			Log.d(this.getClass().getSimpleName(), "updateWidget");
	

			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
//			gm.updateAppWidget(new ComponentName(context.getApplicationContext(),
//					this.getClass()), views);
			
		}catch(Exception e){e.printStackTrace();}
	}
	
	@Override
	public void onDeleted(Context arg, int[] appWidgetIds) {
		Log.d(this.getClass().getSimpleName(), "onDeleted");
		context = arg;
	}

	@Override
	public void onEnabled(Context arg) {
		
		Log.d(this.getClass().getSimpleName(), "onEnabled");
		context = arg;

	}

}
