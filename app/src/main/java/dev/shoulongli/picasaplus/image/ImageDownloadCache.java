package dev.shoulongli.picasaplus.image;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageDownloadCache 
{
	public static final int HARD_CACHE_CAPACITY = 40;
	public static final float HARD_CACHE_LOAD_FACTOR = 0.75f;
	private static ImageDownloadCache instance = new ImageDownloadCache();
	private ImageDownloadCache()
	{
		//hide it
	}
	public static ImageDownloadCache getInstance()
	{
		return instance;
	}
	private HashMap<String, Bitmap> hardCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY/2,HARD_CACHE_LOAD_FACTOR,true)
	{
		@Override
		protected boolean removeEldestEntry(Entry<String, Bitmap> entry)
		{
			if(this.size() > HARD_CACHE_CAPACITY)
			{
				softCache.put(entry.getKey(), new SoftReference<Bitmap>(entry.getValue()));
				return true;
			}
			else
			{
				return false;
			}
		}
	};
	
	private ConcurrentHashMap<String, SoftReference<Bitmap>> softCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY/2);
	
	public Bitmap get(String url)
	{
		synchronized(hardCache)
		{
			final Bitmap bitmap = hardCache.get(url);
			if(bitmap != null)
			{
				Log.i("ImageDownloadCache","found from hard cache");
				// Bitmap found in hard cache
                // Move element to first position, so that it is removed last
				hardCache.remove(url);
				hardCache.put(url, bitmap);
				return bitmap;
			}
		}
		// Then try the soft reference cache
		SoftReference<Bitmap> soft = softCache.get(url);
		if(soft != null)
		{
			final Bitmap bitmap = soft.get();
			if(bitmap != null)
			{
				Log.i("ImageDownloadCache","found from soft cache");
				// Bitmap found in soft cache
				return bitmap;
			}
			else
			{
				
				softCache.remove(url);
			}
		}
		Log.i("ImageDownloadCache","not cached yet");
		return null;
	}
	public void put(String url, Bitmap bitmap)
	{
		//All elements are permitted as keys or values, including null
		if(bitmap != null)
		{
			Log.i("ImageDownloadCache","cach "+url);
			synchronized(hardCache)
			{
				hardCache.put(url, bitmap);
			}
		}
	}
	
	public void clearCache()
	{
		Log.i("ImageDownloadCache","cach clared");
		hardCache.clear();
		softCache.clear();
	}
	
	
	
}
