package dev.shoulongli.picasaplus.auth.oauth;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.http.HttpResponseException;

public class PrepareRequestTokenActivity extends Activity
{
	private static final String TAG = PrepareRequestTokenActivity.class.getName();
	public static final String KEY_TOKEN_EXPIRED = "KEY_TOKEN_EXPIRED";
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Bundle extras = this.getIntent().getExtras();
		boolean tokenexpired = false;
		if(extras != null)
		{
			tokenexpired = extras.getBoolean(KEY_TOKEN_EXPIRED);
		}
		auth(tokenexpired);
	}
	private void auth(boolean tokenExpired) 
	{
		try 
		{
			boolean isViewAction = Intent.ACTION_VIEW.equals(getIntent().getAction());
			if (tokenExpired && !PicasaAuthenticatorOAuth.isTemporary && PicasaAuthenticatorOAuth.credentials != null) 
			{
				PicasaAuthenticatorOAuth.getInstance().revokeAccessToken();
			}
			if (tokenExpired || !isViewAction
					&& (PicasaAuthenticatorOAuth.isTemporary || PicasaAuthenticatorOAuth.credentials == null)) {

				GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl = PicasaAuthenticatorOAuth.getInstance().retrieveTemporaryToken(Constants.OAUTH_CALLBACK_URL);
				Intent webIntent = new Intent(Intent.ACTION_VIEW);
				webIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
				webIntent.setData(Uri.parse(authorizeUrl.build()));
				startActivity(webIntent);
			} 
			else 
			{
				if (isViewAction) 
				{
					Uri uri = this.getIntent().getData();
					PicasaAuthenticatorOAuth.getInstance().retrieveAccessToken(uri);
				}
				authenticated();
			}
		} 
		catch (IOException e) 
		{
			handleException(e);
		}
		return;

	}
	
	public void authenticated()
	{
		
	}
	private void handleException(Exception e) 
	{
		e.printStackTrace();
		if (e instanceof HttpResponseException) {
			int statusCode = ((HttpResponseException) e).response.statusCode;
			if (statusCode == 401 || statusCode == 403) 
			{
				auth(true);
			}
			return;
		}
		
		
	}

	

	
	
	
}
