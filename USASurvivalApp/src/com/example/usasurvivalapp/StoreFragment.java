package com.example.usasurvivalapp;

import java.util.Date;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.usasurvivalapp.helper.CurrencyValueLoader;
import com.example.usasurvivalapp.helper.CurrencyValueLoader.CurrencyValueLoaderInterface;

public class StoreFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static final int ARG_SECTION_NUMBER_STORE = 1;
	Button btn_currency;
	Button btn_sizes;
	Button btn_measure;
	
	private Button currencyButton;
	private TextView currencyTextView;
	
	double currencyValue = -1;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static StoreFragment newInstance() {
		StoreFragment fragment = new StoreFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, ARG_SECTION_NUMBER_STORE);
		fragment.setArguments(args);
		return fragment;
	}

	public StoreFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_store, container,
				false);
		
		btn_currency = (Button) rootView.findViewById(R.id.btn_currency);
		btn_sizes = (Button) rootView.findViewById(R.id.btn_sizes);
		btn_measure = (Button) rootView.findViewById(R.id.btn_measure);
		
		btn_currency.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CurrencyActivity.class);
				startActivity(intent);
				
			}
			
		});
		
		btn_sizes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SizeActivity.class);
				startActivity(intent);
			}
			
		});
		
		btn_measure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MeasureActivity.class);
				startActivity(intent);
			}
			
		});

		currencyButton = (Button) rootView.findViewById(R.id.currency_refresh_button);
		currencyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				reloadCurrencyValue();
			}
		});
		
		currencyTextView = (TextView) rootView
				.findViewById(R.id.currency_refresh_textview);
		
		loadSavedCurrencyValue();

		return rootView;
	}
	
	private void loadSavedCurrencyValue() {
		CurrencyValueLoader cvl = new CurrencyValueLoader(getActivity());
		double lastSavedCurrencyValue = cvl.getLastSavedCurrencyValue();
		Date lastSaveDate = cvl.getLastSaveDate();
		currencyValue = lastSavedCurrencyValue;
		setCurrencyValueTextView(lastSavedCurrencyValue,lastSaveDate);
	}

	private void reloadCurrencyValue(){
		currencyTextView.setText("loading...");
		final CurrencyValueLoader cvl = new CurrencyValueLoader(getActivity());
		cvl.getRefreshedCurrencyValue(new CurrencyValueLoaderInterface() {
			
			@Override
			public void refreshedCurrencyValueIsReady(double value) {
				currencyValue = value;
				setCurrencyValueTextView(currencyValue, cvl.getLastSaveDate());
			}
		});
	}
	
	private void setCurrencyValueTextView(double value, Date date){
		currencyTextView.setText("value: " + value + "(" + date.toString()+ ")");
	}
	
}
