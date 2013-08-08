package de.interoberlin.modelboat.model.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Http
{
	public static void sendPost(final String url, final int x, final int y)
	{
		if (url.equals(""))
		{
			return;
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://" + url);
				System.out.println("URL " + url);

				try
				{
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("x", Integer.toString(x)));
					nameValuePairs.add(new BasicNameValuePair("y", Integer.toString(y)));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					httpclient.execute(httppost);
				} catch (ClientProtocolException e)
				{
					// ModelBoatActivity.uiToast("ClientProtocolException");
				} catch (IOException e)
				{
					// ModelBoatActivity.uiToast("IOException");
				}
			}
		}).start();
	}

	public static void sendGet(final String url, final int x, final int y)
	{
		if (url.equals(""))
		{
			return;
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://" + url + "?x=" + x + "&y=" + y);

				try
				{
					// Execute HTTP Get Request
					httpclient.execute(httpget);
				} catch (ClientProtocolException e)
				{
					// ModelBoatActivity.uiToast("ClientProtocolException");
				} catch (IOException e)
				{
					// ModelBoatActivity.uiToast("IOException");
				}
			}
		}).start();
	}
}
