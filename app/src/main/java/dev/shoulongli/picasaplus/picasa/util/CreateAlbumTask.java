package dev.shoulongli.picasaplus.picasa.util;

import android.os.AsyncTask;

public class CreateAlbumTask extends AsyncTask<String,Void,String>
{
	private String userId; 
	public CreateAlbumTask(String userId )
	{
		this.userId = userId; 
	}
	@Override
	public String doInBackground(String... parameters)
	{
		if(parameters != null && parameters.length > 0)
		{
			for(int i = 0;i < parameters.length;i++)
			{
				String albumName = parameters[i];
				PicasaWebAlbum.createAlbum(userId, albumName);
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