package com.example.usasurvivalapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usasurvivalapp.currency.CurrencyContentProvider;
import com.example.usasurvivalapp.currency.CurrencyDB;

public class StoreFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	public static final int ARG_SECTION_NUMBER_STORE = 1;

	EditText input_euro;
	EditText input_dollar;
	ImageButton btn_update;
	TextView text_currencydate;
	TextView text_currencyactual;
	double OneDollarToXEuro;

	Spinner spinner_from;
	Spinner spinner_to;
	EditText input_from;
	TextView text_to;
	Spinner spinner_fromW;
	Spinner spinner_toW;
	EditText input_fromW;
	TextView text_toW;
	String[] unity = null;
	String[] arrayWeight = { "pound", "ounce", "kilo", "gram" };
	String[] arrayLiquid = { "gallon", "quart", "fluid ounce", "pint", "liter",
			"milliliter", "deciliter" };
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

	boolean liquidSelected = true;

	Spinner spinner_ussizes;
	Spinner spinner_gersizes;

	RadioGroup radioGroup_gender;
	RadioButton radio_mensizes;
	RadioButton radio_womensizes;

	RadioGroup radioGroup_clothtype;
	RadioButton radio_clothes;
	RadioButton radio_shoes;

	boolean shoesSelected = false;

	CurrencyDB db;
	Context context;

	public static StoreFragment newInstance() {
		StoreFragment fragment = new StoreFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, ARG_SECTION_NUMBER_STORE);
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_store, container,
				false);

		context = this.getActivity();

		input_euro = (EditText) rootView.findViewById(R.id.input_euro);
		input_dollar = (EditText) rootView.findViewById(R.id.input_dollar);
		btn_update = (ImageButton) rootView.findViewById(R.id.btn_update);
		text_currencyactual = (TextView) rootView
				.findViewById(R.id.text_currencyactual);

		text_currencydate = (TextView) rootView
				.findViewById(R.id.text_currencydate);

		spinner_from = (Spinner) rootView.findViewById(R.id.spinner_from);
		spinner_to = (Spinner) rootView.findViewById(R.id.spinner_to);
		input_from = (EditText) rootView.findViewById(R.id.input_from);
		text_to = (TextView) rootView.findViewById(R.id.text_to);

		spinner_fromW = (Spinner) rootView.findViewById(R.id.spinner_fromW);
		spinner_toW = (Spinner) rootView.findViewById(R.id.spinner_toW);
		input_fromW = (EditText) rootView.findViewById(R.id.input_fromW);
		text_toW = (TextView) rootView.findViewById(R.id.text_toW);

		spinner_ussizes = (Spinner) rootView.findViewById(R.id.spinner_ussizes);
		spinner_gersizes = (Spinner) rootView
				.findViewById(R.id.spinner_gersizes);

		radioGroup_clothtype = (RadioGroup) rootView
				.findViewById(R.id.radioGroup_clothtype);
		radio_clothes = (RadioButton) rootView.findViewById(R.id.radio_clothes);
		radio_shoes = (RadioButton) rootView.findViewById(R.id.radio_shoes);

		radioGroup_gender = (RadioGroup) rootView
				.findViewById(R.id.radioGroup_gender);
		radio_mensizes = (RadioButton) rootView
				.findViewById(R.id.radio_mensizes);
		radio_womensizes = (RadioButton) rootView
				.findViewById(R.id.radio_womensizes);

		setSpinner(arrayLiquid, spinner_from, spinner_to);
		// spinner_to.setSelection(4);

		setSpinner(arrayWeight, spinner_fromW, spinner_toW);
		// spinner_toW.setSelection(4);

		db = new CurrencyDB(context);

		getLoaderManager().initLoader(0, null, this);

		radioGroup_gender
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (radio_mensizes.isChecked()) {
							if (radio_shoes.isChecked()) {
								setShoesM();
							} else {
								setClothesM();
							}
						} else if (radio_womensizes.isChecked()) {
							if (radio_shoes.isChecked()) {
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

		btn_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateCurrency();
			}

		});
		input_euro.addTextChangedListener(new TextWatcher() {

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
				calcValue("euro");

			}

		});

		input_dollar.addTextChangedListener(new TextWatcher() {

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
				calcValue("dollar");

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
				calcLiquid();

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		input_fromW.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				calcWeight();

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});
		return rootView;
	}

	public void setSpinner(String[] array, Spinner from, Spinner to) {
		unity = array;
		ArrayAdapter adapter = new ArrayAdapter(context,
				android.R.layout.simple_spinner_item, unity);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		from.setAdapter(adapter);
		to.setAdapter(adapter);
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
		System.out.println("from " + from + " to " + to);
		if (input > 0) {
			if (from.equals("quart")) {
				convertLiter = input * 0.946352946;
			} else if (from.equals("gallon")) {
				convertLiter = input * 3.78541178;
			} else if (from.equals("fluid ounce")) {
				convertLiter = input * 0.0295735296;
			} else if (from.equals("pint")) {
				convertLiter = input * 0.473176473;
			} else if (from.equals("liter")) {
				convertLiter = input;
			} else if (from.equals("deciliter")) {
				convertLiter = input * 0.1;
			} else if (from.equals("milliliter")) {
				convertLiter = input * 0.001;
			}

			if (to.equals("quart")) {
				output = convertLiter * 1.05668821;
			} else if (to.equals("gallon")) {
				output = convertLiter * 0.264172052;
			} else if (to.equals("fluid ounce")) {
				output = convertLiter * 33.8140227;
			} else if (to.equals("pint")) {
				output = convertLiter * 2.11337642;
			} else if (to.equals("liter")) {
				output = convertLiter;
			} else if (to.equals("deciliter")) {
				output = convertLiter * 10;
			} else if (to.equals("milliliter")) {
				output = convertLiter * 1000;
			}

			DecimalFormat df = new DecimalFormat("0.000");
			text_to.setText(df.format(output));
		}

	}

	public void calcWeight() {

		String from = spinner_fromW.getSelectedItem().toString();
		String to = spinner_toW.getSelectedItem().toString();
		double output = -1;
		double input = -1;
		double convertKg = -1;

		try {
			input = Double.parseDouble(input_fromW.getText().toString());
		} catch (Exception e) {

		}

		System.out.println("input: " + input);
		if (input > 0) {
			if (from.equals("pound")) {
				convertKg = input * 0.45359237;
			} else if (from.equals("ounce")) {
				convertKg = input * 0.0283495231;
			} else if (from.equals("gram")) {
				convertKg = input * 0.001;
			} else if (from.equals("kilo")) {
				convertKg = input;
			}

			System.out.println("convertkg:" + convertKg);

			if (to.equals("pound")) {
				output = convertKg * 2.20462262;
			} else if (to.equals("ounce")) {
				output = convertKg * 35.2739619;
			} else if (to.equals("gram")) {
				output = convertKg * 1000;
			} else if (to.equals("kilo")) {
				output = convertKg;
			}
			System.out.println("output:" + output);

			DecimalFormat df = new DecimalFormat("0.000");
			text_toW.setText(df.format(output));

		}
	}

	public void setSpinnerSizes(Spinner spinner, String[] array) {
		unity = array;
		ArrayAdapter adapter = new ArrayAdapter(context,
				android.R.layout.simple_spinner_item, unity);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	public void setShoesW() {
		setSpinnerSizes(spinner_gersizes, arrayShoesWomenGER);
		setSpinnerSizes(spinner_ussizes, arrayShoesWomenUS);
	}

	public void setShoesM() {
		setSpinnerSizes(spinner_gersizes, arrayShoesMenGER);
		setSpinnerSizes(spinner_ussizes, arrayShoesMenUS);
	}

	public void setClothesW() {
		setSpinnerSizes(spinner_gersizes, arrayWomenGER);
		setSpinnerSizes(spinner_ussizes, arrayWomenUS);
	}

	public void setClothesM() {
		setSpinnerSizes(spinner_gersizes, arrayMenGER);
		setSpinnerSizes(spinner_ussizes, arrayMenUS);
	}

	public double getOneDollarToXEuro() {
		return OneDollarToXEuro;
	}

	public String getDate() {
		Date dt = new Date();
		String result = (dt.getMonth() + 1) + "/" + dt.getDate() + "/"
				+ (dt.getYear() + 1900);
		return result;
	}

	public void setCurrencyInfo(double currency, String date) {
		Log.d("CurrencyActivity", "setCurrencyInfo");
		OneDollarToXEuro = currency;
		text_currencyactual.setText("$1 =" + currency + "€");
		text_currencydate.setText(date);
	}

	private void updateCurrency() {
		Log.d("CurrencyActivity", "updateCurrency()");

		new RequestTask()
				.execute("http://www.freecurrencyconverterapi.com/api/v2/convert?q=USD_EUR&compact=y");
		

	}

	private void calcValue(String currency) {
		Log.d("CurrencyActivity", "calcValue()");

		double euro = -1;
		double dollar = -1;
		double OneDollarToXEuro = this.OneDollarToXEuro;
		DecimalFormat df = new DecimalFormat("0.00");

		if (currency.equals("euro")) {
			try {
				euro = Double.parseDouble(input_euro.getText().toString());
				dollar = euro / OneDollarToXEuro;
				input_dollar.setText(df.format(dollar));

			} catch (Exception e) {
			}

		} else if (currency.equals("dollar")) {
			try {
				dollar = Double.parseDouble(input_dollar.getText().toString());
				euro = dollar * OneDollarToXEuro;
				input_euro.setText(df.format(euro));
			} catch (Exception e) {
			}

		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d("CurrencyActivity", "onCreateLoader()");
		Log.d("DB", "Called onCreateLoader");
		Uri locations = CurrencyContentProvider.CONTENT_URI;
		CursorLoader cursor = new CursorLoader(context, locations, null, null,
				null, null);

		return cursor;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d("CurrencyActivity", "Called OnLoadFinished");

		double dollartoeuro = 0;
		String date = "";

		if (data.getCount() > 0) {
			data.moveToLast();
			dollartoeuro = data.getDouble(1);
			date = data.getString(2);
			OneDollarToXEuro = dollartoeuro;
			setCurrencyInfo(dollartoeuro, date);

		} else {
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub

	}

	public class CurrencyInsertTask extends
			AsyncTask<ContentValues, Void, Void> {

		@Override
		protected Void doInBackground(final ContentValues... params) {
			Log.d("CurrencyActivity", "doInBackground()");
			Thread t = new Thread() {

				@Override
				public void run() {
					context.getContentResolver().insert(
							CurrencyContentProvider.CONTENT_URI, params[0]);
				}
			};
			t.start();
			return null;
		}

	}

	class RequestTask extends AsyncTask<String, String, List<String>> {

		private double newCurrency;
		private String date = getDate();
		private final ProgressDialog dialog = new ProgressDialog(context);

		// CurrencyActivity.this);

		public RequestTask() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected LinkedList<String> doInBackground(String... uri) {
			Log.d("RequestTask", "doInBackground");
			HttpClient httpclient = new DefaultHttpClient();
			System.out.println("oder?");
			HttpResponse response;
			System.out.println("oder?");
			LinkedList<String> currency = null;
			try {
				String uriS = uri[0];
				System.out.println(uriS);
				response = httpclient.execute(new HttpGet(uriS));
				System.out.println(response);
				StatusLine statusLine = response.getStatusLine();
				System.out.println("oder?");
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();

					String outS = out.toString();
					String currencyS = outS.substring(18);

					currencyS = currencyS.substring(0, currencyS.indexOf('}'));
					System.out.println("currency string:"+currencyS);
					
					try {
						newCurrency = Double.parseDouble(currencyS);

						SharedPreferences appPrefs = context
								.getSharedPreferences(
										"com.example.usasurvivalapp.currency.currency_preferences",
										context.MODE_PRIVATE);
						Editor prefsEditor = appPrefs.edit();
						
						prefsEditor.putString("currency", ""+newCurrency);
						prefsEditor.commit();
						

					} catch (Exception e) {
						dialog.setMessage(context.getString(R.string.string_updateerror));
						dialog.show();
					}

				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				 System.out.println(e.getMessage());
			}
			return currency;
		}

		@Override
		protected void onPreExecute() {
			Log.d("RequestTask", "onPreExecute()");
			super.onPreExecute();
			dialog.setMessage(context.getString(R.string.string_update));
			dialog.show();
		}

		@Override
		protected void onPostExecute(List<String> result) {
			Log.d("RequestTask", "onPostExecute()");
			super.onPostExecute(result);
			dialog.dismiss();
			ContentValues contentValues = new ContentValues();
			if (newCurrency > 0) {
				contentValues.put("CURRENCY", newCurrency);
				contentValues.put("DATE", date);

				setCurrencyInfo(newCurrency, date);
			} else {
				System.out.println("Currency"+newCurrency);
				Toast.makeText(context, context.getString(R.string.string_updateerror),
						Toast.LENGTH_SHORT).show();
			}

			try {
				CurrencyInsertTask lit = new CurrencyInsertTask();
				lit.execute(contentValues);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
	}
}
