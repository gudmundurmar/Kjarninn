package is.hi.hbv.kjarninn;

/**
 * Create bookshelf item
 */
public class BookshelfItem {

    public int Id;
    public String Version;
    public String IconFile;
    public String Title;
    public String Headline;
    public String Date;
    public String Buttontext;
    public Boolean Showdelete;

    /**
     * Creation of item
     * @param id
     * @param iconFile
     * @param title
     * @param headline
     * @param date
     * @param buttontext
     */
    public BookshelfItem(int id,String version, String iconFile, String title, String headline, String date, String buttontext, Boolean showdelete) {

        Id = id;
        Version = version;
        IconFile = iconFile;
        Title = title;
        Headline = headline;
        Date = date;
        Buttontext = buttontext;
        Showdelete = showdelete;
        
        

    }

}
