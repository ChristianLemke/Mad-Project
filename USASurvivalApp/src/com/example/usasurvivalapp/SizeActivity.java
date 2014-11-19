package com.example.usasurvivalapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class SizeActivity extends Activity {

	Spinner spinner_ussizes;
	Spinner spinner_gersizes;
	Button btn_clothes;
	Button btn_shoes;
	RadioGroup radioGroup_gender;
	RadioButton radio_mensizes;
	RadioButton radio_womensizes;

	boolean shoesSelected = false;

	String[] unity = null;
	String[] arrayMenUS = { "42", "44", "46", "48", "50", "52", "54", "56",
			"58" };
	String[] arrayMenGER = { "32", "34", "36", "38", "40", "42", "44", "46",
			"48" };
	String[] arrayWomenUS = { "4", "6", "8", "10", "12", "14", "16", "18", "20" };
	String[] arrayWomenGER = { "32", "34", "36", "38", "40", "42", "44", "46",
			"48" };
	String[] arrayShoesMenUS = { "6.5", "7", "7.5", "8", "8.5", "9", "9.5",
			"10", "10.5", "11", "11.5", "12", "13", "14", "15" };
	String[] arrayShoesMenGER = { "39", "40", "40.5", "41", "41.5", "42",
			"42.5", "43", "43.5", "44", "44.5", "45", "46", "47", "48" };
	String[] arrayShoesWomenUS = { "4", "5", "5.5", "6", "6.5", "7", "7.5",
			"8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5" };
	String[] arrayShoesWomenGER = { "35", "35.5", "36", "36.5", "37", "37.5",
			"38", "38.5", "39", "39.5", "40", "40.5", "41", "41.5", "42" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_size);

		spinner_ussizes = (Spinner) this.findViewById(R.id.spinner_ussizes);
		spinner_gersizes = (Spinner) this.findViewById(R.id.spinner_gersizes);

		btn_clothes = (Button) this.findViewById(R.id.btn_clothes);
		btn_shoes = (Button) this.findViewById(R.id.btn_shoes);

		radioGroup_gender = (RadioGroup) this
				.findViewById(R.id.radioGroup_gender);
		radio_mensizes = (RadioButton) this.findViewById(R.id.radio_mensizes);
		radio_womensizes = (RadioButton) this
				.findViewById(R.id.radio_womensizes);


		btn_clothes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shoesSelected = false;
				if (radio_mensizes.isChecked()) {
					setClothesM();
				} else if (radio_womensizes.isChecked()) {
					setClothesW();
				}

			}
		});

		btn_shoes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shoesSelected = true;

				if (radio_mensizes.isChecked()) {
					setShoesM();
				} else if (radio_womensizes.isChecked()) {
					setShoesW();
				}

			}
		});

		radioGroup_gender
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (radio_mensizes.isChecked()) {
							if (shoesSelected) {
								setShoesM();
							} else {
								setClothesM();
							}
						} else if (radio_womensizes.isChecked()) {
							if (shoesSelected) {
								setShoesW();
							} else {
								setClothesW();
							}
						}

					}
				});

		spinner_ussizes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				spinner_gersizes.setSelection(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_gersizes
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						spinner_ussizes.setSelection(position);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void setSpinner(Spinner spinner, String[] array) {
		unity = array;
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, unity);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	public void setShoesW() {
		setSpinner(spinner_gersizes, arrayShoesWomenGER);
		setSpinner(spinner_ussizes, arrayShoesWomenUS);
	}

	public void setShoesM() {
		setSpinner(spinner_gersizes, arrayShoesMenGER);
		setSpinner(spinner_ussizes, arrayShoesMenUS);
	}

	public void setClothesW() {
		setSpinner(spinner_gersizes, arrayWomenGER);
		setSpinner(spinner_ussizes, arrayWomenUS);
	}

	public void setClothesM() {
		setSpinner(spinner_gersizes, arrayMenGER);
		setSpinner(spinner_ussizes, arrayMenUS);
	}
}
