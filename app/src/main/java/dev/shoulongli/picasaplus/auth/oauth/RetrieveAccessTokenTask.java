package dev.shoulongli.picasaplus.auth.oauth;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.api.client.auth.oauth.OAuthCallbackUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;

public class RetrieveAccessTokenTask extends AsyncTask<Uri,Void,Void>
{
	final String TAG = getClass().getName();
	private OAuthCredentialsResponse credentials;
	public RetrieveAccessTokenTask()
	{
	}
	@Override
	public Void doInBackground(Uri... params)
	{
		try
		{
//			final Uri uri = params[0];
//			OAuthCallbackUrl callbackUrl = new OAuthCallbackUrl(uri.toString());
//			GoogleOAuthGetAccessToken accessToken = new GoogleOAuthGetAccessToken();
//			accessToken.temporaryToken = callbackUrl.token;
//			accessToken.verifier = callbackUrl.verifier;			
//			
//			accessToken.signer = PicasaAuthenticatorOAuth.getInstance().getSigner();
//			accessToken.consumerKey = Constants.CONSUMER_KEY;
//
//			credentials = accessToken.execute();
//
//
//			OAuthParameters authorizer = new OAuthParameters();
//			authorizer.consumerKey = Constants.CONSUMER_KEY;
//			PicasaAuthenticatorOAuth.getInstance().getSigner().tokenSharedSecret = credentials.tokenSecret;
//			
//			authorizer.signer = PicasaAuthenticatorOAuth.getInstance().getSigner();
//			authorizer.token = credentials.token;
//			authorizer.signRequestsUsingAuthorizationHeader(PicasaAuthenticatorOAuth.getInstance().getTransport());
//			Log.i(TAG, "OAuth - Access Token Retrieved");
//			
//			saveAuthInformation(credentials.token,credentials.tokenSecret);			
//			PicasaAuthenticatorOAuth.getInstance().anthenticated(PicasaAuthenticatorOAuth.STATUS_SUCESS, "OAuth - Access Token Retrieved");
//			
			final Uri uri = params[0];
			PicasaAuthenticatorOAuth.getInstance().retrieveAccessToken(uri);
			PicasaAuthenticatorOAuth.getInstance().getCallback().authenticated();
		}
		catch(Exception e)
		{
			Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			
		}

		return null;
	}
}
