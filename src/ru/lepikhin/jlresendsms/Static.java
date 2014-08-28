package ru.lepikhin.jlresendsms;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Static extends SQLiteOpenHelper {
	private Context context;
	
	public Static(Context context) {
		super(context, "resendsms", null, 1);
		
		this.context = context;
	}

	public void Remove (int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (id > 0) {
			Log.i("zzz Static.Remove", "start");
			db.delete("queue", "id="+id, null);
		}
	}
	
	public void Add (int tp, String rcpt, String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.i("zzz Static.Add", "start, tp="+tp+", rcpt="+rcpt);
		
		ContentValues values = new ContentValues();
		values.put("tp", tp);
		values.put("rcpt", rcpt);
		values.put("data", text);
		db.insert("queue", null, values);
	}
	
	public void ProcessQueue (long sleep) throws Exception {
		Intent intent = new Intent(context, ProcessQueue.class);
		intent.putExtra("sleep", sleep);
		context.startService(intent);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table queue (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, tp int, rcpt text, data text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
