package dev.shoulongli.picasaplus.picasa.util;

import android.os.AsyncTask;

public class DeleteAlbumTask extends AsyncTask<String,Void,String>
{
	private String userId; 
	public DeleteAlbumTask(String userId  )
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
				String albumId = parameters[i];
				PicasaWebAlbum.deleteAlbum(userId, albumId);
			}
		}
		return "";

	}
	@Override
	public void onPostExecute(String status)
	{
		 
	}
}