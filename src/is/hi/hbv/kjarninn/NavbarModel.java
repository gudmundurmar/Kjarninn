package is.hi.hbv.kjarninn;

import java.util.ArrayList;

/**
 * holds the model of the navbar
 */
public class NavbarModel {

    public static ArrayList<NavbarItem> Items;

    /**
     * loads the model
     */
    public static void LoadModel() {

        Items = new ArrayList<NavbarItem>();
        Items.add(new NavbarItem(1, "book_icon.png", "I lestri"));
        Items.add(new NavbarItem(2, "bookshelf_icon.png", "Blaðahilla"));
        Items.add(new NavbarItem(3, "play_icon.png", "Kjarnaofninn"));
        Items.add(new NavbarItem(4, "pistlar_icon.png", "Pistlar"));

    }

    /**
     * returns item by id
     */
    public static NavbarItem GetbyId(int id){

        for(NavbarItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }

}
