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
    private List<Bitmap> mPics;
    private int mLocationId;
    private List<ImageView> mImageViews;


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

        mPics = new ArrayList<>();
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
            if (mPics.get(1) != null) {
                ivPic0.setImageBitmap(mPics.get(1));
                ivPic1.setVisibility(View.GONE);
                ibRemovePic1.setVisibility(View.GONE);

            }
        } else if (num == 3) {
            ivPic0.setImageBitmap(mPics.get(1));
            ivPic1.setImageBitmap(mPics.get(2));
            ivPic2.setVisibility(View.GONE);
            ibRemovePic2.setVisibility(View.GONE);
        }



        if (mPics.get(0) != null) {
            mPics.remove(0);
        }
        mPicBase64Code.remove(0);
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
            if (mPics.get(2) != null) {
                ivPic1.setImageBitmap(mPics.get(2));
                ivPic2.setVisibility(View.GONE);
                ibRemovePic2.setVisibility(View.GONE);
            }
        }

        if (mPics.get(1) != null) {
            mPics.remove(1);
        }
        mPicBase64Code.remove(1);
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

        if (mPics.get(2) != null) {
            mPics.remove(2);
        }
        mPicBase64Code.remove(2);
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
        //show progress dialog
        final List<Integer> imageIdList = new ArrayList<>();

        for (int i = 0; i < mPicBase64Code.size(); i++) {
            LogUtil.d(TAG, "image: " + i);
//            LogUtil.d(TAG, "base64: " + mPicBase64Code.get(i));
            HttpUtil.uploadImage(mPicBase64Code.get(i), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.d(TAG, "failed");
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
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for ( ; ; ) {
                    if (imageIdList.size() == mPicBase64Code.size()) {
                        HttpUtil.sendMessage(mTextContent, mLocationId, false, imageIdList, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Info info = ResponseUtil.hadSentMessage(response);

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

                        break;
                    }
                }
            }
        }).start();


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

            if (Build.VERSION.SDK_INT >= 19) {
                mPicPaths.addAll(getImagePathOnKitkat(picUris));
            } else {
                mPicPaths.addAll(getImagePathBeforeKitKat(picUris));
            }

            displayThumb();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    int num = mPics.size();
                    for (int i = 0; i < num; i++) {
                        if (i > mPicBase64Code.size() - 1) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            mPics.get(i).compress(Bitmap.CompressFormat.PNG, 50, baos);
                            byte[] bytes = baos.toByteArray();
                            byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
                            mPicBase64Code.add(new String(encode));
                        }
                    }


                }
            }).start();


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

    private void displayThumb() {

        for (int i = 0; i < mPicPaths.size(); i++) {

            if (ivPic0.getVisibility() == View.GONE) {
                Bitmap bitmap = getImageBitmap(mPicPaths.get(i), ivPic0);
                if (bitmap != null) {
                    mPics.add(bitmap);
                }
                ivPic0.setVisibility(View.VISIBLE);
                ibRemovePic0.setVisibility(View.VISIBLE);
                mLastSelectablePicNum--;
            } else if (ivPic1.getVisibility() == View.GONE) {
                Bitmap bitmap = getImageBitmap(mPicPaths.get(i), ivPic1);
                if (bitmap != null) {
                    mPics.add(bitmap);
                }
                ivPic1.setVisibility(View.VISIBLE);
                ibRemovePic1.setVisibility(View.VISIBLE);
                mLastSelectablePicNum--;
            } else if (ivPic2.getVisibility() == View.GONE) {
                Bitmap bitmap = getImageBitmap(mPicPaths.get(i), ivPic2);
                if (bitmap != null) {
                    mPics.add(bitmap);
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

    private Bitmap getImageBitmap(String imagePath, ImageView imageView) {

        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
            return bitmap;
        } else {
            Toast.makeText(getActivity(), "无法获取图片", Toast.LENGTH_SHORT).show();
        }
        return null;
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
