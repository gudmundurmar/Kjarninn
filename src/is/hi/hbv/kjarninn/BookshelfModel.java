package is.hi.hbv.kjarninn;

import java.util.ArrayList;

public class BookshelfModel {

    public static ArrayList<BookshelfItem> Items;

    public static void LoadModel() {

        Items = new ArrayList<BookshelfItem>();
        /*Items.add(new BookshelfItem(1, "book2.png", "Í lestri blablabla"));
        Items.add(new BookshelfItem(2, "bookshelf.png", "Blaðahilla"));
        Items.add(new BookshelfItem(3, "wat.png", "Hjálp"));
        Items.add(new BookshelfItem(4, "widi.png", "Á vefnum"));*/

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

}
