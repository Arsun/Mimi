package org.scau.mimi.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.scau.mimi.R;
import org.scau.mimi.activity.LoginActivity;
import org.scau.mimi.activity.MainActivity;
import org.scau.mimi.activity.SendMomentActivity;
import org.scau.mimi.base.BaseFragment;
import org.scau.mimi.gson.ImagesInfo;
import org.scau.mimi.gson.Info;
import org.scau.mimi.other.Constants;
import org.scau.mimi.util.HttpUtil;
import org.scau.mimi.util.LogUtil;
import org.scau.mimi.util.ResponseUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 10313 on 2017/8/24.
 */

public class SendMomentFragment extends BaseFragment {

    private static final String TAG = "SendMomentFragment";


    //Constants
    private static final int MAX_SELECTABLE_PIC_NUM = 3;
    //Request code
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_MATISSE = 2;

    //Varibles
    private String mTextContent;
    private List<String> mPicBase64Code;
    private int mLastSelectablePicNum;
    private List<Bitmap> mThumbnails;
    private int mLocationId;
    private List<ImageView> mImageViews;
    private List<Integer> imageIdList = new ArrayList<>();


    //Data
    private String mNickname;
    private List<String> mPicPaths;


    //Views
    private TextView tvNickname;
    private ImageButton ibClose;
    private Button btnSend;
    private TextInputLayout tilEditMoment;
    private EditText etEditMoment;
    private ImageView ivPic0;
    private ImageView ivPic1;
    private ImageView ivPic2;
    private ImageButton ibRemovePic0;
    private ImageButton ibRemovePic1;
    private ImageButton ibRemovePic2;
    private ImageButton ibAddPic;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_moment, container, false);

        loadData();
        initVariables();
        initViews(view);

        return view;
    }

    @Override
    protected void initVariables() {

        mTextContent = "";
        mLastSelectablePicNum = 3;

        mThumbnails = new ArrayList<>();
        mImageViews = new ArrayList<>();
        mPicPaths = new ArrayList<>();

        mPicBase64Code = new ArrayList<>();

    }

    @Override
    protected void initViews(View view) {

        tvNickname = (TextView) view.findViewById(R.id.tv_send_moment_nickname);
        ibClose = (ImageButton) view.findViewById(R.id.ib_send_moment_close);
        btnSend = (Button) view.findViewById(R.id.btn_send_moment_send);
        tilEditMoment = (TextInputLayout) view.findViewById(R.id.til_send_moment_edit);
        etEditMoment = (EditText) view.findViewById(R.id.et_send_moment_edit);
        ivPic0 = (ImageView) view.findViewById(R.id.iv_send_moment_pic_0);
        ivPic1 = (ImageView) view.findViewById(R.id.iv_send_moment_pic_1);
        ivPic2 = (ImageView) view.findViewById(R.id.iv_send_moment_pic_2);
        ibRemovePic0 = (ImageButton) view.findViewById(R.id.ib_send_moment_remove_pic_0);
        ibRemovePic1 = (ImageButton) view.findViewById(R.id.ib_send_moment_remove_pic_1);
        ibRemovePic2 = (ImageButton) view.findViewById(R.id.ib_send_moment_remove_pic_2);
        ibAddPic = (ImageButton) view.findViewById(R.id.ib_send_moment_add_pic);

        tvNickname.setText(Constants.NICKNAME);


        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                MainActivity.actionStart(getActivity());

            }
        });

        tvNickname.setText(mNickname);

        btnSend.setEnabled(false);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send message
                sendMessage();
            }
        });

        etEditMoment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextContent = s.toString();
                if (hasText()) {
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }
            }
        });


        mImageViews.add(ivPic0);
        mImageViews.add(ivPic1);
        mImageViews.add(ivPic2);
        ivPic0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferConfig config = TransferConfig.build()
                        .setOriginImageList(mImageViews)
                        .setSourceImageList(mPicPaths)
                        .setImageLoader(GlideImageLoader.with(getActivity()))
                        .setIndexIndicator(new NumberIndexIndicator())
                        .setProgressIndicator(new ProgressBarIndicator())
                        .setNowThumbnailIndex(0)
                        .setOnLongClcikListener(new Transferee.OnTransfereeLongClickListener() {
                            @Override
                            public void onLongClick(ImageView imageView, int i) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                        .setView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_popup_window, null))
                                        .setCancelable(true)
                                        .create();
                                alertDialog.show();
                            }
                        })
                        .create();
                ((SendMomentActivity)getActivity()).getTransferee().apply(config).show();
            }
        });

        ivPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferConfig config = TransferConfig.build()
                        .setOriginImageList(mImageViews)
                        .setSourceImageList(mPicPaths)
                        .setImageLoader(GlideImageLoader.with(getActivity()))
                        .setIndexIndicator(new NumberIndexIndicator())
                        .setProgressIndicator(new ProgressBarIndicator())
                        .setNowThumbnailIndex(1)
                        .create();
                ((SendMomentActivity)getActivity()).getTransferee().apply(config).show();
            }
        });

        ivPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferConfig config = TransferConfig.build()
                        .setOriginImageList(mImageViews)
                        .setSourceImageList(mPicPaths)
                        .setImageLoader(GlideImageLoader.with(getActivity()))
                        .setIndexIndicator(new NumberIndexIndicator())
                        .setProgressIndicator(new ProgressBarIndicator())
                        .setNowThumbnailIndex(2)
                        .create();
                ((SendMomentActivity)getActivity()).getTransferee().apply(config).show();
            }
        });

        ivPic0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        ibRemovePic0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePic0();
            }
        });

        ibRemovePic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePic1();
            }
        });

        ibRemovePic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePic2();
            }
        });

        ibAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPic();
            }
        });

    }

    private void removePic0() {
        int num = getVisiblePicNum();
        if (num == 1) {
            ivPic0.setVisibility(View.GONE);
            ibRemovePic0.setVisibility(View.GONE);

        } else if (num == 2) {
            if (mThumbnails.get(1) != null) {
                ivPic0.setImageBitmap(mThumbnails.get(1));
                ivPic1.setVisibility(View.GONE);
                ibRemovePic1.setVisibility(View.GONE);

            }
        } else if (num == 3) {
            ivPic0.setImageBitmap(mThumbnails.get(1));
            ivPic1.setImageBitmap(mThumbnails.get(2));
            ivPic2.setVisibility(View.GONE);
            ibRemovePic2.setVisibility(View.GONE);
        }

        if (imageIdList.size() >= 1) {
            imageIdList.remove(0);
        }


        if (mThumbnails.get(0) != null && !mThumbnails.get(0).isRecycled()) {
            mThumbnails.get(0).recycle();
            mThumbnails.remove(0);
        }
        if (mPicBase64Code.size() > 0) {
            mPicBase64Code.remove(0);
        }
        mLastSelectablePicNum++;
        mPicPaths.remove(0);

        LogUtil.d(TAG, "picPaths: " + mPicPaths.size());

        ibAddPic.setVisibility(View.VISIBLE);
        if (!canSend()) {
            btnSend.setEnabled(false);
        }
    }

    private void removePic1() {
        int num = getVisiblePicNum();
        if (num == 2) {
            ivPic1.setVisibility(View.GONE);
            ibRemovePic1.setVisibility(View.GONE);
        } else if (num == 3) {
            if (mThumbnails.get(2) != null) {
                ivPic1.setImageBitmap(mThumbnails.get(2));
                ivPic2.setVisibility(View.GONE);
                ibRemovePic2.setVisibility(View.GONE);
            }
        }

        if (imageIdList.size() > 1) {
            imageIdList.remove(1);
        }

        if (mThumbnails.get(1) != null && !mThumbnails.get(1).isRecycled()) {
            mThumbnails.get(1).recycle();
            mThumbnails.remove(1);
        }
        if (mPicBase64Code.size() > 1) {
            mPicBase64Code.remove(1);
        }
        mLastSelectablePicNum++;
        mPicPaths.remove(1);
        ibAddPic.setVisibility(View.VISIBLE);
        if (!canSend()) {
            btnSend.setEnabled(false);
        }
    }

    private void removePic2() {
        ivPic2.setVisibility(View.GONE);
        ibRemovePic2.setVisibility(View.GONE);

        if (imageIdList.size() >= 3) {
            imageIdList.remove(2);
        }

        if (mThumbnails.get(2) != null && !mThumbnails.get(2).isRecycled()) {
            mThumbnails.get(2).recycle();
            mThumbnails.remove(2);
        }
        if (mPicBase64Code.size() > 2) {
            mPicBase64Code.remove(2);
        }
        mLastSelectablePicNum++;
        mPicPaths.remove(2);
        ibAddPic.setVisibility(View.VISIBLE);
        if (!canSend()) {
            btnSend.setEnabled(false);
        }
    }

    private void addPic() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(getActivity()
                    , new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    , REQUEST_CODE_PERMISSION);
        } else {
            startMatisse();
        }
    }

    private void sendMessage() {
        /**
         * 1、先检查网络
         * 2、网络可用：
         * 先显示进度框，
         *
         *
         *
         */

        new Thread(new Runnable() {
            @Override
            public void run() {

                int num = mPicPaths.size();
                for (int i = 0; i < num; i++) {
                    if (i > mPicBase64Code.size() - 1) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
//                        BitmapFactory.decodeFile(mPicPaths.get(i), options);
//                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        Bitmap bitmap;
                        bitmap = BitmapFactory.decodeFile(mPicPaths.get(i), options);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        LogUtil.d(TAG, "length " + i + " " + bytes.length / 1024);
                        double ratio = bytes.length / 1024.00 / 400.00;
                        LogUtil.d(TAG, "ratio " + i + " " + ratio);
                        byte[] encode;
                        if (ratio > 1.0) {
                            bitmap.recycle();
                            bitmap = null;
                            baos.reset();
                            options.inSampleSize = (int)ratio + 1;
                            bitmap = BitmapFactory.decodeFile(mPicPaths.get(i), options);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bytes1 = baos.toByteArray();
                            LogUtil.d(TAG, "length " + i + " " + bytes1.length / 1024);
                            encode = Base64.encode(bytes1, Base64.DEFAULT);
                        } else {
                            encode = Base64.encode(bytes, Base64.DEFAULT);
                        }
                        mPicBase64Code.add(new String(encode));
                        bitmap.recycle();
//                        for (double d = 400.00; d > 300.00; options.inSampleSize = options.inSampleSize * 2
//                                , options.outHeight = options.outHeight / 2
//                                , options.outWidth = options.outWidth / 2) {
//                            bitmap = BitmapFactory.decodeFile(mPicPaths.get(i), options);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
//                            byte[] bytes = baos.toByteArray();
//                            d = bytes.length / 1024;
//                            LogUtil.d(TAG, "length: " + i + "  " + d);
//                            if (d <= 300.00) {
//                                byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
//                                mPicBase64Code.add(new String(encode));
//                                bitmap.recycle();
////                                LogUtil.d(TAG, mPicBase64Code.get(i));
//                            }
//
//                        }
                    }
                }

                LogUtil.d(TAG, "base64 string encode done.");

                if (mPicBase64Code.size() == mPicPaths.size()) {


                    if (num == 0) {
                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Info info = ResponseUtil.getInfo(response);

                                if (info.code != 200) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //处理草稿箱逻辑
                                    LoginActivity.actionStart(getActivity());
                                } else {
                                    MainActivity.actionStart(getActivity());
                                }

                                getActivity().finish();
                            }
                        });
                    } else if (num == 1) {
                        if (imageIdList.size() == 0) {
                            HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d(TAG, "failed 0.");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                    int id = con.imageid;
                                    String path = con.webPath;
                                    LogUtil.d(TAG, "imageId: " + id);
                                    LogUtil.d(TAG, "image path: " + path);
                                    imageIdList.add(
                                            con.imageid
                                    );
                                    HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Info info = ResponseUtil.getInfo(response);

                                            if (info.code != 200) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //处理草稿箱逻辑
                                                LoginActivity.actionStart(getActivity());
                                            } else {
                                                MainActivity.actionStart(getActivity());
                                            }

                                            getActivity().finish();
                                        }
                                    });
                                }
                            });
                        } else {
                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Info info = ResponseUtil.getInfo(response);

                                    if (info.code != 200) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //处理草稿箱逻辑
                                        LoginActivity.actionStart(getActivity());
                                    } else {
                                        MainActivity.actionStart(getActivity());
                                    }

                                    getActivity().finish();
                                }
                            });
                        }
                    } else if (num == 2) {
                        if (imageIdList.size() == 0) {
                            HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d(TAG, "failed 0.");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                    int id = con.imageid;
                                    String path = con.webPath;
                                    LogUtil.d(TAG, "imageId: " + id);
                                    LogUtil.d(TAG, "image path: " + path);
                                    imageIdList.add(
                                            con.imageid
                                    );
                                    HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            LogUtil.d(TAG, "failed 1.");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                            int id = con.imageid;
                                            String path = con.webPath;
                                            LogUtil.d(TAG, "imageId: " + id);
                                            LogUtil.d(TAG, "image path: " + path);
                                            imageIdList.add(
                                                    con.imageid
                                            );
                                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {

                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    Info info = ResponseUtil.getInfo(response);

                                                    if (info.code != 200) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        //处理草稿箱逻辑
                                                        LoginActivity.actionStart(getActivity());
                                                    } else {
                                                        MainActivity.actionStart(getActivity());
                                                    }

                                                    getActivity().finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else if (imageIdList.size() == 1) {
                            HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d(TAG, "failed 1.");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                    int id = con.imageid;
                                    String path = con.webPath;
                                    LogUtil.d(TAG, "imageId: " + id);
                                    LogUtil.d(TAG, "image path: " + path);
                                    imageIdList.add(
                                            con.imageid
                                    );
                                    HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Info info = ResponseUtil.getInfo(response);

                                            if (info.code != 200) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //处理草稿箱逻辑
                                                LoginActivity.actionStart(getActivity());
                                            } else {
                                                MainActivity.actionStart(getActivity());
                                            }

                                            getActivity().finish();
                                        }
                                    });
                                }
                            });
                        } else if (imageIdList.size() == 2) {
                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Info info = ResponseUtil.getInfo(response);

                                    if (info.code != 200) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //处理草稿箱逻辑
                                        LoginActivity.actionStart(getActivity());
                                    } else {
                                        MainActivity.actionStart(getActivity());
                                    }

                                    getActivity().finish();
                                }
                            });
                        }
                    } else if (num == 3) {
                        if (imageIdList.size() == 0) {
                            HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d(TAG, "failed 0.");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                    int id = con.imageid;
                                    String path = con.webPath;
                                    LogUtil.d(TAG, "imageId: " + id);
                                    LogUtil.d(TAG, "image path: " + path);
                                    imageIdList.add(
                                            con.imageid
                                    );
                                    HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            LogUtil.d(TAG, "failed 1.");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                            int id = con.imageid;
                                            String path = con.webPath;
                                            LogUtil.d(TAG, "imageId: " + id);
                                            LogUtil.d(TAG, "image path: " + path);
                                            imageIdList.add(
                                                    con.imageid
                                            );
                                            HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    LogUtil.d(TAG, "failed 2.");
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                                    int id = con.imageid;
                                                    String path = con.webPath;
                                                    LogUtil.d(TAG, "imageId: " + id);
                                                    LogUtil.d(TAG, "image path: " + path);
                                                    imageIdList.add(
                                                            con.imageid
                                                    );
                                                    HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {

                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            Info info = ResponseUtil.getInfo(response);

                                                            if (info.code != 200) {
                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                //处理草稿箱逻辑
                                                                LoginActivity.actionStart(getActivity());
                                                            } else {
                                                                MainActivity.actionStart(getActivity());
                                                            }

                                                            getActivity().finish();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else if (imageIdList.size() == 1) {
                            HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d(TAG, "failed 1.");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                    int id = con.imageid;
                                    String path = con.webPath;
                                    LogUtil.d(TAG, "imageId: " + id);
                                    LogUtil.d(TAG, "image path: " + path);
                                    imageIdList.add(
                                            con.imageid
                                    );
                                    HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            LogUtil.d(TAG, "failed 2.");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                            int id = con.imageid;
                                            String path = con.webPath;
                                            LogUtil.d(TAG, "imageId: " + id);
                                            LogUtil.d(TAG, "image path: " + path);
                                            imageIdList.add(
                                                    con.imageid
                                            );
                                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {

                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    Info info = ResponseUtil.getInfo(response);

                                                    if (info.code != 200) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        //处理草稿箱逻辑
                                                        LoginActivity.actionStart(getActivity());
                                                    } else {
                                                        MainActivity.actionStart(getActivity());
                                                    }

                                                    getActivity().finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else if (imageIdList.size() == 2) {
                            HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d(TAG, "failed 2.");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
                                    int id = con.imageid;
                                    String path = con.webPath;
                                    LogUtil.d(TAG, "imageId: " + id);
                                    LogUtil.d(TAG, "image path: " + path);
                                    imageIdList.add(
                                            con.imageid
                                    );
                                    HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Info info = ResponseUtil.getInfo(response);

                                            if (info.code != 200) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //处理草稿箱逻辑
                                                LoginActivity.actionStart(getActivity());
                                            } else {
                                                MainActivity.actionStart(getActivity());
                                            }

                                            getActivity().finish();
                                        }
                                    });
                                }
                            });
                        } else if (imageIdList.size() == 3) {
                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Info info = ResponseUtil.getInfo(response);

                                    if (info.code != 200) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //处理草稿箱逻辑
                                        LoginActivity.actionStart(getActivity());
                                    } else {
                                        MainActivity.actionStart(getActivity());
                                    }

                                    getActivity().finish();
                                }
                            });
                        }
                    }

                }

            }
        }).start();





