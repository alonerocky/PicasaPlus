package dev.shoulongli.picasaplus.picasa.util;

import android.os.AsyncTask;

public class DeletePhotoTask extends AsyncTask<String,Void,String>
{
	private String userId; 
	private String albumId;
	public DeletePhotoTask(String userId , String albumId )
	{
		this.userId = userId; 
		this.albumId = albumId;
	}
	@Override
	public String doInBackground(String... parameters)
	{
		if(parameters != null && parameters.length > 0)
		{
			for(int i = 0;i < parameters.length;i++)
			{
				String photoId = parameters[i];
				PicasaWebAlbum.deletePhoto(userId, albumId,photoId);
			}
		}
		return "";

	}
	@Override
	public void onPostExecute(String status)
	{
		 
	}
}
