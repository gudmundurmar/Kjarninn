package is.hi.hbv.kjarninn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ListView;

public class MainActivity extends Activity {
	 private DrawerLayout mDrawerLayout;
	/**
	 *  Handler for callbacks to the UI thread
	 */
	final Handler callbackHandler = new Handler();
	
	// declare the dialog as a member field of your activity
	ProgressDialog mProgressDialog;
    
    //Navbar stuff
    private ListView navbarListView;
    private ListView bookshelfListView;
    public JSONArray versions;
    //versionSizes = array of pdf file sizes [1.utg.....nyjasta.utg]
    public int[] versionsSizes;
    public static String[] versionNames;
    
    public File[] localFiles;
    public BookshelfAdapter bookshelfadapter;
    private static Context context;
    public static localstorage_helper localstorage;
	
	/**
	 * Responsible for making appropriate buttons depending on what 
	 * files are already being stored in the application
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		
		localstorage = new localstorage_helper();
		
        NavbarModel.LoadModel();
        BookshelfModel.LoadModel();
        navbarListView = (ListView) findViewById(R.id.navbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        bookshelfListView = (ListView) findViewById(R.id.bookshelf);
        String[] ids = new String[NavbarModel.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }
        NavbarAdapter NavAdapter = new NavbarAdapter(this,R.layout.navbar_row, ids); 
        navbarListView.setAdapter(NavAdapter); //

        navbarListView.setOnItemClickListener(new DrawerItemClickListener());
        
        Log.d("Getting","Json");
        Log.d("isOnline:",Boolean.toString(isOnline()));
        getJson();
        
        //Tests
		TDD tdd = new TDD();
		tdd.testSharedPrefsInt();
		tdd.testSharedPrefsString();
    }

	
	/**
	* Class for handling asyncronous downloading.
	*/
    public class StartDownload extends AsyncTask <String, Integer, String>{
		private Context context;
		
		public StartDownload(Context context) {
			//constructor
			this.context = context;
		}
		
