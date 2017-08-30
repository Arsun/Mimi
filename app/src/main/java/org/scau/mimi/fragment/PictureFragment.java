package org.scau.mimi.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.scau.mimi.R;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10313 on 2017/8/29.
 */

public class PictureFragment extends BaseFragment {

    //Constants
    private static final String ARG_PIC_URL = "pic_url";
    private static final String ARG_THUMBNAIL_SIZE = "thumbnail_size";

    //Varibles
    private String mPicUrl;

    //Views
    private ImageView ivPic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);

        loadData();
        initVariables();
        initViews(view);

        return view;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initVariables() {
        mPicUrl = getArguments().getString(ARG_PIC_URL);
    }

    @Override
    protected void initViews(View view) {
        ivPic = (ImageView) view.findViewById(R.id.iv_picture_image);

        Glide.with(getActivity())
                .load(mPicUrl)
                .asBitmap()
                .dontAnimate()
                .into(
                        new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                ivPic.setImageBitmap(resource);
                            }
                        }
                );

        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(getActivity());
            }
        });

        ivPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }


    public static PictureFragment newInstance(String picUrl) {
        PictureFragment fragment = new PictureFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_PIC_URL, picUrl);

        fragment.setArguments(bundle);

        return fragment;
    }
}
