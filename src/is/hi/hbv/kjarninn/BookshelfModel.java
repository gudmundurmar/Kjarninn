package is.hi.hbv.kjarninn;

import java.util.ArrayList;

public class BookshelfModel {

    public static ArrayList<BookshelfItem> Items;

    public static void LoadModel() {

        Items = new ArrayList<BookshelfItem>();
        //Item(id, imageurl, name, headline, date, buttontext);


    }

    public static BookshelfItem GetbyId(int id){

        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
    
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
    
    public static String getThumbUrl(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.IconFile;
            }
        }
        return null;
    }
    
    public static String getHeadline(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Headline;
            }
        }
        return null;
    }
    
    public static String getDate(int id){
        for(BookshelfItem item : Items) {
            if (item.Id == id) {
                return item.Date;
            }
        }
        return null;
    }
    
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
