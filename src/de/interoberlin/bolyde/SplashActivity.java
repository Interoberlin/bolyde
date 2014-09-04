package de.interoberlin.bolyde;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import de.interoberlin.bolyde.controller.BolydeController;
import de.interoberlin.bolyde.view.activities.BolydeActivity;
import de.interoberlin.sauvignon.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.model.svg.SVG;
import de.interoberlin.sauvignon.model.svg.elements.AGeometric;
import de.interoberlin.sauvignon.model.svg.elements.path.SVGPath;
import de.interoberlin.sauvignon.model.svg.transform.transform.SVGTransformTranslate;
import de.interoberlin.sauvignon.model.util.SVGPaint;
import de.interoberlin.sauvignon.view.SVGPanel;

public class SplashActivity extends Activity
{
    private static Context	  context;
    private static Activity	 activity;
    private static BolydeController controller;

    // private static SensorManager sensorManager;
    // private WindowManager windowManager;
    // private static Display display;

    private static SVG	      svg;
    private static SVGPanel	 panel;
    private static ImageView	ivLogo;

    private static long	     millisStart;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_splash);

	// Get activity and context
	activity = this;
	context = getApplicationContext();
	controller = (BolydeController) getApplicationContext();

	// Get instances of managers
	// sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	// windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	// display = windowManager.getDefaultDisplay();

	svg = SvgLoader.getSVGFromAsset(context, "bolyde.svg");

	panel = new SVGPanel(activity);
	panel.setSVG(svg);
	panel.setBackgroundColor(new SVGPaint(255, 255, 255, 255));

	ivLogo = new ImageView(activity);
	ivLogo.setImageDrawable(loadFromAssets("lymbo.png"));

	// Add surface view
	activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	activity.addContentView(ivLogo, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	panel.setOnTouchListener(new OnTouchListener()
	{
	    @Override
	    public boolean onTouch(View v, MotionEvent event)
	    {
		startActivity(new Intent(SplashActivity.this, BolydeActivity.class));

		return true;
	    }
	});

	// Initialize
	uiInit();
    }

    public void onResume()
    {
	super.onResume();
	panel.resume();
	uiUpdate();
	draw();

	// Simulation.getInstance(activity).start();
    }

    @Override
    protected void onPause()
    {
	super.onPause();
	panel.pause();

	// Simulation.getInstance(activity).stop();
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();

    }

    // public Display getDisplay()
    // {
    // return display;
    // }

    // public SensorManager getSensorManager()
    // {
    // return sensorManager;
    // }

    public static void draw()
    {
    }

    private Drawable loadFromAssets(String image)
    {
	try
	{
	    InputStream is = getAssets().open("bolyde.png");
	    return Drawable.createFromStream(is, null);
	} catch (IOException ex)
	{
	    return null;
	}
    }

    public static void uiInit()
    {
	synchronized (svg)
	{
	}
    }

    public static void uiUpdate()
    {
	Thread t = new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		millisStart = System.currentTimeMillis();

		long millisBefore = 0;
		long millisAfter = 0;
		long millisFrame = (long) (1000 / panel.getFpsDesired());

		boolean running = true;

		while (running)
		{
		    millisBefore = System.currentTimeMillis();

		    synchronized (svg)
		    {
			for (AGeometric e : svg.getAllSubElements())
			{
			    if (e instanceof SVGPath)
			    {
				long seconds = System.currentTimeMillis() / 1000;

				int Z_INDEX_WEIGHT = 10;
				int AMPLITUTDE = 5;
				
				float x = 0;
//				float y = ((seconds + e.getzIndex()) % svg.getMaxZindex()) * 5;
				float y = (float) Math.sin((seconds  + (e.getzIndex() * Z_INDEX_WEIGHT)) % 360) * AMPLITUTDE;

				e.setAnimationTransform(new SVGTransformTranslate(x, y));
			    }
			}

			millisAfter = System.currentTimeMillis();
		    }

		    // P A U S E

		    if (millisAfter - millisBefore < millisFrame)
		    {
			try
			{
			    Thread.sleep(millisFrame - (millisAfter - millisBefore));
			} catch (InterruptedException e)
			{
			    e.printStackTrace();
			}
		    }
		    millisAfter = System.currentTimeMillis();

		}
	    }
	});

	t.start();
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

    public static void uiToast(final String message)
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