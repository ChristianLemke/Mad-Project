package com.example.usasurvivalapp.currency;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class CurrencyContentProvider extends ContentProvider {

	public static final String PROVIDERS_NAME = "com.example.usasurvivalapp.currency.CurrencyContentProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDERS_NAME + "/" + CurrencyDB.TABLE_NAME);

	private static final UriMatcher sUriMatcher;

	private static final int CURRENCY = 1;

	private CurrencyDB db;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(PROVIDERS_NAME, CurrencyDB.TABLE_NAME, 1);
	}

	@Override
	public boolean onCreate() {
		Log.d("DB", "ContentProvider created");
		db = new CurrencyDB(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d("DB", "Query called");

		if (sUriMatcher.match(uri) == CURRENCY) {
			return db.getCurrency();
		} else {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (values != null) {
			db.insertLabel(values);
		}

		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}


}
