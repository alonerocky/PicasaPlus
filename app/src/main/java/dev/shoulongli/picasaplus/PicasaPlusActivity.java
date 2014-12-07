package dev.shoulongli.picasaplus;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
 

 
public class PicasaPlusActivity extends TabActivity
{
	public static final String ALBUMS = "ALBUMS";
	public static final String UPLOAD = "UPLOAD"; 
	public static final String SETTING = "SETTING";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost host = this.getTabHost();
		TabHost.TabSpec spec ;
		
		Intent intent ;
		
		intent = new Intent(this,PicasaPlusAlbumsActivity.class);
		spec = host.newTabSpec(ALBUMS).setIndicator("Albums",null);
		spec.setContent(intent);
		host.addTab(spec);
		
			
		intent = new Intent(this,PicasaPlusUploadActivity.class);
		spec = host.newTabSpec(UPLOAD).setIndicator("Upload",null);
		spec.setContent(intent);
		host.addTab(spec);
		
		intent = new Intent(this,PicasaPlusSettings.class);
		spec = host.newTabSpec(SETTING).setIndicator("Settings",null);
		spec.setContent(intent);
		host.addTab(spec);
		 
		 
    }
//    public static void setTabColor(TabHost tabhost) {
//    	for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
//    	{
//    		tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.lightyellow); //unselected
//    	}
//    	tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.color.darkyellow); // selected
//    }
    @Override
    public void onAttachedToWindow() 
    {
    	super.onAttachedToWindow();
    	Window window = getWindow();
    	window.setFormat(PixelFormat.RGBA_8888);
    }
   
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	//FIXME
    	//stopService(new Intent(TripLoggerActivity.this,TrackingService.class));
    }
   
}
