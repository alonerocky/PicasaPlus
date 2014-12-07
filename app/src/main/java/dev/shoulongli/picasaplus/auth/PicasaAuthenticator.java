package dev.shoulongli.picasaplus.auth;

import org.apache.http.HttpRequest;

import com.google.api.client.googleapis.GoogleTransport;

public interface PicasaAuthenticator 
{
	public void applyToken(HttpRequest request) ;
	public void auth(boolean tokenExpired);
	public GoogleTransport getTransport();
}
