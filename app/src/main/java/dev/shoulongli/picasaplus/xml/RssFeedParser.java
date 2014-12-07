package dev.shoulongli.picasaplus.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;
import dev.shoulongli.picasaplus.picasa.model.PhotoEntry;

public interface RssFeedParser 
{
	
	public static final String APP_NAMESPACE = "http://www.w3.org/2007/app";
	public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
	public static final String GD_NAMESPACE = "http://schemas.google.com/g/2005";
	public static final String GPHOTO_NAMESPACE = "http://schemas.google.com/photos/2007";
	public static final String MEDIA_RSS_NAMESPACE = "http://search.yahoo.com/mrss/";
	public static final String GML_NAMESPACE = "http://www.opengis.net/gml";
	
	public static final String FEED_ELEMENT = "feed";
	public static final String ENTRY_ELEMENT = "entry";
	public static final String ID_ELEMENT = "id";
	public static final String PUBLISHED_ELEMENT = "published";
	public static final String USER_ELEMENT = "user";
	
	public static final String TITLE_ELEMENT = "title";
	public static final String PUB_DATE = "pubDate";
	
	public static final String MEDIA_CONTENT = "media:content";
	public static final String MEDIA_DESC = "media:description";
	public ArrayList<AlbumEntry> getAlbums(InputStream is);
	public ArrayList<PhotoEntry> getPhotos(InputStream is);
}
