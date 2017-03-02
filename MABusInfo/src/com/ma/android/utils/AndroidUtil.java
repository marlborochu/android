package com.ma.android.utils;
/**
 * @author Marlboro.chu@gmail.com
 * @copyright 2015 ������T�������q
 * 
 * */
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;








import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

public class AndroidUtil {

	private static AndroidUtil instance;
	public final String KEY_DATA = "data";
	public final static int REQUEST_TAKE_PHOTO = 1;
	public final static int REQUEST_PHOTO = 2;
	
	public final static String KEY_IMG_TYPE = ".jpeg";

	private AndroidUtil() {
	}

	public synchronized static AndroidUtil getInstance() {
		if (instance == null) {
			instance = new AndroidUtil();
		}
		return instance;
	}

	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	public static int generateViewId() {
		for (;;) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range
			// under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF)
				newValue = 1; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				return result;
			}
		}
	}

//	public PopupWindow getDefaultPopupWindow(Context context, ArrayList<String> infos,
//			final Handler handler) {
//
//		final PopupWindow popup = new PopupWindow(context);
//		ListView pwListView = new ListView(context);
//
//		StringArrayAdapter saa = new StringArrayAdapter(context,
//				android.R.layout.simple_list_item_1, infos);
//		pwListView.setAdapter(saa);
//
//		pwListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				if (popup != null && popup.isShowing())
//					popup.dismiss();
//
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				data.putInt(KEY_DATA, position);
//
//				msg.setData(data);
//
//				handler.sendMessage(msg);
//
//			}
//
//		});
//
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		pwListView.setLayoutParams(params);
//		popup.setFocusable(true);
//		// popup.setBackgroundDrawable(null);
//
//		popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//		popup.setContentView(pwListView);
//
//		return popup;
//
//	}
//	public PopupWindow getPopupWindow(Context context, ArrayList<View> infos,
//			final Handler handler) {
//
//		final PopupWindow popup = new PopupWindow(context);
//		ListView pwListView = new ListView(context);
//
//		CustArrayAdapter saa = new CustArrayAdapter(context,
//				android.R.layout.simple_list_item_1, infos);
//		pwListView.setAdapter(saa);
//
//		pwListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				if (popup != null && popup.isShowing())
//					popup.dismiss();
//
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				data.putInt(KEY_DATA, position);
//
//				msg.setData(data);
//
//				handler.sendMessage(msg);
//
//			}
//
//		});
//
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		pwListView.setLayoutParams(params);
//		popup.setFocusable(true);
//		// popup.setBackgroundDrawable(null);
//
//		popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//		popup.setContentView(pwListView);
//
//		return popup;
//
//	}
	
	public boolean getNetWorkStatus(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
	}
	
	public void saveKeyValue(Activity context, String key, String value) {

		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();

	}
	public void removeKeyValue(Activity context, String key) {

		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.commit();

	}
	public String getKeyValue(Activity context, String key) {
		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getString(key, "");
	}
	public String getKeyValue(Activity context, String key,String defaultValue) {
		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getString(key, defaultValue);
	}
	public float convertPixelsToDp(float px, Context context) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public float convertDpToPixel(float dp, Context context) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;

	}

	public void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	public File getStorageDir(String appName){
		File f = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		File fd = new File(f.getAbsolutePath()+File.separator+appName);
		if(!fd.exists()) fd.mkdirs();
		return fd;
	}
	public void takePictureIntent(Activity context,String appName, String fileName) {

		Intent tpi = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (tpi.resolveActivity(context.getPackageManager()) != null) {
			
			try {
				
				File storageDir = getStorageDir(appName);
//				File storageDir = Environment.getDownloadCacheDirectory();
//				File storageDir = new File("/data");
				fileName = fileName + KEY_IMG_TYPE;
				File photoFile = new File(storageDir, fileName);
				
				tpi.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				context.startActivityForResult(tpi, REQUEST_TAKE_PHOTO);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showToast(context, "Cannot open camera!!");
			}
			
		}

	}

	public void getPicture(Activity context) {

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		context.startActivityForResult(intent, REQUEST_PHOTO);

	}
	
	public Drawable getDrawable(Context context,File f){
		
		BitmapFactory.Options options = new BitmapFactory.Options();		
		options.inSampleSize = 6;
		Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
		return new BitmapDrawable(context.getResources(),myBitmap);
//		((LinearLayout)v.findViewById(R.id.linearlayout1)).setBackgroundDrawable(drawable);
		
	}
	public void downloadFile(String url, String appName, String fileName) {

		File filePath = getStorageDir(appName);
		try {
			
			Log.d("Temp", "beging download file["+filePath.getPath()+"/"+fileName+"]");
			File f = new File(getStorageDir(appName), fileName);
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			
			
			URL u = new URL(url);
			URLConnection con = u.openConnection();
			InputStream is = con.getInputStream();
			FileOutputStream fos = new FileOutputStream(f);
			try{
			byte[] buffer = new byte[1024];
			for (int length; (length = is.read(buffer)) > 0; fos.write(buffer,
					0, length)){
//				Log.d("Temp", "download length ["+length+"]");
			}
			fos.flush();	
			}finally{
				fos.close();
				is.close();
			}
			Log.d("Temp", "download success ["+filePath.getPath()+":"+fileName+"]");
		} catch (Exception e) {
			Log.e("Temp", "download fail ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	public void showButtonDialog(Context context, String title, String message,
			String button) {

		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(button,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						}).show();

	}
	
	private TextToSpeech tts = null;
	private Boolean ttsStatus = new Boolean (false);
	public Boolean getTTSStatus(){ return ttsStatus;}
	public TextToSpeech getTTS(final Context context){
		
		if(tts == null){
			tts = new TextToSpeech (context , new TextToSpeech.OnInitListener(){
				@Override
				public void onInit(int status) {
					
					if (status == TextToSpeech.SUCCESS) {
						tts.setLanguage(Locale.getDefault());
						tts.setSpeechRate((float) 0.8); // 
						tts.setPitch((float) 1.0); 
						ttsStatus = true;
					}
				}
			});
		}
		return tts;
	}
	
	public int getPhoneState(Context context){
		
        //取得TelephonyManager
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getCallState();
//        //將電話狀態的Listener加到取得TelephonyManager
//        telephonyManager.listen(new PhoneStateListener(){
//        	@Override
//            public void onCallStateChanged(int state, String phoneNumber) {
//        		if(state != TelephonyManager.CALL_STATE_IDLE){
//        			
//        		}
//        	}
//        	
//        }, PhoneStateListener.LISTEN_CALL_STATE);
        
	}
	AnimationSet showTop;
	AnimationSet showBottom;
	AnimationSet dismissTop;
	AnimationSet dismissBottom;

	public AnimationSet getShowTop() {
		if (showTop == null) {
			TranslateAnimation trans = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0f, Animation.RELATIVE_TO_SELF, -1f,
					Animation.RELATIVE_TO_SELF, 0f);
			trans.setDuration(300);
			showTop = new AnimationSet(true);
			showTop.addAnimation(trans);
		}
		return showTop;
	}

	public AnimationSet getShowBottom() {
		if (showBottom == null) {
			TranslateAnimation trans = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0f, Animation.RELATIVE_TO_SELF, 1f,
					Animation.RELATIVE_TO_SELF, 0f);
			trans.setDuration(300);
			showBottom = new AnimationSet(true);
			showBottom.addAnimation(trans);
		}
		return showBottom;
	}

	public AnimationSet getDismissTop() {
		if (dismissTop == null) {
			TranslateAnimation trans = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, -1f);
			trans.setDuration(300);
			dismissTop = new AnimationSet(true);
			dismissTop.addAnimation(trans);
		}
		return dismissTop;
	}

	public AnimationSet getDismissBottom() {

		if (dismissBottom == null) {
			TranslateAnimation trans = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 1f);
			trans.setDuration(300);
			dismissBottom = new AnimationSet(true);
			dismissBottom.addAnimation(trans);
		}
		return dismissBottom;
	}

	public AnimationSet getDismissBottom(float dismiss) {

		TranslateAnimation trans = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				dismiss);
		trans.setDuration(300);
		AnimationSet dismissBottom = new AnimationSet(true);
		dismissBottom.addAnimation(trans);

		return dismissBottom;
	}
	float moveY = -1;
	public void setTopBottomShowDismissCtrl(MotionEvent event,Handler showCtrl,Handler dismissCtrl){
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			moveY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d("Test", event.getRawY() + "==" +moveY);
			break;
		case MotionEvent.ACTION_UP:
			if ((event.getRawY() - moveY) > 300 ) {
					dismissCtrl.removeMessages(0);
					showCtrl.sendEmptyMessageDelayed(0, 300);
					dismissCtrl.sendEmptyMessageDelayed(0,
									10 * 1000);
				moveY = -1;
			}else{
				dismissCtrl.sendEmptyMessage(0);
				moveY = -1;
			}
			break;
		}
	}
	public void setDefaultSpinner(Activity activity,ArrayList datas,Spinner spinner){
		
		try{
			{
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						activity,
						android.R.layout.simple_spinner_item, datas);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapter);
			}
		}catch(Exception e){e.printStackTrace();}
		
	}
}