//        if (num == 0) {
//            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Info info = ResponseUtil.getInfo(response);
//
//                    if (info.code != 200) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        //处理草稿箱逻辑
//                        LoginActivity.actionStart(getActivity());
//                    } else {
//                        MainActivity.actionStart(getActivity());
//                    }
//
//                    getActivity().finish();
//                }
//            });
//        } else if (num == 1) {
//            if (imageIdList.size() == 0) {
//                HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 0.");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                Info info = ResponseUtil.getInfo(response);
//
//                                if (info.code != 200) {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                    //处理草稿箱逻辑
//                                    LoginActivity.actionStart(getActivity());
//                                } else {
//                                    MainActivity.actionStart(getActivity());
//                                }
//
//                                getActivity().finish();
//                            }
//                        });
//                    }
//                });
//            } else {
//                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Info info = ResponseUtil.getInfo(response);
//
//                        if (info.code != 200) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            //处理草稿箱逻辑
//                            LoginActivity.actionStart(getActivity());
//                        } else {
//                            MainActivity.actionStart(getActivity());
//                        }
//
//                        getActivity().finish();
//                    }
//                });
//            }
//        } else if (num == 2) {
//            if (imageIdList.size() == 0) {
//                HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 0.");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                LogUtil.d(TAG, "failed 1.");
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                int id = con.imageid;
//                                String path = con.webPath;
//                                LogUtil.d(TAG, "imageId: " + id);
//                                LogUtil.d(TAG, "image path: " + path);
//                                imageIdList.add(
//                                        con.imageid
//                                );
//                                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                    @Override
//                                    public void onFailure(Call call, IOException e) {
//
//                                    }
//
//                                    @Override
//                                    public void onResponse(Call call, Response response) throws IOException {
//                                        Info info = ResponseUtil.getInfo(response);
//
//                                        if (info.code != 200) {
//                                            getActivity().runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                            //处理草稿箱逻辑
//                                            LoginActivity.actionStart(getActivity());
//                                        } else {
//                                            MainActivity.actionStart(getActivity());
//                                        }
//
//                                        getActivity().finish();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//            } else if (imageIdList.size() == 1) {
//                HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 1.");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                Info info = ResponseUtil.getInfo(response);
//
//                                if (info.code != 200) {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                    //处理草稿箱逻辑
//                                    LoginActivity.actionStart(getActivity());
//                                } else {
//                                    MainActivity.actionStart(getActivity());
//                                }
//
//                                getActivity().finish();
//                            }
//                        });
//                    }
//                });
//            } else if (imageIdList.size() == 2) {
//                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Info info = ResponseUtil.getInfo(response);
//
//                        if (info.code != 200) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            //处理草稿箱逻辑
//                            LoginActivity.actionStart(getActivity());
//                        } else {
//                            MainActivity.actionStart(getActivity());
//                        }
//
//                        getActivity().finish();
//                    }
//                });
//            }
//        } else if (num == 3) {
//            if (imageIdList.size() == 0) {
//                HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 0.");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                LogUtil.d(TAG, "failed 1.");
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                int id = con.imageid;
//                                String path = con.webPath;
//                                LogUtil.d(TAG, "imageId: " + id);
//                                LogUtil.d(TAG, "image path: " + path);
//                                imageIdList.add(
//                                        con.imageid
//                                );
//                                HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
//                                    @Override
//                                    public void onFailure(Call call, IOException e) {
//                                        LogUtil.d(TAG, "failed 2.");
//                                    }
//
//                                    @Override
//                                    public void onResponse(Call call, Response response) throws IOException {
//                                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                        int id = con.imageid;
//                                        String path = con.webPath;
//                                        LogUtil.d(TAG, "imageId: " + id);
//                                        LogUtil.d(TAG, "image path: " + path);
//                                        imageIdList.add(
//                                                con.imageid
//                                        );
//                                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                            @Override
//                                            public void onFailure(Call call, IOException e) {
//
//                                            }
//
//                                            @Override
//                                            public void onResponse(Call call, Response response) throws IOException {
//                                                Info info = ResponseUtil.getInfo(response);
//
//                                                if (info.code != 200) {
//                                                    getActivity().runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                    //处理草稿箱逻辑
//                                                    LoginActivity.actionStart(getActivity());
//                                                } else {
//                                                    MainActivity.actionStart(getActivity());
//                                                }
//
//                                                getActivity().finish();
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//            } else if (imageIdList.size() == 1) {
//                HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 1.");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                LogUtil.d(TAG, "failed 2.");
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                int id = con.imageid;
//                                String path = con.webPath;
//                                LogUtil.d(TAG, "imageId: " + id);
//                                LogUtil.d(TAG, "image path: " + path);
//                                imageIdList.add(
//                                        con.imageid
//                                );
//                                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                    @Override
//                                    public void onFailure(Call call, IOException e) {
//
//                                    }
//
//                                    @Override
//                                    public void onResponse(Call call, Response response) throws IOException {
//                                        Info info = ResponseUtil.getInfo(response);
//
//                                        if (info.code != 200) {
//                                            getActivity().runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                            //处理草稿箱逻辑
//                                            LoginActivity.actionStart(getActivity());
//                                        } else {
//                                            MainActivity.actionStart(getActivity());
//                                        }
//
//                                        getActivity().finish();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//            } else if (imageIdList.size() == 2) {
//                HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 2.");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                Info info = ResponseUtil.getInfo(response);
//
//                                if (info.code != 200) {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                    //处理草稿箱逻辑
//                                    LoginActivity.actionStart(getActivity());
//                                } else {
//                                    MainActivity.actionStart(getActivity());
//                                }
//
//                                getActivity().finish();
//                            }
//                        });
//                    }
//                });
//            } else if (imageIdList.size() == 3) {
//                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Info info = ResponseUtil.getInfo(response);
//
//                        if (info.code != 200) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            //处理草稿箱逻辑
//                            LoginActivity.actionStart(getActivity());
//                        } else {
//                            MainActivity.actionStart(getActivity());
//                        }
//
//                        getActivity().finish();
//                    }
//                });
//            }
//        }


