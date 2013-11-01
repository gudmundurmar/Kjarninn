package is.hi.hbv.kjarninn;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class BookshelfAdapter extends BaseAdapter {
 
    private ArrayList listData;
 
    private LayoutInflater layoutInflater;
 
    public BookshelfAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }
 
    @Override
    public int getCount() {
        return listData.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.bookshelf_row, null);
            holder = new ViewHolder();
            
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.headline = (TextView) convertView.findViewById(R.id.headline);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.title.setText(BookshelfModel.getTitle(position));
        holder.headline.setText(BookshelfModel.getHeadline(position));
        holder.date.setText(BookshelfModel.getDate(position));
 
        if (holder.thumbnail != null) {
            new ImageDownloaderTask(holder.thumbnail).execute(BookshelfModel.getThumbUrl(position));
        }
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView title;
        TextView headline;
        TextView date;
        ImageView thumbnail;
        ProgressBar progressBar;
        Button button;
        
    }
}
