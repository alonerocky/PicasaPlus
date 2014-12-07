package dev.shoulongli.picasaplus.auth.oauth;

public class Constants 
{
	public static final String APP_NAME = "Picasa Plus";
	public static final String CONSUMER_KEY = "anonymous";
	public static final String CONSUMER_CLIENT_SHARED_SECRET 	= "anonymous";
	public static final String SCOPE = "http://picasaweb.google.com/data/";
	
	public static final String DISPLAY_NAME = "Picasa Plus for Android";
	//a mobile version of the approval  page or {@code null} for normal.
	public static final String TEMPLATE = "mobile";
	public static final String	OAUTH_CALLBACK_SCHEME	= "picasaplus";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + ":///" ;//+ OAUTH_CALLBACK_HOST;
	
	public static final String PREF_KEY_OAUTH_TOKEN_SECRET = "picasaplus_oauth_token_secret";
	public static final String PREF_KEY_OAUTH_TOKEN = "picasaplus_oauth_token";
	
}
