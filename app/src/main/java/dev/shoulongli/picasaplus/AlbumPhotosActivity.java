package dev.shoulongli.picasaplus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpStatus;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;
import dev.shoulongli.picasaplus.picasa.model.PhotoEntry;
import dev.shoulongli.picasaplus.picasa.util.DeleteAlbumTask;
import dev.shoulongli.picasaplus.picasa.util.DeletePhotoTask;
import dev.shoulongli.picasaplus.picasa.util.PicasaUtil;
import dev.shoulongli.picasaplus.picasa.util.PicasaWebAlbum;
import dev.shoulongli.picasaplus.ui.PhotoGridAdapter;
import dev.shoulongli.picasaplus.xml.RssFeedParserFactory;
import dev.shoulongli.picasaplus.xml.RssFeedParserType;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import dev.shoulongli.picasaplus.R;

public class AlbumPhotosActivity extends Activity 
{
	private static final String TAG = AlbumPhotosActivity.class.getName();
	private static final String PREF = "MyPrefs";
	public static final String KEY_ALBUM_ID = "KEY_ALBUM_ID";
	public static final String KEY_USER_ID = "KEY_USER_ID";
	private long albumId;
	private String userId;
	public static final String ENCODING = "utf-8";
	private ArrayList<PhotoEntry> photos;
	private GridView photoGrid;
	private byte[] sBuffer = new byte[512];
	private Handler handler = new Handler();
	//https://picasaweb.google.com/data/feed/api/user/userID/albumid/albumID
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_photos);
		photoGrid = (GridView)findViewById(R.id.photos);
		Bundle extras = this.getIntent().getExtras();
		if(extras != null)
		{
			albumId = extras.getLong(KEY_ALBUM_ID);
			userId = extras.getString(KEY_USER_ID);		
			loadPhotos();
		}
		this.registerForContextMenu(photoGrid);
	}


	private void loadPhotos()
	{


		//		setListAdapter(new ArrayAdapter<String>(this,
		//				android.R.layout.simple_list_item_1, albumNames));

		String fields = null;
		try {
			fields = URLEncoder.encode(PicasaWebAlbum.PHOTO_FIELDS, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//https://picasaweb.google.com/data/feed/api/user/userID/albumid/albumID
		String url = PicasaWebAlbum.PICASA_WEB_ALBUM_URL +"/"+userId+"/albumid/"+albumId; 
		if(fields != null)
		{
			url += "?fields=" + fields;
		}
		executeGet_OAuth(url); 

	}

	private void  executeGet_OAuth(String url)
	{
		HttpRequest request = PicasaUtil.getTransport().buildGetRequest();
		request.url = new GenericUrl(url);
		try
		{
			com.google.api.client.http.HttpResponse response = request.execute();
			int status = response.statusCode;
			if(status == HttpStatus.SC_OK)
			{
				photos = RssFeedParserFactory.getParser(RssFeedParserType.SAX).getPhotos(response.getContent());
				PhotoGridAdapter adapter = new PhotoGridAdapter(this,R.layout.album_photo_item,photos);
				photoGrid.setAdapter(adapter);
			}
			else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
			{
				//ERROR
			}
		}
		catch(Exception e)
		{

		}
	}
	
	@Override
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		menu.setHeaderTitle("Options"); 
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.photoscontext, menu);
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{ 
		AdapterContextMenuInfo info;
		switch(item.getItemId())
		{
		case R.id.delete:
			info = (AdapterContextMenuInfo)item.getMenuInfo();			
			delete(info.position);
			break;
		}
		return true;
	}
	private void delete(int index)
	{

		if(this.photos != null && index >= 0 && index < photos.size())
		{
			PhotoEntry photo = photos.get(index);
			if(photo != null)
			{				 
				new DeletePhotoTask(userId,""+albumId).execute(photo.getId()+"");
			}
		}  
	}

}
