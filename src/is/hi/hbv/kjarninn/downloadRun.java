package is.hi.hbv.kjarninn;

public class downloadRun implements Runnable {
	String urlToPdf;
	String savePdfAs;
    downloadRun(String urlToPdf,String savePdfAs) {
        this.urlToPdf = urlToPdf;
        this.savePdfAs = savePdfAs;
    }
    
    public void run() {
        // compute primes larger than minPrime
    }
}