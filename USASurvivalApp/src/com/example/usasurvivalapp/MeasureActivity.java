package com.example.usasurvivalapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MeasureActivity extends Activity {

	Button btn_liquid;
	Button btn_weight;
	Spinner spinner_from;
	Spinner spinner_to;
	EditText input_from;
	TextView text_to;
	String[] unity = null;
	String[] arrayWeight = { "pound", "ounce", "kilo", "gram" };
	String[] arrayLiquid = {  "gallon","quart", "fluid ounce", "pint",
			"liter", "milliliter", "deciliter" };
	boolean liquidSelected = true;

	public void setSpinner(String[] array) {
		unity = array;
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, unity);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_from.setAdapter(adapter);
		spinner_to.setAdapter(adapter);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_measure);

		btn_liquid = (Button) this.findViewById(R.id.btn_liquid);
		btn_weight = (Button) this.findViewById(R.id.btn_weight);
		spinner_from = (Spinner) this.findViewById(R.id.spinner_from);
		spinner_to = (Spinner) this.findViewById(R.id.spinner_to);
		input_from = (EditText) this.findViewById(R.id.input_from);
		text_to = (TextView) this.findViewById(R.id.text_to);

		
		setSpinner(arrayLiquid);
		spinner_to.setSelection(4);
		
		btn_liquid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				liquidSelected=true;
				setSpinner(arrayLiquid);
				spinner_to.setSelection(4);

			}
		});

		btn_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				liquidSelected=false;
				setSpinner(arrayWeight);
				spinner_to.setSelection(2);
				btn_weight.setPressed(true);

			}
		});

		

		input_from.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (liquidSelected) {
					calcLiquid();
				} else {
					calcWeight();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		spinner_from.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (liquidSelected) {
					calcLiquid();
				} else {
					calcWeight();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		spinner_to.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (liquidSelected) {
					calcLiquid();
				} else {
					calcWeight();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void calcLiquid() {
		String from = spinner_from.getSelectedItem().toString();
		String to = spinner_to.getSelectedItem().toString();
		double output = -1;
		double input = -1;
double convertLiter = -1;
		try {
			input = Double.parseDouble(input_from.getText().toString());
		} catch (Exception e) {

		}
System.out.println("from "+from+" to " +to);
		if (input > 0) {
			if (from.equals("quart")) {
				convertLiter = input*0.946352946 ;
			} else if (from.equals("gallon")) {
				convertLiter = input*3.78541178;
			} else if (from.equals("fluid ounce")) {
				convertLiter = input*0.0295735296;
			} else if (from.equals("pint")) {
				convertLiter = input*0.473176473;
			} else if (from.equals("liter")) {
				convertLiter = input;
			} else if (from.equals("deciliter")) {
				convertLiter = input*0.1;
			} else if (from.equals("milliliter")) {
				convertLiter = input*0.001;
			}
		
	
			if (to.equals("quart")) {
				output = convertLiter*1.05668821;
			} else if (to.equals("gallon")) {
				output = convertLiter*0.264172052;
			} else if (to.equals("fluid ounce")) {
				output = convertLiter*33.8140227;
			} else if (to.equals("pint")) {
				output = convertLiter*2.11337642;
			} else if (to.equals("liter")) {
				output = convertLiter;
			} else if (to.equals("deciliter")) {
				output = convertLiter*10;
			} else if (to.equals("milliliter")) {
				output = convertLiter*1000;
			}

			DecimalFormat df = new DecimalFormat("0.000");
			text_to.setText(df.format(output));
		}

	}

	public void calcWeight() {

		String from = spinner_from.getSelectedItem().toString();
		String to = spinner_to.getSelectedItem().toString();
		double output = -1;
		double input = -1;
		double convertKg = -1;

		try {
			input = Double.parseDouble(input_from.getText().toString());
		} catch (Exception e) {

		}
		
		
		System.out.println("input: "+input);
		if (input > 0) {
			if (from.equals("pound")) {
				convertKg=input*0.45359237;
				} else if (from.equals("ounce")) {
					convertKg=input*0.0283495231;
				} else if (from.equals("gram")) {
					convertKg=input*0.001;
				} else if (from.equals("kilo")) {
					convertKg=input;
				}
			
			System.out.println("convertkg:"+convertKg);
			

		if (to.equals("pound")) {
			output=convertKg*2.20462262;
			} else if (to.equals("ounce")) {
				output=convertKg*35.2739619;
			} else if (to.equals("gram")) {
				output=convertKg*1000;
			} else if (to.equals("kilo")) {
				output=convertKg;
			}
		System.out.println("output:"+output);

//		if (input > 0) {
//			if (from.equals("pound")) {
//				if (to.equals("kilo")) {
//					output = input * 0.45359237;
//				} else if (to.equals("ounce")) {
//					output = input * 16;
//				} else if (to.equals("gram")) {
//					output = input * 453.59237;
//				} else
//					output = input;
//			} else if (from.equals("ounce")) {
//				if (to.equals("pound")) {
//					output = input * 0.0625;
//				} else if (to.equals("kilo")) {
//					output = input * 0.0283495231;
//				} else if (to.equals("gram")) {
//					output = input * 28.3495231;
//				} else
//					output = input;
//			} else if (from.equals("gram")) {
//				if (to.equals("pound")) {
//					output = input * 0.00220462262;
//				} else if (to.equals("ounce")) {
//					output = input * 0.0352739619;
//				} else if (to.equals("kilo")) {
//					output = input * 0.001;
//				} else
//					output = input;
//			} else if (from.equals("kilo")) {
//				if (to.equals("pound")) {
//					output = input * 2.20462262;
//				} else if (to.equals("ounce")) {
//					output = input * 35.2739619;
//				} else if (to.equals("gram")) {
//					output = input * 1000;
//				} else
//					output = input;
//			}
			DecimalFormat df = new DecimalFormat("0.000");
			text_to.setText(df.format(output));
		
		}
	}
}
