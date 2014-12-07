package dev.shoulongli.picasaplus.xml;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;

import android.util.Log;

public class PicasaAlbumHandler extends DefaultHandler2
{
	//public static final String APP_NAMESPACE = "http://www.w3.org/2007/app";
	public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
	//public static final String GD_NAMESPACE = "http://schemas.google.com/g/2005";
	public static final String GPHOTO_NAMESPACE = "http://schemas.google.com/photos/2007";
	public static final String MEDIA_RSS_NAMESPACE = "http://search.yahoo.com/mrss/";
	//public static final String GML_NAMESPACE = "http://www.opengis.net/gml";
	
	public static final String FEED_ELEMENT = "feed";
	public static final String ENTRY_ELEMENT = "entry";
	public static final String ID_ELEMENT = "id";//gphoto:id
	public static final String PUBLISHED_ELEMENT = "published";
	public static final String USER_ELEMENT = "user";//gphoto:user
	public static final String PHOTO_NUM_ELEMENT = "numphotos";//gphoto:numphotos
	public static final String THUMBNAIL_URL_ELEMENT = "thumbnail"; //media:thumbnail
	public static final String TITLE_ELEMENT = "title";
	public static final String PUB_DATE = "pubDate";
	
	private ArrayList<AlbumEntry> list;
	private AlbumEntry currentItem;
	private StringBuilder sb;
	private String urlAttr = null;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		super.characters(ch, start, length);
		sb.append(ch,start,length);
	}
	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		sb = new StringBuilder();
		list = new ArrayList<AlbumEntry>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException
	{
		/*
		uri 	The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
		localName 	The local name (without prefix), or the empty string if Namespace processing is not being performed.
		qName 	The qualified name (with prefix), or the empty string if qualified names are not available.
		*/
		super.startElement(uri, localName, qName, attr);
		if(ATOM_NAMESPACE.equals(uri))
		{
			if(localName.equalsIgnoreCase(ENTRY_ELEMENT))
			{
				currentItem = new AlbumEntry();
			} 
		}
		else if(MEDIA_RSS_NAMESPACE.equals(uri))
		{
			if(currentItem != null && localName.equalsIgnoreCase(THUMBNAIL_URL_ELEMENT))
			{
				currentItem.setThumbnailurl(attr.getValue("", "url"));
			}
		} 
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		super.endElement(uri, localName, qName);
		if(currentItem != null)
		{
			if(ATOM_NAMESPACE.equals(uri))
			{
				if(localName.equalsIgnoreCase(TITLE_ELEMENT) )
				{
					currentItem.setTitle(sb.toString().trim());				
				}			
				else if(localName.equalsIgnoreCase(ENTRY_ELEMENT))
				{
					list.add(currentItem);
				}
			}
			else if(GPHOTO_NAMESPACE.equals(uri))
			{
				if(localName.equalsIgnoreCase(ID_ELEMENT))
				{
					try
					{
						currentItem.setId(Long.parseLong(sb.toString().trim()));
					}
					catch(Exception e)
					{

					}
				}
				else if(localName.equalsIgnoreCase(USER_ELEMENT))
				{
					currentItem.setUser(sb.toString().trim());
				}
				else if(localName.equalsIgnoreCase(PHOTO_NUM_ELEMENT))
				{
					try
					{
						currentItem.setNumOfPhotos(Integer.parseInt(sb.toString().trim()));
					}
					catch(Exception e)
					{

					}
				}
			}
			else if(MEDIA_RSS_NAMESPACE.equals(uri))
			{
				if(localName.equalsIgnoreCase(THUMBNAIL_URL_ELEMENT))
				{
					String test = sb.toString().trim();
					int i = 5;
				}
			}
			
			
			
			
			
		}
		sb.setLength(0);
		
	}
	public ArrayList<AlbumEntry> getList()
	{
		return list;
	}
}
