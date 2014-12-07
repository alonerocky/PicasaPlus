package dev.shoulongli.picasaplus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;



public class PicasaPlusSettings extends PreferenceActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencescreen);
		addPreferencesFromResource(R.xml.preferences);
	}
	@Override
    public void onAttachedToWindow() 
    {
    	super.onAttachedToWindow();
    	Window window = getWindow();
    	window.setFormat(PixelFormat.RGBA_8888);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.albumsoptions, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.about:
    		showAbout();
    		break;
    	}
    	return true;
    }
    
    private void showAbout()
    {
    	Intent intent = new Intent(PicasaPlusSettings.this, PicasaPlusAboutActivity.class);
    	startActivity(intent);
    }
}
