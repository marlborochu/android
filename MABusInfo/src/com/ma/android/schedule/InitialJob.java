package com.ma.android.schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import com.ma.android.dao.SQLLiteDAO;
import com.ma.android.utils.FileUtil;
import android.content.Context;

public class InitialJob extends ScheduleJob {

	private Context context;

	@Override
	public void setContext(Context arg) {
		// TODO Auto-generated method stub
		context = arg;
	}

	@Override
	public boolean isRun() {
		// TODO Auto-generated method stub
		return false;
	}

	boolean isRun;
	

	@Override
	public void run() {
		try {

			if (!isRun) {
				try {
					isRun = true;
					
				} finally {
					isRun = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
