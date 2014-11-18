package com.example.usasurvivalapp.currency;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class CurrencyDB extends SQLiteOpenHelper{


	public final static String DATABASE_NAME = "database";
	public final static String TABLE_NAME = "CURRENCY";
	public static final String FIELD_ID = "id";
	public static final String FIELD_CURRENCY = "currency";
	public static final String FIELD_DATE = "date";
	SQLiteDatabase db;

	public CurrencyDB(Context context) {
		super(context, DATABASE_NAME, null, 1);
		db = this.getWritableDatabase();
		if (db == null) {
			callback();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + FIELD_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + FIELD_CURRENCY + " REAL, "
				+ FIELD_DATE + " DATE);";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		onCreate(db);
	}

	void callback() {
		Log.d("DB", "Could not connect to db");
	}

	public long insertLabel(ContentValues values) {
		db = this.getWritableDatabase();
		long rowID = 0;

		if (db != null) {
			rowID = db.insert(TABLE_NAME, null, values);
		} else {
			callback();
		}
		return rowID;
	}

	public Cursor getCurrency() {

		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

}
