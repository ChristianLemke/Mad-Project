package com.example.usasurvivalapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		return rootView;
	}
}
