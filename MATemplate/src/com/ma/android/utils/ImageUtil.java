package com.ma.android.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImageUtil {
	
	private static ImageUtil instance;
	private ImageUtil(){}
	public synchronized static ImageUtil getInstance(){
		if(instance == null) instance = new ImageUtil();
		return instance;
	}
	
	public ByteArrayInputStream readJPEGImage(InputStream br,String length) throws Exception{
		
		try{
			
			byte[] imgbyte = new byte[Integer.valueOf(length)];
			
			boolean end = false;
			imgbyte[0] = (byte) 0xff;
			imgbyte[1] = (byte) 0xd8;
			int imgIndex = 2;
			int[] b = new int[2];
			while(true){
				
				b[0] = br.read();
				b[1] = br.read();
				if( (b[0] == 0xff && b[1] == 0xd9) ){
					end = true;
				}
				if(imgbyte.length-2 > imgIndex){
					imgbyte[imgIndex++] =  (byte) b[0];
					imgbyte[imgIndex++] =  (byte) b[1];
				}else{
					imgbyte[imgbyte.length-2] =  (byte)0xff;
					imgbyte[imgbyte.length-1] =  (byte)0xd9;
					break;
				}
				if(end) break;
				
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(imgbyte);
			return bais;
				        
		}finally{
//			fos.close();
		}
	
	}
}
