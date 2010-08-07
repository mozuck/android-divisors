package com.makeramen.divisor;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NumberActivity extends Activity {
	
	TextView text;
	ListView list;
	
	ProgressDialog progressDialog;
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_DESCRIPTION = "description";
	
	SimpleAdapter adapter;
	
//	int[] factors = new int[10000];
	
	ArrayList<HashMap<String, String>> divisors = new ArrayList<HashMap<String, String>>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		text = (TextView) findViewById(R.id.text);
		list = (ListView) findViewById(R.id.list);
		
		adapter = new SimpleAdapter(
				this,
				divisors,
				android.R.layout.simple_list_item_2,
				new String[] {KEY_NUMBER, KEY_DESCRIPTION},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		new DivisorTask().execute();
		
	}
	
	private class DivisorTask extends AsyncTask<Void, Integer, Void> {

		long startTime;
		int count = 0;
		int fcount;
		HashMap<String, String> mMap;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = new ProgressDialog(NumberActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setMax(10000);
			progressDialog.show();
			
			startTime = System.currentTimeMillis();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			int time = 0;
			
			for (int n = 1; n <= 10000; n++) {
				
				fcount = 0;
				
				double sq = Math.sqrt(n);
				
				for (int f = 1; f <= sq; f++) {
					
					if (n % f == 0) {
						fcount += ((f == sq) ? 1 : 2);
					}
					
				}

				mMap = new HashMap<String , String>();
				mMap.put(KEY_NUMBER, Integer.toString(n));
				
				mMap.put(KEY_DESCRIPTION, (fcount == 2) ? LABEL_PRIME :(Integer.toString(fcount) + LABEL_DIVISORS));
				
				divisors.add(mMap);
				
				
				if (n % 379 == 0 && System.currentTimeMillis() - startTime > time){
					publishProgress(n);
					time += 500;
				}

			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			text.append("\ntime elapsed(ms): " + (System.currentTimeMillis() - startTime));
			text.append("\ntotal number of factors: " + count);
			list.setAdapter(adapter);
			progressDialog.dismiss();
		}
	}
	
}