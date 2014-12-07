package dev.shoulongli.picasaplus.picasa.model;

public class AlbumEntry 
{
	private long id;
	private long published;
	private long updated;
	private String user;
	private String title;
	private int numphotos;
	
	private String htmlurl;
	private String thumbnailurl;
	
	public long getId()
	{
		return id;
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
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public int getNumOfPhotos()
	{
		return this.numphotos;
	}
	public void setNumOfPhotos(int numofphotos)
	{
		this.numphotos = numofphotos;
	}
	public String getHtmlUrl()
	{
		return this.htmlurl;
	}
	public void setHtmlUrl(String htmlurl)
	{
		this.htmlurl = htmlurl;
	}
	public String getThumbnailurl()
	{
		return this.thumbnailurl;
	}
	public void setThumbnailurl(String thumbnailurl)
	{
		this.thumbnailurl = thumbnailurl;
	}
	
}
