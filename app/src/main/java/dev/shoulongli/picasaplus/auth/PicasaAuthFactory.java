package dev.shoulongli.picasaplus.auth;

public class PicasaAuthFactory 
{
	public static PicasaAuthenticator getAuthenticator()
	{
		return getAuthenticator(PicasaAuthType.OAUTH);
	}
	public static PicasaAuthenticator getAuthenticator(PicasaAuthType type)
	{
		switch(type)
		{
		case OAUTH:
			
			break;
		case ACCOUNT_MANAGER:
			break;
		}
		
		return null;
	}
	
}
