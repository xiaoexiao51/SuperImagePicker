package com.superimagepicker.adapter;

import android.content.Context;
import android.view.View;

import com.lqr.imagepicker.bean.ImageItem;
import com.superimagepicker.R;
import com.superimagepicker.utils.GlideUtils;

import java.util.List;


public class ImageListAdapter extends BaseRecyclerAdapter<ImageItem> {

    public static final int TYPE_ADDITEM = 1;
    public static final int TYPE_PICTURE = 2;
    private int selectMax = 8;

    public ImageListAdapter(Context ctx, List<ImageItem> list, int selectMax) {
        super(ctx, list);
        this.selectMax = selectMax;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_image_list;
    }

    @Override
    public int getItemCount() {
        if (mItems.size() < selectMax) {
            return mItems.size() + 1;
        } else {
            return mItems.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showAddItem(position)) {
            return TYPE_ADDITEM;
        } else {
            return TYPE_PICTURE;
        }
    }

    private boolean showAddItem(int position) {
        int size = mItems.size() == 0 ? 0 : mItems.size();
        return position == size;
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, final int position, ImageItem item) {
        if (item == null && getItemViewType(position) == TYPE_ADDITEM) {
            holder.getView(R.id.ll_delete).setVisibility(View.GONE);
            holder.getImageView(R.id.tv_image).setImageResource(R.drawable.ic_addtion);
        } else {
            GlideUtils.loadWithDefult(mContext, item.path, holder.getImageView(R.id.tv_image));

            holder.getView(R.id.ll_delete).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(position);
                    notifyItemRangeChanged(position, mItems.size());
                }
            });
        }
    }
}