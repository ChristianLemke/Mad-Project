package com.example.usasurvivalapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RestaurantFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static final int ARG_SECTION_NUMBER_RESTAURANT = 3;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static RestaurantFragment newInstance() {
		RestaurantFragment fragment = new RestaurantFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, ARG_SECTION_NUMBER_RESTAURANT);
		fragment.setArguments(args);
		return fragment;
	}

	public RestaurantFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_restaurant, container,
				false);
		return rootView;
	}
}
