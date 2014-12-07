package dev.shoulongli.picasaplus.auth.oauth;

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void>
{
	final String TAG = getClass().getName();
	//http://blog.doityourselfandroid.com/2011/04/12/oauth-android-google-apis-client-library-java/
	private Context context;
	public OAuthRequestTokenTask( )
	{
	}
	@Override
	public Void doInBackground(Void... params )
	{
		try
		{
			Log.i(TAG, "Retrieving request token from Google servers");
			
//			GoogleOAuthGetTemporaryToken temporaryToken = new GoogleOAuthGetTemporaryToken();
//			temporaryToken.signer = PicasaAuthenticatorOAuth.getInstance().getSigner();
//			temporaryToken.consumerKey = Constants.CONSUMER_KEY;
//			temporaryToken.scope = Constants.SCOPE;
//			temporaryToken.displayName = Constants.DISPLAY_NAME;
//			temporaryToken.callback = Constants.OAUTH_CALLBACK_URL;
//
//			/*
//			 * By calling the execute() method, Google verifies that the requesting application has been registered with Google or 
//			 * is using an approved signature (in the case of installed applications). The temporary token acquired with this request is 
//			 * found in OAuthCredentialsResponse This temporary token is used in GoogleOAuthAuthorizeTemporaryTokenUrl to direct the end
//			 * user to a Google Accounts web page to allow the end user to authorize the temporary token (see next step  Authorize Token).
//
//			   The exeucte() method returns a com.google.api.client.auth.oauth.OAuthCredentialsResponse, containing our request token, 
//			   and token secret.
//			 * 
//			 */
//			OAuthCredentialsResponse temporaryCredential = temporaryToken.execute();
//			PicasaAuthenticatorOAuth.getInstance().getSigner().tokenSharedSecret = temporaryCredential.tokenSecret;
//
//			GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl = new GoogleOAuthAuthorizeTemporaryTokenUrl();
//			authorizeUrl.temporaryToken = temporaryCredential.token;
//			authorizeUrl.template = Constants.TEMPLATE;
//
//			String authorizationUrl = authorizeUrl.build();
//			Log.i(TAG, "Popping a browser with the authorize URL : " + authorizationUrl);			
//			
//			
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
//			context.startActivity(intent);
			
			
			GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl = PicasaAuthenticatorOAuth.getInstance().retrieveTemporaryToken(Constants.OAUTH_CALLBACK_URL);
			Intent webIntent = new Intent(Intent.ACTION_VIEW);
//			webIntent.setData(Uri.parse(authorizeUrl.build()));
//			startActivity(webIntent);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizeUrl.build()));
			context.startActivity(intent);

		}
		catch(Exception e)
		{
			Log.e(TAG, "Error during OAUth retrieve request token", e);
		}
		return null;
	}

}
