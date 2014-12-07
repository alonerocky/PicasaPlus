package dev.shoulongli.picasaplus;

import java.io.File;


import dev.shoulongli.picasaplus.auth.PicasaAuthCallback;
import dev.shoulongli.picasaplus.auth.oauth.PicasaAuthenticatorOAuth;
import dev.shoulongli.picasaplus.picasa.util.PicasaWebAlbum;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PicasaPlusTestActivity extends Activity implements PicasaAuthCallback
{
	private TextView result;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_test);

		result = (TextView)findViewById(R.id.result);
		Button createAlbum = (Button)findViewById(R.id.createAlbum);
		createAlbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new CreateAlbumTask("lishoulong").execute( "test2");
			}
		});
		Button delete = (Button)findViewById(R.id.deleteAlbum);
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new DeleteAlbumTask("lishoulong").execute("test1");
			}
		});
		Button upload = (Button)findViewById(R.id.uploadImage);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				pickImage();
				//new UploadImageTask("lishoulong","test 1").execute("test 1");
			}
		});
		PicasaAuthenticatorOAuth.getInstance().setCallback(this);
		PicasaAuthenticatorOAuth.getInstance().setActivity(this);
		PicasaAuthenticatorOAuth.getInstance().auth(false); 

	}
	//public static final int PICK_UP_FILE_UPLOAD = 1;
	public static final int PICK_FROM_FILE = 2;
	public static final int PICK_FROM_CAMERA  = 3;
	private Uri mImageCaptureUri ;
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
	public final String getImagePathTemp()
	{
		String path	= Environment.getExternalStorageDirectory()+ File.separator + 
				"picasaplus" + File.separator + "images";		
		return path;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK) 
			return;

		String path = null;
		switch(requestCode)
		{
		case PICK_FROM_FILE:
			if(data == null)
				return;
			mImageCaptureUri = data.getData();
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery
			System.out.println(path);
			if (path == null)
				path = mImageCaptureUri.getPath(); //from File Manager

//			if (path != null)
//				bitmap  = BitmapFactory.decodeFile(path);
			break;
		case PICK_FROM_CAMERA:
			if(mImageCaptureUri == null)
				return;
			path    = mImageCaptureUri.getPath();
			System.out.println(path);
			//bitmap  = BitmapFactory.decodeFile(path);
			break;
		}
		if(path != null)
		{
			new UploadImageTask("116633710550156986148l","5827617605998541441").execute(mImageCaptureUri);
		}
		
	}


	public String getRealPathFromURI(Uri contentUri)  
	{
		String [] proj      = {MediaStore.Images.Media.DATA};
		Cursor cursor       = managedQuery( contentUri, proj, null, null,null);

		if (cursor == null) 
			return null;

		int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}



	public void authenticated()
	{
		result.setText("authenticated");
	}
	public void failure(int status,String message)
	{
		//
	}
	class DeleteAlbumTask extends AsyncTask<String,Void,String>
	{
		private String userId; 
		public DeleteAlbumTask(String userId )
		{
			this.userId = userId; 
		}
		@Override
		public String doInBackground(String... parameters)
		{
			if(parameters != null && parameters.length > 0)
			{
				for(int i = 0;i < parameters.length;i++)
				{
					String albumName = parameters[i];
					PicasaWebAlbum.deleteAlbum(userId, "5792261972374816833");
				}
			}
			return "";

		}
		@Override
		public void onPostExecute(String status)
		{
			result.setText(status);
		}
	}
	class CreateAlbumTask extends AsyncTask<String,Void,String>
	{
		private String userId; 
		public CreateAlbumTask(String userId )
		{
			this.userId = userId; 
		}
		@Override
		public String doInBackground(String... parameters)
		{
			if(parameters != null && parameters.length > 0)
			{
				for(int i = 0;i < parameters.length;i++)
				{
					String albumName = parameters[i];
					PicasaWebAlbum.createAlbum(userId, albumName);
				}
			}
			return ""; 
		}
		@Override
		public void onPostExecute(String status)
		{
			result.setText(status);
		}
	}
	class UploadImageTask extends AsyncTask<Uri,Void,String>
	{
		private String userId;
		private String albumId;
		public UploadImageTask(String userId, String albumId )
		{
			this.userId = userId;
			this.albumId = albumId;
		}
		@Override
		public String doInBackground(Uri... parameters)
		{
			if(parameters != null && parameters.length > 0)
			{
				for(int i = 0;i < parameters.length;i++)
				{

					Uri uri = parameters[i];
					try
					{
						if(uri != null)
							PicasaWebAlbum.uploadImage(userId,albumId, uri );
					}
					catch(Exception e)
					{

					} 
					 
				}
			}
			return "";  
		}
		@Override
		public void onPostExecute(String status)
		{
			result.setText(status);
		}
	} 
}
