package dev.shoulongli.picasaplus.picasa.model;

public class ThumbnailEntry 
{
	private int width;
	private int height;
	private String url;
	public ThumbnailEntry(int width, int height, String url)
	{
		this.width = width;
		this.height = height;
		this.url = url; 
	}
	public int getWidth()
	{
		return this.width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getHeight()
	{
		return this.height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public String getUrl()
	{
		return this.url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
}
