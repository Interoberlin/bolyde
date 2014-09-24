package de.interoberlin.bolyde.model.settings;

import de.interoberlin.bolyde.controller.http.EBroadcastMode;

public class Settings
{
    private static boolean	debug	       = true;
    private static EBroadcastMode broadcastMode       = null;
    private static int	    logBuffer	   = 1000;
    private static int	    stabilizationBuffer = 10;
    private static int	    sensitivityX	= 50;
    private static int	    sensitivityY	= 50;
    private static String	 url		 = "192.168.1.1";
    private static int	    frequency	   = 1;
    private static boolean	post		= true;
    private static boolean	get		 = false;
    private static boolean	landscape	   = false;

    public static void toggleDebug()
    {
	if (isDebug())
	{
	    setDebug(false);
	} else
	{
	    setDebug(true);
	}
    }

    public static boolean isDebug()
    {
	return debug;
    }

    public static void setDebug(boolean debug)
    {
	Settings.debug = debug;
    }

    public static EBroadcastMode getBroadcastMode()
    {
	return broadcastMode;
    }

    public static void setBroadcastMode(EBroadcastMode broadcastMode)
    {
	Settings.broadcastMode = broadcastMode;
    }

    public static int getLogBuffer()
    {
	return logBuffer;
    }

    public static void setLogBuffer(int logBuffer)
    {
	Settings.logBuffer = logBuffer;
    }

    public static int getStabilizationBuffer()
    {
	return stabilizationBuffer;
    }

    public static void setStabilizationBuffer(int stabilizationBuffer)
    {
	Settings.stabilizationBuffer = stabilizationBuffer;
    }

    public static int getSensitivityX()
    {
	return sensitivityX;
    }

    public static void setSensitivityX(int sensitivityX)
    {
	Settings.sensitivityX = sensitivityX;
    }

    public static int getSensitivityY()
    {
	return sensitivityY;
    }

    public static void setSensitivityY(int sensitivityY)
    {
	Settings.sensitivityY = sensitivityY;
    }

    public static String getUrl()
    {
	return url;
    }

    public static void setUrl(String url)
    {
	Settings.url = url;
    }

    public static int getFrequency()
    {
	return frequency;
    }

    public static void setFrequency(int frequency)
    {
	Settings.frequency = frequency;
    }

    public static boolean isPost()
    {
	return post;
    }

    public static void setPost(boolean post)
    {
	Settings.post = post;
    }

    public static boolean isGet()
    {
	return get;
    }

    public static void setGet(boolean get)
    {
	Settings.get = get;
    }

    public static boolean isLandscape()
    {
	return landscape;
    }

    public static void setLandscape(boolean landscape)
    {
	Settings.landscape = landscape;
    }

}
