package de.interoberlin.bolyde.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import de.interoberlin.bolyde.R;
import de.interoberlin.bolyde.controller.log.ELog;
import de.interoberlin.bolyde.controller.log.Log;
import de.interoberlin.bolyde.controller.log.LogEntry;
import de.interoberlin.bolyde.model.settings.Settings;

public class LogActivity extends Activity
{
	private static Context		context;
	private static Activity		activity;

	private static TableLayout	tblLog;
	private static ScrollView	scrl;
	private static CheckBox		cbAutoRefresh;
	private static Spinner		spnnrLogLevel;

	private static ELog			threshold	= ELog.TRACE;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// Get activity and context
		activity = this;
		context = getApplicationContext();

		// Get views by id
		tblLog = (TableLayout) findViewById(R.id.tbl);
		scrl = (ScrollView) findViewById(R.id.scrl);
		cbAutoRefresh = (CheckBox) findViewById(R.id.cbAutoRefresh);
		spnnrLogLevel = (Spinner) findViewById(R.id.spnnrLogLevel);
	}

	public void onResume()
	{
		super.onResume();
		draw();

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.loglevel,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spnnrLogLevel.setAdapter(adapter);

		new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					if (cbAutoRefresh.isChecked())
					{
						try
						{
							sleep(250);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						LogActivity.uiDraw();
					}
				}
			}
		}.start();

		spnnrLogLevel.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				String selected = parent.getItemAtPosition(pos).toString();

				if (selected.equals(ELog.TRACE.toString()))
				{
					threshold = ELog.TRACE;
				} else if (selected.equals(ELog.DEBUG.toString()))
				{
					threshold = ELog.DEBUG;
				} else if (selected.equals(ELog.INFO.toString()))
				{
					threshold = ELog.INFO;
				} else if (selected.equals(ELog.WARN.toString()))
				{
					threshold = ELog.WARN;
				} else if (selected.equals(ELog.ERROR.toString()))
				{
					threshold = ELog.ERROR;
				} else if (selected.equals(ELog.FATAL.toString()))
				{
					threshold = ELog.FATAL;
				}

				uiDraw();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				threshold = ELog.TRACE;
			}
		});
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
			{
				finish();
				break;
			}
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
			case R.id.menu_orientation:
			{
				switchOrientation();
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}

		return true;
	}

	private void switchOrientation()
	{
		if (Settings.isLandscape())
		{
			uiToast("Orientation portrait");
			Settings.setLandscape(false);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else
		{
			uiToast("Orientation landscape");
			Settings.setLandscape(true);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

	}

	public static void draw()
	{
		activity.setTitle("Log");

		tblLog.removeAllViews();

		for (LogEntry l : Log.getAll())
		{
			if (l.getLogLevel().ordinal() >= threshold.ordinal())
			{
				TableRow tr = new TableRow(activity);
				TextView tvTimestamp = new TextView(activity);
				TextView tvLogLevel = new TextView(activity);
				TextView tvMessage = new TextView(activity);

				tvTimestamp.setText(l.getTimeStamp());
				tvLogLevel.setText(l.getLogLevel().toString());
				tvMessage.setText(l.getMessage());

				switch (l.getLogLevel())
				{
					case TRACE:
					{
						tvTimestamp.setTextColor(activity.getResources().getColor(R.color.green));
						tvLogLevel.setTextColor(activity.getResources().getColor(R.color.green));
						tvMessage.setTextColor(activity.getResources().getColor(R.color.green));
						break;
					}
					case DEBUG:
					{
						tvTimestamp.setTextColor(activity.getResources().getColor(R.color.blue));
						tvLogLevel.setTextColor(activity.getResources().getColor(R.color.blue));
						tvMessage.setTextColor(activity.getResources().getColor(R.color.blue));
						break;
					}
					case INFO:
					{
						tvTimestamp.setTextColor(activity.getResources().getColor(R.color.white));
						tvLogLevel.setTextColor(activity.getResources().getColor(R.color.white));
						tvMessage.setTextColor(activity.getResources().getColor(R.color.white));
						break;
					}
					case WARN:
					{
						tvTimestamp.setTextColor(activity.getResources().getColor(R.color.yellow));
						tvLogLevel.setTextColor(activity.getResources().getColor(R.color.yellow));
						tvMessage.setTextColor(activity.getResources().getColor(R.color.yellow));
						break;
					}
					case ERROR:
					{
						tvTimestamp.setTextColor(activity.getResources().getColor(R.color.red));
						tvLogLevel.setTextColor(activity.getResources().getColor(R.color.red));
						tvMessage.setTextColor(activity.getResources().getColor(R.color.red));
						break;
					}
					case FATAL:
					{
						tvTimestamp.setTextColor(activity.getResources().getColor(R.color.red));
						tvLogLevel.setTextColor(activity.getResources().getColor(R.color.red));
						tvMessage.setTextColor(activity.getResources().getColor(R.color.red));
						break;
					}
				}

				tvTimestamp.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.3f));
				tvLogLevel.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.2f));
				tvMessage.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.5f));

				tr.addView(tvTimestamp, 0);
				tr.addView(tvLogLevel, 1);
				tr.addView(tvMessage, 2);

				tblLog.addView(tr);
			}
		}

		scrl.post(new Runnable()
		{
			@Override
			public void run()
			{
				scrl.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
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