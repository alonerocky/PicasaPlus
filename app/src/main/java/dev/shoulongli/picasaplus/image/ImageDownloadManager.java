package dev.shoulongli.picasaplus.image;

import dev.shoulongli.picasaplus.image.DownloadedDrawable;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;


public class ImageDownloadManager 
{
	private static ImageDownloadManager instance = new ImageDownloadManager();
	private static final int DELAY_BEFORE_PURGE = 30 * 1000; // in milliseconds
	private Handler purgeHandler = new Handler();
	private Runnable purger = new Runnable()
	{
		@Override
		public void run()
		{
			ImageDownloadCache.getInstance().clearCache();
		}
	};
	
	private ImageDownloadManager()
	{
		//hide it
	}
	public static ImageDownloadManager getInstance()
	{
		return instance;
	}
	
	public void download(String url, ImageView view)
	{
		//if there is no downlading after some time, we need clear all the cache
		//resetPurgerTimer();
		Bitmap bitmap = getImageFromCache(url);
		if(bitmap == null)
		{
			forceDownload(url, view);
		}
		else
		{
			Log.i("ImageDownloadManager","get image from cache!!!");
			cancelPotentialDownload(url, view);
			view.setImageBitmap(bitmap);
		}
	}
	private void forceDownload(String url, ImageView imageView)
	{
		if(url == null)
		{
			imageView.setImageDrawable(null);
			return;
		}
		if(cancelPotentialDownload(url,imageView))
		{
			Log.i("ImageDownloadManager","start downloading!!!");
			ImageDownloadTask task = new ImageDownloadTask(imageView);
			DownloadedDrawable drawable = new DownloadedDrawable(task);
			imageView.setImageDrawable(drawable);
			task.execute(url);
		}
	}
	/**
     * Returns true if the current download has been canceled or if there was no download in
     * progress on this image view.
     * Returns false if the download in progress deals with the same url. The download is not
     * stopped in that case.
     */
	private boolean cancelPotentialDownload(String url, ImageView view)
	{
		ImageDownloadTask task = ImageDownloadTask.getImageDownloaderTask(view);
		if(task != null)
		{
			Log.i("ImageDownloadManager","task not null!!!");
			String currentUrl = task.getUrl();
			if(task.isCancelled() || currentUrl == null || !currentUrl.equals(url))
			{
				Log.i("ImageDownloadManager","cancel downloading!!!");
				task.cancel(true);
				return true;
			}
			else
			{
				// The same URL is already being downloaded.
				Log.i("ImageDownloadManager","The same URL is already being downloaded.!!!");
				return false;
			}
		}
		else
		{
			Log.i("ImageDownloadManager","task is NULL!!!");
		}
		return true;
	}
	public Bitmap getImageFromCache(String url)
	{
		return ImageDownloadCache.getInstance().get(url);
	}
	public void cacheImage(String url, Bitmap bitmap)
	{
		ImageDownloadCache.getInstance().put(url, bitmap);
	}
	public void resetPurgerTimer()
	{
		this.purgeHandler.removeCallbacks(purger);
		this.purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}
}