		@Override
		/**
		 * savePdfAs verður að vera array af strengjum
		 */
		protected String doInBackground(String... savePdfAs) {
			// take CPU lock to prevent CPU from going off if the user 
	        // presses the power button during download
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
	        wl.acquire();
	        
			// TODO Auto-generated method stub
	        try {
	            InputStream input = null;
	            OutputStream output = null;
	            HttpURLConnection connection = null;
			try {
				String namePdf = savePdfAs[1];
				long correctPdfSize = 0;
				
				for (int j=0; j<versionNames.length; j++){
					Log.d(namePdf,versionNames[j]+"_//_"+ Integer.toString(versionsSizes[j]));
					if (namePdf.equals(versionNames[j])){
						correctPdfSize = versionsSizes[j];
					}
				}
				
				
				//Setja inn namePdf og correct! file size í isInLocal
				boolean[] localCheckResult = localstorage.isInLocal(namePdf,correctPdfSize);
				
				if (localCheckResult[0] && localCheckResult[1]){
						Log.d("Correct file exists","Cancelling download");
						cancel(true);
				}
				
				if (isCancelled()){
					return null;
				}
				else{
					String urlToPdf = savePdfAs[0];
	                FileOutputStream fileOutputStream = openFileOutput(namePdf, Context.MODE_PRIVATE);//
	                String pathRootInternalMemory = getFilesDir().getAbsolutePath() + "/" + namePdf; // path to the root of internal memory.
	                Log.d("Saving to path:",pathRootInternalMemory);
	                File f = new File(pathRootInternalMemory);
	                f.setReadable(true, false);
	                URL url = new URL(urlToPdf);
	                connection = (HttpURLConnection) url.openConnection();
	                connection.connect();
	                
	             // expect HTTP 200 OK, so we don't mistakenly save error report 
	                // instead of the file
	                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
	                     return "Server returned HTTP " + connection.getResponseCode() 
	                         + " " + connection.getResponseMessage();
	
	                
	                int file_size = connection.getContentLength();
	                Log.d("Download: file_size", Integer.toString(file_size));
	                input = connection.getInputStream();
	                
	                byte[] buffer = new byte[1024];
	                int read;
	                long total = 0;
	                int percentageProcess;
	                int startingOffset = 0;
	                int EndOfFile = -1;
	                while ((read = input.read(buffer)) != EndOfFile) {
	                	if (isCancelled())
	                        return null;
	                    total += read;
	                    if (file_size > 0) {// only if total length is known
	                        percentageProcess = (int) (total * 100 / file_size);
	                    	publishProgress(percentageProcess);
	                    }
	                	fileOutputStream.write(buffer, startingOffset, read);
	                }
	                fileOutputStream.close();
	                input.close();
				}
                
            } 
            catch (Exception e) {
            	Log.e("Something broke while fetching PDF", e.toString());
            }
			finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                    	input.close();
                } 
                catch (IOException ignored) {
                }

                if (connection != null)
                	connection.disconnect();
            }
        } finally {
            wl.release();
        }
			return null;
		}
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	        
	    }

	    @Override
	    /**
	    * Updates the progress bar
	    */
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);
	    }

	    @Override
	    /**
	    * When download is complete
	    */
	    protected void onPostExecute(String result) {
	        mProgressDialog.dismiss();
	        UpdateView();
	        if (result != null)
	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
	        else
	            Toast.makeText(context,"Blað sótt", Toast.LENGTH_SHORT).show();
	    }

		   		
	}
	
	/**
	 * Options menu stuff
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);//

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (mDrawerLayout.isDrawerOpen(navbarListView))
    	mDrawerLayout.closeDrawer(navbarListView);
    	else mDrawerLayout.openDrawer(navbarListView);
    	return true;
    		
    	
    }
    
    
    /**
     * Opens the files pdf file in a PDF reader.
     */
	public void OpenPDF(File filz) {
        try
        {
        	sharedprefs shpref = new sharedprefs();
        	shpref.setPrefString("lastopened", filz.getPath());
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        String uri = (Uri.fromFile(filz)).toString();
	        Uri newUri = Uri.parse(uri);
	        Log.d("OpenPDF checking path",newUri.toString());
	        intent.setDataAndType(newUri,"application/pdf");
	        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	        
	        PackageManager manager = context.getPackageManager();
	        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
	            if (infos.size() > 0) {
	            	startActivity(intent);
	            }else{
	            	Toast.makeText(context,"Enginn PDF lesari til staðar", Toast.LENGTH_SHORT).show();
	            }
	        
        }
        catch (Exception e) 
        {
        	Log.e("Something went wrong!", "Error log below:");
        	
        }  
    }
	
	/**
	 * Get JSON from server on button click:
	 */
	public void getJson() {
		
		class GetJSON extends AsyncTask <Void, Void, Void>{
			private Context context;
			// Fire off a thread to do some work that we shouldn't do directly in the UI thread
			public GetJSON(Context context) {
				//constructor
				this.context = context;
			}	

			@Override
			protected Void doInBackground(Void...as) {
				if (isOnline()){
					try {
						JSONObject json = getJson("http://kjarninn.com/json");
						Log.d("test", "Finished fetching JSON: ");
						
						versions = json.getJSONArray("versions");
						Assert.assertNotNull(versions);
						
						return null;
					}
					catch (Exception e) {
						Log.e("Error in getJson","");
						
					}
				}
				else {
					Log.e("Entering JsonFallback",".");
					JsonFallback();
					return null;
				}
				return null;
			}
			
		    @Override
		    protected void onPostExecute(Void v) {
		    	Log.d("Entering Post", "Post");
		    	try{
		    		LoadBooks();
		    	}
		    	catch(Exception e) {
		    	}
		    }


		}
		final GetJSON json = new GetJSON(this);
		json.execute();	
	}
	/**
	* Load books into bookshelf
	*/
	private void LoadBooks() throws JSONException {
		int id = 0;	
		for (int i = versions.length(); i > 0; --i) {
			JSONObject version = versions.getJSONObject(i-1);
			BookshelfModel.Items.add(new BookshelfItem(id,version.getString("version"), 
					version.getString("imageurl"), version.getString("name"), 
					version.getString("headline"), version.getString("date"), "Sækja", false));
			id++;
		}
		bookshelfadapter = new BookshelfAdapter(this, BookshelfModel.Items);
		bookshelfListView.setAdapter(bookshelfadapter);
		getFileSizes();
	}
	

	/*
	 * Updates Bookshelf ListView, changes buttons and buttonOnclickListeners
	 */
	private void UpdateView() {	
		Log.d("Entering","UpdateView()");
		Log.e("Radom tests","");
		Log.e("VersionNames[3]",versionNames[3]);
		Log.e("VersionSizes[3]",Integer.toString(versionsSizes[3]));
		int downCounter = versionNames.length;
		for (int upCounter = 0; upCounter < versionNames.length; ++upCounter) {
			--downCounter;
			boolean[] localResult = localstorage.isInLocal(versionNames[downCounter],versionsSizes[downCounter]);//down
			BookshelfItem item = BookshelfModel.GetbyId(upCounter);//up
			if (localResult[0] && localResult[1]){
				Log.d("This PDF is ready in local:",versionNames[downCounter]);//down
				//Change Model values
				item.Buttontext = "Lesa";
				item.Showdelete = true;		
			}
			else{
				item.Showdelete = false;
			}
		}
		//Reload adapter with new Model values ( refresh list view )
		bookshelfadapter.notifyDataSetChanged();
		
	}
	
	/*
	 *  Fetches JSON from URL and returns object:
	 */
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
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();
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
			Object EndOfFile = null;
			while ((line = reader.readLine()) != EndOfFile) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			localstorage.writeToFile(result,"data.json");
			
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
	/**
	* Selects the menu item that was clicked
	*/
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
			@Override
		    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		            long arg3) {
				selectNavbarItem(arg2);
		    }
	}

	/**
	* Selects navbar item
	*/
	private void selectNavbarItem(int position) {
		switch(position) {
			case 0:
				//I lestri
				Log.d("Navbar Click","Item 0");
				sharedprefs shpref = new sharedprefs();
	        	String last = shpref.getPrefString("lastopened","error");
	        	if (last.equals("error")){
	        		Toast.makeText(context,"Ekkert blað í lestri", Toast.LENGTH_SHORT).show();	
	        	}
	        	else{
	        		File lastopened = new File(last);
		        	OpenPDF(lastopened);	
	        	}
	        	break;
			case 3:
				//Upplýsingar
				Log.d("Navbar Click","Item 3");
		    	Intent browserIntent_toUpplysingar = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kjarninn.com/info"));
				startActivity(browserIntent_toUpplysingar);
		        break;
		    case 1:
		    	//Kjarnaofninn
		    	Log.d("Navbar Click","Item 1");
		    	Intent browserIntent_toOfninn = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kjarninn.is/kjarnaofninn"));
				startActivity(browserIntent_toOfninn);
		        break;
			case 2:
				//Pistlar
				Log.d("Navbar Click","Item 2");
				Intent browserIntent_toSite = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kjarninn.is/#pistlar"));
				startActivity(browserIntent_toSite);
		        break;
		    default:
		}
	}

	/**
	* Selects the bookshelf item that was clicked
	*/
	public void selectBookshelfItem(int position) {
		if (isOnline()){
			try {
				JSONObject version = versions.getJSONObject(position);
				Log.d("Selected Bookshelf Item", version.getString("pdfurl"));
				
				// this needs to be bound to a button instead of calling it here
				//
				//TODO
				String urli = version.getString("pdfurl");
				String nafn = version.getString("version")+"utg.pdf";
				String[] downloads = new String[2];
				
				downloads[0] = urli;
				downloads[1] = nafn;
				final StartDownload download = new StartDownload(this);
				
				String download_nafn = version.getString("version")+". útgáfu";
				// instantiate it within the onCreate method
				mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setMessage("Sæki "+download_nafn);
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(true);
		
				mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				    public void onCancel(DialogInterface dialog) {
				        download.cancel(true);
				    }
				});	
				
				Log.d("Starting Download",nafn);
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				    download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloads);
				else
				    download.execute(downloads);
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("Error in getJson","");
			}
		}
		else{
			Toast.makeText(context,"Þú ert ekki nettengdur!", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	* Opens corresponding pdf when button is pressed
	*/
	public void BookshelfButtonClick(View v) {
	    int id = v.getId();
	    Log.d("ListView Button click","id="+id);
	    ImageButton button = (ImageButton) v.findViewById(id);
	    String buttontext = button.getTag().toString();
	    int lastpos = versionNames.length-1;
	    int fileposition = lastpos-id;
	    String selectedFilename = versionNames[fileposition];
	    
	    if ( buttontext.equals("Lesa")){
	    	File file = localstorage.getFile(selectedFilename);
	    	OpenPDF(file);
	    }
	    else{
		    //-1 síðasta útgáfan er fyrst í adapternum finnum rétta útgáfu
		    selectBookshelfItem(fileposition);
	    }
	    
	}
	
	/*
	 * Makes the Delete buttons available
	 */
	public void DeleteButtonClick(View v) {
	    final int id = v.getId();
	    Log.d("Delete Button click","id="+id);
	    
	    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            switch (which){
	            case DialogInterface.BUTTON_POSITIVE:
	            	/*
	            	 * delete buttons have an id a 1000 higher than the read buttons
	            	 * delta => difference in index between read and delete buttons
	            	 */
	            	int delta = 1000;
	            	int deleteButtonsLastPosition = (versionNames.length-1)+delta;
	            	int deleteButtonChosenPosition = deleteButtonsLastPosition-id;
	                Log.d("Deleting...",versionNames[deleteButtonChosenPosition]);
	            	File dir = getFilesDir();
	            	File file = new File(dir, versionNames[deleteButtonChosenPosition]);
	            	localstorage.deleteFromLocal(file);
	            	
	            	//When the file (paper) has been deleted, change the read (lesa) button to get (sækja)
	            	int ReadFilePos = id-delta;
	            	BookshelfItem item = BookshelfModel.GetbyId(ReadFilePos);
	            	item.Buttontext = "Sækja";
	            	UpdateView();
	                break;
	
	            case DialogInterface.BUTTON_NEGATIVE:
	                //No button clicked
	                break;
	            }
	        }
	    };
	
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Eyða valdri útgáfu?").setPositiveButton("Eyða", dialogClickListener)
	        .setNegativeButton("Hætta", dialogClickListener).show();  
	}
	
	/*
	 * Assign pdf file sizes to array from latest to newest
	 */
	public void getFileSizes(){
		
		versionsSizes = new int[versions.length()];
		versionNames = new String[versions.length()];
		
		class GetFileSizes extends AsyncTask <Void, Void, Void>{
			
			public GetFileSizes (){
	
			}
		
			@Override
			protected Void doInBackground(Void...as) {
				for (int i=0; i < versions.length(); i++){
					try{
					JSONObject version = versions.getJSONObject(i);
					versionNames[i] = version.getString("version")+"utg.pdf";
					}
					catch(Exception e){
						
					}
				}
				sharedprefs shpref = new sharedprefs();
				if (isOnline()){
					for (int i=0; i < versions.length(); i++){
						try{
							
							JSONObject version = versions.getJSONObject(i);
							String urlToPdf = version.getString("pdfurl");
							HttpURLConnection connection = null;
							URL url = new URL(urlToPdf);
						    connection = (HttpURLConnection) url.openConnection();
						    connection.connect();
						    int file_size = connection.getContentLength();
						    shpref.setPrefInt(version.getString("version")+"utg",file_size);
						    versionsSizes[i] = file_size;
						    
						    //Critical þarf að laga ef ekki nettenging   
						}
						catch (Exception e){
							Log.e("Error in GetFileSizes","");
						}
					}
				}
				else{
					Log.e("Entering sizesFallback",".");
					sizesFallback();
				}
				return null;
			}
			
		    @Override
		    protected void onPostExecute(Void v) {
		    	
		    	UpdateView();
		    }
		    
		    
		}	
		final GetFileSizes sizes = new GetFileSizes();
		sizes.execute();
	}
	
	/*
	 * Gets get function for the context in mainAcivity
	 */
	public static Context getAppContext() {
	    return context;
	}
	/*
	 * If the download of the newest JSON does't work it will get the JSON file that is 
	 * located in the internal storage of the phone  
	 */
	public Void JsonFallback(){
		File jsonData = localstorage.getFile("data.json");
		StringBuilder text = new StringBuilder();
		BufferedReader br = null;
	
		try {
		    br = new BufferedReader(new FileReader(jsonData));
		    String line;
	
		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		} catch (IOException e) {
		    // do exception handling
		} finally {
		    try { br.close(); } catch (Exception e) { }
		}
		String result = text.toString();
		try{
			JSONObject json = new JSONObject(result);
			versions = json.getJSONArray("versions");
			Log.e("Loaded json backup",".");
			
		}
		catch (Exception e){
			Log.e("Error in JsonFallback",".");
		}
		return null;
	}
	/*
	 * If size of the PDF files cannot be assessed through the internet 
	 * a file size should be kept in the internal storage 
	 */
	public void sizesFallback(){
		for (int i=0; i < versions.length(); i++){
			try{
				sharedprefs shpref = new sharedprefs();
				JSONObject version = versions.getJSONObject(i);
			    int file_size = shpref.getPrefInt(version.getString("version")+"utg",-1);
			    versionsSizes[i] = file_size;  
			}
			catch (Exception e){
				Log.e("Error in sizesFallback",".");
				e.printStackTrace();
			}
		}
	}
	/*
	 * Checks whether there is any connection available
	 */
	public boolean isOnline() {
		    ConnectivityManager cm =
		        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        return true;
		    }
		    return false;
	}
}
