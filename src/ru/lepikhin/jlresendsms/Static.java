package ru.lepikhin.jlresendsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
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
	
	public void ProcessQueue (final long sleep) throws Exception {
		new AsyncTask<Static, Void, Void>() {

			@Override
			protected Void doInBackground(Static... q) {
				try {
					Thread.sleep (sleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SQLiteDatabase db = q[0].getWritableDatabase();
				Log.i("zzz ProcessQueue", "start");

				Cursor i = db.query("queue", null, null, null, null, null, null);
				while (i.moveToNext()) {
					int id = i.getInt(0);
					int tp = i.getInt(1);
					String rcpt = i.getString(2);
					String data = i.getString(3);
					Retriable r = null;
					switch (tp) {
						case 1: r = new SendGEmail(); break;
						case 2: r = new SendSMS(); break;
						case 3: r = new XMPPSend(); break;
					}
					if (r != null) {
						Log.i("ProcessQueue", "id="+id+", tp="+tp+", rcpt="+rcpt+",    obj="+r);
						r.send(context, id, rcpt, data);
					}
				}
				
				return null;
			}
		}.execute(this);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table queue (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, tp int, rcpt text, data text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
