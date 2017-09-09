package org.scau.mimi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.scau.mimi.R;
import org.scau.mimi.adapter.CommentAdapter;
import org.scau.mimi.gson.Comment;
import org.scau.mimi.gson.CommentsInfo;
import org.scau.mimi.gson.Info;
import org.scau.mimi.gson.MessagesInfo;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.NetworkUtil;
import org.scau.mimi.util.ResponseUtil;
import org.scau.mimi.util.TextUtil;
import org.scau.mimi.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import sumimakito.android.advtextswitcher.AdvTextSwitcher;

/**
 * Created by 10313 on 2017/9/4.
 */

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";

    //Constants
    private static final String EXTRA_MESSAGE = "message";

    //Views
    //Message部分
    private CircleImageView civPortrait;
    private TextView tvMomentNickname;
    private TextView tvMomentPostTime;
    private TextView tvMomentLocation;
    private TextView tvMomentTextContent;
    private ShineButton sbMomentLikeButton;
    private AdvTextSwitcher atsMomentLikeNumber;
    private AdvTextSwitcher atsMomentCommentCount;
    private ImageView ivMomentPic0;
    private ImageView ivMomentPic1;
    private ImageView ivMomentPic2;
    //Comment部分
    private Toolbar tbToolbar;
    private RecyclerView rvComment;
    private TextView tvAllComment;
    private EditText etWriteComment;
    private Button btnSendComment;

    //Test
    private TextView tvTest;
    private CollapsingToolbarLayout ctbl;

    //Variables
    private MessagesInfo.Content.Message mMessage;
    private List<ImageView> mPicList;
    private String mComment;
    private long mTimeAfter;
    private long mTimeBefore;
    private Transferee mTransferee;
    private boolean mIsOnLoadMore;
    private boolean mIsOnRefresh;
    private boolean mCanLoadMore;
    private boolean mCanRefresh;
    //Test
    private List<Comment> mCommentList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

