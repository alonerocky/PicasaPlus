package com.google.api.client.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.util.ByteArrayBuffer;

/**
 * Multi-part related content.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class MultipartContent implements HttpContent {

  // TODO: test it!

  // TODO: instead of getBytes() use getBytes("UTF-8")?
  private static final byte[] CR_LF = "\r\n".getBytes();
  private static final byte[] HEADER = "Media multipart posting".getBytes();
  private static final byte[] CONTENT_TYPE = "Content-Type: ".getBytes();
  private static final byte[] CONTENT_TRANSFER_ENCODING =
      "Content-Transfer-Encoding: binary".getBytes();
  private static final byte[] TWO_DASHES = "--".getBytes();
  private static final byte[] END_OF_PART = "END_OF_PART".getBytes();

  private final byte[] metadataContentTypeBytes;
  private final byte[] mediaTypeBytes;
  private final HttpContent metadata;
  private final HttpContent content;
  private final long length;

  public MultipartContent(HttpContent metadata, HttpContent content) {
    byte[] metadataContentTypeBytes = metadata.getType().getBytes();
    byte[] mediaTypeBytes = content.getType().getBytes();
    long metadataLength = content.getLength();
    this.length =
        metadataLength + content.getLength() + mediaTypeBytes.length
            + metadataContentTypeBytes.length + HEADER.length + 2
            * CONTENT_TYPE.length + CONTENT_TRANSFER_ENCODING.length + 3
            * END_OF_PART.length + 10 * CR_LF.length + 4 * TWO_DASHES.length;
    this.metadata = metadata;
    this.content = content;
    this.metadataContentTypeBytes = metadataContentTypeBytes;
    this.mediaTypeBytes = mediaTypeBytes;
  }

  public void forRequest(HttpRequest request) {
    request.headers.mimeVersion = "1.0";
    request.content = this;
  }

  public void writeTo(OutputStream out) throws IOException {
	  
	
    out.write(HEADER);
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);    
    out.write(CR_LF);
    
    
    out.write(CONTENT_TYPE);
    out.write(metadataContentTypeBytes);
    out.write(CR_LF);
    out.write(CR_LF);
    
    
    metadata.writeTo(out);
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);
    out.write(CR_LF);
    
    
    out.write(CONTENT_TYPE);
    out.write(mediaTypeBytes);
    out.write(CR_LF);
    out.write(CONTENT_TRANSFER_ENCODING);
    out.write(CR_LF);
    out.write(CR_LF);
    content.writeTo(out);
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);
    out.write(TWO_DASHES);
    out.flush();
  }
  public byte[] getContent() throws IOException
  {
	  ByteArrayOutputStream out = new ByteArrayOutputStream();
	  out.write(HEADER);
	    out.write(CR_LF);
	    out.write(TWO_DASHES);
	    out.write(END_OF_PART);    
	    out.write(CR_LF);
	    
	    
	    out.write(CONTENT_TYPE);
	    out.write(metadataContentTypeBytes);
	    out.write(CR_LF);
	    out.write(CR_LF);
	    
	    
	    metadata.writeTo(out);
	    out.write(CR_LF);
	    out.write(TWO_DASHES);
	    out.write(END_OF_PART);
	    out.write(CR_LF);
	    
	    
	    out.write(CONTENT_TYPE);
	    out.write(mediaTypeBytes);
	    out.write(CR_LF);
	    out.write(CONTENT_TRANSFER_ENCODING);
	    out.write(CR_LF);
	    out.write(CR_LF);
	   // content.writeTo(out);
	    out.write(CR_LF);
	    out.write(TWO_DASHES);
	    out.write(END_OF_PART);
	    out.write(TWO_DASHES);
	    out.flush();
	    
	    return out.toByteArray();
  }
  public String getEncoding() {
    return null;
  }

  public long getLength() {
    return this.length;
  }

  public String getType() {
    return "multipart/related; boundary=\"END_OF_PART\"";
  }

}
