package is.hi.hbv.kjarninn;

import java.util.ArrayList;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * extended baseAdapter for bookshelf
 */
public class BookshelfAdapter extends BaseAdapter {

	private ArrayList listData;
 
    private LayoutInflater layoutInflater;
 
    public BookshelfAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }
    
    /**
     * get number of things in the adapter
     */
    @Override
    public int getCount() {
        return listData.size();
    }
 
    /**
     * get item from adapter by position
     */
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
 
    /**
     * get item id from adapter by position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    /**
     * fetches the bookshelf layout and fills in the information
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.bookshelf_row, null);
            holder = new ViewHolder();
            
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.headline = (TextView) convertView.findViewById(R.id.headline);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.button = (ImageButton) convertView.findViewById(R.id.bookButton);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.title.setText(BookshelfModel.getTitle(position));
        holder.headline.setText(BookshelfModel.getHeadline(position));
        holder.date.setText("- "+BookshelfModel.getDate(position));
        holder.button.setId(position);
        holder.button.setTag(BookshelfModel.getButtonText(position));
        holder.button.setImageResource(BookshelfModel.getButtonIcon(position));
        holder.deleteButton.setId(position+1000);
        
        if (BookshelfModel.showDelete(position)){
        	holder.deleteButton.setVisibility(View.VISIBLE);
        }
        else{
        	holder.deleteButton.setVisibility(View.GONE);
        }
        
        String[] thumbnailparams = new String[2];
        thumbnailparams[0] = BookshelfModel.getThumbUrl(position);
        thumbnailparams[1] = BookshelfModel.getVersion(position);
        
        if (holder.thumbnail != null) {
            new ImageDownloaderTask(holder.thumbnail).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,thumbnailparams);
        }
 
        return convertView;
    }
 
    /**
     * info holder
     */
    static class ViewHolder {
        TextView title;
        TextView headline;
        TextView date;
        ImageView thumbnail;
        ProgressBar progressBar;
        ImageButton button;
        ImageButton deleteButton;
        
    }
}
