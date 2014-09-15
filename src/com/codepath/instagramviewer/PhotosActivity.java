package com.codepath.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import comcodepath.instagramviewer.R;

public class PhotosActivity extends Activity {
	
	public static final String CLIENT_ID = "bab1bc1ae7ed46bfb75660aa987a54c7";
	ArrayList<InstagramPhoto> photos;
	InstagramPhotosAdapter adpaterPhotos;
	ListView lvPhotos;

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
		lvPhotos.setAdapter(adpaterPhotos);
		
		fetchPopularPhotos();
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
						photos.add(photo);
					}
					// Notify the adapter to populate the list view
					adpaterPhotos.notifyDataSetChanged();
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				
				

			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
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
