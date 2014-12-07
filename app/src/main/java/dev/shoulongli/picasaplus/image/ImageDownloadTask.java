package dev.shoulongli.picasaplus.image;

import java.lang.ref.WeakReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import dev.shoulongli.picasaplus.image.DownloadedDrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap>
{
	private String url;
	private WeakReference<ImageView> weakRefImageView ;
	public ImageDownloadTask(ImageView view)
	{
		weakRefImageView = new WeakReference<ImageView>(view);
	}
	public String getUrl()
	{
		return url;
	}
	
	@Override
	public Bitmap doInBackground(String... params)
	{
		
		url = params[0];
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		Log.i(ImageDownloadTask.class.getName(), "url: "+url);
		try
		{
			HttpResponse response = client.execute(request);			
			BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
			final Bitmap result = BitmapFactory.decodeStream(entity.getContent());
			return result;
		}
		catch(Exception e)
		{
			request.abort();
		}
		
		
		return null;
	}
	@Override 
	public void onPostExecute(Bitmap bitmap)
	{
		if(isCancelled())
			bitmap = null;
		
		//cache the result
		if(bitmap != null)
		{
			ImageDownloadManager.getInstance().cacheImage(url, bitmap);
		}
		if(weakRefImageView != null)
		{
			ImageView imageView = weakRefImageView.get();
			//FIXME
			//Change bitmap only if this process is still associated with it
			//ImageDownloader.java
			Log.i(ImageDownloadTask.class.getName(),"Current Thread: "+Thread.currentThread().getName());
			Log.i(ImageDownloadTask.class.getName(),"imageView: "+imageView);
			ImageDownloadTask task =  getImageDownloaderTask(imageView);
			Log.i(ImageDownloadTask.class.getName(),"task: "+task);
			if(this == task && imageView != null)
			{
				Log.i(ImageDownloadTask.class.getName(),"!!!!!!set image");
				imageView.setImageBitmap(bitmap);
			}
			else
			{
				Log.i(ImageDownloadTask.class.getName(),"!!!!!!! not this task");
			}
				
		}
	}
	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		this.url = null;
		Log.i("ImageDownloaderTask", "onCancelled()! url set to be NULL");
	}
	public static ImageDownloadTask getImageDownloaderTask(ImageView view)
	{
		if(view != null)
		{			
			Drawable drawable = view.getDrawable();
			if(drawable instanceof DownloadedDrawable)
			{
				Log.i("ImagedownloaderTask", "DownloadedDrawable");
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
				return downloadedDrawable.getImageDownloaderTask();
			}
		}

		return null;
	}
}
