package com.example.usasurvivalapp.helper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

/**
 * A subclass of AsyncTask that calls getFromLocation() in the background. The
 * class definition has these generic types: Location - A Location object
 * containing the current location. Void - indicates that progress units are not
 * used String - An address passed to onPostExecute()
 */
public class GetAddressTask extends AsyncTask<Location, Void, Address> {
	Context mContext;
	private GetAddressTaskListener listener;

	public GetAddressTask(Context context, GetAddressTaskListener gati) {
		super();
		mContext = context;
		this.listener = gati;
	}

	/**
	 * Get a Geocoder instance, get the latitude and longitude look up the
	 * address, and return it
	 * 
	 * @params params One or more Location objects
	 * @return A string containing the address of the current location, or an
	 *         empty string if no address can be found, or an error message
	 */
	@Override
	protected Address doInBackground(Location... params) {
		Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
		// Get the current location from the input parameter list
		Location loc = params[0];
		// Create a list to contain the result address
		List<Address> addresses = null;
		try {
			/*
			 * Return 1 address.
			 */
			addresses = geocoder.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalArgumentException e2) {
			// Error message to post in the log
			String errorString = "Illegal arguments "
					+ Double.toString(loc.getLatitude()) + " , "
					+ Double.toString(loc.getLongitude())
					+ " passed to address service";
			e2.printStackTrace();
			return null;
		}
		// If the reverse geocode returned an address
		if (addresses != null && addresses.size() > 0) {
			// Get the first address
			Address address = addresses.get(0);
			
			return address;
		} else {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Address result) {
		if (result != null) {
			listener.gotAddress(result);
		}
		super.onPostExecute(result);
	}
	
	public interface GetAddressTaskListener{
		/**
		 * returns the first address
		 * @param address
		 */
		public void gotAddress(Address address);
	}
}