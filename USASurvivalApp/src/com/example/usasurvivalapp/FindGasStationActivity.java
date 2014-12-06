package com.example.usasurvivalapp;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usasurvivalapp.helper.GasFeedRequestTask;
import com.example.usasurvivalapp.my_gas_feed.model.Station;

/**
 * Starts the search of the gas stations.
 * Gets the search location by intent bundle extras.
 * Uses the GasFeedRequestTask to handle the load process.
 * Uses the StationsAdapter to generate the list content.
 * 
 * @author Chris
 *
 */
public class FindGasStationActivity extends Activity {

	StationsAdapter adapter = null;

	// Bundle Strings
	public static final String FINDGASSTATION_EXTRA_DISTANCE = "FINDGASSTATION_EXTRA_DISTANCE";
	public static final String FINDGASSTATION_EXTRA_FUELTYPE = "FINDGASSTATION_EXTRA_FUELTYPE";
	public static final String FINDGASSTATION_EXTRA_SORTBY = "FINDGASSTATION_EXTRA_SORTBY";
	public static final String FINDGASSTATION_EXTRA_LAT = "FINDGASSTATION_EXTRA_LAT";
	public static final String FINDGASSTATION_EXTRA_LNG = "FINDGASSTATION_EXTRA_LNG";

	// API Strings
	public static final String MYGASFEED_API_FUELTYPE_REG = "reg";
	public static final String MYGASFEED_API_FUELTYPE_MID = "mid";
	public static final String MYGASFEED_API_FUELTYPE_PRE = "pre";
	public static final String MYGASFEED_API_FUELTYPE_DIESEL = "diesel";

	public static final String MYGASFEED_API_SORTBY_DISTANCE = "Distance";
	public static final String MYGASFEED_API_SORTBY_PRICE = "Price";

	private static final String MYGASFEED_API_DOMAIN = "api.mygasfeed.com";
	private static final String MYGASFEED_API_KEY = "os28ajqvrv";
	private static final String MYGASFEED_API_DEVELOPMENT_DOMAIN = "devapi.mygasfeed.com";
	private static final String MYGASFEED_API_DEVELOPMENT_KEY = "rfej9napna";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findgasstations);

		final ListView listView = (ListView) findViewById(R.id.listView);
		adapter = new StationsAdapter(new LinkedList<Station>(),
				getApplication());
		listView.setAdapter(adapter);

		// /stations/radius/(Latitude)/(Longitude)/(distance)/(fuel type)/(sort
		// by)/apikey.json
		/*
		 * Arguments: Latitude Longitude Distance - A number (miles) of the
		 * radius distance of stations according to the user's geo location Fuel
		 * Type - Argument types: reg,mid,pre or diesel. Which type of gas
		 * prices that will be returned. Sort By (Distance or Price) - Type
		 * arguments: price or distance. Gas stations will be sorted according
		 * to the argument. apikey - An Api key
		 */

		// get Bundle Strings
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}

		String lat = String.valueOf(extras.getDouble(FINDGASSTATION_EXTRA_LAT));
		String lng = String.valueOf(extras.getDouble(FINDGASSTATION_EXTRA_LNG));
		String distance = String.valueOf(extras
				.getInt(FINDGASSTATION_EXTRA_DISTANCE));
		String fuelType = String.valueOf(extras
				.getString(FINDGASSTATION_EXTRA_FUELTYPE));
		String sortBy = String.valueOf(extras
				.getString(FINDGASSTATION_EXTRA_SORTBY));

		String uri = "http://devapi.mygasfeed.com/stations/radius/" + lat + "/"
				+ lng + "/" + distance + "/" + fuelType + "/" + sortBy + "/"
				+ MYGASFEED_API_DEVELOPMENT_KEY + ".json";
		System.out.println("API_URI"+uri);
		new GasFeedRequestTask(this, adapter).execute(uri);
	}

	public class StationsAdapter extends ArrayAdapter<Station> {

		private List<Station> itemList;
		private Context context;

		public StationsAdapter(List<Station> itemList, Context ctx) {
			super(ctx, android.R.layout.simple_list_item_1, itemList);
			this.itemList = itemList;
			this.context = ctx;
		}

		public int getCount() {
			if (itemList != null)
				return itemList.size();
			return 0;
		}

		public Station getItem(int position) {
			if (itemList != null)
				return itemList.get(position);
			return null;
		}

		public long getItemId(int position) {
			if (itemList != null)
				return itemList.get(position).hashCode();
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			v = null;
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.listviewitem_station, null);
			}

			final Station s = itemList.get(position);
			TextView text = (TextView) v
					.findViewById(R.id.listViewItemTextViewStation);
			text.setText(s.getStation());

			TextView text1 = (TextView) v
					.findViewById(R.id.listViewItemTextViewAddress);
			text1.setText(s.getAddress());

			TextView text2 = (TextView) v
					.findViewById(R.id.listViewItemTextViewDistance);
			text2.setText(s.getDistance());

			TextView text3 = (TextView) v
					.findViewById(R.id.listViewItemTextViewRegPrice);
			text3.setText(s.getPrice());

			Button b = (Button) v.findViewById(R.id.listViewItemButtonGoNavi);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW,
							Uri.parse("google.navigation:q=" + s.lat + ","
									+ s.lng));
					startActivity(i);
				}
			});

			return v;

		}

		public List<Station> getItemList() {
			return itemList;
		}

		public void setItemList(List<Station> itemList) {
			this.itemList = itemList;
		}

	}
}
