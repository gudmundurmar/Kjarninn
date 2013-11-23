package is.hi.hbv.kjarninn;

import java.io.File;

import android.content.Context;

public class localstorage_helper {
	
	
	
	//What it does:
	//Checks if filename is in local storage and if correct size (not a partial file)
	public boolean[] isInLocal(String filename, long filesize) {
		
		Context context = MainActivity.getAppContext();
		File[] localFiles = context.getFilesDir().listFiles();
		 
		 boolean[] result = new boolean[2];
		 result[0] = false;
		 result[1] = false;
		 
	     for (int i=0; i < localFiles.length; i++)
	     {
	         if (filename.equals(localFiles[i].getName())){
	        	 result[0] = true;
	        	 if (filesize == localFiles[i].length()){
	        		 result[1] = true;
	        	 }
	        	 
	         }
	     }  
		return result;
	}
	
	public File getFile(String filename){
		Context context = MainActivity.getAppContext();
		File[] localFiles = context.getFilesDir().listFiles();
		
		for (int i=0; i<localFiles.length; i++){
			String name = localFiles[i].getName();
			if (name.equals(filename)){
				return localFiles[i];
			}
		}	
		return null;
	}
	
	public Void saveToLocal(File file){
		
	}

}




