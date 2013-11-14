package de.interoberlin.bolyde.view.panels;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.interoberlin.bolyde.controller.Simulation;
import de.interoberlin.bolyde.model.settings.Properties;
import de.interoberlin.bolyde.model.settings.Settings;

public class DrawingPanel extends SurfaceView implements Runnable
{
    Thread		   thread	= null;
    SurfaceHolder	    surfaceHolder;
    boolean		  running       = false;

    private static final int rgbPercentage = 35;
    private static final int rgbMax	= 255;

    private static boolean   blink	 = false;

    Random		   random;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();
	random = new Random();
    }

    public void onChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
    }

    public void onResume()
    {
	running = true;
	thread = new Thread(this);
	thread.start();
    }

    public void onPause()
    {
	boolean retry = true;
	running = false;

	while (retry)
	{
	    try
	    {
		thread.join();
		retry = false;
	    } catch (InterruptedException e)
	    {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public void run()
    {
	while (running)
	{
	    if (surfaceHolder.getSurface().isValid())
	    {
		Canvas canvas = surfaceHolder.lockCanvas();

		Properties.setCanvasWidth(canvas.getWidth());
		Properties.setCanvasHeight(canvas.getHeight());

		float xValue = Simulation.getRawX();
		float yValue = Simulation.getRawY();

		int red = 0;
		int blue = 0;

		int saturation = rgbMax / 100 * rgbPercentage;

		// Set background color
		if (yValue < 1)
		{
		    red = Math.round(saturation / Settings.getMaxValue() * Math.abs(yValue));
		} else
		{
		    blue = Math.round(saturation / Settings.getMaxValue() * Math.abs(yValue));
		}

		Paint white = new Paint();
		Paint background = new Paint();
		Paint orange = new Paint();

		white.setARGB(255, 255, 255, 255);
		orange.setARGB(255, 238, 118, 0);
		background.setARGB(255, red, 0, blue);

		int w;
		int h;
		int xCenter;
		int yCenter;

		if (Settings.isLandscape())
		{
		    w = canvas.getHeight();
		    h = canvas.getWidth();
		    xCenter = h / 2;
		    yCenter = w / 2;

		} else
		{
		    w = canvas.getWidth();
		    h = canvas.getHeight();
		    xCenter = w / 2;
		    yCenter = h / 2;
		}

		int circleCount = Settings.getCircleCount();
		int pointRadius = Properties.getMinDimension() / 36;
		float lineWidth = Properties.getCanvasWidth() / 90;

		int maxRadius = Properties.getMinDimension() / 2;
		int minRadius = maxRadius / circleCount;

		// Clear
		if (Settings.isLandscape())
		{
		    canvas.drawRect(0, 0, h, w, background);
		} else
		{
		    canvas.drawRect(0, 0, w, h, background);

		}

		// Draw circles
		for (int i = circleCount; i > 0; i--)
		{
		    if (i == 1 && blink)
		    {
			blink = false;
			background.setARGB(255, 255, 255, 255);
		    }

		    float radius = (1.0F / circleCount) * i * maxRadius;
		    canvas.drawCircle(xCenter, yCenter, radius, white);
		    canvas.drawCircle(xCenter, yCenter, radius - lineWidth, background);
		}

		// Draw lines (left, right, top, bottom)
		if (Settings.isLandscape())
		{
		    canvas.drawLine(0 + maxRadius, yCenter, xCenter - minRadius, yCenter, white);
		    canvas.drawLine(h - maxRadius, yCenter, xCenter + minRadius, yCenter, white);
		    canvas.drawLine(xCenter, yCenter - maxRadius, xCenter, yCenter - minRadius, white);
		    canvas.drawLine(xCenter, yCenter + maxRadius, xCenter, yCenter + minRadius, white);
		} else
		{
		    canvas.drawLine(0, yCenter, xCenter - minRadius, yCenter, white);
		    canvas.drawLine(w, yCenter, xCenter + minRadius, yCenter, white);
		    canvas.drawLine(xCenter, yCenter - maxRadius, xCenter, yCenter - minRadius, white);
		    canvas.drawLine(xCenter, yCenter + maxRadius, xCenter, yCenter + minRadius, white);
		}

		// Draw Point
		float xPoint;
		float yPoint;

		if (Settings.isLandscape())
		{
		    xPoint = xCenter + (yCenter * xValue / -Math.abs(Settings.getMaxValue()));
		    yPoint = yCenter + (yCenter * yValue / -Math.abs(Settings.getMaxValue()));
		} else
		{
		    xPoint = xCenter + (xCenter * xValue / -Math.abs(Settings.getMaxValue()));
		    yPoint = yCenter + (xCenter * yValue / -Math.abs(Settings.getMaxValue()));
		}

		float rPoint = pointRadius;

		canvas.drawCircle(xPoint, yPoint, rPoint, orange);

		surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	}
    }

    public static void requestBlink()
    {
	blink = true;
    }
}