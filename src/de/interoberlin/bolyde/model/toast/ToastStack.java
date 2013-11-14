package de.interoberlin.bolyde.model.toast;

import java.util.ArrayList;
import java.util.List;

public class ToastStack
{
	private static List<ToastEntry>	toastStack	= new ArrayList<ToastEntry>();

	public ToastStack()
	{
		
	}
	
	public static void add(ToastEntry toastEntry)
	{
		toastStack.add(toastEntry);
	}
	
	public static void add(String message, int duration)
	{
		toastStack.add(new ToastEntry(message, duration));
	}
}
