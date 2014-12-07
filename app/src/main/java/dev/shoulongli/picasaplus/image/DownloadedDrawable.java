package dev.shoulongli.picasaplus.image;

import java.lang.ref.WeakReference;

import dev.shoulongli.picasaplus.image.ImageDownloadTask;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class DownloadedDrawable extends ColorDrawable
{
	private WeakReference<ImageDownloadTask> weakRefImageDownloaderTask;
	public DownloadedDrawable(ImageDownloadTask task)
	{
		super(Color.BLACK);
		weakRefImageDownloaderTask = new WeakReference<ImageDownloadTask>(task);
	}
	
	public ImageDownloadTask getImageDownloaderTask()
	{
		return weakRefImageDownloaderTask.get();
	}
}
