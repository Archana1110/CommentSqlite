package anand.archana.com.commentcodingtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommetAdapter  extends RecyclerView.Adapter<CommetAdapter.MyViewHolder> {

    private Context context;
    private List<CommentModelClass> commentlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView comment;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            comment = view.findViewById(R.id.comment);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public CommetAdapter(Context context, List<CommentModelClass> commentlist) {
        this.context = context;
        this.commentlist = commentlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentModelClass commnt = commentlist.get(position);

        holder.comment.setText(commnt.getComment());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(commnt.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
