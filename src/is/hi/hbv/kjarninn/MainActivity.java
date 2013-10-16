package is.hi.hbv.kjarninn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.net.Uri;

public class MainActivity extends Activity {


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        File storageDir = getFilesDir();
        localstorage localClass = new localstorage();

       try {
    	   Log.e("Step:","1");
           FileOutputStream fos = openFileOutput("Book.pdf", Context.MODE_PRIVATE);
           Log.e("Step:","2");
           URL url = new URL("http://kjarninn.is/kerfi/wp-content/uploads/2013/09/12_09_2013.pdf");
           Log.e("Step:","3");
           URLConnection urlConnection = url.openConnection();
           Log.e("Step:","4");
           urlConnection.connect();
           Log.e("Step:","5");

           InputStream input = url.openStream();
           Log.e("Step:","6");

            byte[] buffer = new byte[1024];
               int read;
               while ((read = input.read(buffer)) != -1) {
                   fos.write(buffer, 0, read);
               }
               fos.close();
               input.close();

       } 
       catch (Exception e) {
    	   Log.e("Something broke while fetching PDF", e.toString());
       }
       
       
       
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
                   OpenPDF(fileList[ll.getId()-1000]);
               }
           });
           ll.addView(myButton, lp);
           buttons.add(myButton);
       }
       for (Button b:buttons){
    	   Log.d("Id of Button " , String.valueOf(b.getId()));
       }
       
       

       
       
           
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        
        
        return true;
    }
    
    
    //Opens the filz pdf File in a PDF reader.
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
