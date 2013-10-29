package is.hi.hbv.kjarninn;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class BookshelfModel {

    public static ArrayList<BookshelfItem> Items;

    public static void LoadModel() {
    	
    	Items = new ArrayList<BookshelfItem>();

    	JSONObject json = GetJson.getJson(MainActivity.context);
    	
    	if(json != null) {
    		try {
				JSONArray versions = json.getJSONArray("versions");
				for(int i = 0; i<versions.length(); i++) {
					JSONObject version = versions.getJSONObject(i);
			        Items.add(new BookshelfItem(i, "book2.png", version.getString("headline")));
			        Log.e("Headline",version.getString("headline"));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
        
    }

    public static BookshelfItem GetbyId(int id){

        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }

}
