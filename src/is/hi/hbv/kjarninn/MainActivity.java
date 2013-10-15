package is.hi.hbv.kjarninn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.net.Uri;

public class MainActivity extends Activity {

	//Duno tharf til ad LayoutParams virki
    @SuppressLint("NewApi")
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        File storageDir = getFilesDir();
       localstorage localClass = new localstorage();
       
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
