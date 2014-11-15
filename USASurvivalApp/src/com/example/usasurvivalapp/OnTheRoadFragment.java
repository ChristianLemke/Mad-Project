package com.example.usasurvivalapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class OnTheRoadFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static final int ARG_SECTION_NUMBER_ONTHEROAD = 2;

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
		View rootView = inflater.inflate(R.layout.fragment_ontheroad, container,
				false);
		
		// Set up view
		Button button =(Button)rootView.findViewById(R.id.button_find_gas_stations);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startFindCheapGasActivity();
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void startFindCheapGasActivity() {
		Intent intent = new Intent(getActivity(), FindGasStationActivity.class);
		startActivity(intent);
	}
}
