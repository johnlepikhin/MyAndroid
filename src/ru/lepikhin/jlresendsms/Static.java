package ru.lepikhin.jlresendsms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.os.AsyncTask;
import android.util.Log;

final public class Static {
	private static LinkedList<Retriable> retryQueue = new LinkedList<Retriable> ();
	private static ReentrantLock retryLock = new ReentrantLock ();

	public static void Add (Retriable r) {
		Log.i("zzz Static.Add", "start " + r);
		
		new AsyncTask<Retriable, Void, Void>() {
			@Override
			protected Void doInBackground(Retriable... r) {
				retryLock.lock();
				retryQueue.add(r[0]);
				retryLock.unlock();
				return null;
			}
		}.execute (r);
	}
	
	public static void ProcessQueue () {
		Log.i("zzz ProcessQueue", "start");

		new AsyncTask<Lock, Void, Void>() {

			@Override
			protected Void doInBackground(Lock... retryLock) {
				ArrayList<Retriable> forRemove = new ArrayList<Retriable>();
				Log.i("zzz ProcessQueue", "ask lock");
				retryLock[0].lock();
				Log.i("zzz ProcessQueue", "locked");
				try {
					Log.e("zzz ProcessQueue", "2");
					Iterator<Retriable> i = retryQueue.iterator();
					Log.e("zzz ProcessQueue", "3");
					while (i.hasNext()) {
						Log.e("zzz ProcessQueue", "4");
						Retriable r = i.next();
						Log.i("zzz ProcessQueue", "retry " + r);
						forRemove.add (r);
						r.retry();
					}
				} catch (Exception e) {
					Log.e("zzz ProcessQueue", "GOT EXCEPTION");
		            e.printStackTrace();
				} finally {
					Log.i("zzz ProcessQueue", "remove dequeued");
					Iterator<Retriable> i = forRemove.iterator();
					while (i.hasNext()) {
						Retriable r = i.next();
						retryQueue.remove(r);
					}
					retryLock[0].unlock();
					Log.i("zzz ProcessQueue", "unlocked");
				}
				return null;
			}
		}.execute (retryLock);
	}
}
