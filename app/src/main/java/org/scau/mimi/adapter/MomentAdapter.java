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
import org.scau.mimi.gson.MessagesInfo;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.TextUtil;

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
        ImageView ivMomentPic0;
        ImageView ivMomentPic1;
        ImageView ivMomentPic2;

        public NormalViewHolder(View itemView) {
            super(itemView);

            tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tvPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvTextContent = (TextView) itemView.findViewById(R.id.tv_moment_text_content);
            sbLikeButton = (ShineButton) itemView.findViewById(R.id.sb_like_button);
            atsLikeNumber = (AdvTextSwitcher) itemView.findViewById(R.id.ats_like_number);
            atsCommentNumber = (AdvTextSwitcher) itemView.findViewById(R.id.ats_comment_number);
            ivMomentPic0 = (ImageView) itemView.findViewById(R.id.iv_moment_pic_0);
            ivMomentPic1 = (ImageView) itemView.findViewById(R.id.iv_moment_pic_1);
            ivMomentPic2 = (ImageView) itemView.findViewById(R.id.iv_moment_pic_2);
        }

        public void bind(final MessagesInfo.Content.Message message, Context context) {

            if (message.isFake)
                tvNickname.setText(message.fakeName);
            else
                tvNickname.setText(message.user.nname);

            tvPostTime.setText(
                    TextUtil.dateToString(new Date(message.tmCreated))
            );
            tvLocation.setText(message.location.locale);
            tvTextContent.setText(message.content);

            sbLikeButton.init((Activity) context);

            sbLikeButton.setChecked(message.isLike ? true : false);

            sbLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sbLikeButton.isChecked()) {
                        atsLikeNumber.overrideText(String.valueOf(message.likeCount + 1));
                        message.isLike = true;
                    } else {
                        atsLikeNumber.overrideText(String.valueOf(message.likeCount));
                        message.isLike = false;
                    }
                }
            });


            atsLikeNumber.setText(String.valueOf(message.likeCount));
            atsCommentNumber.setText(String.valueOf(message.commentCount));

            List<MessagesInfo.Content.Message.MessageImageSet> imageSetList = message.messageImageSet;
            if (imageSetList != null) {

                int picNum = imageSetList.size();
                if (picNum == 3) {
                    HttpUtil.loadImageByGlide(context, imageSetList.get(0).webPath, ivMomentPic0);
                    HttpUtil.loadImageByGlide(context, imageSetList.get(1).webPath, ivMomentPic1);
                    HttpUtil.loadImageByGlide(context, imageSetList.get(2).webPath, ivMomentPic2);
                } else if (picNum == 2) {
                    HttpUtil.loadImageByGlide(context, imageSetList.get(0).webPath, ivMomentPic0);
                    HttpUtil.loadImageByGlide(context, imageSetList.get(1).webPath, ivMomentPic1);
                    ivMomentPic2.setVisibility(View.GONE);
                } else if (picNum == 1) {
                    HttpUtil.loadImageByGlide(context, imageSetList.get(0).webPath, ivMomentPic0);
                    ivMomentPic1.setVisibility(View.GONE);
                    ivMomentPic2.setVisibility(View.GONE);
                } else if (picNum == 0) {
                    ivMomentPic0.setVisibility(View.GONE);
                    ivMomentPic1.setVisibility(View.GONE);
                    ivMomentPic2.setVisibility(View.GONE);
                }

            }
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
