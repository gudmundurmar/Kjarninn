package is.hi.hbv.kjarninn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class GetJson {
	
	static ProgressDialog mProgressDialog;
	/**
	 * Get JSON from server on button click:
	 */
	public static JSONObject getJson(Context context) {
		final String tag = "test";
		Log.d(tag, "Clicked JSON button");
		
		class GetJSON extends AsyncTask <Void, Void, JSONObject>{
			private Context context;
			private JSONObject json;
			// Fire off a thread to do some work that we shouldn't do directly in the UI thread
			public GetJSON(Context context) {
				//constructor
				Log.e("Here", "Smidur");
				this.json = null;
				this.context = context;
			}	

			@Override
			protected JSONObject doInBackground(Void...as) {
				try {
					Log.e("Here", "Smidur");
					json = getJson("http://hugihlynsson.com/hi/kjarninn/kjarninn.php");
					Log.d("test", "Finished fetching JSON: ");
					Log.d("test", json.toString());
					
					JSONArray versions = json.getJSONArray("versions");
					
					for (int i = 0; i < versions.length(); i++) {
						JSONObject version = versions.getJSONObject(i);
						Log.d("test", version.getString("headline"));
					}
					
					Looper.prepare();
					// Display toast message:
					Context context = this.context;
					CharSequence text = "Successfully loaded json!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					Looper.loop();
					return json;
				}
				catch (Exception e) {
					e.printStackTrace();
					return json;
				}
				
				
			}
			
			@Override
		    protected void onPostExecute(JSONObject result) {
		    	Log.d("error5", "her");
		        mProgressDialog.dismiss();
		        if (result != null) {
		        	Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
		        	BookshelfModel.LoadModel();
		        	String[] ids = new String[BookshelfModel.Items.size()];
		        	for(int i = 1; i<=ids.length; i++) {
		        		ids[i] = Integer.toString(i);
		        	}
		        	
		        	
		        	BookshelfAdapter badapter = new BookshelfAdapter(this.context,R.layout.bookshelf_row,ids);
		        	MainActivity.bookshelfListView.setAdapter(badapter);		        	
		        }
		        else {
		        	Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
		            
		        }
		    }
		}
		final GetJSON json = new GetJSON(context);
		json.execute();
	     // instantiate it within the onCreate method
 		mProgressDialog = new ProgressDialog(context);
 		mProgressDialog.setMessage("A message");
 		mProgressDialog.setIndeterminate(true);
 		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
 		//mProgressDialog.setCancelable(true);

 		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
 		    public void onCancel(DialogInterface dialog) {
 		        json.cancel(true);
 		    }
 		});	
 		return json.json;
		
	}
	
	// Fetches JSON from URL and returns object:
	public static JSONObject getJson(String url){

		Log.d("test", "Starting to get JSON");
		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;
		Log.d("test", "Starting to get JSON");
		// HTTP
		try {	    
			Log.d("test", "Starting to get JSON");
			HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
			HttpGet httppost = new HttpGet(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.d("test", "JSON loaded");
		} catch(Exception e) {
			Log.d("test", "Error loading JSON");
			return null;
		}

		// Read response to string
		try {	    	
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			// Remove first and last letters (who are both '"' and confuse the JSON object parser):
			result = result.substring(1, result.length()-1);
			Log.d("test", "JSON has been read:" + result);    
			Log.d("test", result.toString());          
		} catch(Exception e) {
			Log.d("test", "Error reading JSON");
			return null;
		}

		// Convert string to object
		try {
			jsonObject = new JSONObject(result); 
			Log.d("test", "JSON string has been converted to an Object");            
		} catch(JSONException e) {
			Log.d("test", "Failed to convert string to JSON"); 
			Log.d("test", e.toString());   
			return null;
		}
		return jsonObject;

	}
}
