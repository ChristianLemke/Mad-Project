package com.example.usasurvivalapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OnTheRoadFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static final int ARG_SECTION_NUMBER_ONTHEROAD = 2;

	private RadioGroup radioGroupFuealType;
	private RadioGroup radioGroupSortBy;
	private Button buttonFindGasStations;
	private SeekBar seekBarDistance;
	private EditText editText;
	private EditText editTextSpeed1;
	private EditText editTextSpeed2;
	private EditText editTextFuelCost1;
	private EditText editTextFuelCost2;

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
		buttonFindGasStations = (Button) rootView
				.findViewById(R.id.button_find_gas_stations);
		radioGroupFuealType = (RadioGroup) rootView
				.findViewById(R.id.radioGroupFuelType);
		radioGroupSortBy = (RadioGroup) rootView
				.findViewById(R.id.radioGroupSortBy);
		seekBarDistance = (SeekBar) rootView.findViewById(R.id.seekBarDistance);
		editText = (EditText) rootView.findViewById(R.id.editTextDistance);

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
		
		calcSpeed();
		calcFuelCost();

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
}
