package com.codepath.instagramviewer;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import comcodepath.instagramviewer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

	public InstagramPhotosAdapter(Context context,
			ArrayList<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	// View lookup cache
	private static class ViewHolder {
		ImageView imgProfile;
		ImageView imgPhoto;
		TextView tvCaption;
		TextView tvUserName;
		TextView tvLocation;
		TextView tvLikesCount;
	}

	// Takes a data item at a position, converts it to a row in the list view
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		InstagramPhoto photo = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_photo, parent, false);
			viewHolder.imgProfile = (ImageView) convertView.findViewById(R.id.imgProfile);
			viewHolder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
			viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
			viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
			viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
			viewHolder.tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			// reset the image from the recycled view
			viewHolder.imgPhoto.setImageResource(0);
			viewHolder.tvLocation.setText("");
		}
		// Populate the data into the template view using the data object
		
		// Set the image height
		viewHolder.imgPhoto.getLayoutParams().height = photo.imageHeight;
		// Background: send the network request to the photo and profile urls, download the
		// images, convert them into bitmaps, resize the images, insert the bitmaps
		// into the image views
		Picasso.with(getContext()).load(photo.profileUrl).into(viewHolder.imgProfile);
		Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.imgPhoto);

		// Set the caption
		viewHolder.tvCaption.setText(photo.caption);
		
		// Set the user
		viewHolder.tvUserName.setText(photo.userName);
		
		// Set the location
		viewHolder.tvLocation.setText(photo.location);
		
		// Set the likes count
		viewHolder.tvLikesCount.setText(Integer.valueOf(photo.likesCount).toString());
		
		// Return the completed view to render on screen
		return convertView;
	}

}
