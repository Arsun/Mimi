package org.scau.mimi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.scau.mimi.R;
import org.scau.mimi.activity.MainActivity;
import org.scau.mimi.activity.PictureActivity;
import org.scau.mimi.gson.MessagesInfo;
import org.scau.mimi.other.Constants;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.TextUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sumimakito.android.advtextswitcher.AdvTextSwitcher;

/**
 * Created by 10313 on 2017/8/3.
 */

public class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MomentAdapter";

    private List<MessagesInfo.Content.Message> mMessages;
    private Context mContext;

     static class NormalViewHolder extends RecyclerView.ViewHolder {
         Context mContext;

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
             mContext = itemView.getContext();



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

        public void bind(final MessagesInfo.Content.Message message) {
            final List<String> picUrls;
            picUrls = new ArrayList<>();
            for (int i = 0; i < message.messageImageSet.size(); i++) {
                picUrls.add(Constants.ADDRESS + message.messageImageSet.get(i).webPath);
            }


            if (message.isFake)
                tvNickname.setText(message.fakeName);
            else
                tvNickname.setText(message.user.nname);

            tvPostTime.setText(
                    TextUtil.dateToString(new Date(message.tmCreated))
            );
            tvLocation.setText(message.location.locale);
            tvTextContent.setText(message.content);
            LogUtil.d(TAG, message.content);

            sbLikeButton.init((Activity) mContext);

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

            int picNum = message.messageImageSet.size();
            LogUtil.d(TAG, "picNum: " + picNum);
            if (picNum == 3) {
                HttpUtil.loadImageByGlide(mContext, picUrls.get(0), ivMomentPic0);
                HttpUtil.loadImageByGlide(mContext, picUrls.get(1), ivMomentPic1);
                HttpUtil.loadImageByGlide(mContext, picUrls.get(2), ivMomentPic2);
                ivMomentPic0.setVisibility(View.VISIBLE);
                ivMomentPic1.setVisibility(View.VISIBLE);
                ivMomentPic2.setVisibility(View.VISIBLE);

            } else if (picNum == 2) {
                HttpUtil.loadImageByGlide(mContext, picUrls.get(0), ivMomentPic0);
                HttpUtil.loadImageByGlide(mContext, picUrls.get(1), ivMomentPic1);
                ivMomentPic0.setVisibility(View.VISIBLE);
                ivMomentPic1.setVisibility(View.VISIBLE);
                ivMomentPic2.setVisibility(View.GONE);

            } else if (picNum == 1) {
                HttpUtil.loadImageByGlide(mContext, picUrls.get(0), ivMomentPic0);
                ivMomentPic0.setVisibility(View.VISIBLE);
                ivMomentPic1.setVisibility(View.GONE);
                ivMomentPic2.setVisibility(View.GONE);


            } else if (picNum == 0) {
                ivMomentPic0.setVisibility(View.GONE);
                ivMomentPic1.setVisibility(View.GONE);
                ivMomentPic2.setVisibility(View.GONE);
            }


            //使用transferee
            final List<ImageView> imageViewList = new ArrayList<>();
            imageViewList.add(ivMomentPic0);
            imageViewList.add(ivMomentPic1);
            imageViewList.add(ivMomentPic2);
            ivMomentPic0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferConfig config = TransferConfig.build()
                            .setOriginImageList(imageViewList)
                            .setSourceImageList(picUrls)
                            .setNowThumbnailIndex(0)
                            .setMissDrawable(mContext.getResources().getDrawable(R.drawable.zs))
                            .setErrorDrawable(mContext.getResources().getDrawable(R.drawable.zs))
                            .setIndexIndicator(new NumberIndexIndicator())
                            .setProgressIndicator(new ProgressBarIndicator())
                            .setImageLoader(GlideImageLoader.with(mContext))
                            .create();
                    ((MainActivity)mContext).getTransferee().apply(config).show();
                }
            });

            ivMomentPic1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferConfig config = TransferConfig.build()
                            .setOriginImageList(imageViewList)
                            .setSourceImageList(picUrls)
                            .setNowThumbnailIndex(1)
                            .setMissDrawable(mContext.getResources().getDrawable(R.drawable.zs))
                            .setErrorDrawable(mContext.getResources().getDrawable(R.drawable.zs))
                            .setIndexIndicator(new NumberIndexIndicator())
                            .setProgressIndicator(new ProgressBarIndicator())
                            .setImageLoader(GlideImageLoader.with(mContext))
                            .create();
                    ((MainActivity)mContext).getTransferee().apply(config).show();
                }
            });

            ivMomentPic2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferConfig config = TransferConfig.build()
                            .setOriginImageList(imageViewList)
                            .setSourceImageList(picUrls)
                            .setNowThumbnailIndex(2)
                            .setMissDrawable(mContext.getResources().getDrawable(R.drawable.zs))
                            .setErrorDrawable(mContext.getResources().getDrawable(R.drawable.zs))
                            .setIndexIndicator(new NumberIndexIndicator())
                            .setProgressIndicator(new ProgressBarIndicator())
                            .setImageLoader(GlideImageLoader.with(mContext))
                            .create();
                    ((MainActivity)mContext).getTransferee().apply(config).show();
                }
            });

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
        ((NormalViewHolder)holder).bind(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
