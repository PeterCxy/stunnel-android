package net.typeblog.stunnel.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static net.typeblog.stunnel.BuildConfig.DEBUG;
import static net.typeblog.stunnel.util.Constants.*;

public class Utility
{
	private static final String TAG = Utility.class.getSimpleName();
	
	public static void checkAndExtract(Context context) {
		if (new File(HOME + EXECTUABLE).exists()) {
			return;
		}
		
		new File(HOME).mkdir();
		
		// Extract stunnel exectuable
		AssetManager am = context.getAssets();
		try {
			InputStream in = am.open(EXECTUABLE);
			OutputStream out = new FileOutputStream(HOME + EXECTUABLE);
			
			byte[] buf = new byte[512];
			int len;
			
			while ((len = in.read(buf)) > -1) {
				out.write(buf, 0, len);
			}
			
			in.close();
			out.flush();
			out.close();
			
			Runtime.getRuntime().exec("chmod 777 " + HOME + EXECTUABLE);
		} catch (Exception e) {
			
		}
	}
	
	public static boolean isRunning() {
		return new File(HOME + PID).exists();
	}
	
	public static void start() {
		if (!isRunning()) {
			try {
				Runtime.getRuntime().exec(HOME + EXECTUABLE + " " + HOME + CONFIG).waitFor();
			} catch (Exception e) {
				
			}
		}
	}
	
	public static void stop() {
		if (isRunning()) {
			String pid = "";
			
			try {
				byte[] buf = readInputStream(new FileInputStream(HOME + PID));
				if (buf != null) {
					pid = new String(buf, "UTF-8");
				}
			} catch (Exception e) {
				
			}
			
			if (DEBUG) {
				Log.d(TAG, "pid = " + pid);
			}
			
			if (!pid.trim().equals("")) {
				try {
					Runtime.getRuntime().exec("kill " + pid).waitFor();
				} catch (Exception e) {
					
				}
				
				if (isRunning()) {
					new File(HOME + PID).delete();
				}
			}
		}
	}
	
	public static String readConfig() {
		File f = new File(HOME + CONFIG);
		
		if (!f.exists()) {
			return DEF_CONFIG;
		} else {
			try {
				byte[] buf = readInputStream(new FileInputStream(f));
				if (buf != null) {
					return new String(buf, "UTF-8");
				} else {
					return null;
				}
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public static void writeConfig(String config) {
		File f = new File(HOME + CONFIG);
		
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (Exception e) {
				
			}
		}
		
		try {
			byte[] buf = config.getBytes("UTF-8");
			OutputStream out = new FileOutputStream(f);
			out.write(buf);
			out.flush();
			out.close();
		} catch (Exception e) {
			
		}
	}
	
	public static byte[] readInputStream(InputStream in) {
		byte[] buf = new byte[512];
		int len;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			while ((len = in.read(buf)) > -1) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			return null;
		}
		
		return out.toByteArray();
	}
}
