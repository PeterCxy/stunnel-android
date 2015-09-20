package net.typeblog.stunnel.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.typeblog.stunnel.service.ForegroundService;
import net.typeblog.stunnel.util.Utility;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Utility.isRunning()) {
			// Keep the running status
			Utility.stop();
			Utility.start();
			
			// Wait for some time
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
			
			if (Utility.isRunning())
				context.startService(new Intent(context, ForegroundService.class));
		}
	}
}
