package dev.shoulongli.picasaplus.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dev.shoulongli.picasaplus.R;
import dev.shoulongli.picasaplus.image.ImageDownloadManager;
import dev.shoulongli.picasaplus.picasa.model.AlbumEntry;

public class AlbumListAdapter extends ArrayAdapter<AlbumEntry>
{
	private ArrayList<AlbumEntry> albums;
	private Context context;
	private int textViewResourceId;
	public AlbumListAdapter(Context context, int textViewResourceId, ArrayList<AlbumEntry> albums)
	{
		super(context, textViewResourceId, albums);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.albums = albums;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		AlbumEntryViewHolder holder = null;
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new AlbumEntryViewHolder();
			v = inflater.inflate(this.textViewResourceId, null);
			holder.thumbnailView = (ImageView)v.findViewById(R.id.albumthumbnail);;
			holder.titleView = (TextView)v.findViewById(R.id.title);
			v.setTag(holder);
		}
		else
		{
			holder = (AlbumEntryViewHolder)v.getTag();
		}
		AlbumEntry album = albums.get(position);
		holder.titleView.setText(album.getTitle());
		if(album.getThumbnailurl() != null)
		{
			ImageDownloadManager.getInstance().download(album.getThumbnailurl(), holder.thumbnailView);
		}
		else
		{
			//holder.thumbnailView.setImageBitmap(null);
		}
		
		return v;
	}
	class AlbumEntryViewHolder
	{
		ImageView thumbnailView;
		TextView  titleView;
	}
	 
}
