package org.scau.mimi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import org.scau.mimi.R;
import org.scau.mimi.bean.Moment;

import java.util.List;

import sumimakito.android.advtextswitcher.AdvTextSwitcher;

/**
 * Created by 10313 on 2017/8/3.
 */

public class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Moment> mMoments;
    private Context mContext;

    static class NormalViewHolder extends RecyclerView.ViewHolder {

        ShineButton mShineButton;
        TextView mTextView;
        AdvTextSwitcher mLikeNumber;
        AdvTextSwitcher mCommentNumber;

        public NormalViewHolder(View itemView) {
            super(itemView);

            mShineButton = (ShineButton) itemView.findViewById(R.id.sb_like_button);
            mTextView = (TextView) itemView.findViewById(R.id.tv_moment_text);
            mLikeNumber = (AdvTextSwitcher) itemView.findViewById(R.id.ats_like_number);
            mCommentNumber = (AdvTextSwitcher) itemView.findViewById(R.id.ats_comment_number);
        }

        public void bind(Moment moment,Context context) {
            mShineButton.init((Activity) context);

            mShineButton.setChecked(false);
            mShineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLikeNumber.next();
                }
            });

            String[] texts = {"123", "134"};
            mLikeNumber.setTexts(texts);
            mCommentNumber.setTexts(texts);
        }
    }

    public MomentAdapter(List<Moment> moments) {
        mMoments = moments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_moment, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NormalViewHolder)holder).bind(mMoments.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mMoments.size();
    }
}
