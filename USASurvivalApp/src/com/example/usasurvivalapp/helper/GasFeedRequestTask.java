package com.example.usasurvivalapp.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.usasurvivalapp.FindGasStationActivity;
import com.example.usasurvivalapp.FindGasStationActivity.StationsAdapter;
import com.example.usasurvivalapp.my_gas_feed.model.Station;

/**
 * Parses the result with json.
 * @author Chris
 *
 */
public class GasFeedRequestTask extends AsyncTask<String, String, List<Station>> {

	private ProgressDialog dialog = null;
	private FindGasStationActivity findGasStationActivity;
	private StationsAdapter adapter;

	public GasFeedRequestTask(FindGasStationActivity findGasStationActivity, StationsAdapter adapter) {
		this.findGasStationActivity = findGasStationActivity;
		this.adapter = adapter;
		dialog = new ProgressDialog(findGasStationActivity);
	}

	@Override
	/**
	 * @returns the stations list or rull for a exception
	 */
	protected List<Station> doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		LinkedList<Station> stations = null;
		try {
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				// parse json!
				// Stations
				JSONObject obj = new JSONObject(out.toString());
				JSONArray jsonArrayStations = obj.getJSONArray("stations");

				System.out.println("jsonArrayStations "
						+ jsonArrayStations.length());
				stations = new LinkedList<Station>();
				for (int i = 0; i < jsonArrayStations.length(); i++) {
					Station station = convertStation(jsonArrayStations
							.getJSONObject(i));
					stations.add(station);
				}

			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
		} catch (JSONException e) {
		} catch (IOException e) {
		}
		return stations;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Searching gas-stations...");
		dialog.show();
	}

	@Override
	protected void onPostExecute(List<Station> result) {
		super.onPostExecute(result);
		dialog.dismiss();

		adapter.setItemList(result);
		adapter.notifyDataSetChanged();
	}

	private Station convertStation(JSONObject obj) throws JSONException {
		String country = obj.getString("country");
		String reg_price = obj.getString("reg_price");
		String mid_price = obj.getString("mid_price");
		String pre_price = obj.getString("pre_price");
		String diesel_price = obj.getString("diesel_price");
		String address = obj.getString("address");
		String diesel = obj.getString("diesel");
		String id = obj.getString("id");
		String lat = obj.getString("lat");
		String lng = obj.getString("lng");
		String station = obj.getString("station");
		String region = obj.getString("region");
		String city = obj.getString("city");
		String reg_date = obj.getString("reg_date");
		String mid_date = obj.getString("mid_date");
		String pre_date = obj.getString("pre_date");
		String diesel_date = obj.getString("diesel_date");
		String distance = obj.getString("distance");
		// TODO check reg_price, reg_date
		return new Station(id, country, reg_price, address, diesel, lat,
				lng, station, region, city, reg_date, distance);
	}
}
