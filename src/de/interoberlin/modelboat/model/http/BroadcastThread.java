package de.interoberlin.modelboat.model.http;

import de.interoberlin.modelboat.controller.activities.ModelBoatActivity;
import de.interoberlin.modelboat.model.Simulation;
import de.interoberlin.modelboat.model.log.Log;
import de.interoberlin.modelboat.model.settings.Settings;
import de.interoberlin.modelboat.view.DrawingPanel;

public class BroadcastThread
{
	private static BroadcastThread	instance;

	private static Thread			thread;

	private BroadcastThread()
	{
		thread = new Thread()
		{
			@Override
			public void run()
			{
				while (!isInterrupted())
				{
					if (Simulation.getX() != Integer.MAX_VALUE && Simulation.getY() != Integer.MAX_VALUE)
					{
						if (Settings.getFrequency() > 0)
						{
							try
							{
								Thread.sleep(1000 / Settings.getFrequency());
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}

						if (Settings.isPost())
						{
							Http.sendPost(Settings.getUrl(), Simulation.getX(), Simulation.getY());
						}

						if (Settings.isGet())
						{
							Http.sendGet(Settings.getUrl(), Simulation.getX(), Simulation.getY());
						}
					}

					DrawingPanel.requestBlink();
				}
			}
		};
	}

	public static BroadcastThread getInstance()
	{
		if (instance == null)
		{
			instance = new BroadcastThread();
		}

		return instance;
	}

	public void start()
	{
		ModelBoatActivity.uiToast("Broadcast started");
		Log.info("Broadcast started");
		thread.start();
	}

	public void stop()
	{
		ModelBoatActivity.uiToast("Broadcast stopped");
		Log.info("Broadcast stopped");
		thread.interrupt();
	}
}
