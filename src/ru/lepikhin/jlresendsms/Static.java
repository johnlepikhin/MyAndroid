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
		// TODO Auto-generated constructor stub
	}

//	private static LinkedList<Retriable> retryQueue = new LinkedList<Retriable> ();
//	private static ReentrantLock retryLock = new ReentrantLock ();

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
		
		
//		new AsyncTask<Retriable, Void, Void>() {
//			@Override
//			protected Void doInBackground(Retriable... r) {
//				retryLock.lock();
//				retryQueue.add(r[0]);
//				Log.i("zzz Static.Add", "actual add " + r);
//				retryLock.unlock();
//				return null;
//			}
//		}.execute (r);
	}
	
	public void ProcessQueue () throws Exception {
		new AsyncTask<Static, Void, Void>() {

			@Override
			protected Void doInBackground(Static... q) {
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
		
		
		
		
//		new AsyncTask<Lock, Void, Void>() {
//
//			@Override
//			protected Void doInBackground(Lock... retryLock) {
//				ArrayList<Retriable> forRemove = new ArrayList<Retriable>();
//				Log.i("zzz ProcessQueue", "ask lock");
//				retryLock[0].lock();
//				Log.i("zzz ProcessQueue", "locked");
//				try {
//					Log.e("zzz ProcessQueue", "2");
//					Iterator<Retriable> i = retryQueue.iterator();
//					Log.e("zzz ProcessQueue", "3");
//					while (i.hasNext()) {
//						Log.e("zzz ProcessQueue", "4");
//						Retriable r = i.next();
//						Log.i("zzz ProcessQueue", "retry " + r);
//						forRemove.add (r);
//						r.retry();
//					}
//				} catch (Exception e) {
//					Log.e("zzz ProcessQueue", "GOT EXCEPTION");
//		            e.printStackTrace();
//				} finally {
//					Log.i("zzz ProcessQueue", "remove dequeued");
//					Iterator<Retriable> i = forRemove.iterator();
//					while (i.hasNext()) {
//						Retriable r = i.next();
//						retryQueue.remove(r);
//					}
//					retryLock[0].unlock();
//					Log.i("zzz ProcessQueue", "unlocked");
//				}
//				return null;
//			}
//		}.execute (retryLock);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table queue (id integer PRIMARY KEY AUTOINCREMENT NOT NULL, tp int, rcpt text, data text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
