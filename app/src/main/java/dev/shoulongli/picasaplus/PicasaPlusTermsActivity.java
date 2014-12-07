package dev.shoulongli.picasaplus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

import dev.shoulongli.appframework.oauth.google.GoogleLoginActivity;

public class PicasaPlusTermsActivity extends Activity
{
	private static final String KEY_ACCEPTED = "TermsAndConditionAccepted";
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(isAccepted()) //alreay showed the Terms and Conditions, also user Accepted this then continue, otherwise finish
		{
			Intent intent = new Intent(this, GoogleLoginActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{
			setContentView(R.layout.termscondition);
			WebView web = (WebView)findViewById(R.id.terms);
			web.loadUrl("file:///android_asset/tandc.html");
			web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			
			Button accept = (Button)findViewById(R.id.accept);
			Button decline = (Button)findViewById(R.id.decline);
			
			accept.setOnClickListener(new View.OnClickListener() 
			{
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setAccepted(true);
					Intent intent = new Intent(PicasaPlusTermsActivity.this, GoogleLoginActivity.class);
					startActivity(intent);
					finish();
					
				}
			});
			decline.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setAccepted(false);
					finish();
				}
			});
		}
		
	}
	private boolean isAccepted()
	{
		boolean accepted = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean(KEY_ACCEPTED, false);
		
		return accepted;
	}
	public void setAccepted(boolean accepted)
	{
		getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean(KEY_ACCEPTED, accepted).commit();
	}
	@Override
    public void onAttachedToWindow() 
    {
    	super.onAttachedToWindow();
    	Window window = getWindow();
    	window.setFormat(PixelFormat.RGBA_8888);
    }
}
