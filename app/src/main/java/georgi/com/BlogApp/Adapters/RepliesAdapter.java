package georgi.com.BlogApp.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;
import georgi.com.BlogApp.R;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.MyViewHolder> {

    private Context context;

    private List<Reply> replies;

    public RepliesAdapter(Context context, List<Reply> replies) {
        this.context = context;
        this.replies = replies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Reply currReply = replies.get(position);
        User author = currReply.getAuthor();

        String profilePic = author.getProfile_picture();
        if(profilePic.equals("no"))
            profilePic = DEFAULT_USER_IMG;
        else profilePic = USER_IMAGES_URL + author.getUserUrl() + "/" + profilePic;

        Glide.with(context)
                .load(profilePic)
                .override(160, 160)
                .into(holder.profilePic);

        holder.fullName.setText(author.getFullName());
        holder.reply.setText(currReply.getReply());
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView fullName, reply;

        public MyViewHolder(View itemView) {
            super(itemView);

            profilePic = (ImageView) itemView.findViewById(R.id.reply_image);
            fullName = (TextView) itemView.findViewById(R.id.reply_fullName);
            reply = (TextView) itemView.findViewById(R.id.reply_reply);
        }
    }
}
