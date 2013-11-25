package is.hi.hbv.kjarninn;

import android.content.Context;
import android.content.SharedPreferences;



public class sharedprefs {
	
	Context context = MainActivity.getAppContext();
	SharedPreferences prefs = context.getSharedPreferences("is.hi.hbv.kjarninn", Context.MODE_PRIVATE);
	
	//Get Shared preferences key value,ValueType: Integer
	//defValue is return value if key doesn't exist
	public int getPrefInt(String key, Integer defValue){
		int result = prefs.getInt(key, -1);	
		
		return result;
	}
	
	//Set key value pair as shared preferences,ValueType: Integer
	public void setPrefInt(String key, Integer value){
		prefs.edit().putInt(key, value).commit();
	}
	
}
