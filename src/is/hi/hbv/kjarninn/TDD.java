package is.hi.hbv.kjarninn;

import android.util.Log;

public class TDD {
	
	public void testSharedPrefsInt() {
		sharedprefs shpref  = new sharedprefs();
	    int expected = 1337;
	    shpref.setPrefInt("testInt", expected);
	    int result = shpref.getPrefInt("testInt", -1);
	    if (result == expected){
	    	Log.d("Test successful","Result= "+Integer.toString(result));
	    }
	    else{
	    	Log.d("Test failed","Result= "+Integer.toString(result));
	    }
	}
	
	public void testSharedPrefsString() {
		sharedprefs shpref  = new sharedprefs();
		String expected = "hello_world";
		shpref.setPrefString("testString", expected);
		String result = shpref.getPrefString("testString", "defaultValue");
		if (result.equals(expected)){
		    Log.d("Test successful","Result= "+result);
		}
		else{
			Log.d("Test failed","Result= "+result);
		}
	}
}


