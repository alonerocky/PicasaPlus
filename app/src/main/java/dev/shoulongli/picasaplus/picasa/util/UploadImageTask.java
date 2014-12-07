package dev.shoulongli.picasaplus.picasa.util;

import android.net.Uri;
import android.os.AsyncTask;

public class UploadImageTask extends AsyncTask<Uri,Void,String>
{
	private String userId;
	private String albumId;
	public UploadImageTask(String userId, String albumId )
	{
		this.userId = userId;
		this.albumId = albumId;
	}
	@Override
	public String doInBackground(Uri... parameters)
	{
		if(parameters != null && parameters.length > 0)
		{
			for(int i = 0;i < parameters.length;i++)
			{

				Uri uri = parameters[i];
				try
				{
					if(uri != null)
						PicasaWebAlbum.uploadImage(userId,albumId, uri );
				}
				catch(Exception e)
				{

				} 

			}
		}
		return "";  
	}
	@Override
	public void onPostExecute(String status)
	{
		//
	}
} 
