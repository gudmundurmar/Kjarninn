package is.hi.hbv.kjarninn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.net.Uri;

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
	
	/**
	 * Responsible for making appropriate buttons depending on what 
	 * files are already being stored in the application
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
}
