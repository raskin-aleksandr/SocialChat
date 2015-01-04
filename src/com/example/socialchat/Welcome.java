package com.example.socialchat;

import java.util.List;
import java.util.Vector;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Welcome extends Activity implements LocationListener {

	private MapView mMapView;
	private GeoPoint mGeoPoint;
	MyItemizedOverlay myItemizedOverlay;
	MyFriendsItemizedOverlay myFriendsItemizedOverlay;

	private LocationManager mLocationManager;
	private Location mLocation;

	private String locationID;

	private ProgressDialog pd;

	final Vector<OverlayItem> myLocationOverlayItemArray = new Vector<OverlayItem>();
	final Vector<OverlayItem> myFriendsLocationOverlayItemArray = new Vector<OverlayItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		ParsePush.subscribeInBackground("A" + User.getInstance().getmUser().getObjectId());

		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(16);

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (mLocation != null) {

			mGeoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
			mMapView.getController().animateTo(mGeoPoint);
			locationID = User.getInstance().getLocationID();
		} else {
			Toast.makeText(getApplicationContext(), "no gps", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.findme:
			findMe();
			mMapView.getController().setZoom(16);
			mMapView.getController().animateTo(mGeoPoint);
			mMapView.invalidate();
			break;

		case R.id.findfriends:
			findFriends();
			break;

		case R.id.logout:
			ParseUser.getCurrentUser().logOut();

			SharedPreferences sp = getSharedPreferences("socialchat", MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();

			editor.putString("userID", null);
			editor.putString("locatoinID", null);
			System.out.println("from logout: cleared");
			editor.commit();

			Welcome.this.finish();
			startActivity(new Intent(getApplicationContext(), MainActivity.class));

			break;

		case R.id.settings:
			startActivity(new Intent(getApplicationContext(), Settings.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Exit Application?");

		alertDialogBuilder.setMessage("Click yes to exit!");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				SharedPreferences sp = getSharedPreferences("socialchat", MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();

				editor.putString("userID", User.getInstance().getUserID());
				editor.putString("locatoinID", User.getInstance().getLocationID());
				System.out.println("from shared on stop: saved");
				editor.commit();

				finish();
			}
		});
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	public void findMe() {
		mMapView.getOverlays().remove(myItemizedOverlay);
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		locationID = User.getInstance().getLocationID();

		if (mLocation != null) {
			mGeoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());

			OverlayItem olItem = new OverlayItem("You are here", "current location", mGeoPoint);

			Drawable newMarker = getResources().getDrawable(R.drawable.ic_communication_location_on_blue);
			olItem.setMarker(newMarker);

			myLocationOverlayItemArray.clear();
			myLocationOverlayItemArray.add(olItem);

			myItemizedOverlay = new MyItemizedOverlay(this, myLocationOverlayItemArray);
			// mMapView.getOverlays().clear();

			mMapView.getOverlays().add(myItemizedOverlay);

			final ParseGeoPoint parsePoint = new ParseGeoPoint(mGeoPoint.getLatitude(), mGeoPoint.getLongitude());

			ParseQuery<ParseObject> query = ParseQuery.getQuery("location");
			query.getInBackground(locationID, new GetCallback<ParseObject>() {

				@Override
				public void done(ParseObject position, ParseException e) {
					if (e == null) {
						position.put("location", parsePoint);
						position.saveInBackground();
					} else {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
						System.out.println("from find me: " + e.getMessage() + ", id: " + locationID);
					}
				}
			});

		} else {
			Toast.makeText(getApplicationContext(), "no gps", Toast.LENGTH_SHORT).show();
		}

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
	}

	public void findFriends() {
		if (mLocation != null) {
			pd = new ProgressDialog(this);
			pd.setTitle("Friends");
			pd.setMessage("Seraching for friends");
			pd.setIcon(R.drawable.ic_communication_location_on_red);
			pd.show();

			mMapView.getOverlays().clear();

			ParseQuery<ParseObject> pq = ParseQuery.getQuery("location");
			ParseGeoPoint location = new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
			pq.whereWithinKilometers("location", location, 1000.0);
			pq.setLimit(10);
			pq.whereNotEqualTo("objectId", User.getInstance().getLocationID());

			myFriendsItemizedOverlay = new MyFriendsItemizedOverlay(this, myFriendsLocationOverlayItemArray);

			pq.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {

						myFriendsLocationOverlayItemArray.clear();
						for (ParseObject parseObject : objects) {
							ParseGeoPoint friendlocation = parseObject.getParseGeoPoint("location");
							GeoPoint friend = new GeoPoint(friendlocation.getLatitude(), friendlocation.getLongitude());

							OverlayItem olItem = new OverlayItem(parseObject.getString("name"), "current friend location", friend);

							Drawable newMarker = getResources().getDrawable(R.drawable.ic_communication_location_on_red);
							olItem.setMarker(newMarker);

							myFriendsLocationOverlayItemArray.add(olItem);
							// mMapView.getOverlays().add(myItemizedOverlay);

							pd.cancel();
						}
					} else {
						System.out.println("find fuck: " + e);
						pd.cancel();
					}
				}
			});
			mMapView.getOverlays().add(myFriendsItemizedOverlay);
			mMapView.invalidate();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		findMe();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}
