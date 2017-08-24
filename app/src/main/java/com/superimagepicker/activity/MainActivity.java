package com.superimagepicker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.adapter.GridItemDecoration;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.ui.ImagePreviewActivity;
import com.lqr.imagepicker.view.CropImageView;
import com.superimagepicker.R;
import com.superimagepicker.adapter.BaseRecyclerAdapter;
import com.superimagepicker.adapter.ImageListAdapter;
import com.superimagepicker.loader.GlideImgLoader;
import com.superimagepicker.utils.FileSizeUtils;
import com.superimagepicker.utils.GlideUtils;
import com.superimagepicker.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_image)
    ImageView mImageView;
    @Bind(R.id.tv_time)
    TextView mTextView;
    @Bind(R.id.tv_image2)
    ImageView mImageView2;
    @Bind(R.id.tv_time2)
    TextView mTextView2;
    @Bind(R.id.rb_compress)
    CheckBox mCheckBox;

    @Bind(R.id.card_view)
    CardView mCardView;

    private int mWidth;
    private int mLimit = 1;
    private boolean mMode;
    private boolean mCrop;

    private ImageListAdapter mImageListAdapter;
    private ArrayList<ImageItem> mImageItems = new ArrayList<>();
    private ArrayList<ImageItem> mImageItems120 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth();

        initRecyclerView();
        initCheckBox();
    }

    private boolean isCompress;

    private void initCheckBox() {
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCompress = b;
            }
        });
    }

    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initRecyclerView() {
        //设置布局管理器
        GridLayoutManager manager = new GridLayoutManager(this, 4);
//        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setNestedScrollingEnabled(false);
        //设置分隔线
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL));
        mRecyclerView.addItemDecoration(new GridItemDecoration(4, dip2px(this, 10), true));
        //设置动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置适配器
        mImageListAdapter = new ImageListAdapter(this, mImageItems, 8);
        mRecyclerView.setAdapter(mImageListAdapter);
        mImageListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                if (pos == mImageItems.size()) {
                    mMode = true;
                    mCrop = false;
                    mLimit = 8;
                    initImagePicker();
                    Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, mImageItems);
                    startActivityForResult(intent, 130);
                } else {
                    Intent intent = new Intent(MainActivity.this, ImagePreviewActivity.class);
                    intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, pos);
                    intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
                    intent.putExtra(ImagePreviewActivity.ISORIGIN, false);
                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
                }
            }
        });
        mImageListAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int pos) {
                if (pos != mImageItems.size()) {
                    mImageItems.remove(pos);
                    mImageListAdapter.notifyItemRemoved(pos);
                    mImageListAdapter.notifyItemRangeChanged(pos, mImageItems.size());
                }
            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImgLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                    //显示拍照按钮
        imagePicker.setMultiMode(mMode);                    //多选单选模式
        imagePicker.setCrop(mCrop);                         //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                 //是否按矩形区域保存
        imagePicker.setSelectLimit(mLimit);                 //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);//裁剪框的形状
        imagePicker.setFocusWidth(mWidth * 8 / 10);         //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(mWidth * 8 / 10);        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                       //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                       //保存文件的高度。单位像素
    }

    @OnClick({R.id.btn_camera, R.id.btn_single, R.id.btn_multi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_single: //单选，可拍照可选择图片，并剪切，修改头像
                mMode = false;
                mCrop = true;
                mLimit = 1;
                initImagePicker();
                startActivityForResult(new Intent(this, ImageGridActivity.class), 100);
                break;
            case R.id.btn_camera://单选，可拍照可选择图片，不可剪切，上传图片
                mMode = false;
                mCrop = false;
                mLimit = 1;
                initImagePicker();
                startActivityForResult(new Intent(this, ImageGridActivity.class), 110);
                break;
            case R.id.btn_multi://多选，可拍照可选择多张图片，不可剪切，上传图片
                mMode = true;
                mCrop = false;
                mLimit = 8;
                initImagePicker();
                startActivityForResult(new Intent(this, ImageGridActivity.class), 120);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == 130) {
            if (data != null) {
                //选择的图片集
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                //是否上传原图
                boolean isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                //压缩图片方法
                for (int i = 0; i < images.size(); i++) {
                    // 压缩前
                    LogUtils.log("压缩前图片的地址：" + images.get(i).path);
                    LogUtils.log("压缩前图片的大小:" + FileSizeUtils.getFileSize(images.get(i).path));
                    // 压缩后
//                    Bitmap bitmap = BitmapFactory.decodeFile(images.get(i).path);
//                    String filePath = BitmapUtils.compressBitmap(this, bitmap);

//                    String path;
//                    //判断是否有存储卡
//                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                        //有存储卡获取路径
//                        path = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                                "/" + SystemUtils.getPackageName(this) + "/cache/";
//                    } else {
//                        //没有存储卡的时候，存储到这个路径
//                        path = getApplicationContext().getFilesDir().getAbsolutePath() +
//                                "/" + SystemUtils.getPackageName(this) + "/cache/";
//                    }
//                    File file = new File(path);
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    try {
//                        String filePath = new Compressor(this).
//                                setDestinationDirectoryPath(path)
//                                .compressToFile(new File(images.get(i).path)).getAbsolutePath();
//                        LogUtils.log("压缩后图片的地址：" + filePath);
//                        LogUtils.log("压缩后图片的大小:" + FileSizeUtils.getFileSize(filePath));
//                        images.get(i).setPath(filePath);
//                        images.get(i).setSize(FileSizeUtils.getFileSizeLong(filePath));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

                mImageItems.clear();
                mImageItems.addAll(images);
                mImageListAdapter.notifyDataSetChanged();
            }
        }

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == 120) {
            if (data != null) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                mImageItems120.addAll(images);
                long start = System.currentTimeMillis();
                if (isCompress) {
                    GlideUtils.loadWithDefult(this, images.get(0).path, mImageView);
                    GlideUtils.loadWithDefult(this, images.get(0).path, mImageView2);
                } else {
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(images.get(0).path));
                    mImageView2.setImageBitmap(BitmapFactory.decodeFile(images.get(0).path));
                }
                long end = System.currentTimeMillis();
                mTextView.setText(String.format("%s%s", isCompress ? "压缩" : "不压缩", "耗时：" + (end - start) + "毫秒"));
                mTextView2.setText(String.format("%s%s", isCompress ? "压缩" : "不压缩", "耗时：" + (end - start) + "毫秒"));
                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ImagePreviewActivity.class);
                        intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
                        intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems120);
                        intent.putExtra(ImagePreviewActivity.ISORIGIN, false);
                        startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
//                        mMode = true;
//                        mCrop = false;
//                        mLimit = 8;
//                        initImagePicker();
//                        startActivityForResult(new Intent(MainActivity.this, ImageGridActivity.class), 120);
                    }
                });
            }
        }

    }
}
