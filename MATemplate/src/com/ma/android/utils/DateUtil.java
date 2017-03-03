package com.ma.android.utils;
import java.text.*;
import java.util.*;

public class DateUtil{
	
	//private org.apache.log4j.Logger logger = Log4jUtil.getInstance().getLogger(com.bull.prj.Constant.getInstance().getApp_log_name());
	private DateUtil(){}
	private static DateUtil du ;
	private String format_type = "yyyy/MM/dd";
	private String default_datetime_format = "yyyyMMddkkmmss";
	private SimpleDateFormat sdf = new SimpleDateFormat(format_type);
	
	public synchronized static DateUtil getInstance(){
		if(du == null){
			du = new DateUtil();
		}	
		return du;
	}
	/**
	 * ��o�G�ծɶ����@��t
	 **/
	public long TimeInterval(Calendar startdate, Calendar enddate) {
		
		
		long interval_times = 0;
		try {
			interval_times = enddate.getTimeInMillis()
					- startdate.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_times;

	}
	/**
	 * ��o�G�ծɶ����@��t
	 **/
	public long TimeInterval(String startdate, String enddate) {
		
		long interval_times = 0;
		try {
			Date sd = sdf.parse(startdate);
			Date ed = sdf.parse(enddate);
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(sd);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(ed);
			interval_times = TimeInterval(sc, ec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_times;
	}
	
	public long TimeInterval(Date startdate, Date enddate) {
		
		long interval_times = 0;
		try {
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(startdate);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(enddate);
			interval_times = TimeInterval(sc, ec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_times;
	}
	
	/**
	 * ��t�p��
	 * 
	 * @param startdate -
	 *            �_��
	 * @param enddate -
	 *            ����
	 */
	public int DayInterval(Calendar startdate, Calendar enddate) {
		
		int interval_days = 0;
		try {
			Date sd = sdf.parse(formatDate(startdate));
			Date ed = sdf.parse(formatDate(enddate));
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(sd);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(ed);

			interval_days = (int) (TimeInterval(sc, ec) / (1000L * 60L * 60L * 24L));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_days;

	}
	/**
	 * ��t�p��
	 * 
	 * @param startdate -
	 *            �_��
	 * @param enddate -
	 *            ����
	 */

	public int DayInterval(String startdate, String enddate) {
		
		int interval_days = 0;
		try {
			Date sd = sdf.parse(startdate);
			Date ed = sdf.parse(enddate);
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(sd);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(ed);
			interval_days = DayInterval(sc, ec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_days;

	}
	/**
	 * ��t�p��
	 * 
	 * @param startdate -
	 *            �_��
	 * @param enddate -
	 *            ����
	 */
	public int DayInterval(Date begin_date, Date end_date) {
		Calendar tsc = Calendar.getInstance();
		tsc.setTimeInMillis(begin_date.getTime());
		Calendar dc = Calendar.getInstance();
		dc.setTimeInMillis(end_date.getTime());
		return DayInterval(tsc, dc);
	}
	/**
	 * ����϶���� ( if begin_date<=check_date<=end_date return true )
	 *
	 * @param startdate -
	 *            �_��
	 * @param enddate -
	 *            ���� 
	 * @param check_date - 
	 *            
	 **/
	public boolean compareDate(Date begin_date, Date end_date,Date check_date){
		
		
		if( DayInterval(begin_date,check_date) >=0 && 
			DayInterval(check_date,end_date) >= 0 ){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * �榡�ഫ
	 * yyyy/MM/dd
	 */
	public String formatDate(Calendar d) {
		if (d != null) {
			Date dd = d.getTime();
			return formatDate(dd, format_type);
		} else {
			return null;
		}
	}
	/**
	 * �榡�ഫ
	 * yyyy/MM/dd
	 */
	public String formatDate(Date dd) {
		if (dd != null) {
			return formatDate(dd, format_type);
		} else {
			return null;
		}
	}
	/**
	 * �榡�ഫ
	 * @param d : �������?
	 * @param format �榡 yyyy/MM/dd hh:mm:ss ; yyyy-MM-dd hh:mm:ss ; yyyyMMddhhmmss .....
	 */
	public String formatDate(Date d, String format) {
		if (d != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(d);
		} else {
			return null;
		}
	}
	/**
	 * �N string �ର �������?
	 * @param dd : �r���� , �榡�� yyyy/MM/dd
	 */
	public Date parseDate(String dd) {
		
		if (dd != null) {
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(format_type);
				return sdf.parse(dd);
			}catch(Exception e){
				e.printStackTrace();
				return null;	
			}
		} else {
			return null;
		}
	}
	
	/**
	 * �N string �ର �������?
	 * @param dd : �r���� 
	 * @param pattern �r�����榡
	 */
	public Date parseDate(String dd,String pattern) {
		
		if (dd != null) {
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				return sdf.parse(dd);
			}catch(Exception e){
				e.printStackTrace();
				return null;	
			}
		} else {
			return null;
		}
	}
    public Date addMinutes(Date date, int minutes) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE , minutes);
		return c.getTime();
	}
    public Date addSecond(Date date, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND , second);
		return c.getTime();
	}
	public Date addHours(Date date, int hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hours);
		return c.getTime();
	}
	public Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	public Date addWeeks(Date date, int weeks) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR , weeks);
		return c.getTime();

	}
	
	public Date addMonths(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();

	}
	
	/**
	 * ��o����
	 */
	public int getDayOfMonth(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(c.DAY_OF_MONTH);
	}
	
	public String getFormatDate(String pattern){
		return formatDate(new Date(), pattern);
	}
	
	public Date getLatestDateOfMonth(Date date) {
		
		Calendar sc = (Calendar) Calendar.getInstance().clone();
		sc.setTime(date);
		Date rtnDate = null;
		
		int month = sc.get(Calendar.MONTH);
		for (int i = 1; i < 5; i++) {
			sc.set(Calendar.DAY_OF_MONTH, 28 + i);
			if (sc.get(Calendar.MONTH) != month) {
				rtnDate = addDays(sc.getTime(),-1);
				break;
			}
		}
		return rtnDate;
		
	}
	
	public Date getFirstDateOfMonth(Date date){
		
		Calendar sc = (Calendar) Calendar.getInstance().clone();
		sc.setTime(date);
		int dates = sc.get(Calendar.DAY_OF_MONTH);
		sc.add(Calendar.DAY_OF_MONTH, 1-dates) ;
		return sc.getTime();
		
	}
    
    public boolean isLastDateOfMonth(Date date){
        Calendar sc = (Calendar) Calendar.getInstance().clone();
		sc.setTime(date);
        int lastDate = getDayOfMonth(getLatestDateOfMonth(date));
        //System.out.println(lastDate);
        return lastDate == getDayOfMonth(date);
    }
    
    public Date getTWDate(){
    	
    	Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(default_datetime_format);
		sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8:00"));
		String curDateTime = sdf.format(curDate);
		curDate = DateUtil.getInstance().parseDate(curDateTime, default_datetime_format);
		return curDate;
		
    }
    
	public static void main(String args[]){
		
		
		Date bd = new Date();
		/*
		Date ed = getInstance().addDays(bd,20);
		Date cd = getInstance().addDays(bd,20);
		*/
		System.out.println(getInstance().formatDate(getInstance().getLatestDateOfMonth(bd)));	
		System.out.println(getInstance().formatDate(getInstance().getFirstDateOfMonth(bd)));	
	}
}
