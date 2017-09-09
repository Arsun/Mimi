package org.scau.mimi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.scau.mimi.R;
import org.scau.mimi.gson.Comment;
import org.scau.mimi.gson.CommentsInfo;
import org.scau.mimi.util.TextUtil;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 10313 on 2017/9/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_NO_MORE = 3;

    List<Comment> mComments;

    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civCommentPortrait;
        TextView tvCommentNickname;
        TextView tvCommentContent;
        TextView tvCommentTime;

        public CommentViewHolder(View itemView) {
            super(itemView);

            civCommentPortrait = (CircleImageView) itemView.findViewById(R.id.civ_comment_portrait);
            tvCommentNickname = (TextView) itemView.findViewById(R.id.tv_comment_nickname);
            tvCommentContent = (TextView) itemView.findViewById(R.id.tv_comment_content);
            tvCommentTime = (TextView) itemView.findViewById(R.id.tv_comment_time);
        }

        public void bind(Comment comment) {
            tvCommentContent.setText(comment.content);
            tvCommentNickname.setText(comment.user.nname);
            tvCommentTime.setText(TextUtil.dateToString(new Date(comment.tmCreated)));
        }

    }

    static class CommentHeader extends RecyclerView.ViewHolder {

        public CommentHeader(View itemView) {
            super(itemView);
        }
    }

    static class CommentFooter extends RecyclerView.ViewHolder {

        public CommentFooter(View itemView) {
            super(itemView);
        }
    }

    static class CommentNoMore extends RecyclerView.ViewHolder {

        public CommentNoMore(View itemView) {
            super(itemView);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_header, parent, false);
            return new CommentHeader(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_footer, parent, false);
            return new CommentFooter(view);
        } else if (viewType == TYPE_NO_MORE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_no_more, parent, false);
            return new CommentNoMore(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder)holder).bind(mComments.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = mComments.get(position).type;
        if ( type == TYPE_HEADER) {
            return TYPE_HEADER;
        } else if (type == TYPE_FOOTER) {
            return TYPE_FOOTER;
        } else if (type == TYPE_NO_MORE) {
            return TYPE_NO_MORE;
        }
        return TYPE_CONTENT;
    }
}
