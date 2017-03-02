package com.ma.android.utils;
/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 ������T�������q
 * 
 * */
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {
	
	private String default_encoding = "UTF-8";
	private static HttpUtil instance;
	public synchronized static HttpUtil getInstance(){
		if(instance == null) instance = new HttpUtil();
		return instance;
	}
	public String requestPost(String url, HashMap header,HashMap<String,String> parameter){
		
		ArrayList<NameValuePair> pairList = new ArrayList();
		Iterator ite = parameter.keySet().iterator();
		while(ite.hasNext()){
			String key = (String)ite.next();
			String value = parameter.get(key);
			pairList.add(new BasicNameValuePair(key,value));
		}
		return requestPost(url,header,pairList,this.default_encoding);
	}
	public String requestPost(String url, HashMap header,HashMap<String,String> parameter,String encoding){
		
		ArrayList<NameValuePair> pairList = new ArrayList();
		Iterator ite = parameter.keySet().iterator();
		while(ite.hasNext()){
			String key = (String)ite.next();
			String value = parameter.get(key);
			pairList.add(new BasicNameValuePair(key,value));
		}
		return requestPost(url,header,pairList,encoding);
	}
	public String requestPost(String url, HashMap header,List<NameValuePair> pairList){
		return requestPost(url,header,pairList,default_encoding);
	}
	public String requestPost(String url, HashMap header,List<NameValuePair> pairList,String encoding) {

		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 1000 * 120;

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 1000 * 15;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		try {

			HttpResponse httpResponse = null;

			HttpPost method = new HttpPost(url);
			if (header != null) {
				Iterator ite = header.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					String value = (String) header.get(key);
					method.setHeader(key, value);
				}
			}
			if(encoding == null) encoding = this.default_encoding;
			if(pairList != null)
				method.setEntity(new UrlEncodedFormEntity(pairList, encoding));

			httpResponse = httpclient.execute(method);

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();
				String result = new String(EntityUtils.toByteArray(resentity),encoding);
				return result;

			}
			
			Log.d(UtilsResource.ResourceName, httpResponse.getStatusLine().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public String request(String url) {
		return request(url,null,this.default_encoding);
	}
	public String request(String url,String encoding) {
		return request(url,null,encoding);
	}
	public String request(String url,HashMap header) {
		return request(url,header,this.default_encoding);
	}
	public String request(String url, HashMap header,String encoding) {

		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 1000 * 15;

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 1000 * 15;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);

		try {

			HttpResponse httpResponse = null;

			HttpGet method = new HttpGet(url);
			if (header != null) {
				Iterator ite = header.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					String value = (String) header.get(key);
					method.setHeader(key, value);
				}
			}
			
			if(encoding == null) encoding = this.default_encoding;
			
			httpResponse = httpclient.execute(method);
			
			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();
				String result = new String(EntityUtils.toByteArray(resentity),encoding);
				return result;

			}
			
			Log.d(UtilsResource.ResourceName, httpResponse.getStatusLine().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
		return null;
	}
}
