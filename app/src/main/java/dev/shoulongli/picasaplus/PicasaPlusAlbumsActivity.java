package dev.shoulongli.picasaplus;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import dev.shoulongli.appframework.network.VolleyWrapper;
import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;
import dev.shoulongli.picasaplus.picasa.util.DeleteAlbumTask;
import dev.shoulongli.picasaplus.picasa.util.PicasaUtil;
import dev.shoulongli.picasaplus.picasa.util.PicasaWebAlbum;
import dev.shoulongli.picasaplus.picasa.util.UploadImageTask;
import dev.shoulongli.picasaplus.ui.AlbumListAdapter;
import dev.shoulongli.picasaplus.xml.RssFeedParserFactory;
import dev.shoulongli.picasaplus.xml.RssFeedParserType;



import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PicasaPlusAlbumsActivity extends ListActivity 
{


	private static final String TAG = PicasaPlusAlbumsActivity.class.getName(); 
	public static final String ENCODING = "utf-8";

	public static final int REQUEST_OAUTH = 1;
	public static final int PICK_FROM_FILE = 2;
	public static final int PICK_FROM_CAMERA  = 3;
	private Uri mImageCaptureUri ;
	//private AccountManager accountManager;
	/**
	 * Shared buffer used by {@link #getUrlContent(String)} when reading results
	 * from an API request.
	 */
	private ArrayList<AlbumEntry> albums;
	private LinearLayout progressIndicator;


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.albums);
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
		ImageView add = (ImageView)findViewById(R.id.newalbum);
		progressIndicator = (LinearLayout)findViewById(R.id.progress_indicator);
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});
		ImageView upload = (ImageView)findViewById(R.id.upload);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub

			}
		});
		ImageView settings = (ImageView)findViewById(R.id.settings);
		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub

			}
		});
		executeRefreshAlbums();

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.albumsoptions, menu);
		return true;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		menu.setHeaderTitle("Options"); 
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.albumscontext, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{

		case R.id.about:
			showAbout();
			break;
		}
		return true;
	}
	private void showAbout()
	{
		Intent intent = new Intent(PicasaPlusAlbumsActivity.this, PicasaPlusAboutActivity.class);
		startActivity(intent);
	}
	private void delete(int position)
	{

		if(albums != null && position >= 0 && position < albums.size())
		{
			AlbumEntry album = albums.get(position);
			if(album != null)
			{
				String albumId = ""+ album.getId( );
				String userId = ""+ album.getUser( );
				new DeleteAlbumTask(userId).execute(albumId);
			}
		} 
	}
	public final String getImagePathTemp()
	{
		String path	= Environment.getExternalStorageDirectory()+ File.separator + 
				"picasaplus" + File.separator + "images";		
		return path;
	}
	public void pickImage()
	{
		final String [] items           = new String [] {"From Camera", "From SD Card"};
		ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder     = new AlertDialog.Builder(this);

		builder.setTitle("Upload Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) 
				{

					Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					String path =  getImagePathTemp();
					File dir = new File(path);
					if (!dir.exists()) 
					{
						dir.mkdirs();
					}

					File file        = new File(dir, "picasaplus_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try 
					{
						intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						//intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}

					dialog.cancel();
				}
				else 
				{
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent, "Choose one image"), PICK_FROM_FILE);
				}
			}
		} );

		final AlertDialog dialog = builder.create(); 

		dialog.show(); 
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		if(albums != null)
		{
			AlbumEntry album = albums.get(position);
			if(album != null)
			{
				Intent intent = new Intent(this, AlbumPhotosActivity.class);
				intent.putExtra(AlbumPhotosActivity.KEY_ALBUM_ID, album.getId());
				intent.putExtra(AlbumPhotosActivity.KEY_USER_ID, album.getUser());
				startActivity(intent);
			}
		}
	} 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK) 
			return;

		switch(requestCode)
		{
		case PICK_FROM_FILE:
			if(data == null)
				return;
			mImageCaptureUri = data.getData(); 
			break;
		case PICK_FROM_CAMERA:
			if(mImageCaptureUri == null)
				return; 
			break;
		case REQUEST_OAUTH:
			this.executeRefreshAlbums();
			break;
		}
		if(mImageCaptureUri != null)
		{
			//116633710550156986148
			new UploadImageTask("lishoulong","default").execute(mImageCaptureUri);
		}

	}



	private void executeRefreshAlbums() 
	{

		//		setListAdapter(new ArrayAdapter<String>(this,
		//				android.R.layout.simple_list_item_1, albumNames));

		String fields = null;
		try {
			fields = URLEncoder.encode(PicasaWebAlbum.ALBUM_FIELDS, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = PicasaWebAlbum.PICASA_WEB_ALBUM_URL +"/lishoulong@gmail.com";
		if(fields != null)
		{
			url += "?kind=album&alt=json&v=2.0&fields=" + fields;
		}

		new RefreshAlbumsTask().execute(url); 

	} 
	
	public void refreshAlbums(ArrayList<AlbumEntry> alumList)
	{
		albums = alumList;//RssFeedParserFactory.getParser(RssFeedParserType.SAX).getAlbums(response.getContent());
		AlbumListAdapter adapter = new AlbumListAdapter(this, R.layout.album_list_item, albums);
		this.setListAdapter(adapter);
	}
	class RefreshAlbumsTask extends AsyncTask<String, Void, ArrayList<AlbumEntry>>
	{
		@Override
		public ArrayList<AlbumEntry> doInBackground(String... parameters)
		{
			String url = parameters[0];
			
			HttpRequest request = PicasaUtil.getTransport().buildGetRequest();
			request.url = new GenericUrl(url);

// Preparing volley's json object request
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "Albums Response: " + response.toString());




                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Volley Error: " + error.getMessage());



                }
            });

            // disable the cache for this request, so that it always fetches updated
            // json
            jsonObjReq.setShouldCache(false);
            VolleyWrapper.getInstance(PicasaPlusAlbumsActivity.this).getRequestQueue().add(jsonObjReq);
			ArrayList<AlbumEntry> result = new ArrayList<AlbumEntry>();
//			try
//			{
//				com.google.api.client.http.HttpResponse response = request.execute();
//				int status = response.statusCode;
//				if(status == HttpStatus.SC_OK)
//				{
//					result = RssFeedParserFactory.getParser(RssFeedParserType.SAX).getAlbums(response.getContent());
//				}
//				else if(status == HttpStatus.SC_FORBIDDEN || status == HttpStatus.SC_UNAUTHORIZED )
//				{
//
//				}
//			}
//			catch(Exception e)
//			{
//				Log.e(TAG, "Error! "+e);
//			}
			return result;
		}
		@Override 
		public void onPostExecute(ArrayList<AlbumEntry> result)
		{
			progressIndicator.setVisibility(View.GONE);
			refreshAlbums(result);
		}
	}

}
