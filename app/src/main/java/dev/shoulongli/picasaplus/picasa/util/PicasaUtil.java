package dev.shoulongli.picasaplus.picasa.util;



import com.google.api.client.apache.ApacheHttpTransport;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.data.picasa.v2.PicasaWebAlbums;

import dev.shoulongli.picasaplus.PicasaPlusApplication;
import dev.shoulongli.picasaplus.auth.oauth.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.Time;

public class PicasaUtil 
{



	public static long parseAtomTimestamp(String timestamp) 
	{
		Time time = new Time();
		time.parse3339(timestamp);
		return time.toMillis(true);
	}

	public static HttpTransport getTransport()
	{
		GoogleTransport transport =  new GoogleTransport();
		transport.setVersionHeader(PicasaWebAlbums.VERSION);
		//	    AtomParser parser = new AtomParser();
		//	    parser.namespaceDictionary = PicasaWebAlbumsAtom.NAMESPACE_DICTIONARY;
		//	    transport.addParser(parser);
		transport.applicationName = Constants.APP_NAME;
		HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);

		OAuthCredentialsResponse credentials = new OAuthCredentialsResponse();
		Context context = PicasaPlusApplication.getInstance().getApplicationContext();
		SharedPreferences prefs = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
		String token = prefs.getString(Constants.PREF_KEY_OAUTH_TOKEN, null);
		String secret = prefs.getString(Constants.PREF_KEY_OAUTH_TOKEN_SECRET, null);
		credentials.token = token;
		credentials.tokenSecret = secret;
		createOAuthParameters(credentials).signRequestsUsingAuthorizationHeader(transport);
		return transport;
	}
	private static OAuthParameters createOAuthParameters(OAuthCredentialsResponse credentials) 
	{
		OAuthParameters authorizer = new OAuthParameters();
		authorizer.consumerKey = Constants.CONSUMER_KEY;
		authorizer.signer = createOAuthSigner(credentials);
		authorizer.token = credentials.token;
		return authorizer;
	}
	private static OAuthHmacSigner createOAuthSigner(OAuthCredentialsResponse credentials) 
	{
		OAuthHmacSigner result = new OAuthHmacSigner();
		if (credentials != null) 
		{
			result.tokenSharedSecret = credentials.tokenSecret;
		}
		result.clientSharedSecret = Constants.CONSUMER_KEY;
		return result;
	}
	public static boolean isOAuthSuccessful() 
	{
		Context context = PicasaPlusApplication.getInstance().getApplicationContext();
		SharedPreferences prefs = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
		String token = prefs.getString(Constants.PREF_KEY_OAUTH_TOKEN, null);
		String secret = prefs.getString(Constants.PREF_KEY_OAUTH_TOKEN_SECRET, null);
		return (token != null && secret != null);
	}
	public static Boolean clearCredentials() 
	{
		Context context = PicasaPlusApplication.getInstance().getApplicationContext();
		SharedPreferences prefs = context.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.remove(Constants.PREF_KEY_OAUTH_TOKEN);
		edit.remove(Constants.PREF_KEY_OAUTH_TOKEN_SECRET);
		edit.commit(); 
		return true;
	}
	public static void storeCredential(OAuthCredentialsResponse credential)
	{
		if(credential != null)
		{
			Context context = PicasaPlusApplication.getInstance().getApplicationContext();
			SharedPreferences prefs = context.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
			Editor edit = prefs.edit();
			edit.putString(Constants.PREF_KEY_OAUTH_TOKEN, credential.token) ;
			edit.putString(Constants.PREF_KEY_OAUTH_TOKEN_SECRET, credential.tokenSecret) ;
			edit.commit(); 
		}
	}

    public static void storeToken( String token) {
        if(token != null)
        {
            Context context = PicasaPlusApplication.getInstance().getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
            Editor edit = prefs.edit();
            edit.putString(Constants.ACCESS_TOKEN, token) ;
            edit.commit();
        }
    }
    public static String getToken() {
        Context context = PicasaPlusApplication.getInstance().getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.ACCESS_TOKEN, null);
        return token;
    }

    public static void storeUserId(String id) {
        if(id != null)
        {
            Context context = PicasaPlusApplication.getInstance().getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
            Editor edit = prefs.edit();
            edit.putString(Constants.USER_ID, id) ;
            edit.commit();
        }
    }
    public static String getUserId() {
        Context context = PicasaPlusApplication.getInstance().getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        String id = prefs.getString(Constants.USER_ID, null);
        return id;
    }
	

}
