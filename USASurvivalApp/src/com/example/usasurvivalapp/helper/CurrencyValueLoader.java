package com.example.usasurvivalapp.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class CurrencyValueLoader {

	private CurrencyValueLoaderInterface cvli;
	
	private static final String PREFS_NAME = "CurrencyValueLoaderName";
	private static final String PREFS_VALUE = "CurrencyValueLoaderValue";
	private static final String PREFS_DATE = "CurrencyValueLoaderDate";
	
	private static final long FIRST_START = -1;
	double lastCurrencyValue = 0.8022;
	Date lastValueDate = new Date(1416287830438l);
	
	
	private SharedPreferences storage;

	public CurrencyValueLoader(Activity activity) {
		storage = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		saveDefaults();
	}
	
	/**
	 * for the first app start save all the defaults in the Shared Preferences
	 */
	private void saveDefaults(){
		if(getLastSavedCurrencyValue() == FIRST_START){
			saveToSharedPrederences(lastCurrencyValue, lastValueDate);
		}
	}

	/**
	 * Load the actual currency value from the internet. The result will return by the listener.
	 * @param cvli
	 */
	public void getRefreshedCurrencyValue(CurrencyValueLoaderInterface cvli) {
		this.cvli = cvli;
		// start request
		String uriUSDtoEUR = "http://rate-exchange.herokuapp.com/fetchRate?from=USD&to=EUR";
		String uriEURtoUSD = "http://rate-exchange.herokuapp.com/fetchRate?from=EUR&to=USD";
		AsyncTask<String, String, String> task = new RequestTask();
		task.execute(uriUSDtoEUR);
	}

	/**
	 * @return The last saved currencyValue.
	 */
	public double getLastSavedCurrencyValue() {
		String tmp = storage.getString(PREFS_VALUE, String.valueOf(FIRST_START));
		if(String.valueOf(tmp) == String.valueOf(FIRST_START)){
			lastCurrencyValue = lastCurrencyValue;
		}
		return lastCurrencyValue;
	}
	
	/**
	 * @return The last saved savedate.
	 */
	public Date getLastSaveDate(){
		lastValueDate = new Date(Long.parseLong(storage.getString(PREFS_DATE, "-1")));
		return lastValueDate;
	}

	private void saveCurrencyValue(double value) {
		lastValueDate = new Date(System.currentTimeMillis());
		lastCurrencyValue = value;
		saveToSharedPrederences(lastCurrencyValue, lastValueDate);
	}
	
	private void saveToSharedPrederences(double value, Date date){
		SharedPreferences.Editor editor = storage.edit();
	    editor.putString(PREFS_DATE, String.valueOf(date.getTime()));
	    editor.putString(PREFS_VALUE, String.valueOf(value));

	    // Commit the edits!
	    editor.commit();
	}

	private void gotTaskRespone(double value){
		// save value
		saveCurrencyValue(value);
		
		// call listener
		cvli.refreshedCurrencyValueIsReady(value);
	}

	/**
	 * Uses http://rate-exchange.herokuapp.com/ <br>
	 * example: "http://rate-exchange.herokuapp.com/fetchRate?from=USD&to=EUR"
	 *
	 */
	class RequestTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String rate = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();

					// {"To":"EUR","From":"USD","Rate":"0.8022"}
					JSONObject obj = new JSONObject(out.toString());
					String to = obj.getString("To");
					String from = obj.getString("From");
					rate = obj.getString("Rate");

					System.out.println("response: " + to + " " + from + " "
							+ rate);

				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (JSONException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return rate;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Double value = Double.valueOf(result);
			gotTaskRespone(value);
		}
	}

	public interface CurrencyValueLoaderInterface{
		void refreshedCurrencyValueIsReady(double value);
	}
}
