package dev.shoulongli.picasaplus.picasa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpStatus;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.webkit.MimeTypeMap;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.InputStreamContent;

import dev.shoulongli.picasaplus.PicasaPlusApplication;
//import dev.shoulongli.picasaplus.auth.oauth.PicasaAuthenticatorOAuth;
import dev.shoulongli.picasaplus.picasa.model.ImageItem;
import dev.shoulongli.picasaplus.picasa.model.MultipartContent;
import dev.shoulongli.picasaplus.util.AtomMetaDataHttpContent;

public class PicasaWebAlbum 
{
	//https://developers.google.com/picasa-web/docs/2.0/developers_guide_protocol
	//https://developers.google.com/gdata/docs/2.0/reference#PartialUpdate
	public static final String ALBUM_FIELDS = "entry(title,gphoto:id,gphoto:user,gphoto:numphotos,media:group(media:thumbnail),link[@rel='http://schemas.google.com/g/2005#feed'](@href))";

	/*
	"entry(title,gphoto:numphotos,media:group(media:thumbnail),link[@rel='http://schemas.google.com/g/2005#feed'](@href))"
	....
	<entry>
    <title>Macro Shots</title>
    <link href='https://picasaweb.google.com/data/feed/api/user/userID/albumid/albumID'/>
    <gphoto:numphotos>5</gphoto:numphotos>
    <media:group>
      <media:thumbnail url='https://thumbnailPath/MacroShots.jpg' height='160' width='160'/>
    </media:group>
  </entry>
  ....
	 */
	public static final String PHOTO_FIELDS = "entry(gphoto:id,gphoto:albumid,media:group(media:content,media:thumbnail),link[@rel='http://schemas.google.com/g/2005#feed'](@href))";

	public static final String PICASA_WEB_ALBUM_URL = "http://picasaweb.google.com/data/feed/api/user";
	public static final String PICASA_WEB_ALBUM_URL_ENTRY = "http://picasaweb.google.com/data/entry/api/user";

	//	public static final String THUMBNAIL_SIZE = "72X54";//72, 54  144, 108   288, 216

	//https://picasaweb.google.com/data/feed/api/user/userID/albumid/albumID

