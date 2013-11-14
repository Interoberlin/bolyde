package de.interoberlin.bolyde.model.toast;

public class ToastEntry
{
	private String message;
	private int duration;
	
	public ToastEntry(String message, int duration)
	{
		this.message = message;
		this.duration = duration;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}
}
