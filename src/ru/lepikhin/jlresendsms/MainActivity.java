package ru.lepikhin.jlresendsms;

import org.jivesoftware.smack.SmackAndroid;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SmackAndroid.init(this);
	}
}
