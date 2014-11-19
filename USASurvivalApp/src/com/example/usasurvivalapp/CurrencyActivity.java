package com.example.usasurvivalapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.usasurvivalapp.currency.CurrencyContentProvider;
import com.example.usasurvivalapp.currency.CurrencyDB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyActivity extends Activity implements
		LoaderCallbacks<Cursor> {

	EditText input_euro;
	EditText input_dollar;
	Button btn_update;
	TextView text_currencydate;
	TextView text_currencyactual;
	double OneDollarToXEuro;

	CurrencyDB db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_currency);

		input_euro = (EditText) this.findViewById(R.id.input_euro);
		input_dollar = (EditText) this.findViewById(R.id.input_dollar);
		btn_update = (Button) this.findViewById(R.id.btn_update);
		text_currencyactual = (TextView) this
				.findViewById(R.id.text_currencyactual);

		text_currencydate = (TextView) this
				.findViewById(R.id.text_currencydate);

		db = new CurrencyDB(this);

		getLoaderManager().initLoader(0, null, this);

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
		CursorLoader cursor = new CursorLoader(this, locations, null, null,
				null, null);

		return cursor;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d("CurrencyActivity", "Called OnLoadFinished");

		double dollartoeuro = 0;
		String date = "";

		if (data.getCount()>0) {
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

	private class CurrencyInsertTask extends
			AsyncTask<ContentValues, Void, Void> {

		@Override
		protected Void doInBackground(ContentValues... params) {
			Log.d("CurrencyActivity", "doInBackground()");
			getContentResolver().insert(CurrencyContentProvider.CONTENT_URI,
					params[0]);
			return null;
		}

	}

	class RequestTask extends AsyncTask<String, String, List<String>> {

		private double newCurrency;
		private String date = getDate();
		private final ProgressDialog dialog = new ProgressDialog(
				CurrencyActivity.this);

		@Override
		protected LinkedList<String> doInBackground(String... uri) {
			Log.d("RequestTask", "doInBackground");
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			LinkedList<String> currency = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();

					String outS = out.toString();
					String currencyS = outS.substring(18);

					currencyS = currencyS.substring(0, currencyS.indexOf('}'));
					try {
						newCurrency = Double.parseDouble(currencyS);
					} catch (Exception e) {
						// dialog.setMessage("Error: Could not update currency!");
						// dialog.show();
					}

				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			} catch (Exception e) {
				// System.out.println(e.getMessage());
			}
			return currency;
		}

		@Override
		protected void onPreExecute() {
			Log.d("RequestTask", "onPreExecute()");
			super.onPreExecute();
			dialog.setMessage("update currency...");
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
				Toast.makeText(getApplicationContext(),
						"Error: Could not update currency!", Toast.LENGTH_SHORT)
						.show();
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
