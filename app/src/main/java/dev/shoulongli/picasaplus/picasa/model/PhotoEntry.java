package dev.shoulongli.picasaplus.picasa.model;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoEntry 
{
	private long albumId;
	private long id;
	private long published;
	private long updated;
	private String  url;
	private ArrayList<ThumbnailEntry> thumbnails = new ArrayList<ThumbnailEntry>();
	
	
	public long getAlbumId()
	{
		return this.albumId;
	}
	public void setAlbumId(long albumId)
	{
		this.albumId = albumId;
	}
	public long getId()
	{
		return this.id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	
	public long getPublished()
	{
		return published;
	}
	public void setPublished(long published)
	{
		this.published = published;
	}
	public long getUpdated()
	{
		return updated;
	}
	public void setUpdated(long updated)
	{
		this.updated = updated;
	}
	public String getUrl()
	{
		return this.url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public String getThumbnailUrl()
	{
		if(this.thumbnails != null && this.thumbnails.size() > 0)
			return this.thumbnails.get(0).getUrl();
		return null;
	}
	public void addThumbnailUrl(int width, int height, String thumbnailurl)
	{
		this.thumbnails.add(new ThumbnailEntry(width, height,thumbnailurl ));
	}
	
}
