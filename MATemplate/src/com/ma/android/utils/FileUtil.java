package com.ma.android.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.JSONArray;

import android.os.Environment;
import android.util.Log;

public class FileUtil {
	
	private static FileUtil instance;
	public synchronized static FileUtil getInstance(){
		if(instance == null) instance = new FileUtil();
		return instance;
	}
	///storage 
	//.*\\.(mp3|png|jpg)$
	public ArrayList listFile(String dir,String pattern){
		
		ArrayList result = new ArrayList();
		IOFileFilter fileFilter = new RegexFileFilter(pattern);
		Collection<File> files = org.apache.commons.io.FileUtils.listFiles(
				new File(dir)
				, fileFilter, TrueFileFilter.INSTANCE);
		Iterator ite = files.iterator();
		while(ite.hasNext()){
			File f = (File) ite.next();
			result.add(f);
		}
		return result;
	}
	public JSONArray readFile(String filePath){
		
		try{
			File f = new File(filePath);
			if(f.exists()){
				
				JSONArray lyricStr = new JSONArray();
				
				
				FileInputStream fis = new FileInputStream(filePath);
				InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
				BufferedReader br = new BufferedReader(isr);
				try{
				String s;
				while((s = br.readLine()) != null){
					lyricStr.put(s);
				}
				}finally{
					br.close();
					isr.close();
					fis.close();
				}
				return lyricStr;
			}
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	public void writeFile(String filePath,ArrayList data){
		
		try{
			
			StringBuffer sb = new StringBuffer();
			for(int i = 0;i<data.size();i++){
				sb.append(data.get(i)+"\r\n");
			}
			
			File f = new File(filePath);
			if(f.exists()){
				
				java.io.FileWriter fw = new java.io.FileWriter(f, false);

				try {
					fw.write(sb.toString());
				} finally {
					fw.flush();
					fw.close();
				}
				
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
}
