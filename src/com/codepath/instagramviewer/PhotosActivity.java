package com.codepath.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import comcodepath.instagramviewer.R;

/**
 * An app to view popular photos and their attributes from Instagram.
 * <p>
 * @author Damodar Periwal
 *
 */
public class PhotosActivity extends Activity {
	
	public static final String CLIENT_ID = "bab1bc1ae7ed46bfb75660aa987a54c7";
	ArrayList<InstagramPhoto> photos;
	InstagramPhotosAdapter adpaterPhotos;
	ListView lvPhotos;	
	private SwipeRefreshLayout swipeContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
			
		// Initialize the photos array
		photos = new ArrayList<InstagramPhoto>(); 
		// Create adapter and bind it to the data in the array list
		adpaterPhotos = new InstagramPhotosAdapter(this, photos);
		// populate the data in the list view
		lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		// Doing the following to avoid the error View too large to fit into drawing cache,
		// needs 1618560 bytes, only 1536000 available
		// lvPhotos.setScrollingCacheEnabled(false);  
		lvPhotos.setAdapter(adpaterPhotos);
		
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				fetchPopularPhotos();
			}
		});
		
		fetchPopularPhotos(); // for the first time
	}

	private void fetchPopularPhotos() {
		// https://api.instagram.com/v1/media/popular?client_id=<clintId>
		// { "data" => [x] => "images" => "standard_resolution" => "url" }
		
		// Setup popular url endpoint
		String popularURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
		
		// Create the network client
		AsyncHttpClient client = new AsyncHttpClient();
		// Handle the succesful response (popular photos JSON)
		client.get(popularURL, new JsonHttpResponseHandler() {
			// define success and failure callbacks
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				photos.clear(); // start fresh
				// fired once the successful response comes back
				// response object == popular photos json
				// "data" => [x] => "images" => "standard_resolution" => "url"
				// "data" => [x] => "images" => "standard_resolution" => "height"
				// "data" => [x] => "user" => "username"
				// "data" => [x] => "caption" => "text"
				// "data" => [x] => "likes" => "count"
				// "data" => [x] => "comments" => "count"
				// "data" => [x] => "comments" => "data" => [x] => "from" => "username"
				// "data" => [x] => "comments" => "data" => [x] => text
				JSONArray photosJSON = null;
				try {
					photosJSON = response.getJSONArray("data");
					// Log.i("INFO", "Number of images = " + photosJSON.length());
					for (int i = 0; i < photosJSON.length(); i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i); // 0,1, 2, 3...
						if (null == photoJSON) {
							Log.i("INFO", "photoJSON is null for index " + i);
							continue;
						}
						InstagramPhoto photo = new InstagramPhoto();
						photo.userName = photoJSON.getJSONObject("user").getString("username");
						
						if (!photoJSON.isNull("location")) {
							JSONObject locationJSON =  photoJSON.getJSONObject("location");
							if (!locationJSON.isNull("name")) {	
							   photo.location = locationJSON.getString("name");
							}
						}
						
						photo.createdTime = photoJSON.getLong("created_time");
						
						if (!photoJSON.isNull("caption")) {
							photo.caption = photoJSON.getJSONObject("caption").getString("text");
						}
						
						photo.profileUrl = photoJSON.getJSONObject("user").getString("profile_picture");
						photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
						photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
						photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
						
						JSONObject commentsJSON = photoJSON.getJSONObject("comments");
						photo.commentsCount = commentsJSON.getInt("count");
						if (photo.commentsCount > 0) {
							JSONArray commentsDataJSON = commentsJSON.getJSONArray("data");
							photo.commenter1 = commentsDataJSON.getJSONObject(0).getJSONObject("from").getString("username");
							photo.comment1 = commentsDataJSON.getJSONObject(0).getString("text");
							if (photo.commentsCount > 1) {
								photo.commenter2 = commentsDataJSON.getJSONObject(1).getJSONObject("from").getString("username");
								photo.comment2 = commentsDataJSON.getJSONObject(1).getString("text");
							}
						}		
						
						photos.add(photo);
					}				
					// Notify the adapter to populate the list view
					adpaterPhotos.notifyDataSetChanged();
					
				} catch (JSONException ex) {
					ex.printStackTrace();
				} finally {
					Log.i("INFO", "Photo count=" + photos.size());
					swipeContainer.setRefreshing(false);
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Log.e("Instagram Service", responseString);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.e("Instagram Service", errorResponse.toString());
			}				
		});				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
