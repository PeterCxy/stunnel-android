package net.typeblog.stunnel.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.typeblog.stunnel.R;

public class ForegroundService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Only a stub to protect the process
		Notification notification = new Notification.Builder(this)
			.setPriority(Notification.PRIORITY_MIN)
			.setSmallIcon(android.R.color.transparent)
			.setContentTitle(getString(R.string.running))
			.setContentText(getString(R.string.desc))
			.build();
		startForeground(R.string.app_name, notification);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
	}
	
	@Override
	public IBinder onBind(Intent p1) {
		// Not bindable
		return null;
	}
}
