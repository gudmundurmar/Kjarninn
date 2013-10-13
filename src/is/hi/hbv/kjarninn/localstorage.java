package is.hi.hbv.kjarninn;

import java.io.File;
import android.util.Log;

public class localstorage {
	
	
	//Usage <localstorage instance>.FetchFiles( (File) directory)
	// returns: File[] containing files in the directory.
	public File[] FetchFiles(File dir) {
		
        Log.e("Storage location", "is.hi.hbv.kjarninn/"+dir.getName());
        File[] list=dir.listFiles();
        Log.e("Getting file list:", "Done");     
        Log.e("Files", "Size: "+ list.length);
        for (int i=0; i < list.length; i++)
        {
            Log.e("Filename:", list[i].getName());
        }
		return list;
	}
	
	//Returns names of files in directory
	public String[] FetchNames(File dir) {
        File[] list=dir.listFiles();
        String[] names = new String[list.length];
        
        for (int i=0; i < list.length; i++)
        {
        	names[i]= list[i].getName();
        }
		return names;
	}
	
	
}
