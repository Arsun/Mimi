package org.scau.mimi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.scau.mimi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 10313 on 2017/9/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> mList;

    public CommentAdapter(List<String> list) {
        mList = list;
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

        public void bind(String s) {
            tvCommentContent.setText(s);
        }

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CommentViewHolder)holder).bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
