package com.example.usasurvivalapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class OnTheRoadFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static final int ARG_SECTION_NUMBER_ONTHEROAD = 2;

	private RadioGroup radioGroupFuealType;
	private RadioGroup radioGroupSortBy;
	private ImageButton buttonFindGasStations;
	private SeekBar seekBarDistance;
	private EditText editText;
	private EditText editTextSpeed1;
	private EditText editTextSpeed2;
	private EditText editTextFuelCost1;
	private EditText editTextFuelCost2;
	private TextView textViewCompassDirection;
	private EditText editTextAddress;
	private ImageButton imageButtonReloadLocation;
	private ImageButton imageButtonShareLocation;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static OnTheRoadFragment newInstance() {
		OnTheRoadFragment fragment = new OnTheRoadFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, ARG_SECTION_NUMBER_ONTHEROAD);
		fragment.setArguments(args);
		return fragment;
	}

	public OnTheRoadFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ontheroad,
				container, false);

		// speed
		editTextSpeed1 = (EditText) rootView.findViewById(R.id.editTextSpeed1);
		editTextSpeed2 = (EditText) rootView.findViewById(R.id.editTextSpeed2);

		// fuel cost
		editTextFuelCost1 = (EditText) rootView
				.findViewById(R.id.EditTextFuelCost1);
		editTextFuelCost2 = (EditText) rootView
				.findViewById(R.id.EditTextFuelCost2);

		// find gas
		buttonFindGasStations = (ImageButton) rootView
				.findViewById(R.id.button_find_gas_stations);
		radioGroupFuealType = (RadioGroup) rootView
				.findViewById(R.id.radioGroupFuelType);
		radioGroupSortBy = (RadioGroup) rootView
				.findViewById(R.id.radioGroupSortBy);
		seekBarDistance = (SeekBar) rootView.findViewById(R.id.seekBarDistance);
		editText = (EditText) rootView.findViewById(R.id.editTextDistance);

		textViewCompassDirection = (TextView) rootView.findViewById(R.id.textViewCompassDirection);
		editTextAddress = (EditText)rootView.findViewById(R.id.editTextAddress);
		imageButtonReloadLocation = (ImageButton) rootView.findViewById(R.id.imageButtonLocationReload);
		imageButtonShareLocation = (ImageButton) rootView.findViewById(R.id.imageButtonLocationShare);
		
		// speed listener
		editTextSpeed1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				calcSpeed();
			}
		});
		
		// fuel cost
		editTextFuelCost1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				calcFuelCost();
			}
		});

		// set seekbarDistance and editTextDistance dependence
		seekBarDistance
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO try to keep the size of the editText
						String value = String.valueOf(progress);

						if (progress < 100)
							value = " " + value;
						if (progress < 10)
							value = " " + value;

						editText.setText(value);
					}
				});

		buttonFindGasStations.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startFindCheapGasActivity();
			}
		});
		
		// location
		imageButtonReloadLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				findLocation();
			}
		});
		
		imageButtonShareLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareLocation();
			}
		});
		
		calcSpeed();
		calcFuelCost();
		
		reloadCompass();

		return rootView;
	}

	private void calcSpeed() {
		// 1 mile per hour = 1.609344 kilometers per hour
		double value1 = 0;
		try {
			value1 = Double.parseDouble(editTextSpeed1.getText().toString());
		} catch (Exception e) {
			value1 = 0;
		}
		editTextSpeed2.setText(String.valueOf(value1 * 1.609344));
	}

	private void calcFuelCost() {
		// 1 U.S. dollar / US gallon = 0.210794635 Euros / liter
		double value1 = 0;
		try {
			value1 = Double.parseDouble(editTextFuelCost1.getText().toString());
		} catch (Exception e) {
			value1 = 0;
		}
				
		editTextFuelCost2.setText(String.valueOf(value1 * 0.210794635));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	public void startFindCheapGasActivity() {
		// get values
		Intent intent = new Intent(getActivity(), FindGasStationActivity.class);
		Bundle extras = new Bundle();
		// TODO get location!!!
		extras.putDouble(FindGasStationActivity.FINDGASSTATION_EXTRA_LAT,
				Double.valueOf("33.770050"));
		extras.putDouble(FindGasStationActivity.FINDGASSTATION_EXTRA_LNG,
				Double.valueOf("-118.193739"));
		extras.putInt(FindGasStationActivity.FINDGASSTATION_EXTRA_DISTANCE,
				seekBarDistance.getProgress());

		switch (radioGroupFuealType.getCheckedRadioButtonId()) {
		case R.id.radioFuelType0:
			extras.putString(
					FindGasStationActivity.FINDGASSTATION_EXTRA_FUELTYPE,
					FindGasStationActivity.MYGASFEED_API_FUELTYPE_REG);
			break;
		case R.id.radioFuelType1:
			extras.putString(
					FindGasStationActivity.FINDGASSTATION_EXTRA_FUELTYPE,
					FindGasStationActivity.MYGASFEED_API_FUELTYPE_MID);
			break;
		case R.id.radioFuelType2:
			extras.putString(
					FindGasStationActivity.FINDGASSTATION_EXTRA_FUELTYPE,
					FindGasStationActivity.MYGASFEED_API_FUELTYPE_PRE);
			break;
		case R.id.radioFuelType3:
			extras.putString(
					FindGasStationActivity.FINDGASSTATION_EXTRA_FUELTYPE,
					FindGasStationActivity.MYGASFEED_API_FUELTYPE_DIESEL);
			break;

		default:
			break;
		}

		switch (radioGroupSortBy.getCheckedRadioButtonId()) {
		case R.id.radioSortBy0:
			extras.putString(
					FindGasStationActivity.FINDGASSTATION_EXTRA_SORTBY,
					FindGasStationActivity.MYGASFEED_API_SORTBY_DISTANCE);
			break;
		case R.id.radioSortBy1:
			extras.putString(
					FindGasStationActivity.FINDGASSTATION_EXTRA_SORTBY,
					FindGasStationActivity.MYGASFEED_API_SORTBY_PRICE);
			break;

		default:
			break;
		}

		intent.putExtras(extras);
		startActivity(intent);
	}
	
	private void findLocation(){
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		      makeUseOfNewLocation(location);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	private void makeUseOfNewLocation(Location location) {
		// TODO set address
		editTextAddress.setText("Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
	}
	
	private void shareLocation(){
		//TODO share
	}
	
	private void setCompass(float d){
		// TODO Problem with landscape
		
		// 0=North, 90=East, 180=South, 270=West
		String value = "";
		// N NE E SE S SW W NW (22.5 steps)
		// starting with N between >337.5 - 22.5 continuing in 45er steps
		if(d > 337.5 || d <= 22.5)
			value = "N";
		else if(d > 22.5 * 1 && d <= 22.5 * 3)
			value = "NE";
		else if(d > 22.5 * 3 && d <= 22.5 * 5)
			value = "E";
		else if(d > 22.5 * 5 && d <= 22.5 * 7)
			value = "SE";
		else if(d > 22.5 * 7 && d <= 22.5 * 9)
			value = "S";
		else if(d > 22.5 * 9 && d <= 22.5 * 11)
			value = "SW";
		else if(d > 22.5 * 11 && d <= 22.5 * 13)
			value = "W";
		else if(d > 22.5 * 13 && d <= 22.5 * 15)
			value = "NW";
		
		textViewCompassDirection.setText(value);
	}
	
	SensorManager sensorService;
	Sensor sensor;
	
	private void reloadCompass(){
		// http://www.vogella.com/tutorials/AndroidSensor/article.html#compass
		// https://www.codeofaninja.com/2013/08/android-compass-code-example.html
		sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    if (sensor != null) {
	      sensorService.registerListener(mySensorEventListener, sensor,
	          SensorManager.SENSOR_DELAY_NORMAL);
	      Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");
	    } else {
	      Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
	    }
		
	}
	
	private SensorEventListener mySensorEventListener = new SensorEventListener() {

	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }

	    @Override
	    public void onSensorChanged(SensorEvent event) {
	      // angle between the magnetic north direction
	      // 0=North, 90=East, 180=South, 270=West
	      float azimuth = event.values[0];
	      setCompass(azimuth);
	    }
	  };
	  
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if (sensor != null) {
	      sensorService.unregisterListener(mySensorEventListener);
	    }
	  }
}
