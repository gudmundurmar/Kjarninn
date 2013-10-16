package is.hi.hbv.kjarninn;

import java.io.File;
import android.util.Log;

public class localstorage {
	
	
	/**
	 * Usage <localstorage instance>.FetchFiles( (File) directory)
	 * returns: File[] containing files in the directory.
	 * @param dir
	 * @return
	 */
	public File[] FetchFiles(File dir) {
		
        Log.d("Storage location", "is.hi.hbv.kjarninn/"+dir.getName());
        File[] list=dir.listFiles();
        Log.d("Getting file list:", "Done");     
        Log.d("Files", "Size: "+ list.length);
        for (int i=0; i < list.length; i++)
        {
            Log.d("Filename:", list[i].getName());
        }
		return list;
	}
	
	/**
	 * Returns names of files in directory
	 * @param dir
	 * @return
	 */
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
