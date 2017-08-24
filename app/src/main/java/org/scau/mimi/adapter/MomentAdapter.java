package org.scau.mimi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import org.scau.mimi.R;
import org.scau.mimi.bean.Moment;
import org.scau.mimi.gson.MessagesInfo;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sumimakito.android.advtextswitcher.AdvTextSwitcher;

/**
 * Created by 10313 on 2017/8/3.
 */

public class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessagesInfo.Content.Message> mMessages;
    private Context mContext;

    static class NormalViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civPortrait;
        TextView tvNickname;
        TextView tvPostTime;
        TextView tvLocation;
        TextView tvTextContent;
        ShineButton sbLikeButton;
        AdvTextSwitcher atsLikeNumber;
        AdvTextSwitcher atsCommentNumber;
        ImageView ivPic0;
        ImageView ivPic1;
        ImageView ivPic2;

        public NormalViewHolder(View itemView) {
            super(itemView);

            tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tvPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvTextContent = (TextView) itemView.findViewById(R.id.tv_moment_text);
            sbLikeButton = (ShineButton) itemView.findViewById(R.id.sb_like_button);
            atsLikeNumber = (AdvTextSwitcher) itemView.findViewById(R.id.ats_like_number);
            atsCommentNumber = (AdvTextSwitcher) itemView.findViewById(R.id.ats_comment_number);
            ivPic0 = (ImageView) itemView.findViewById(R.id.iv_pic_0);
            ivPic1 = (ImageView) itemView.findViewById(R.id.iv_pic_1);
            ivPic2 = (ImageView) itemView.findViewById(R.id.iv_pic_2);
        }

        public void bind(MessagesInfo.Content.Message message, Context context) {

            if (message.isFake)
                tvNickname.setText(message.fakeName);
            else
                tvNickname.setText(message.user.nname);

            tvPostTime.setText(new Date(message.tmCreated).toString());
            tvLocation.setText(message.location.locale);
            tvTextContent.setText(message.content);

            sbLikeButton.init((Activity) context);

            sbLikeButton.setChecked(false);
            sbLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    atsLikeNumber.next();
                }
            });

            String[] texts = {"123", "134"};
            atsLikeNumber.setTexts(texts);
            atsCommentNumber.setTexts(texts);
        }
    }

    public MomentAdapter(List<MessagesInfo.Content.Message> messages) {
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_moment, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NormalViewHolder)holder).bind(mMessages.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
