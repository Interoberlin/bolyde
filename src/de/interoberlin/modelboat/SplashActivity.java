/**
 * 
 */
package de.interoberlin.modelboat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.interoberlin.modelboat.controller.activities.ModelBoatActivity;

public class SplashActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Thread timer = new Thread()
		{
			public void run()
			{
				try
				{
					sleep(1500);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				} finally
				{
					Intent openStartingPoint = new Intent(SplashActivity.this, ModelBoatActivity.class);
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
