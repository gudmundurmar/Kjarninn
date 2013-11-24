package is.hi.hbv.kjarninn;
import org.junit.*;
import static org.junit.Assert.*;

public class TDD {
	@Test
	  public void shouldDisplay2MinFor5Cents() {
	    sharedprefs shpref  = new sharedprefs();
	    assertEquals( "Should display 2 min for 5 cents", 
	                  2, ps.readDisplay() ); 
	  }
}


