package com.ma.template.dao;

import java.util.ArrayList;
import java.util.Date;

import com.ma.android.dao.SQLLiteDAO;
import com.ma.template.pojos.MA_PARAMETERS;

import android.content.Context;

public class MA_PARAMTERDAO {
	public String getParameter(Context context, String key) {

		try {

			String sql = "SELECT * FROM MA_PARAMETERS WHERE MA_KEY = ?";
			String[] args = { key };
			ArrayList<MA_PARAMETERS> result = SQLLiteDAO.getInstance(context)
					.query(sql, args, MA_PARAMETERS.class);
			if (result != null && !result.isEmpty()) {
				return result.get(0).getMA_VALUE();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public String getParameter(Context context, String key,String defaultValue) {

		try {

			String sql = "SELECT * FROM MA_PARAMETERS WHERE MA_KEY = ?";
			String[] args = { key };
			ArrayList<MA_PARAMETERS> result = SQLLiteDAO.getInstance(context)
					.query(sql, args, MA_PARAMETERS.class);
			if (result != null && !result.isEmpty()) {
				return result.get(0).getMA_VALUE();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;

	}
	public Date getParameterTime(Context context, String key) {

		try {

			String sql = "SELECT * FROM MA_PARAMETERS WHERE MA_KEY = ?";
			String[] args = { key };
			ArrayList<MA_PARAMETERS> result = SQLLiteDAO.getInstance(context)
					.query(sql, args, MA_PARAMETERS.class);
			if (result != null && !result.isEmpty()) {
				return result.get(0).getMA_DATA_DATE();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public void setParameter(Context context, String key, String value) {
		try {
			
			removeParameter(context,key);
			
			MA_PARAMETERS param = new MA_PARAMETERS();
			param.setMA_KEY(key);
			param.setMA_VALUE(value);
			param.setMA_DATA_DATE(new Date());
			
			SQLLiteDAO.getInstance(context).insert(param);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void removeParameter(Context context, String key){
		try {

			String sql = "SELECT * FROM MA_PARAMETERS WHERE MA_KEY = ?";
			String[] args = { key };
			ArrayList<MA_PARAMETERS> result = SQLLiteDAO.getInstance(context)
					.query(sql, args, MA_PARAMETERS.class);
			if (result != null && !result.isEmpty()) {
				SQLLiteDAO.getInstance(context).delete(result.get(0), "MA_KEY");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
