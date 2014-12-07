package dev.shoulongli.picasaplus.util;

import java.io.IOException;
import java.io.OutputStream;

import com.google.api.client.http.HttpContent;
import com.google.api.client.xml.atom.Atom;

public class AtomMetaDataHttpContent implements HttpContent
{
	/*
	 * <entry xmlns='http://www.w3.org/2005/Atom'>
	 * <title>"+item.imageName+"</title>
	 * <category scheme=\"http://schemas.google.com/g/2005#kind\" 
	 * term=\"http://schemas.google.com/photos/2007#photo\"/>
	 * </entry>"
	 */
	private String metadata;
	public AtomMetaDataHttpContent(String metadata)
	{
		this.metadata = metadata;
	}
	@Override
	public String getType() 
	{
		return Atom.CONTENT_TYPE;
	}
	@Override
	public String getEncoding()
	{
		return null;
	}
	@Override
	public long getLength()
	{
		int length = -1;
		if(metadata != null)
			length = metadata.getBytes().length;
		return length;
	}
	@Override
	public final void writeTo(OutputStream out) throws IOException
	{
		out.write(metadata.getBytes());
	}

}
