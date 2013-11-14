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
import android.widget.TextView;
import android.widget.Toast;
import de.interoberlin.bolyde.R;
import de.interoberlin.bolyde.controller.Simulation;
import de.interoberlin.bolyde.controller.log.Log;
import de.interoberlin.bolyde.model.settings.Properties;
import de.interoberlin.bolyde.model.settings.Settings;
import de.interoberlin.bolyde.view.panels.DrawingPanel;

public class BolydeActivity extends Activity
{
    private static Context       context;
    private static Activity      activity;

    private static SensorManager mSensorManager;
    private WindowManager	mWindowManager;
    private static Display       mDisplay;

    private static DrawingPanel  srfc;

    private static LinearLayout  lnr;

    private static LinearLayout  oneLnr;
    private static TextView      oneTvFirst;
    private static TextView      oneTvSecond;
    private static TextView      oneTvThird;

    private static LinearLayout  twoLnr;
    private static TextView      twoTvFirst;
    private static TextView      twoTvSecond;
    private static TextView      twoTvThird;

    private static LinearLayout  threeLnr;
    private static TextView      threeTvFirst;
    private static TextView      threeTvSecond;
    private static TextView      threeTvThird;

    private static LinearLayout  fourLnr;
    private static TextView      fourTvFirst;
    private static TextView      fourTvSecond;
    private static TextView      fourTvThird;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_model_boat);

	// Get activity and context
	activity = this;
	context = getApplicationContext();

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

		float deltaX = Math.abs(Properties.getCanvasWidth() / 2 - x);
		float deltaY = Math.abs(Properties.getCanvasHeight() / 2 - y);

		float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		// Check if clicked in inner circle
		if (distance < Properties.getMinDimension() / 2 / Settings.getCircleCount())
		{
		    setOffset(-Simulation.getDataX(), -Simulation.getDataY());
		}

		return true;
	    }

	    private void setOffset(float x, float y)
	    {
		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

		Settings.setOffsetX(-x);
		Settings.setOffsetY(-y);

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

	// Stop the simulartion
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

	    // Add text views
	    oneLnr = new LinearLayout(activity);
	    oneTvFirst = new TextView(activity);
	    oneTvSecond = new TextView(activity);
	    oneTvThird = new TextView(activity);
	    oneTvFirst.setText("Data");
	    oneTvSecond.setText(String.valueOf(Simulation.getDataX()));
	    oneTvThird.setText(String.valueOf(Simulation.getDataY()));
	    oneLnr.addView(oneTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    oneLnr.addView(oneTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    oneLnr.addView(oneTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

	    // Add text views
	    twoLnr = new LinearLayout(activity);
	    twoTvFirst = new TextView(activity);
	    twoTvSecond = new TextView(activity);
	    twoTvThird = new TextView(activity);
	    twoTvFirst.setText("Offset");
	    twoTvSecond.setText(String.valueOf(Settings.getOffsetX()));
	    twoTvThird.setText(String.valueOf(Settings.getOffsetY()));
	    twoLnr.addView(twoTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    twoLnr.addView(twoTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    twoLnr.addView(twoTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

	    // Add text views
	    threeLnr = new LinearLayout(activity);
	    threeTvFirst = new TextView(activity);
	    threeTvSecond = new TextView(activity);
	    threeTvThird = new TextView(activity);
	    threeTvFirst.setText("Raw");
	    threeTvSecond.setText(String.valueOf(Simulation.getRawX()));
	    threeTvThird.setText(String.valueOf(Simulation.getRawY()));
	    threeLnr.addView(threeTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    threeLnr.addView(threeTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    threeLnr.addView(threeTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

	    // Add text views
	    fourLnr = new LinearLayout(activity);
	    fourTvFirst = new TextView(activity);
	    fourTvSecond = new TextView(activity);
	    fourTvThird = new TextView(activity);
	    fourTvFirst.setText("Values");
	    fourTvSecond.setText(String.valueOf(Simulation.getX()));
	    fourTvThird.setText(String.valueOf(Simulation.getY()));
	    fourLnr.addView(fourTvFirst, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    fourLnr.addView(fourTvSecond, new LayoutParams(200, LayoutParams.WRAP_CONTENT));
	    fourLnr.addView(fourTvThird, new LayoutParams(200, LayoutParams.WRAP_CONTENT));

	    lnr.setOrientation(1);
	    lnr.addView(oneLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    lnr.addView(twoLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    lnr.addView(threeLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    lnr.addView(fourLnr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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