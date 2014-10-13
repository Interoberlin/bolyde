package de.interoberlin.bolyde.view.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import de.interoberlin.bolyde.controller.BolydeController;
import de.interoberlin.bolyde.controller.Simulation;
import de.interoberlin.bolyde.model.settings.Settings;

public class DrawingPanel extends SurfaceView implements Runnable
{
    private static BolydeController controller;

    Thread			  thread	= null;
    SurfaceHolder		   surfaceHolder;
    boolean			 running       = false;

    private static final int	rgbPercentage = 35;
    private static final int	rgbMax	= 255;

    private static boolean	  blink	 = false;

    Random			  random;

    public DrawingPanel(Context context)
    {
	super(context);
	surfaceHolder = getHolder();
	random = new Random();

	controller = (BolydeController) context.getApplicationContext();
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

		controller.setCanvasWidth(canvas.getWidth());
		controller.setCanvasHeight(canvas.getHeight());

		float xValue = Simulation.getRawX();
		float yValue = Simulation.getRawY();

		int red = 255;
        int green = 255;
        int blue = 255;

		int saturation = rgbMax / 100 * rgbPercentage;

		// Set background color
		if (yValue < 1)
		{
            blue = green=  255 - Math.round(saturation / controller.getMAX_VALUE() * Math.abs(yValue));
		} else
		{
		    red = green = 255 -Math.round(saturation / controller.getMAX_VALUE() * Math.abs(yValue));
		}

        Paint white = new Paint();
        Paint black = new Paint();
		Paint background = new Paint();
		Paint orange = new Paint();

        white.setARGB(255, 255, 255, 255);
        black.setARGB(255, 0, 0, 0);
		orange.setARGB(255, 238, 118, 0);
		background.setARGB(255, red, green, blue);

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

		int circleCount = controller.getCircleCount();
		int pointRadius = controller.getMinDimension() / 36;
		float lineWidth = controller.getMinDimension() / 126;

		int maxRadius = controller.getMinDimension() / 2;
		int minRadius = maxRadius / circleCount;

		// Clear
		canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), white);

		// Draw circles
		for (int i = circleCount; i > 0; i--)
		{
		    if (i == 1 && blink)
		    {
			blink = false;
			background.setARGB(255, 255, 255, 255);
		    }

		    float radius = (1.0F / circleCount) * i * maxRadius;
		    canvas.drawCircle(xCenter, yCenter, radius, black);
		    canvas.drawCircle(xCenter, yCenter, radius - lineWidth, background);
		}

		// Draw lines (left, right, top, bottom)
		if (Settings.isLandscape())
		{
		    canvas.drawLine(0 + maxRadius, yCenter, xCenter - minRadius, yCenter, black);
		    canvas.drawLine(h - maxRadius, yCenter, xCenter + minRadius, yCenter, black);
		    canvas.drawLine(xCenter, yCenter - maxRadius, xCenter, yCenter - minRadius, black);
		    canvas.drawLine(xCenter, yCenter + maxRadius, xCenter, yCenter + minRadius, black);
		} else
		{
		    canvas.drawLine(0, yCenter, xCenter - minRadius, yCenter, black);
		    canvas.drawLine(w, yCenter, xCenter + minRadius, yCenter, black);
		    canvas.drawLine(xCenter, yCenter - maxRadius, xCenter, yCenter - minRadius, black);
		    canvas.drawLine(xCenter, yCenter + maxRadius, xCenter, yCenter + minRadius, black);
		}

		// Draw Point
		float xPoint;
		float yPoint;

		if (Settings.isLandscape())
		{
		    xPoint = xCenter + (yCenter * xValue / -Math.abs(controller.getMAX_VALUE()));
		    yPoint = yCenter + (yCenter * yValue / -Math.abs(controller.getMAX_VALUE()));
		} else
		{
		    xPoint = xCenter + (xCenter * xValue / -Math.abs(controller.getMAX_VALUE()));
		    yPoint = yCenter + (xCenter * yValue / -Math.abs(controller.getMAX_VALUE()));
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