package dev.shoulongli.picasaplus;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class PicasaPlusAboutActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		WebView web = (WebView)findViewById(R.id.about);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		web.loadUrl("file:///android_asset/about.html");
				
	}
	@Override
    public void onAttachedToWindow() 
    {
    	super.onAttachedToWindow();
    	Window window = getWindow();
    	window.setFormat(PixelFormat.RGBA_8888);
    }
}
