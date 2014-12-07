package dev.shoulongli.picasaplus.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;
import dev.shoulongli.picasaplus.picasa.model.PhotoEntry;

public class SaxRssFeedParser implements RssFeedParser
{

	public ArrayList<AlbumEntry> getAlbums(InputStream is)
	{
		ArrayList<AlbumEntry> result = null;
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			PicasaAlbumHandler handler = new PicasaAlbumHandler();
			parser.parse(is, handler);
			result = handler.getList();
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	public ArrayList<PhotoEntry> getPhotos(InputStream is)
	{
		ArrayList<PhotoEntry> result = null;
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			PicasaPhotoHandler handler = new PicasaPhotoHandler();
			parser.parse(is, handler);
			result = handler.getList();
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
}
