package is.hi.hbv.kjarninn;
import org.junit.*;
import static org.junit.Assert.*;

public class TDD {
	@Test
	  public void shouldDisplay2MinFor5Cents() throws IllegalCoinException {
	    PayStation ps = new PayStationImpl();
	    ps.addPayment( 5 );
	    assertEquals( "Should display 2 min for 5 cents", 
	                  2, ps.readDisplay() ); 
	  }
}
