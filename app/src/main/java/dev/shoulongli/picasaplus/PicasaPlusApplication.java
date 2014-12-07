package dev.shoulongli.picasaplus;

import android.app.Application;

import dev.shoulongli.appframework.network.VolleyWrapper;

public class PicasaPlusApplication extends Application
{
	private static PicasaPlusApplication instance;
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
	}
	public static PicasaPlusApplication getInstance()
	{
		return instance;
	}
	
}
