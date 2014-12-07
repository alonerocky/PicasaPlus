package dev.shoulongli.picasaplus.auth.oauth;



import java.io.IOException;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.api.client.apache.ApacheHttpTransport;
import com.google.api.client.auth.oauth.OAuthCallbackUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.xml.atom.AtomParser;
import com.google.api.data.picasa.v2.PicasaWebAlbums;
import com.google.api.data.picasa.v2.atom.PicasaWebAlbumsAtom;

import dev.shoulongli.picasaplus.PicasaPlusApplication;
import dev.shoulongli.picasaplus.auth.PicasaAuthCallback;
import dev.shoulongli.picasaplus.picasa.util.PicasaUtil;

public class PicasaAuthenticatorOAuth //implements PicasaAuthenticator
{
	private static PicasaAuthenticatorOAuth instance = new PicasaAuthenticatorOAuth();
	private Activity activity;
	private PicasaAuthCallback callback;
	public static final byte STATUS_SUCESS = 0;
	public static final byte STATUS_FAILED = 1;
	public byte status;
	private final GoogleTransport transport = new GoogleTransport();
	//private final OAuthHmacSigner signer = new OAuthHmacSigner();
	
	public static boolean isTemporary;
	public static OAuthCredentialsResponse credentials;
	
	public static PicasaAuthenticatorOAuth getInstance()
	{
		return instance;
	}
	private PicasaAuthenticatorOAuth()
	{
		//setup transport
		transport.setVersionHeader(PicasaWebAlbums.VERSION);
	    AtomParser parser = new AtomParser();
	    parser.namespaceDictionary = PicasaWebAlbumsAtom.NAMESPACE_DICTIONARY;
	    transport.addParser(parser);
	    
	    transport.applicationName = "Picasa Plus";
	    HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);
	    
	    //signer.clientSharedSecret = Constants.CONSUMER_CLIENT_SHARED_SECRET;
	    
	}
	public GoogleTransport getTransport()
	{
		return transport;
	}
//	public OAuthHmacSigner getSigner()
//	{
//		return signer;
//	}
	
	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}
	public void setCallback(PicasaAuthCallback callback)
	{
		this.callback = callback;
		
	}
	public PicasaAuthCallback getCallback()
	{
		return callback;
	}
//	public void auth(boolean tokenExpired)
//	{
//		Intent intent = new Intent(PicasaPlusApplication.getInstance().getApplicationContext(),PrepareRequestTokenActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra(PrepareRequestTokenActivity.KEY_TOKEN_EXPIRED, tokenExpired);
//		PicasaPlusApplication.getInstance().getApplicationContext().startActivity(intent);
//	}
	
	public void auth(boolean tokenExpired) 
	{
		try 
		{
			boolean isViewAction = false;
			if(activity != null && activity.getIntent() != null)
				isViewAction = Intent.ACTION_VIEW.equals(activity.getIntent().getAction());
			if (tokenExpired && !PicasaAuthenticatorOAuth.isTemporary && PicasaAuthenticatorOAuth.credentials != null) 
			{
				PicasaAuthenticatorOAuth.getInstance().revokeAccessToken();
			}
			if (tokenExpired || !isViewAction
					&& (PicasaAuthenticatorOAuth.isTemporary || PicasaAuthenticatorOAuth.credentials == null)) {

				GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl = PicasaAuthenticatorOAuth.getInstance().retrieveTemporaryToken(Constants.OAUTH_CALLBACK_URL);
				Intent webIntent = new Intent(Intent.ACTION_VIEW);
				webIntent.setData(Uri.parse(authorizeUrl.build()));
				activity.startActivity(webIntent); 
			} 
			else 
			{
				if (isViewAction) 
				{
					final Uri uri = activity.getIntent().getData();
					if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME))
					{
						PicasaAuthenticatorOAuth.getInstance().retrieveAccessToken(uri);
					} 

				} 				
				PicasaAuthenticatorOAuth.getInstance().getCallback().authenticated();
			}
		} catch (IOException e) 
		{
			handleException(e);
		}
		return;

	}

	private void handleException(Exception e) 
	{
		e.printStackTrace();
		if (e instanceof HttpResponseException) {
			int statusCode = ((HttpResponseException) e).response.statusCode;
			if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) 
			{
				auth(true);
			}
			return;
		} 
	} 
	private static OAuthHmacSigner createOAuthSigner() 
	{
		OAuthHmacSigner result = new OAuthHmacSigner();
		if (credentials != null) 
		{
			result.tokenSharedSecret = credentials.tokenSecret;
		}
		result.clientSharedSecret = Constants.CONSUMER_KEY;
		return result;
	}

	private static OAuthParameters createOAuthParameters() 
	{
		OAuthParameters authorizer = new OAuthParameters();
		authorizer.consumerKey = Constants.CONSUMER_KEY;
		authorizer.signer = createOAuthSigner();
		authorizer.token = credentials.token;
		return authorizer;
	}
	public GoogleOAuthAuthorizeTemporaryTokenUrl retrieveTemporaryToken(String callback) throws IOException
	{
		GoogleOAuthGetTemporaryToken temporaryToken =
			new GoogleOAuthGetTemporaryToken();
		temporaryToken.signer = createOAuthSigner();
		temporaryToken.consumerKey = Constants.CONSUMER_KEY;
		temporaryToken.scope = PicasaWebAlbums.ROOT_URL;
		temporaryToken.displayName = Constants.DISPLAY_NAME;
		temporaryToken.callback = callback;
		isTemporary = true;
		credentials = temporaryToken.execute();
		GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl =
			new GoogleOAuthAuthorizeTemporaryTokenUrl();
		authorizeUrl.temporaryToken = credentials.token;
		return authorizeUrl;
	}
	public void retrieveAccessToken(Uri uri) throws IOException
	{		
		OAuthCallbackUrl callbackUrl =
			new OAuthCallbackUrl(uri.toString());
		GoogleOAuthGetAccessToken accessToken =
			new GoogleOAuthGetAccessToken();
		accessToken.temporaryToken = callbackUrl.token;
		accessToken.verifier = callbackUrl.verifier;
		accessToken.signer = createOAuthSigner();
		accessToken.consumerKey = Constants.CONSUMER_KEY;
		isTemporary = false;
		credentials = accessToken.execute();
		PicasaUtil.storeCredential(credentials);
		createOAuthParameters().signRequestsUsingAuthorizationHeader(transport);
	}
	public void revokeAccessToken() throws IOException
	{
		GoogleOAuthGetAccessToken.revokeAccessToken(createOAuthParameters());
		PicasaUtil.clearCredentials();
		credentials = null;
	}
	
}