//        show progress dialog
        //old old-------------------------------------------
//        final int num = mPicBase64Code.size();
//        if (num > 0) {
//
//            if (imageIdList.size() == 0) {
//                HttpUtil.uploadImage(mPicBase64Code.get(0), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d(TAG, "failed 0");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                        int id = con.imageid;
//                        String path = con.webPath;
//                        LogUtil.d(TAG, "imageId: " + id);
//                        LogUtil.d(TAG, "image path: " + path);
//                        imageIdList.add(
//                                con.imageid
//                        );
//                        if (num > 1) {
//                            HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    LogUtil.d(TAG, "failed 1");
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//                                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                    int id = con.imageid;
//                                    String path = con.webPath;
//                                    LogUtil.d(TAG, "imageId: " + id);
//                                    LogUtil.d(TAG, "image path: " + path);
//                                    imageIdList.add(
//                                            con.imageid
//                                    );
//                                    if (num > 2) {
//                                        HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
//                                            @Override
//                                            public void onFailure(Call call, IOException e) {
//                                                LogUtil.d(TAG, "failed 2");
//                                            }
//
//                                            @Override
//                                            public void onResponse(Call call, Response response) throws IOException {
//                                                ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                                int id = con.imageid;
//                                                String path = con.webPath;
//                                                LogUtil.d(TAG, "imageId: " + id);
//                                                LogUtil.d(TAG, "image path: " + path);
//                                                imageIdList.add(
//                                                        con.imageid
//                                                );
//                                                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                                    @Override
//                                                    public void onFailure(Call call, IOException e) {
//
//                                                    }
//
//                                                    @Override
//                                                    public void onResponse(Call call, Response response) throws IOException {
//                                                        Info info = ResponseUtil.getInfo(response);
//
//                                                        if (info.code != 200) {
//                                                            getActivity().runOnUiThread(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                                }
//                                                            });
//                                                            //处理草稿箱逻辑
//                                                            LoginActivity.actionStart(getActivity());
//                                                        } else {
//                                                            MainActivity.actionStart(getActivity());
//                                                        }
//
//                                                        getActivity().finish();
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    } else {
//                                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                            @Override
//                                            public void onFailure(Call call, IOException e) {
//
//                                            }
//
//                                            @Override
//                                            public void onResponse(Call call, Response response) throws IOException {
//                                                Info info = ResponseUtil.getInfo(response);
//
//                                                if (info.code != 200) {
//                                                    getActivity().runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                    //处理草稿箱逻辑
//                                                    LoginActivity.actionStart(getActivity());
//                                                } else {
//                                                    MainActivity.actionStart(getActivity());
//                                                }
//
//                                                getActivity().finish();
//                                            }
//                                        });
//                                    }
//                                }
//                            });
//                        } else {
//                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//                                    Info info = ResponseUtil.getInfo(response);
//
//                                    if (info.code != 200) {
//                                        getActivity().runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                        //处理草稿箱逻辑
//                                        LoginActivity.actionStart(getActivity());
//                                    } else {
//                                        MainActivity.actionStart(getActivity());
//                                    }
//
//                                    getActivity().finish();
//                                }
//                            });
//                        }
//                    }
//                });
//            } else {
//
//                if (num > 1) {
//
//                    if (imageIdList.size() <= 1) {
//                        HttpUtil.uploadImage(mPicBase64Code.get(1), new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                LogUtil.d(TAG, "failed 1");
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                int id = con.imageid;
//                                String path = con.webPath;
//                                LogUtil.d(TAG, "imageId: " + id);
//                                LogUtil.d(TAG, "image path: " + path);
//                                imageIdList.add(
//                                        con.imageid
//                                );
//                                if (num > 2) {
//                                    HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
//                                        @Override
//                                        public void onFailure(Call call, IOException e) {
//                                            LogUtil.d(TAG, "failed 2");
//                                        }
//
//                                        @Override
//                                        public void onResponse(Call call, Response response) throws IOException {
//                                            ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                            int id = con.imageid;
//                                            String path = con.webPath;
//                                            LogUtil.d(TAG, "imageId: " + id);
//                                            LogUtil.d(TAG, "image path: " + path);
//                                            imageIdList.add(
//                                                    con.imageid
//                                            );
//                                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                                @Override
//                                                public void onFailure(Call call, IOException e) {
//
//                                                }
//
//                                                @Override
//                                                public void onResponse(Call call, Response response) throws IOException {
//                                                    Info info = ResponseUtil.getInfo(response);
//
//                                                    if (info.code != 200) {
//                                                        getActivity().runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//                                                        //处理草稿箱逻辑
//                                                        LoginActivity.actionStart(getActivity());
//                                                    } else {
//                                                        MainActivity.actionStart(getActivity());
//                                                    }
//
//                                                    getActivity().finish();
//                                                }
//                                            });
//                                        }
//                                    });
//                                } else {
//                                    HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                        @Override
//                                        public void onFailure(Call call, IOException e) {
//
//                                        }
//
//                                        @Override
//                                        public void onResponse(Call call, Response response) throws IOException {
//                                            Info info = ResponseUtil.getInfo(response);
//
//                                            if (info.code != 200) {
//                                                getActivity().runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                                //处理草稿箱逻辑
//                                                LoginActivity.actionStart(getActivity());
//                                            } else {
//                                                MainActivity.actionStart(getActivity());
//                                            }
//
//                                            getActivity().finish();
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    } else {
//
//                        if (num > 2) {
//
//                            if (imageIdList.size() <= 2) {
//                                HttpUtil.uploadImage(mPicBase64Code.get(2), new Callback() {
//                                    @Override
//                                    public void onFailure(Call call, IOException e) {
//                                        LogUtil.d(TAG, "failed 2");
//                                    }
//
//                                    @Override
//                                    public void onResponse(Call call, Response response) throws IOException {
//                                        ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                                        int id = con.imageid;
//                                        String path = con.webPath;
//                                        LogUtil.d(TAG, "imageId: " + id);
//                                        LogUtil.d(TAG, "image path: " + path);
//                                        imageIdList.add(
//                                                con.imageid
//                                        );
//                                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                            @Override
//                                            public void onFailure(Call call, IOException e) {
//
//                                            }
//
//                                            @Override
//                                            public void onResponse(Call call, Response response) throws IOException {
//                                                Info info = ResponseUtil.getInfo(response);
//
//                                                if (info.code != 200) {
//                                                    getActivity().runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                    //处理草稿箱逻辑
//                                                    LoginActivity.actionStart(getActivity());
//                                                } else {
//                                                    MainActivity.actionStart(getActivity());
//                                                }
//
//                                                getActivity().finish();
//                                            }
//                                        });
//                                    }
//                                });
//                            } else {
//                                HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                    @Override
//                                    public void onFailure(Call call, IOException e) {
//
//                                    }
//
//                                    @Override
//                                    public void onResponse(Call call, Response response) throws IOException {
//                                        Info info = ResponseUtil.getInfo(response);
//
//                                        if (info.code != 200) {
//                                            getActivity().runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                            //处理草稿箱逻辑
//                                            LoginActivity.actionStart(getActivity());
//                                        } else {
//                                            MainActivity.actionStart(getActivity());
//                                        }
//
//                                        getActivity().finish();
//                                    }
//                                });
//                            }
//
//                        } else {
//                            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//                                    Info info = ResponseUtil.getInfo(response);
//
//                                    if (info.code != 200) {
//                                        getActivity().runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                        //处理草稿箱逻辑
//                                        LoginActivity.actionStart(getActivity());
//                                    } else {
//                                        MainActivity.actionStart(getActivity());
//                                    }
//
//                                    getActivity().finish();
//                                }
//                            });
//                        }
//
//                    }
//
//                } else {
//                    HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            Info info = ResponseUtil.getInfo(response);
//
//                            if (info.code != 200) {
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                //处理草稿箱逻辑
//                                LoginActivity.actionStart(getActivity());
//                            } else {
//                                MainActivity.actionStart(getActivity());
//                            }
//
//                            getActivity().finish();
//                        }
//                    });
//                }
//
//            }
//
//        } else {
//            HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Info info = ResponseUtil.getInfo(response);
//
//                    if (info.code != 200) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        //处理草稿箱逻辑
//                        LoginActivity.actionStart(getActivity());
//                    } else {
//                        MainActivity.actionStart(getActivity());
//                    }
//
//                    getActivity().finish();
//                }
//            });
//        }
        //-----------------------------------------------------------------------


        //old-------------------------------------------------------
//        for (int i = 0; i < mPicBase64Code.size(); i++) {
//
//            LogUtil.d(TAG, "image: " + i);
////            LogUtil.d(TAG, "base64: " + mPicBase64Code.get(i));
//            HttpUtil.uploadImage(mPicBase64Code.get(i), new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    LogUtil.d(TAG, "failed");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    ImagesInfo.Content con = ResponseUtil.getImageIdAndWebpath(response);
//                    int id = con.imageid;
//                    String path = con.webPath;
//                    LogUtil.d(TAG, "imageId: " + id);
//                    LogUtil.d(TAG, "image path: " + path);
//                    imageIdList.add(
//                            con.imageid
//                    );
//                }
//            });
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for ( ; ; ) {
//                    if (imageIdList.size() == mPicBase64Code.size()) {
//                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                Info info = ResponseUtil.getInfo(response);
//
//                                if (info.code != 200) {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(getActivity(), "登录过期，请重新登陆", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                    //处理草稿箱逻辑
//                                    LoginActivity.actionStart(getActivity());
//                                } else {
//                                    MainActivity.actionStart(getActivity());
//                                }
//
//                                getActivity().finish();
//                            }
//                        });
//
//                        break;
//                    }
//                }
//            }
//        }).start();
        //old----------------------------------------------------------------------

    }

    @Override
    protected void loadData() {
        mLocationId = Constants.LID;
        mNickname = Constants.NICKNAME;
        mPicPaths = new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {

                for (int i = 0; i < grantResults.length; i++) {

                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "You denide the permission.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                startMatisse();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MATISSE && resultCode == RESULT_OK) {

            List<Uri> picUris = Matisse.obtainResult(data);
            int oldPicNum = mPicPaths.size();
            if (Build.VERSION.SDK_INT >= 19) {
                mPicPaths.addAll(getImagePathOnKitkat(picUris));
            } else {
                mPicPaths.addAll(getImagePathBeforeKitKat(picUris));
            }

            //显示图片，并且添加到bitmapList中
            displayThumbnail(oldPicNum);

//            encodeBitmapToBase64(mPicPaths);

            if (canSend()) {
                btnSend.setEnabled(true);
            }

            if (ivPic0.getVisibility() == View.VISIBLE
                    && ivPic1.getVisibility() == View.VISIBLE
                    && ivPic2.getVisibility() == View.VISIBLE) {
                ibAddPic.setVisibility(View.GONE);
            }

        }

    }

    private void encodeBitmapToBase64(final List<String> paths) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                int num = paths.size();
                for (int i = 0; i < num; i++) {
                    if (i > mPicBase64Code.size() - 1) {
                        Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
                        byte[] bytes = baos.toByteArray();
                        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
                        mPicBase64Code.add(new String(encode));
                        LogUtil.d(TAG, mPicBase64Code.get(i));
                        bitmap.recycle();
                    }
//                    if (i > mPicBase64Code.size() - 1) {
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        mThumbnails.get(i).compress(Bitmap.CompressFormat.PNG, 20, baos);
//                        byte[] bytes = baos.toByteArray();
//                        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
//                        mPicBase64Code.add(new String(encode));
//                        LogUtil.d(TAG, mPicBase64Code.get(i));
//                    }
                }


            }
        }).start();

    }

    private void displayThumbnail(int oldPicNum) {

        for (int i = oldPicNum; i < mPicPaths.size(); i++) {

            if (ivPic0.getVisibility() == View.GONE) {
                Bitmap bitmap = getThumbnailAndDisplay(mPicPaths.get(i), ivPic0);
                if (bitmap != null) {
                    mThumbnails.add(bitmap);
                }
                ivPic0.setVisibility(View.VISIBLE);
                ibRemovePic0.setVisibility(View.VISIBLE);
                mLastSelectablePicNum--;
            } else if (ivPic1.getVisibility() == View.GONE) {
                Bitmap bitmap = getThumbnailAndDisplay(mPicPaths.get(i), ivPic1);
                if (bitmap != null) {
                    mThumbnails.add(bitmap);
                }
                ivPic1.setVisibility(View.VISIBLE);
                ibRemovePic1.setVisibility(View.VISIBLE);
                mLastSelectablePicNum--;
            } else if (ivPic2.getVisibility() == View.GONE) {
                Bitmap bitmap = getThumbnailAndDisplay(mPicPaths.get(i), ivPic2);
                if (bitmap != null) {
                    mThumbnails.add(bitmap);
                }
                ivPic2.setVisibility(View.VISIBLE);
                ibRemovePic2.setVisibility(View.VISIBLE);
                mLastSelectablePicNum--;
            }

        }

    }

    public static SendMomentFragment newInstance() {
        return new SendMomentFragment();
    }

    private boolean hasText() {
        if (!mTextContent.equals("") && mTextContent != null) {
            return true;
        }
        return false;
    }

    private boolean canSend() {
        if (!mTextContent.equals("")) {
            return true;
        } else if (hasPic()) {
            return true;
        }
        return false;
    }

    private boolean hasPic() {
        if (ivPic0.getVisibility() == View.VISIBLE
                || ivPic1.getVisibility() == View.VISIBLE
                || ivPic2.getVisibility() == View.VISIBLE) {
            return true;
        }

        return false;
    }

    private boolean hasPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void startMatisse() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(mLastSelectablePicNum)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.matisse_pic_grid))
                .imageEngine(new GlideEngine())
                .theme(R.style.Matisse_Dracula)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .forResult(REQUEST_CODE_MATISSE);
    }

    @TargetApi(19)
    private List<String> getImagePathOnKitkat(List<Uri> uriList) {
        List<String> paths = new ArrayList<>();

        for (int i = 0; i < uriList.size(); i++) {

            Uri uri = uriList.get(i);
            if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    paths.add(
                            getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                    );
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                            "//downloads/public_downloads"), Long.valueOf(docId));
                    paths.add(
                            getImagePath(contentUri, null)
                    );
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                paths.add(
                        getImagePath(uri, null)
                );
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                paths.add(
                        uri.getPath()
                );
            }

        }

        return paths;
    }

    private List<String> getImagePathBeforeKitKat(List<Uri> uriList) {
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            Uri uri = uriList.get(i);
            paths.add(
                    getImagePath(uri, null)
            );
        }

        return paths;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private Bitmap getThumbnailAndDisplay(String imagePath, ImageView imageView) {
        Bitmap bitmap;

        if (imagePath != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            int dstHeight = 70;
            int dstWidth = 70;
            if (height > dstHeight || width > dstWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) > dstHeight
                        && (halfWidth / inSampleSize) > dstWidth) {
                    inSampleSize *= 2;
                }
//                int ratio1 = height / dstHeight;
//                int ratio2 = width / dstWidth;
//                options.inSampleSize = ratio1 > ratio2 ? ratio2 : ratio1;
            }
            options.inSampleSize = inSampleSize;

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(imagePath, options);
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            options.inSampleSize = calculateInSampleSize(options, imageView.getWidth(), imageView.getHeight());
//            options.outWidth = imageView.getWidth();
//            options.outHeight =imageView.getHeight();
            bitmap = BitmapFactory.decodeFile(imagePath,options);
            imageView.setImageBitmap(bitmap);
            return bitmap;
        } else {
            Toast.makeText(getActivity(), "无法获取图片", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int dstWidth, int dstHeight) {
        int inSampleSize = 1;
        final int width = options.outWidth;
        final int height = options.outWidth;
        if (width > dstWidth || height > dstHeight) {
            final int widthRatio = Math.round((float)width / (float)dstWidth);
            final int heightRatio = Math.round((float)height / (float)dstHeight);
            inSampleSize = widthRatio > heightRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    private int getVisiblePicNum() {
        int num = 0;
        if (ivPic0.getVisibility() == View.VISIBLE) {
            num++;
        }
        if (ivPic1.getVisibility() == View.VISIBLE) {
            num++;
        }
        if (ivPic2.getVisibility() == View.VISIBLE) {
            num++;
        }

        return num;
    }

}
