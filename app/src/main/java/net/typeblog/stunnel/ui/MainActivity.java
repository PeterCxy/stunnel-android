package net.typeblog.stunnel.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import net.typeblog.stunnel.R;
import net.typeblog.stunnel.service.ForegroundService;
import net.typeblog.stunnel.util.Utility;
import static net.typeblog.stunnel.util.Constants.*;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
	public EditText mConfig;
	public Switch mSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Utility.checkAndExtract(this);
		
		mConfig = (EditText) findViewById(R.id.config);
		
		mConfig.setText(Utility.readConfig());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem i = menu.findItem(R.id.switch_action);
		mSwitch = (Switch) i.getActionView().findViewById(R.id.switch_action_button);
		mSwitch.setOnCheckedChangeListener(this);
		reloadState();
		
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (mSwitch != null)
			reloadState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.save_config:
				save();
				return true;
			case R.id.revert_to_default:
				mConfig.setText(DEF_CONFIG);
				return true;
			case R.id.log:
				startActivity(new Intent(this, LogActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean checked) {
		button.setEnabled(false);
		save();
		if (!Utility.isRunning()) {
			Utility.start();
		} else {
			Utility.stop();
		}
		
		mSwitch.postDelayed(new Runnable() {
			@Override
			public void run() {
				reloadState();
			}
		}, 1000);
	}
	
	private void save() {
		Utility.writeConfig(mConfig.getText().toString());
	}
	
	private void reloadState() {
		// Release the listener first
		mSwitch.setOnCheckedChangeListener(null);
		mSwitch.setChecked(Utility.isRunning());
		mSwitch.setOnCheckedChangeListener(this);
		mSwitch.setEnabled(true);
		
		if (Utility.isRunning()) {
			startService(new Intent(this, ForegroundService.class));
		} else {
			stopService(new Intent(this, ForegroundService.class));
		}
	}
}