//        Fade fade = new android.transition.Fade();
//        fade.setDuration(2000);
//        Slide slide = new Slide();
//        slide.setSlideEdge(Gravity.RIGHT);
        setTransition();

        loadData();
        initVariables();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransferee = Transferee.getDefault(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTransferee.destroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return true;
    }

    private void initVariables() {
        mCommentList = new ArrayList<>();
        mPicList = new ArrayList<>();
        mComment = "";
        mIsOnLoadMore = false;
        mIsOnRefresh = false;
    }

    private void initViews() {
        //Message part
        civPortrait = (CircleImageView) findViewById(R.id.civ_moment_portrait);
        tvMomentNickname = (TextView) findViewById(R.id.tv_moment_nickname);
        tvMomentPostTime = (TextView) findViewById(R.id.tv_moment_post_time);
        tvMomentLocation = (TextView) findViewById(R.id.tv_moment_location);
        tvMomentTextContent = (TextView) findViewById(R.id.tv_moment_text_content);
        ivMomentPic0 = (ImageView) findViewById(R.id.iv_moment_pic_0);
        ivMomentPic1 = (ImageView) findViewById(R.id.iv_moment_pic_1);
        ivMomentPic2 = (ImageView) findViewById(R.id.iv_moment_pic_2);
        sbMomentLikeButton = (ShineButton) findViewById(R.id.sb_moment_like_button);
        atsMomentLikeNumber = (AdvTextSwitcher) findViewById(R.id.ats_moment_like_number);
        atsMomentCommentCount = (AdvTextSwitcher) findViewById(R.id.ats_moment_comment_number);
        //Comment part
        tbToolbar = (Toolbar)findViewById(R.id.tb_comment_toolbar);
        rvComment = (RecyclerView)findViewById(R.id.rv_comment);
        tvAllComment = (TextView) findViewById(R.id.tv_comment_all_comment);
        etWriteComment = (EditText) findViewById(R.id.et_comment_write_comment);
        btnSendComment = (Button) findViewById(R.id.btn_comment_send_comment);


        //Message part
        if (mMessage.isFake)
            tvMomentNickname.setText(mMessage.fakeName);
        else
            tvMomentNickname.setText(mMessage.user.nname);

        tvMomentPostTime.setText(
                TextUtil.dateToString(new Date(mMessage.tmCreated))
        );
        tvMomentLocation.setText(mMessage.location.locale);
        tvMomentTextContent.setText(mMessage.content);

        mPicList.add(ivMomentPic0);
        mPicList.add(ivMomentPic1);
        mPicList.add(ivMomentPic2);
        final List<String> picUrls = new ArrayList<>();
        for (MessagesInfo.Content.Message.MessageImageSet mis :
                mMessage.messageImageSet) {
            picUrls.add(HttpUtil.ADDRESS + mis.webPath);
        }
        for (int i = 0; i < mMessage.messageImageSet.size(); i++) {
            HttpUtil.loadImageByGlide(this, mMessage.messageImageSet.get(i).webPath, mPicList.get(i));
            mPicList.get(i).setVisibility(View.VISIBLE);

            final int finalI = i;
            mPicList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferConfig config = TransferConfig.build()
                            .setOriginImageList(mPicList)
                            .setSourceImageList(picUrls)
                            .setNowThumbnailIndex(finalI)
                            .setMissDrawable(getResources().getDrawable(R.drawable.zs))
                            .setErrorDrawable(getResources().getDrawable(R.drawable.zs))
                            .setIndexIndicator(new NumberIndexIndicator())
                            .setProgressIndicator(new ProgressBarIndicator())
                            .setImageLoader(GlideImageLoader.with(CommentActivity.this))
                            .create();
                    mTransferee.apply(config).show();
                }
            });
        }
        

        sbMomentLikeButton.init(this);
        sbMomentLikeButton.setChecked(mMessage.isLike ? true : false);
        sbMomentLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sbMomentLikeButton.isChecked()) {
                    atsMomentLikeNumber.overrideText(String.valueOf(mMessage.likeCount + 1));
                    mMessage.isLike = true;
                } else {
                    atsMomentLikeNumber.overrideText(String.valueOf(mMessage.likeCount));
                    mMessage.isLike = false;
                }
            }
        });


        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setAdapter(new CommentAdapter(mCommentList));


        tvAllComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkAvailable(CommentActivity.this)) {
                    if (!mIsOnLoadMore && !mIsOnRefresh) {
                        mIsOnRefresh = true;
                        onRefreshComment();
                    }

                } else {
                    ToastUtil.toastWhenOnUiThread("网络不可用");
                }



            }
        });

        etWriteComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mComment = s.toString();
            }
        });

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mIsOnRefresh && !mIsOnLoadMore && !recyclerView.canScrollVertically(1) && mCommentList.size() != 0
                        && mCommentList.get(mCommentList.size() - 1).type != Comment.TYPE_NO_MORE) {
                    mIsOnLoadMore = true;
                    onLoadMoreComment();
                }
            }
        });

        if (NetworkUtil.isNetworkAvailable(this)) {
            mIsOnRefresh = true;
            onRefreshComment();

        } else {
            ToastUtil.toastWhenOnUiThread("网络不可用");
        }



    }

    private void onLoadMoreComment() {
        Comment comment = new Comment();
        comment.type = Comment.TYPE_FOOTER;
        mCommentList.add(comment);
        rvComment.getAdapter().notifyDataSetChanged();

        HttpUtil.requestCommentsBefore(mMessage.mid, mTimeBefore, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toastWhenOnUiThread("加载失败");
                        mCommentList.remove(mCommentList.size() - 1);
                        rvComment.getAdapter().notifyDataSetChanged();
                        mIsOnLoadMore = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommentsInfo commentsInfo = ResponseUtil.getCommentsInfo(response);
                List<Comment> comments = commentsInfo.content.commentList;
                if (comments.size() != 0) {
                    mCommentList.addAll(mCommentList.size() - 1, comments);
                    mTimeAfter = mCommentList.get(0).tmCreated + 1;
                    mTimeBefore = mCommentList.get(mCommentList.size() - 2).tmCreated - 1;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commentsInfo.content.numThisPage < commentsInfo.content.numPerPage) {
                            Comment c = new Comment();
                            c.type = Comment.TYPE_NO_MORE;
                            mCommentList.add(mCommentList.size() - 1, c);
                        }
                        mCommentList.remove(mCommentList.size() - 1);
                        rvComment.getAdapter().notifyDataSetChanged();
                        mIsOnLoadMore = false;
                    }
                });
            }
        });


    }

    private void onRefreshComment() {
        Comment comment = new Comment();
        comment.type = Comment.TYPE_HEADER;
        mCommentList.add(0, comment);
        rvComment.smoothScrollToPosition(0);
        rvComment.getAdapter().notifyDataSetChanged();

        if (mCommentList.size() > 1) {
            HttpUtil.requestCommentsAfter(mMessage.mid, mTimeAfter, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toastWhenOnUiThread("加载失败");
                            mCommentList.remove(0);
                            rvComment.getAdapter().notifyDataSetChanged();
                            mIsOnRefresh = false;
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    CommentsInfo commentsInfo = ResponseUtil.getCommentsInfo(response);
                    List<Comment> comments = commentsInfo.content.commentList;
                    mCommentList.remove(0);
                    if (comments.size() != 0) {
                        mCommentList.addAll(0, comments);
                        mTimeAfter = mCommentList.get(0).tmCreated + 1;
                        mTimeBefore = mCommentList.get(mCommentList.size() - 1).tmCreated - 1;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvComment.getAdapter().notifyDataSetChanged();
                            mIsOnRefresh = false;
                        }
                    });
                }
            });
        } else {
            HttpUtil.requestCommentsBefore(mMessage.mid, mTimeBefore, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toastWhenOnUiThread("加载失败");
                            mCommentList.remove(0);
                            rvComment.getAdapter().notifyDataSetChanged();
                            mIsOnRefresh = false;
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final CommentsInfo commentsInfo = ResponseUtil.getCommentsInfo(response);
                    List<Comment> comments = commentsInfo.content.commentList;
                    mCommentList.remove(0);
                    if (comments.size() != 0) {
                        mCommentList.addAll(0, comments);
                        mTimeAfter = mCommentList.get(0).tmCreated + 1;
                        mTimeBefore = mCommentList.get(mCommentList.size() - 1).tmCreated - 1;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (commentsInfo.content.numThisPage < commentsInfo.content.numPerPage) {
                                Comment comment1 = new Comment();
                                comment1.type = Comment.TYPE_NO_MORE;
                                mCommentList.add(comment1);

                            }
                            rvComment.getAdapter().notifyDataSetChanged();
                            mIsOnRefresh = false;
                        }
                    });
                }
            });
        }
    }

    private void loadData() {

        mTimeAfter = new Date().getTime();
        mTimeBefore = mTimeAfter;

        mMessage = (MessagesInfo.Content.Message) getIntent().getSerializableExtra(EXTRA_MESSAGE);



    }


    public static void actionStart(Context context, MessagesInfo.Content.Message message) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

    public static void actionStartByTransition(Activity activity, MessagesInfo.Content.Message message) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
        activity.startActivity(intent, optionsCompat.toBundle());
    }

    private void setTransition() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            getWindow().setEnterTransition(explode);
        } else {

        }

    }

    private void sendComment() {
        if (!mComment.equals("")) {
            HttpUtil.sendComment(mComment, mMessage.mid, null, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Info info = ResponseUtil.getInfo(response);
                    LogUtil.d(TAG, info.message);
                    if (info.code == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                etWriteComment.setText("");
                            }
                        });
                    } else {
                        LoginActivity.actionStart(CommentActivity.this);
                    }


                }
            });
        } else {
            Toast.makeText(CommentActivity.this, "评论内容为空", Toast.LENGTH_SHORT).show();
        }
    }


}