	public static String createAlbum(String userId, String name)
	{
		String result = "";
		try
		{
			String url = PICASA_WEB_ALBUM_URL +"/"+userId;
			String album = "<entry xmlns=\'http://www.w3.org/2005/Atom'" +
			" xmlns:media='http://search.yahoo.com/mrss/'"+
			" xmlns:gphoto='http://schemas.google.com/photos/2007'>"+
			"<title type='text'>"+name+"</title>"+ 
			"<category scheme='http://schemas.google.com/g/2005#kind'"+
			" term='http://schemas.google.com/photos/2007#album'></category>"+
			"</entry>";

			AtomMetaDataHttpContent metadataContent = new AtomMetaDataHttpContent(album);
			HttpRequest request = PicasaUtil.getTransport().buildPostRequest();
			request.url = new GenericUrl(url);
			request.content = metadataContent;

			com.google.api.client.http.HttpResponse response = request.execute();
			int status = response.statusCode;
			if(status == HttpStatus.SC_CREATED)
			{
				//done uploading
				InputStream is = response.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String line;
				while((line = reader.readLine()) != null)
				{
					sb.append(line);

				}
				//System.out.println(sb);
				result = sb.toString();
			}
			else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
			{
				result = ""+ status;
			}
		}
		catch(Exception e)
		{
			//Log.e(TAG, "Error! "+e);
			result = e.getMessage();
		}
		return result;

	}
	public static String deleteAlbum(String userId, String albumId)
	{
		String result = "";
		try
		{
			String url = PICASA_WEB_ALBUM_URL_ENTRY +"/"+userId+"/albumid/"+albumId;
			 

			HttpRequest request = PicasaUtil.getTransport().buildDeleteRequest();
			request.headers.set("If-Match", "*");
			request.url = new GenericUrl(url);

			com.google.api.client.http.HttpResponse response = request.execute();
			int status = response.statusCode;
			if(status == HttpStatus.SC_OK)
			{
				//done uploading
				InputStream is = response.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String line;
				while((line = reader.readLine()) != null)
				{
					sb.append(line);

				}
				//System.out.println(sb);
				result = sb.toString();
			}
			else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
			{ 
				result = ""+ status;
			}
		}
		catch(Exception e)
		{
			//Log.e(TAG, "Error! "+e);
			result = e.getMessage();
		}
		return result;
	}
	//DELETE https://picasaweb.google.com/data/entry/api/user/userID/albumid/albumID/photoid/photoID
	public static String deletePhoto(String userId, String albumId, String photoID)
	{
		String result = "";
		try
		{
			String url = PicasaWebAlbum.PICASA_WEB_ALBUM_URL+"/"+userId+"/albumid/"+albumId+"/photoid/"+photoID;
			HttpRequest request = PicasaUtil.getTransport().buildDeleteRequest();
			request.headers.set("If-Match", "*");
			request.url = new GenericUrl(url);
			com.google.api.client.http.HttpResponse response = request.execute();
			int status = response.statusCode;
			if(status == HttpStatus.SC_OK)
			{
				//done uploading
				InputStream is = response.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String line;
				while((line = reader.readLine()) != null)
				{
					sb.append(line);

				}
				//System.out.println(sb);
				result = sb.toString();
			}
			else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
			{ 
				result = ""+ status;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	public static void handleUploadingImage (String userId, String albumId,Intent intent, ContentResolver contentResolver)
	{
		String action = intent.getAction();
	    String type = intent.getType();
	    if(type != null)
	    {
	    	if(Intent.ACTION_SEND.equals(action))
	    	{
	    		if ("text/plain".equals(type)) 
	    		{
	                handleSendText(intent); // Handle text being sent
	            }
	    		else if (type.startsWith("image/")) 
	            {
	                handleSendImage(intent,   contentResolver); // Handle single image being sent
	            }
	    	}
	    	else if(Intent.ACTION_SEND_MULTIPLE.equals(action))
	    	{
	    		if (type.startsWith("image/")) 
	    		{
	    			handleSendMultipleImages(  intent) ;
	    		}
	    	}
	    	
	    	
	    	
	    }
	}
	private static void handleSendText(Intent intent) 
	{
		//TODO not implemented yet
	    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
	    if (sharedText != null)
	    {
	        // Update UI to reflect text being shared
	    }
	}

	private static void handleSendImage(Intent intent, ContentResolver contentResolver) 
	{
	    Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	    if (uri != null) 
	    {
	    	 String scheme = uri.getScheme();
	         if (scheme.equals("content")) 
	         {
	           Cursor cursor = contentResolver.query(uri, null, null, null, null);
	           cursor.moveToFirst();
	           String fileName = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
	           String contentType = intent.getType();
	           long contentLength =  cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
	         }
	    }
	}
 
	private static void handleSendMultipleImages(Intent intent) 
	{
		//TODO not implemented yet
	    ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
	    if (imageUris != null) 
	    {
	        // Update UI to reflect multiple images being shared
	    }
	}
	//DELETE https://picasaweb.google.com/data/entry/api/user/userID/albumid/albumID/photoid/photoID/tag/tagID
	public static void deleteTag()
	{
		////Not implemented yet
	}
	/*
	 * POST https://picasaweb.google.com/data/feed/api/user/userID/albumid/albumID/photoid/photoID

		<entry xmlns='http://www.w3.org/2005/Atom'>
	  	<title>awesome</title>
	  	<category scheme="http://schemas.google.com/g/2005#kind"
	    term="http://schemas.google.com/photos/2007#tag"/>
			</entry>
	 		*/
	public static void addTag()
	{
		//Not implemented yet
	}
	//GET https://picasaweb.google.com/data/feed/api/user/userID?kind=tag
	public static void getTagList()
	{
		////Not implemented yet
	}
	//Works fine
	public static void uploadImageSlug(String userId, String albumId, Uri uri )
	{

		ContentResolver contentResolver = PicasaPlusApplication.getInstance().getContentResolver();  
		ImageItem item = getImageItem(uri);

		if(item != null)
		{
			try
			{
				HttpRequest request = PicasaUtil.getTransport().buildPostRequest();
				//String url = "http://picasaweb.google.com/data/feed/api/user/116633710550156986148/albumid/5827617605998541441";
				String url = PICASA_WEB_ALBUM_URL+"/"+userId+"/albumid/"+albumId;
				request.url = new GenericUrl(url);
				GoogleHeaders.setSlug(request.headers, item.imageName);
				InputStreamContent content = new InputStreamContent();
				content.inputStream = contentResolver.openInputStream(uri);
				content.type = item.imageType;
				content.length = item.imageSize;
				request.content = content;
				com.google.api.client.http.HttpResponse response = request.execute();
				int status = response.statusCode;
				if(status == HttpStatus.SC_OK || status == HttpStatus.SC_ACCEPTED)
				{
					//done uploading
					//System.out.println("done uploading");
				}
				else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
				{
					//ERROR
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} 

	}
	//http://vikaskanani.wordpress.com/2011/01/29/android-image-upload-activity/
	//http://vikaskanani.wordpress.com/2011/01/11/android-upload-image-or-file-using-http-post-multi-part/
	//works fine
	public static String uploadImage(String userId, String albumId, Uri uri)  
	{
		String result = "";
		try
		{
			//			ContentResolver contentResolver = PicasaPlusApplication.getInstance().getContentResolver();
			//			Cursor cursor = contentResolver.query(uri, null, null, null, null);
			//			cursor.moveToFirst();
			//			String fileName = cursor.getString(cursor .getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
			//			String contentType = "image/jpeg";//intent.getType();
			//			long contentLength = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));

			ImageItem item = getImageItem(uri);
			if(item != null)
			{

				String url = PICASA_WEB_ALBUM_URL +"/"+userId+"/albumid/"+  albumId  ;

				String metadata = "<entry xmlns='http://www.w3.org/2005/Atom'>"+
						"<title>"+ item.imageName +"</title>"+
						"<category scheme=\"http://schemas.google.com/g/2005#kind\""+
						" term=\"http://schemas.google.com/photos/2007#photo\"/></entry>";
				AtomMetaDataHttpContent metadataContent = new AtomMetaDataHttpContent(metadata);


				InputStreamContent content = new InputStreamContent(); 
				content.type = item.imageType;
				File imageFile = new File(item.imagePath); 
				content.setFileInput(imageFile);

				MultipartContent multipart = new MultipartContent(metadataContent, content);

				HttpRequest request = PicasaUtil.getTransport().buildPostRequest();
				request.url = new GenericUrl(url);
				multipart.forRequest(request);


				//for testing
				//			Multipart multipart2  = new Multipart("Media multipart posting", "END_OF_PART");
				//
				//			// create entity parts
				//			multipart2.addPart("<entry xmlns='http://www.w3.org/2005/Atom'><title>"+imageFile.getName()+"</title><category scheme=\"http://schemas.google.com/g/2005#kind\" term=\"http://schemas.google.com/photos/2007#photo\"/></entry>", "application/atom+xml");
				//			multipart2.addPart(imageFile, "image/jpeg");
				//			
				//			String result1 = new String(multipart.getContent());
				//			String result2 = new String(multipart2.getContent());

				com.google.api.client.http.HttpResponse response = request.execute();
				int status = response.statusCode;
				result = ""+status;
				if(status == HttpStatus.SC_OK || status == HttpStatus.SC_ACCEPTED)
				{
					//done uploading
					//System.out.println("done uploading");
				}
				else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
				{
					//ERROR
				}
			}
		}
		catch(Exception e)
		{
			//Log.e(TAG, "Error! "+e);
			result = e.getMessage();
		}

		return result;

	}
	public static ImageItem getImageItem(Uri uri)
	{
		try
		{
			String scheme = uri.getScheme();
			if (scheme.equals("content"))
			{
				ContentResolver contentResolver = PicasaPlusApplication.getInstance().getContentResolver();
				Cursor cursor = contentResolver.query(uri, null, null, null, null);
				cursor.moveToFirst();
				ImageItem item = new ImageItem();
				item.imageId   = cursor.getLong(cursor.getColumnIndexOrThrow(Media._ID));
				item.imagePath = cursor.getString(cursor.getColumnIndexOrThrow(Media.DATA));
				item.imageName = cursor.getString(cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME));
				item.imageType = cursor.getString(cursor.getColumnIndexOrThrow(Media.MIME_TYPE));
				item.imageSize = cursor.getLong(cursor.getColumnIndexOrThrow(Media.SIZE));
				return item;
			}
			else if(scheme.equals("file"))
			{
				ImageItem item = new ImageItem();
				File imageFile = new File(uri.getPath()); 
				item.imagePath = imageFile.getPath();
				item.imageName = imageFile.getName();
				item.imageType = getMimeType(uri.toString());
				item.imageSize = imageFile.length();
				return item;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static String getMimeType(String url)
	{
	    String type = "image/jpeg";//default
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
	 
}
