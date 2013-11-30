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
        Items.add(new NavbarItem(1, "book_icon.png", "Í lestri"));
        Items.add(new NavbarItem(2, "play_icon.png", "Kjarnaofninn"));
        Items.add(new NavbarItem(3, "pistlar_icon.png", "Pistlar"));
        Items.add(new NavbarItem(4, "help_icon.png", "Upplýsingar"));


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
