package is.hi.hbv.kjarninn;

import java.io.File;
import android.content.Intent;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.net.Uri;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //Accessing Internal Storage and fetching files.
        Log.e("Step 1", "Done");
        File storageDir = getFilesDir(); 
        Log.e("Step 2", storageDir.getName());
        File[] list=storageDir.listFiles();
        Log.e("Step 3", "Done");
        
        Log.e("Files", "Size: "+ list.length);
        for (int i=0; i < list.length; i++)
        {
        	Log.e("Loop",""+i);
            Log.e("Files", "FileName:" + list[i].getName());
        }
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(list[0]),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        
        
        return true;
    }
    
}
