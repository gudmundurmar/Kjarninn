package is.hi.hbv.kjarninn;

import java.util.ArrayList;

/**
 * Create bookshelf model
 */
public class BookshelfModel {

    public static ArrayList<BookshelfItem> Items;

    public static void LoadModel() {

        Items = new ArrayList<BookshelfItem>();
        //Item(id, imageurl, name, headline, date, buttontext);


    }

    /**
     * get bookshelf item by id
     */
    public static BookshelfItem GetbyId(int id){

        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * get the title of item in bookshelf by id
     */
    public static String getTitle(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Title;
            }
        }
        return null;
    }
    
    public static String getVersion(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Version;
            }
        }
        return null;
    }
    
    /**
     * get the thumbnail url of item in bookshelf by id
     */
    public static String getThumbUrl(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.IconFile;
            }
        }
        return null;
    }
    
    /**
     * get the headline of item in bookshelf by id
     */
    public static String getHeadline(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Headline;
            }
        }
        return null;
    }
    
    /**
     * get the date of item in bookshelf by id
     */
    public static String getDate(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Date;
            }
        }
        return null;
    }
    
    /**
     * get the button text of item in bookshelf by id
     */
    public static String getButtonText(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Buttontext;
            }
        }
        return null;
    }
    
    public static Boolean showDelete(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Showdelete;
            }
        }
        return null;
    }

}
