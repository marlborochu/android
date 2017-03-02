package com.ma.android.dao;
/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 ������T�������q
 * 
 * */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.ma.android.utils.DateUtil;
import com.ma.android.utils.UtilsResource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLLiteDAO extends SQLiteOpenHelper {
	
	
	private static String DATABASE_NAME;
	private static int DATABASE_VERSION = 1;
	public String SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public String KEY_STRING_TYPE = "string";
	public String KEY_INTEGER_TYPE = "integer";
	public String KEY_DATE_TYPE = "date";
	public String KEY_GET = "get";
	public String KEY_SET = "set";
	public String SQL_STRING_FLAG = "'";
	public String SQL_END_FLAG = ";";
	
	private Context context;
	
	private static SQLLiteDAO instance;
	public synchronized static SQLLiteDAO getInstance(Context context) {
		if (instance == null){
			DATABASE_NAME = UtilsResource.getInstance().getData(UtilsResource.KEY_DATABASE_NAME,null);
			DATABASE_VERSION = Integer.valueOf(UtilsResource.getInstance().getData(UtilsResource.KEY_DATABASE_VERSION, null));
			instance = new SQLLiteDAO(context);
		}
		return instance;
	} 

	private SQLLiteDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {

			{
				InputStream is = SQLLiteDAO.class.getResourceAsStream(
						UtilsResource.getInstance().getData(UtilsResource.KEY_SQL_FILE_INIT_TABLE, null)
						);
				Reader fis = new InputStreamReader(is);
				try {
					BufferedReader br = new BufferedReader(fis);
					String line = "";
					StringBuffer sqlsb = new StringBuffer();
					while ((line = br.readLine()) != null) {
						sqlsb.append(line);
						if (line.trim().endsWith(SQL_END_FLAG)) {
							db.execSQL(sqlsb.toString());
							sqlsb.delete(0, sqlsb.length());
						}
					}
				} finally {
					fis.close();
					is.close();
				}
			}
			{
				
				try{
					
					String dataFile = UtilsResource.getInstance().getData(UtilsResource.KEY_SQL_FILE_INIT_DATA, null);
					if(dataFile != null && !dataFile.trim().equals("")){
						Log.d(this.getClass().getSimpleName(), dataFile+"=========");
						InputStream is = SQLLiteDAO.class
								.getResourceAsStream(dataFile);
						Reader fis = new InputStreamReader(is);
						try { 
							BufferedReader br = new BufferedReader(fis);
							String line = "";
							StringBuffer sqlsb = new StringBuffer();
							while ((line = br.readLine()) != null) {
								sqlsb.append(line);
								if (line.trim().endsWith(SQL_END_FLAG)) {
									db.execSQL(sqlsb.toString());
									sqlsb.delete(0, sqlsb.length());
								}
							}
						} finally {
							fis.close();
							is.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		try {

			{
				Log.d(SQLLiteDAO.class.getSimpleName(), "UPDATE DB");
				InputStream is = SQLLiteDAO.class.getResourceAsStream(
						UtilsResource.getInstance().getData(UtilsResource.KEY_SQL_FILE_UPGRADE_TABLE, null)
						);
				Reader fis = new InputStreamReader(is);
				try {
					BufferedReader br = new BufferedReader(fis);
					String line = "";
					StringBuffer sqlsb = new StringBuffer();
					while ((line = br.readLine()) != null) {
						sqlsb.append(line);
						if (line.trim().endsWith(SQL_END_FLAG)) {
							db.execSQL(sqlsb.toString());
							sqlsb.delete(0, sqlsb.length());
						}
					}
				} finally {
					fis.close();
					is.close();
				}
			}
			{
				InputStream is = SQLLiteDAO.class
						.getResourceAsStream(UtilsResource.getInstance().getData(UtilsResource.KEY_SQL_FILE_UPGRADE_DATA, null));
				Reader fis = new InputStreamReader(is);
				try {
					BufferedReader br = new BufferedReader(fis);
					String line = "";
					StringBuffer sqlsb = new StringBuffer();
					while ((line = br.readLine()) != null) {
						sqlsb.append(line);
						if (line.trim().endsWith(SQL_END_FLAG)) {
							db.execSQL(sqlsb.toString());
							sqlsb.delete(0, sqlsb.length());
						}
					}
				} finally {
					fis.close();
					is.close();
				}
			}
//			new InitialJob().run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		
	}

	public ArrayList query(String sql, String[] args, Class c) {

		ArrayList result = new ArrayList();
		SQLiteDatabase db = getReadableDatabase();
	
		Cursor cursor = db.rawQuery(sql, args);
		try {

			String[] names = cursor.getColumnNames();
			int count = cursor.getCount();
			try {
				for (int i = 0; i < count; i++) {
					cursor.moveToNext();
					Object o = c.newInstance();
					for (int j = 0; j < names.length; j++) {

						String name = names[j];
						Method methods = o.getClass().getMethod(
								KEY_GET + name, null);
						if (cursor.isNull(j))
							continue;
						if (methods.getReturnType().getSimpleName()
								.toLowerCase().equals(KEY_STRING_TYPE)) {
							o.getClass()
									.getMethod(KEY_SET + name,
											methods.getReturnType())
									.invoke(o, cursor.getString(j));
						} else if (methods.getReturnType().getSimpleName()
								.toLowerCase()
								.equals(KEY_INTEGER_TYPE)) {
							o.getClass()
									.getMethod(KEY_SET + name,
											methods.getReturnType())
									.invoke(o, cursor.getInt(j));
						} else if (methods.getReturnType().getSimpleName()
								.toLowerCase().equals(KEY_DATE_TYPE)) {
							SimpleDateFormat format = new SimpleDateFormat(
									SQL_DATETIME_FORMAT);
							o.getClass()
									.getMethod(KEY_SET + name,
											methods.getReturnType())
									.invoke(o,
											format.parse(cursor.getString(j)));
						}
						// Log.d(Constant.TAG_NAME,methods.getReturnType().getSimpleName()+"========="+cursor.getString(j));
					}
					result.add(o);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			cursor.close();
			
		}
		return result;
	}

	

	public void execute(String str) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.execSQL(str);
		} finally {
			
		}
	}
	
	public void insert(String tableName,ContentValues cvalues) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.insert(tableName, null, cvalues);
		} finally {
			
		}
	}
	
	public void insert(Object o) throws Exception {

		ArrayList result = new ArrayList();
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			Method[] methods = o.getClass().getMethods();
//			HashMap hm = new HashMap();
			ContentValues cvalues = new ContentValues();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith(KEY_GET)) {
					String methodName = methods[i].getName().replaceAll(
							KEY_GET, "");
					if (methodName.equalsIgnoreCase("class")) {
						continue;
					}
					Object input = methods[i].invoke(o, null);
					if (input != null) {
						if (input instanceof String){
//							hm.put(methodName,
//									SQL_STRING_FLAG + input.toString()
//											+ SQL_STRING_FLAG);
							cvalues.put(methodName, input.toString());
						}else if (input instanceof Integer){
//							hm.put(methodName,
//									SQL_STRING_FLAG + input.toString()
//											+ SQL_STRING_FLAG);
							cvalues.put(methodName, input.toString());
						}else if (input instanceof Date){
//							hm.put(methodName,
//									SQL_STRING_FLAG
//											+ DateUtil
//													.getInstance()
//													.formatDate(
//															(Date) input,
//															SQL_DATETIME_FORMAT)
//											+ SQL_STRING_FLAG);
							cvalues.put(methodName, DateUtil
									.getInstance()
									.formatDate(
											(Date) input,
											SQL_DATETIME_FORMAT));
						}
					}
				}
			}
			String tableName = o.getClass().getSimpleName();
//			Log.d(this.getClass().getSimpleName(), "insert "+tableName+" ["+cvalues+"]");
//			Log.d(SQLLiteDAO.class.getSimpleName(), "insert "+tableName+" ["+cvalues+"]");
			insert(o.getClass().getSimpleName(), cvalues);
			
			
//			StringBuffer sb = new StringBuffer();
//			sb.append("INSERT INTO " + tableName + "(");
//			Iterator ite = hm.keySet().iterator();
//			while (ite.hasNext()) {
//				sb.append(ite.next() + ",");
//			}
//			sb.delete(sb.length() - 1, sb.length());
//			sb.append(") VALUES (");
//			ite = hm.keySet().iterator();
//			while (ite.hasNext()) {
//				sb.append(hm.get(ite.next()) + ",");
//			}
//			sb.delete(sb.length() - 1, sb.length());
//			sb.append(")");
//			db.execSQL(sb.toString());
			
		} finally {
			
		}
	}

	

	public void delete(Object o, String byColName) throws Exception {

		ArrayList result = new ArrayList();
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			delete(db,o,byColName);
		} finally {
			
		}
	}

	public void delete(SQLiteDatabase db, Object o, String byColName)
			throws Exception {

		ArrayList result = new ArrayList();
		try {
			Method[] methods = o.getClass().getMethods();
			HashMap hm = new HashMap();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith(KEY_GET)) {
					String methodName = methods[i].getName().replaceAll(
							KEY_GET, "");
					if (methodName.equalsIgnoreCase("class")) {
						continue;
					}
					Object input = methods[i].invoke(o, null);
					if (input != null) {
						if (input instanceof String)
							hm.put(methodName,
									SQL_STRING_FLAG + input.toString()
											+ SQL_STRING_FLAG);
						else if (input instanceof Integer)
							hm.put(methodName,
									SQL_STRING_FLAG + input.toString()
											+ SQL_STRING_FLAG);
						else if (input instanceof Date)
							hm.put(methodName,
									SQL_STRING_FLAG
											+ DateUtil
													.getInstance()
													.formatDate(
															(Date) input,
															SQL_DATETIME_FORMAT)
											+ SQL_STRING_FLAG);
					}
				}
			}
			String tableName = o.getClass().getSimpleName();
			StringBuffer sb = new StringBuffer();
			sb.append("DELETE FROM " + tableName);

			if (byColName != null) {
				sb.append(" WHERE " + byColName + " = " + hm.get(byColName)
						+ "");
			} else {
				sb.append(" WHERE 1=1 ");
				Iterator ite = hm.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					sb.append(" AND " + key + " = " + hm.get(key) + "");
				}
			}
