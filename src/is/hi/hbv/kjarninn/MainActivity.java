package is.hi.hbv.kjarninn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
//Navbar
//Navbar

public class MainActivity extends Activity {
	
	/**
	 *  Handler for callbacks to the UI thread
	 */
	final Handler mHandler = new Handler();
	
	/**
	 *  Create runnable for asynchronous thread
	 */
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
        	whenDownloadComplete();
        }
    };
    
    //Navbar stuff
    private ListView navbarListView;
	
	/**
	 * Responsible for making appropriate buttons depending on what 
	 * files are already being stored in the application
	 */
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
		startDownloadingPDF("http://kjarninn.is/kerfi/wp-content/uploads/2013/10/2013_10_17.pdf","Utgafa009.pdf");
		
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
		
    }
	
	/**
	 * Description:
	 * Creates a new thread for downloading pdf file.
	 * When done downloading it calls whenDownloadComplete() where we update the UI 
	 * String urlToPdf is a web url to the pdf we want to download, String savePdfAs is the
	 * name of the file we want to save the pdf to
	 */
	protected void startDownloadingPDF(String urlToPdf,String savePdfAs) {
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
		downloadRun p = new downloadRun(urlToPdf, savePdfAs){
			@Override
			public void run() {
                try {
                    FileOutputStream fos = openFileOutput(savePdfAs, Context.MODE_PRIVATE);
                    String path = getFilesDir().getAbsolutePath() + "/" + savePdfAs; // path to the root of internal memory.
                    File f = new File(path);
                    f.setReadable(true, false);
                    URL url = new URL(urlToPdf);
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
		};
		new Thread(p).start();
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
