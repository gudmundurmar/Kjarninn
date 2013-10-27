package is.hi.hbv.kjarninn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DownloadManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

public class MainActivity extends Activity {
	
	/**
	 *  Handler for callbacks to the UI thread
	 */
	final Handler mHandler = new Handler();
	
	/**
	 *  Create runnable for posting
	 */
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
        	whenDownloadComplete();
        }
    };
	
	/**
	 * Kicking things off ..
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		File storageDir = getFilesDir();
		localstorage localClass = new localstorage();
		
		startDownloadingPDF();
		
		final File[] fileList = localClass.FetchFiles(storageDir);
		String[] filenames = localClass.FetchNames(storageDir);
		
		LinearLayout ll = (LinearLayout)findViewById(R.id.books);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		List<Button> buttons = new ArrayList<Button>();
		
		for (int i=0; i < fileList.length; i++)
		{
			Button myButton = new Button(this);
			//Button ids in books (view) will start at 1000 
			myButton.setId(1000+i);
			myButton.setText(filenames[i]);
			myButton.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View ll) {
					Log.d("Button Pressed Id:", String.valueOf(ll.getId()));
					Log.d("Filelist0=",fileList[0].getName());
					OpenPDF(fileList[0]);
				}
			});
			ll.addView(myButton, lp);
			buttons.add(myButton);
		}
		for (Button b:buttons){
			Log.d("Id of Button " , String.valueOf(b.getId()));
		}
		
    }
	
	/**
	 * Creates a new thread for downloading pdf file.
	 * When done downloading it calls whenDownloadComplete() where we update the UI 
	 */
	protected void startDownloadingPDF() {
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
		Thread t = new Thread(new Runnable(){
			@Override
            public void run() {
                try {
                    try {
                        FileOutputStream fos = openFileOutput("Book.pdf", Context.MODE_PRIVATE);
                        String path = getFilesDir().getAbsolutePath() + "/Book.pdf"; // path to the root of internal memory.
                        File f = new File(path);
                        f.setReadable(true, false);
                        URL url = new URL("http://kjarninn.is/kerfi/wp-content/uploads/2013/09/12_09_2013.pdf");
                        URLConnection urlConnection = url.openConnection();
                        urlConnection.connect();
                        
                        InputStream input = url.openStream();
                        
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = input.read(buffer)) != -1) {
                        	fos.write(buffer, 0, read);
                        }
                        fos.close();
                        input.close(); 
                        mHandler.post(mUpdateResults);
                    } 
                    catch (Exception e) {
                 	   Log.e("Something broke while fetching PDF", e.toString());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
       });
       t.start();	
	}
	
	/**
	 * Function for updating the UI after download of pdf has completed
	 */
	private void whenDownloadComplete() {
		// update the UI here
		Log.d("This state reached.","yayy");
    }
	
	/**
	 * Options menu stuff
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    /**
     * Opens the filz pdf file in a PDF reader.
     */
	public void OpenPDF(File filz) {
        try
        {
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(filz),"application/pdf");
	        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	        startActivity(intent);
        }
        catch (Exception e) 
        {
        	Log.e("Something went wrong!", "Error log below:");
        	e.printStackTrace();
        }  
    }
	
	/**
	 * Get JSON from server on button click:
	 */
	public void getJson(View view) {
		final String tag = "test";
		TextView text = (TextView) findViewById(R.id.testText);
		Log.d(tag, "Clicked JSON button");
		
		
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
		Thread getJsonT = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					JSONObject json = getJson("http://hugihlynsson.com/hi/kjarninn/kjarninn.php");
					Log.d("test", "Finished fetching JSON: ");
					Log.d("test", json.toString());
					
					JSONArray versions = json.getJSONArray("versions");
					
					for (int i = 0; i < versions.length(); i++) {
						JSONObject version = versions.getJSONObject(i);
						Log.d("test", version.getString("headline"));
					}
					
					Looper.prepare();
					// Display toast message:
					Context context = getApplicationContext();
					CharSequence text = "Successfully loaded json!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					
					Looper.loop();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getJsonT.start();
	}
	
	// Fetches JSON from URL and returns object:
	public static JSONObject getJson(String url){

		Log.d("test", "Starting to get JSON");
		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;

		// HTTP
		try {	    	
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
