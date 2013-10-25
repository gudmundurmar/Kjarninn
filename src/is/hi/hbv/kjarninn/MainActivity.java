
package is.hi.hbv.kjarninn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.Toast;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	/**
	 *  Handler for callbacks to the UI thread
	 */
	final Handler mHandler = new Handler();

    
    //Navbar stuff
    private ListView navbarListView;

	
	
	// declare the dialog as a member field of your activity
	ProgressDialog mProgressDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        NavbarModel.LoadModel();
        navbarListView = (ListView) findViewById(R.id.navbar);
        String[] ids = new String[NavbarModel.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        NavbarAdapter adapter = new NavbarAdapter(this,R.layout.navbar_row, ids);
        navbarListView.setAdapter(adapter);
        navbarListView.setOnItemClickListener(new DrawerItemClickListener());
		
		
		List<Button> buttons = new ArrayList<Button>();
		
		File storageDir = getFilesDir();
		localstorage localClass = new localstorage();
		
		
		// this needs to be bound to a button instead of calling it here
		//
		//TODO
		//Herna þarf að breyta..Mögulega búa til fall hérna
		String urli = "http://kjarninn.is/kerfi/wp-content/uploads/2013/10/2013_10_17.pdf";
		String nafn = "NafnTest";
		String[] downloads = new String[2];
		
		downloads[0] = urli;
		downloads[1] = nafn;
		final StartDownload download = new StartDownload(this);
		
		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("A message");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);

		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    public void onCancel(DialogInterface dialog) {
		        download.cancel(true);
		    }
		});	
		
		
		download.execute(downloads);
		
		final File[] fileList = localClass.FetchFiles(storageDir);
		String[] filenames = localClass.FetchNames(storageDir);
		
		LinearLayout ll = (LinearLayout)findViewById(R.id.books);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
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
		
		/**
		 * Responsible for making appropriate buttons depending on what 
		 * files are already being stored in the application
		 */
		


    }
	
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
				String urlToPdf = savePdfAs[0];
                FileOutputStream fos = openFileOutput(namePdf, Context.MODE_PRIVATE);
                String path = getFilesDir().getAbsolutePath() + "/" + namePdf + ".pdf"; // path to the root of internal memory.
                File f = new File(path);
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
                Log.e("file_size", Integer.toString(file_size));
                input = connection.getInputStream();
                
                byte[] buffer = new byte[1024];
                int read;
                long total = 0;
                while ((read = input.read(buffer)) != -1) {
                	if (isCancelled())
                        return null;
                    total += read;
                    if (file_size > 0) // only if total length is known
                        publishProgress((int) (total * 100 / file_size));
                	fos.write(buffer, 0, read);
                }
                fos.close();
                input.close(); 
                
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
                catch (IOException ignored) {Log.d("error", "her"); }

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
	    protected void onProgressUpdate(Integer... progress) {
	    	Log.d("error4", "her");
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        Log.d("error4", "her");
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	Log.d("error5", "her");
	        mProgressDialog.dismiss();
	        if (result != null)
	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
	        else
	            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
	    }

		   		
	}
	
	/**
	 * Description:
	 * Creates a new thread for downloading pdf file.
	 * When done downloading it calls whenDownloadComplete() where we update the UI 
	 * String urlToPdf is a web url to the pdf we want to download, String savePdfAs is the
	 * name of the file we want to save the pdf to
	 */

	

	
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
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
			@Override
		    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		            long arg3) {
				selectNavbarItem(arg2);
		    }
	}


	private void selectNavbarItem(int position) {
		switch(position) {
			case 0:
				Log.d("Navbar Click","Item 0");
				//Intent a = new Intent(MainActivity.this, Activity1.class);
		        //startActivity(a);
		        break;
			case 1:
				Log.d("Navbar Click","Item 1");
				//Intent a = new Intent(MainActivity.this, Activity1.class);
		        //startActivity(a);
		        break;
		    case 2:
		    	Log.d("Navbar Click","Item 2");
		    	Intent browserIntent_viewHelp = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kjarninn.is/kerfi/wp-content/uploads/2013/08/hjalp-kjarninn.jpg"));
				startActivity(browserIntent_viewHelp);
		        break;
			case 3:
				Log.d("Navbar Click","Item 3");
				Intent browserIntent_toSite = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kjarninn.is"));
				startActivity(browserIntent_toSite);
		        break;
		    default:
		}
	}
		    
		    
}
