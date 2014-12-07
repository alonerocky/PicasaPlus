package dev.shoulongli.picasaplus.auth;

public interface PicasaAuthCallback
{
	public static final int AUTH_ERROR = -102;
	public void authenticated();
	public void failure(int status,String message);
}
