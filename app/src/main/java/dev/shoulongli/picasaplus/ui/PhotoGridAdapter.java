package dev.shoulongli.picasaplus.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import dev.shoulongli.picasaplus.R;
import dev.shoulongli.picasaplus.image.ImageDownloadManager;
import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;
import dev.shoulongli.picasaplus.picasa.model.PhotoEntry;
import dev.shoulongli.picasaplus.picasa.util.PicasaWebAlbum;

public class PhotoGridAdapter extends ArrayAdapter<PhotoEntry>
{
	private Context context;
	private int textViewResourceId;
	private ArrayList<PhotoEntry> photos;
	public PhotoGridAdapter(Context context, int textViewResourceId, ArrayList<PhotoEntry> photos)
	{
		super(context,textViewResourceId,photos);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.photos = photos;
	}
	
	@Override
	public View getView(int position,View convertView, ViewGroup parent)
	{
		View v = convertView;
		PhotoEntryViewHolder holder;
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
			v = inflater.inflate(this.textViewResourceId, null);
			holder = new PhotoEntryViewHolder();
			holder.photoEntryView = (ImageView)v.findViewById(R.id.photoentry);
			v.setTag(holder);
		}
		else 
		{
			holder =  (PhotoEntryViewHolder)v.getTag();
		}
		if(photos != null)
		{
			PhotoEntry photo = photos.get(position);
			String url = photo.getUrl();
			
			if(url  != null)
			{
				ImageDownloadManager.getInstance().download(url, holder.photoEntryView);
			}
			else
			{
				holder.photoEntryView.setImageResource(R.drawable.ic_gallery_empty2);
			}
		}
		
		return v;
	}
	class PhotoEntryViewHolder
	{
		ImageView photoEntryView;
	}
}
