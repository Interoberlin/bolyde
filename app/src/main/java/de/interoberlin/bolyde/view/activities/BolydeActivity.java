package de.interoberlin.bolyde.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.interoberlin.bolyde.R;
import de.interoberlin.bolyde.controller.BolydeController;
import de.interoberlin.bolyde.controller.Simulation;
import de.interoberlin.bolyde.controller.log.Log;
import de.interoberlin.bolyde.model.settings.Settings;
import de.interoberlin.bolyde.view.DebugLine;
import de.interoberlin.bolyde.view.panels.DrawingPanel;
import de.interoberlin.mate.lib.view.LogActivity;
import de.interoberlin.mate.lib.view.SupportActivity;

public class BolydeActivity extends Activity
{
    private static Context       context;
    private static Activity      activity;
    private static BolydeController controller;

    private static SensorManager mSensorManager;
    private WindowManager	mWindowManager;
    private static Display       mDisplay;

    private static DrawingPanel  srfc;

    private static LinearLayout  lnr;
    private static DebugLine     dlOffset;
    private static DebugLine     dlData;
    private static DebugLine     dlRaw;
    private static DebugLine     dlValues;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_model_boat);

	// Get activity and context
	activity = this;
	context = getApplicationContext();
	controller = (BolydeController) getApplicationContext();

	// Get an instance of the SensorManager
	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

	// Get an instance of the WindowManager
	mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	mDisplay = mWindowManager.getDefaultDisplay();

	// Add surface view
	srfc = new DrawingPanel(activity);
	activity.addContentView(srfc, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	// Add linear layout
	lnr = new LinearLayout(activity);
	activity.addContentView(lnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

	// Start the simulation
	Simulation.getInstance(activity).start();
    }

    public void onResume()
    {
	super.onResume();
	srfc.onResume();

	draw();

	srfc.setOnTouchListener(new OnTouchListener()
	{
	    @Override
	    public boolean onTouch(View v, MotionEvent event)
	    {
		float x = event.getX();
		float y = event.getY();

		float deltaX = Math.abs(controller.getCanvasWidth() / 2 - x);
		float deltaY = Math.abs(controller.getCanvasHeight() / 2 - y);

		float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		// Check if clicked in inner circle
		if (distance < controller.getMinDimension() / 2 / controller.getCircleCount())
		{
		    setOffset(-Simulation.getDataX(), -Simulation.getDataY());
		}

		return true;
	    }

	    private void setOffset(float x, float y)
	    {
		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

		controller.setOffsetX(-x);
		controller.setOffsetY(-y);

		uiToast("Set offset " + Simulation.getRawX() + "/" + Simulation.getRawY());
		Log.info("Set offset " + x + " / " + y);
	    }
	});
    }

    @Override
    protected void onPause()
    {
	super.onPause();
	srfc.onPause();
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
	Simulation.getInstance(activity).stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.activity_model_boat, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	    case R.id.menu_debug:
	    {
		if (Settings.isDebug())
		{
		    uiToast("Debug disabled");
		    Settings.setDebug(false);
		} else
		{
		    Settings.setDebug(true);
		    uiToast("Debug enabled");
		}
		break;
	    }
	    case R.id.menu_log:
	    {
		Intent i = new Intent(BolydeActivity.this, LogActivity.class);
		startActivity(i);
		break;
	    }
	    case R.id.menu_settings:
	    {
		Intent i = new Intent(BolydeActivity.this, SettingsActivity.class);
		startActivity(i);
		break;
	    }
	    case R.id.menu_support:
	    {
		Intent i = new Intent(BolydeActivity.this, SupportActivity.class);
		startActivity(i);
		break;
	    }
	    default:
	    {
		return super.onOptionsItemSelected(item);

	    }
	}

	return true;
    }

    public static void draw()
    {
	activity.setTitle("Bolyde");

	if (lnr != null)
	{
		lnr.removeAllViews();

		// Add debug lines
		dlOffset = new DebugLine(activity, "Offset", String.valueOf(controller.getOffsetX()), String.valueOf(controller.getOffsetY()));
		dlData = new DebugLine(activity, "Data", String.valueOf(Simulation.getDataX()), String.valueOf(Simulation.getDataY()));
		dlRaw = new DebugLine(activity, "Raw", String.valueOf(Simulation.getRawX()), String.valueOf(Simulation.getRawY()));
		dlValues = new DebugLine(activity, "Values", String.valueOf(Simulation.getX()), String.valueOf(Simulation.getY()));

		lnr.setOrientation(LinearLayout.VERTICAL);
		lnr.addView(dlOffset, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lnr.addView(dlData, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lnr.addView(dlRaw, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lnr.addView(dlValues, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
    }

    public SensorManager getSensorManager()
    {
	return mSensorManager;
    }

    public Display getDisplay()
    {
	return mDisplay;
    }

    public static void uiToast(final String message)
    {
	if (Settings.isDebug())
	{
	    activity.runOnUiThread(new Runnable()
	    {
		@Override
		public void run()
		{
		    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	    });
	}
    }

    public static void uiDraw()
    {
	activity.runOnUiThread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		draw();
	    }
	});
    }
}