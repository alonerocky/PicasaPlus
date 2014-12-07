package dev.shoulongli.picasaplus;


import java.io.IOException;

import org.apache.http.HttpStatus;

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

import dev.shoulongli.picasaplus.auth.PicasaAuthCallback;
import dev.shoulongli.picasaplus.auth.oauth.Constants;
import dev.shoulongli.picasaplus.picasa.util.PicasaUtil;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PicasaOAuthActivity extends Activity //implements PicasaAuthCallback
{
	private final GoogleTransport transport = new GoogleTransport();
	//private final OAuthHmacSigner signer = new OAuthHmacSigner();
	
	public static boolean isTemporary;
	public static OAuthCredentialsResponse credentials;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.authentication);
		//setup transport
		transport.setVersionHeader(PicasaWebAlbums.VERSION);
		AtomParser parser = new AtomParser();
		parser.namespaceDictionary = PicasaWebAlbumsAtom.NAMESPACE_DICTIONARY;
		transport.addParser(parser);

		transport.applicationName = "Picasa Plus";
		HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);
//		Button retry = (Button)findViewById(R.id.retry);
//		retry.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				auth(false);
//			}
//		});
		if(PicasaUtil.isOAuthSuccessful())
		{
			authenticated();
		}
		else 
		{
			 auth(false); 
		}
	}

 
	 
	public void auth(boolean tokenExpired) 
	{
		try 
		{
			boolean isViewAction = false;

			isViewAction = Intent.ACTION_VIEW.equals(getIntent().getAction());
			if (tokenExpired && !isTemporary && credentials != null) 
			{
				revokeAccessToken();
			}
			if (tokenExpired || !isViewAction
					&& (isTemporary || credentials == null)) {

				GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl = retrieveTemporaryToken(Constants.OAUTH_CALLBACK_URL);
				Intent webIntent = new Intent(Intent.ACTION_VIEW);
				webIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
				webIntent.setData(Uri.parse(authorizeUrl.build()));
				startActivity(webIntent);
				finish();
				//				new OAuthRequestTokenTask().execute();
			} 
			else 
			{
				if (isViewAction) 
				{
					final Uri uri = getIntent().getData();
					if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME))
					{
						retrieveAccessToken(uri);
					}

					

				} 				
				authenticated();
			}
		} catch (IOException e) 
		{
			handleException(e);
		}
		return;

	}

	public void authenticated()
	{
		Intent intent = new Intent(this,PicasaPlusAlbumsActivity.class);
		startActivity(intent);
		finish(); 
	}
	public void fail(int errCode, String errMsg)
	{
		
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
			else
			{
				fail(statusCode, e.getMessage());
				
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