//			Log.d(SQLLiteDAO.class.getSimpleName(), sb.toString());
			db.execSQL(sb.toString());
		
		} finally {
		}
	}

	public String genKEY(String TABLE_NAME, String PK_NAME, String PK_PREFIX,
			int PK_LENGTH) {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT MAX(" + PK_NAME + ") FROM "
				+ TABLE_NAME, null);
		try {
			String key = null;
			if (PK_PREFIX != null && cursor != null && cursor.moveToNext()
					&& (key = cursor.getString(0)) != null) {

				String type = "";
				String num = "";
				char[] chartArray = key.toCharArray();

				for (int i = 0; i < chartArray.length; i++) {
					if (chartArray[i] >= 48 && chartArray[i] <= 57) {
						char[] n = new char[1];
						n[0] = chartArray[i];
						num += new String(n);
					} else {
						char[] n = new char[1];
						n[0] = chartArray[i];
						type += new String(n);
					}
				}
				int numIndex = num.length();
				int retNum = (Integer.valueOf(num) + 1);
				String retNums = "";
				for (int i = 0; i < numIndex; i++) {
					retNums += "0";
				}
				retNums += retNum;
				retNums = retNums.substring(retNums.length() - numIndex);
				return type + retNums;
			}else if( cursor != null && cursor.moveToNext()
					&& (key = cursor.getString(0)) != null ){
				return (Integer.valueOf(key)+1)+"";
				
			} else {
				String retNums = "";
				for (int i = 0; i < PK_LENGTH; i++) {
					retNums += "0";
				}
				retNums += 1;
				retNums = retNums.substring(retNums.length() - PK_LENGTH);
//				return PK_PREFIX + retNums;
				if(PK_PREFIX != null) return PK_PREFIX + retNums;
				else return retNums;
			}
		} finally {
			cursor.close();
		}
	}
}
