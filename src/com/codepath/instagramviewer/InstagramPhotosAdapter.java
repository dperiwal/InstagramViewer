package com.codepath.instagramviewer;

import java.util.ArrayList;
import java.util.Date;

import com.squareup.picasso.Picasso;

import comcodepath.instagramviewer.R;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		TextView tvCreatedTime;
		TextView tvLikesCount;
		LinearLayout llCommentsCount;
		TextView tvCommentsCount;
		TextView tvComment1;
		TextView tvComment2;
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
			viewHolder.tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);
			viewHolder.tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
		
			viewHolder.llCommentsCount = (LinearLayout) convertView.findViewById(R.id.llCommentsCount);
			viewHolder.tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentsCount);
			viewHolder.tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
			viewHolder.tvComment2 = (TextView) convertView.findViewById(R.id.tvComment2);
			
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
		
		// Picasso.with(getContext()).load(photo.profileUrl).into(viewHolder.imgProfile);
		
		Picasso.with(getContext()).load(photo.profileUrl).placeholder(R.drawable.ic_launcher).into(viewHolder.imgProfile); 
		
		Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.imgPhoto);

		// Set the caption
		viewHolder.tvCaption.setText(photo.caption);
		
		// Set the user
		viewHolder.tvUserName.setText(photo.userName);
		
		// Set the location
		viewHolder.tvLocation.setText(photo.location);
		
		// Set the relative created time	
		viewHolder.tvCreatedTime.setText(DateUtils.getRelativeTimeSpanString(
				photo.createdTime * 1000, new Date().getTime(), DateUtils.MINUTE_IN_MILLIS));
		
		// Set the likes count
		viewHolder.tvLikesCount.setText(Integer.valueOf(photo.likesCount).toString());
		
/*		// Set the comments section
		if (photo.commentsCount == 0) {
			// Hide away all the comments areas
			viewHolder.llCommentsCount.setVisibility(View.GONE);
			viewHolder.llComment1.setVisibility(View.GONE);
			viewHolder.llComment2.setVisibility(View.GONE);
		} else if (photo.commentsCount > 0) {
			viewHolder.llCommentsCount.setVisibility(View.VISIBLE);
			viewHolder.tvCommentsCount.setText(Integer.valueOf(photo.commentsCount).toString());
			
			// Show the first comment
			viewHolder.llComment1.setVisibility(View.VISIBLE);
			viewHolder.tvCommenter1.setText(photo.commenter1);
			viewHolder.tvComment1.setText(photo.comment1);
			
			if (photo.commentsCount > 1) {
				// Show the second comment
				viewHolder.llComment2.setVisibility(View.VISIBLE);
				viewHolder.tvCommenter2.setText(photo.commenter2);
				viewHolder.tvComment2.setText(photo.comment2);				
			} else {
				viewHolder.llComment2.setVisibility(View.GONE);
			}
		}*/
		
		// Set the comments section
		if (photo.commentsCount == 0) {
			// Hide away all the comments areas
			viewHolder.llCommentsCount.setVisibility(View.GONE);
			viewHolder.tvComment1.setVisibility(View.GONE);
			viewHolder.tvComment2.setVisibility(View.GONE);
		} else if (photo.commentsCount > 0) {
			viewHolder.llCommentsCount.setVisibility(View.VISIBLE);
			viewHolder.tvCommentsCount.setText(Integer.valueOf(photo.commentsCount).toString());
			
			// Show the first comment
			viewHolder.tvComment1.setVisibility(View.VISIBLE);
			String formattedComment = "<font color=\"#0066CC\">" + "<b>" + 
					photo.commenter1 + "</b> </font>" + photo.comment1 ;
			viewHolder.tvComment1.setText((Html.fromHtml(formattedComment)));
			
			if (photo.commentsCount > 1) {
				// Show the second comment
				viewHolder.tvComment2.setVisibility(View.VISIBLE);
				formattedComment = "<font color=\"#0066CC\">" + "<b>" + 
						photo.commenter2 + "</b> </font>" + photo.comment2 ;
				viewHolder.tvComment2.setText((Html.fromHtml(formattedComment)));		
			} else {
				viewHolder.tvComment2.setVisibility(View.GONE);
			}
		}
		
		// Return the completed view to render on screen
		return convertView;
	}

}
