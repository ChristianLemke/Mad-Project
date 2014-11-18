package com.example.usasurvivalapp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RestaurantFragment extends Fragment {

	TextView tips_value;
	TextView total_value;
	TextView euro_value;
	EditText input_restaurant;
	EditText input_customtip;
	RadioGroup radioGroup_tips;;
	RadioButton radio_tip15;
	RadioButton radio_tip18;
	RadioButton radio_tip20;
	RadioButton radio_customTip;

	/**
	 * The fragment argument representing the section number for this fragment.
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
		View rootView = inflater.inflate(R.layout.fragment_restaurant,
				container, false);

		euro_value = (TextView) rootView.findViewById(R.id.euro_value);
		total_value = (TextView) rootView.findViewById(R.id.total_value);
		tips_value = (TextView) rootView.findViewById(R.id.tips_value);
		radioGroup_tips = (RadioGroup) rootView
				.findViewById(R.id.radioGroup_tips);
		radio_tip15 = (RadioButton) rootView.findViewById(R.id.radio_tip15);
		radio_tip18 = (RadioButton) rootView.findViewById(R.id.radio_tip18);
		radio_tip20 = (RadioButton) rootView.findViewById(R.id.radio_tip20);
		input_customtip = (EditText) rootView
				.findViewById(R.id.input_customtip);
		radio_customTip = (RadioButton) rootView
				.findViewById(R.id.radio_customTip);
		input_restaurant = (EditText) rootView
				.findViewById(R.id.input_restaurant);
		input_restaurant.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				calcTotal();
			}

		});
		radioGroup_tips
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						calcTotal();

					}

				});

		return rootView;
	}

	private void calcTotal() {

		double currency=0.803077393;
		double euro=0;
		double tip_percent = 0;
		double tip = 0;
		double total = -1;
		double input = -1;
		try {
			input = Double.parseDouble(input_restaurant.getText().toString());
		} catch (Exception e) {

		}
		if (input > 0) {
			if (radio_tip15.isChecked()) {
				System.out.println("15");
				tip_percent = 15;
			} else if (radio_tip18.isChecked()) {
				tip_percent = 18;
			} else if (radio_tip20.isChecked()) {
				tip_percent = 20;
			} else if (radio_customTip.isChecked()) {
				try {
					tip_percent = Double.parseDouble(input_customtip.getText()
							.toString());
				} catch (Exception e) {
				}

			}

			if (tip_percent > 0) {
				tip = input * (tip_percent / 100);
			}
			
			DecimalFormat df = new DecimalFormat("0.00");

			tips_value.setText(df.format(tip));

			total = input + tip;
			total_value.setText(df.format(total));
			
			euro = total * currency;
			euro_value.setText(df.format(euro));
		}

	}
}
