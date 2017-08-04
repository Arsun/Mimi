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
import org.scau.mimi.other.MultiScrollNumber;

import java.util.List;

/**
 * Created by 10313 on 2017/8/3.
 */

public class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Moment> mMoments;
    private Context mContext;

    static class NormalViewHolder extends RecyclerView.ViewHolder {

        ShineButton mShineButton;
        MultiScrollNumber mMultiScrollNumber;
        TextView mTextView;

        public NormalViewHolder(View itemView) {
            super(itemView);

            mShineButton = (ShineButton) itemView.findViewById(R.id.sb_like_button);
            mMultiScrollNumber = (MultiScrollNumber) itemView.findViewById(R.id.msn_like_number);
            mTextView = (TextView) itemView.findViewById(R.id.tv_moment_text);
        }

        public void bind(Moment moment,Context context) {
            mMultiScrollNumber.setNumber(moment.getLikeCount());
            mShineButton.init((Activity) context);
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
