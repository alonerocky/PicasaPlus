package dev.shoulongli.picasaplus.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import dev.shoulongli.picasaplus.picasa.model.PhotoEntry;

public class PicasaPhotoHandler extends DefaultHandler2
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
	public static final String ALBUM_ID_ELEMENT = "albumid";//gphoto:albumid
	public static final String PUBLISHED_ELEMENT = "published";
	public static final String USER_ELEMENT = "user";//gphoto:user
	public static final String PHOTO_NUM_ELEMENT = "numphotos";//gphoto:numphotos
	public static final String THUMBNAIL_URL_ELEMENT = "thumbnail"; //media:thumbnail
	public static final String CONTENT_URL_ELEMENT = "content";//media:content
	public static final String TITLE_ELEMENT = "title";
	public static final String PUB_DATE = "pubDate";
	
	private ArrayList<PhotoEntry> list;
	private PhotoEntry currentItem;
	private StringBuilder sb;
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
		list = new ArrayList<PhotoEntry>();
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
				currentItem = new PhotoEntry();
			} 
		}
		else if(MEDIA_RSS_NAMESPACE.equals(uri))
		{
			if(currentItem != null && localName.equalsIgnoreCase(CONTENT_URL_ELEMENT))
			{
				currentItem.setUrl(attr.getValue("", "url"));
			}
			else if(currentItem != null && localName.equalsIgnoreCase(THUMBNAIL_URL_ELEMENT))
			{
				try
				{
					currentItem.addThumbnailUrl(Integer.parseInt(attr.getValue("", "width")), Integer.parseInt(attr.getValue("", "height")), attr.getValue("", "url"));
				}
				catch(Exception e)
				{
					
				}
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
				if(localName.equalsIgnoreCase(ENTRY_ELEMENT))
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
				else if(localName.equalsIgnoreCase(ALBUM_ID_ELEMENT))
				{
					try
					{
						currentItem.setAlbumId(Long.parseLong(sb.toString().trim()));
					}
					catch(Exception e)
					{

					}
				}
			}
			
			
		}
		sb.setLength(0);
		
	}
	public ArrayList<PhotoEntry> getList()
	{
		return list;
	}
}
