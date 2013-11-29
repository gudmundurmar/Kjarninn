package is.hi.hbv.kjarninn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * For downloading images asyncronously
 */
class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference imageViewReference;
 
    /**
     * Create new imageView Reference
     */
    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference(imageView);
    }
 
    /**
     * Actual download method, run in the task thread
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
    	localstorage_helper localstorage = new localstorage_helper();
    	
    	int version = Integer.parseInt(params[1]);
    	String thumbName = "thumb"+Integer.toString(version)+".jpg";
    	
    	boolean isFileNameInLocalstorage = localstorage.isInLocal(thumbName,1)[0];
    	if (isFileNameInLocalstorage){
    		File thumb = localstorage.getFile(thumbName);
    		String path = thumb.getPath();
    		//Log.d("Getting bitmap form path...", path);
    		Bitmap bMap = BitmapFactory.decodeFile(path);
    		return bMap;
    	}
    	
    	String url = params[0];
    	String version_number = params[1];
        return downloadBitmap(url,version_number);
    }
 
    /**
     * Once the image is downloaded, associates it to the imageView
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
 
        if (imageViewReference != null) {
            ImageView imageView = (ImageView) imageViewReference.get();
            if (imageView != null) {
 
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                	try 
                	{
                	    // get input stream
                	    InputStream ims = imageView.getContext().getAssets().open("book_icon.png");
                	    // load image as Drawable
                	    Drawable d = Drawable.createFromStream(ims, null);
                	    // set image to ImageView
                	    imageView.setImageDrawable(d);
                	}
                	catch(Exception ex) 
                	{
                	    return;
                	}
                    
                }
            }
 
        }
    }
    
    /**
     * download frontpage images
     */
    static Bitmap downloadBitmap(String url, String version) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }
 
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    
                    try {
                    	Context context = MainActivity.getAppContext();
                    	String path = context.getFilesDir().getAbsolutePath()+"/thumb"+version+".jpg";
                    	//Log.d("Saving file to path..",path);
                    	FileOutputStream fos = context.openFileOutput("thumb"+version+".jpg", Context.MODE_PRIVATE);
                    	File thumb = new File(path);
                    	thumb.setReadable(true, false);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.close();
                 } catch (Exception e) {
                        e.printStackTrace();
                 }
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
 
}


