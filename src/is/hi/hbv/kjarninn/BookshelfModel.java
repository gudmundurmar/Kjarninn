package is.hi.hbv.kjarninn;

import java.util.ArrayList;

public class BookshelfModel {

    public static ArrayList<BookshelfItem> Items;

    public static void LoadModel() {

        Items = new ArrayList<BookshelfItem>();
        Items.add(new BookshelfItem(1, "book2.png", "Í lestri blablabla"));
        Items.add(new BookshelfItem(2, "bookshelf.png", "Blaðahilla"));
        Items.add(new BookshelfItem(3, "wat.png", "Hjálp"));
        Items.add(new BookshelfItem(4, "widi.png", "Á vefnum"));

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
