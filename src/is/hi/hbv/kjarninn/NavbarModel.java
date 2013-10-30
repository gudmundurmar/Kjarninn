package is.hi.hbv.kjarninn;

import java.util.ArrayList;

public class NavbarModel {

    public static ArrayList<NavbarItem> Items;

    public static void LoadModel() {

        Items = new ArrayList<NavbarItem>();
        Items.add(new NavbarItem(1, "book2.png", "Í lestri"));
        Items.add(new NavbarItem(2, "bookshelf.png", "Blaðahilla"));
        Items.add(new NavbarItem(3, "wat.png", "Hjálp"));
        Items.add(new NavbarItem(4, "widi.png", "Á vefnum"));

    }

    public static NavbarItem GetbyId(int id){

        for(NavbarItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }

}