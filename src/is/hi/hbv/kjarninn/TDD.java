package is.hi.hbv.kjarninn;

import android.test.AndroidTestCase;


public class TDD extends AndroidTestCase{
	
	public void testPutKeyInt(){
		sharedprefs shpref = new sharedprefs(this.getContext(),"testPref");
		shpref.setPrefInt("testInt", 1337);
		int result = shpref.getPrefInt("testInt", -1);
	}
	  
}


