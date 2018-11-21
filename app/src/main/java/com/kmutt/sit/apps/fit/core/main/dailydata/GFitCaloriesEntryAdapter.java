package com.kmutt.sit.apps.fit.core.main.dailydata;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.main.dailydata.calories.realm.CaloriesEntry;
import com.kmutt.sit.apps.fit.utils.InstantDialog;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Freshy on 7/20/2017 AD.
 */

public class GFitCaloriesEntryAdapter extends SectionedRecyclerViewAdapter<GFitCaloriesEntryAdapter.HeaderViewHolder, GFitCaloriesEntryAdapter.ViewHolder, GFitCaloriesEntryAdapter.FooterViewHolder> {

    Activity mContext;
    LayoutInflater mInflater;

    /**
     *   Deleting
     */
    public void showDeleteDialog(Activity context, final ViewHolder viewHolder, final DeleteDialogCallback callback) {
        String itemName = viewHolder.foodData.getTitle();
        String message = String.format(context.getString(R.string.text_prompt_remove_formattable), itemName);

        AlertDialog dialog = InstantDialog.makeAlertDialog(context, "", message);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.action_confirm_remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform item remove
                CaloriesEntry foodData = viewHolder.foodData;

                FITApplication.getUserDataRealm().beginTransaction();
                foodData.deleteFromRealm();
                FITApplication.getUserDataRealm().commitTransaction();

                if (callback != null)
                    callback.onItemDeleted();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.action_cancel_remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing..
            }
        });
        dialog.show();
    }
    public interface DeleteDialogCallback {
        void onItemDeleted();
    }

    /**
     *  ItemClickListener
     */
    OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    OnItemLongPressedListener mItemLongPressedListener;
    public void setOnItemLongPressedListener(OnItemLongPressedListener listener) {
        mItemLongPressedListener = listener;
    }

    /**
     *  Data
     */
    List<CaloriesEntry> mCaloriesEntryList;
    public void setCaloriesEntryList(List<CaloriesEntry> foodList) {
        mCaloriesEntryList = foodList;
        notifyDataSetChanged();
    }

    public GFitCaloriesEntryAdapter(Activity context, List<CaloriesEntry> foodList) {
        mContext = context;
        mCaloriesEntryList = foodList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     *   SectionedViewHolder
     */
    private static final int VIEWTYPE_FOOD = 0;
    private static final int VIEWTYPE_GFIT = 1;

    private static final int SECTION_FOOD = 0;
    private static final int SECTION_GFIT = 1;

    @Override
    protected int getSectionCount() {
        return 2;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (section == SECTION_FOOD)
            return mCaloriesEntryList.size();
        else
            return 10;
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        switch (section) {
            case SECTION_FOOD:
                return VIEWTYPE_FOOD;
            case SECTION_GFIT:
                return VIEWTYPE_GFIT;
            default:
                return -1;
        }
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.listrow_item_header_v2, null, false);
        final HeaderViewHolder viewHolder = new HeaderViewHolder(itemView);
        return viewHolder;
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.listrow_item_footer_v2, null, false);
        final FooterViewHolder viewHolder = new FooterViewHolder(itemView);
        return viewHolder;
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_FOOD) {
            if (mInflater == null)
                mInflater = LayoutInflater.from(mContext);

            View itemView = mInflater.inflate(R.layout.listrow_item_food, null, false);
            final ViewHolder viewHolder = new ViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(viewHolder);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mItemLongPressedListener != null) {
                        mItemLongPressedListener.onItemLongPressed(viewHolder);
                        return true;
                    }
                    return false;
                }
            });

            return viewHolder;
        }
//        else if (viewType == VIEWTYPE_GFIT) {
        else {
            View itemView = mInflater.inflate(R.layout.listrow_item_workout, null, false);
            final ViewHolder viewHolder = new ViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(viewHolder);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mItemLongPressedListener != null) {
                        mItemLongPressedListener.onItemLongPressed(viewHolder);
                        return true;
                    }
                    return false;
                }
            });

            return viewHolder;
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        if (section == SECTION_FOOD)
            holder.bindData("อาหาร");
        else
            holder.bindData("การออกกำลัง");
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int section, int position) {
        // FOOD ITEMS
        if (section == SECTION_FOOD) {
            holder.currentPosition = position;
            holder.bindData(mCaloriesEntryList.get(position));
        }
        else {
            holder.tvTitle.setText("ออกกำลัง #" + (position + 1));
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvSubtitle;
        int currentPosition;
        CaloriesEntry foodData;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
        }

        public void bindData(CaloriesEntry food) {
            tvTitle.setText(food.getTitle());

            tvSubtitle.setText(String.format("%.0f", food.getKcals()));

            this.foodData = food;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        int currentPosition;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }

        public void bindData(String titleText) {
            tvTitle.setText(titleText);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder);
    }

    public interface OnItemLongPressedListener {
        void onItemLongPressed(ViewHolder viewHolder);
    }
}
